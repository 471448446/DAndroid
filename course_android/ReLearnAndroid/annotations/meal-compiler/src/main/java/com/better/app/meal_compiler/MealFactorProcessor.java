package com.better.app.meal_compiler;

import com.better.app.meal_annotation.MealFactory;
import com.google.auto.service.AutoService;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * AutoService 只是用来注册注解解析器的
 */
@AutoService(Processor.class)
public class MealFactorProcessor extends AbstractProcessor {
    // 操作元素的工具类
    private Elements elementUtils;
    // 用来创建文件
    private Filer filer;
    // 用来输出日志、错误或警告信息
    private Messager messager;
    private Types typeUtils;
    // 用于保存扫描的注解类
    // key: 父类的类名称
    // value： 所有继承于该父类的被标注了类
    private Map<String, MealFactoryGroupedClasses> factoryClasses =
            new LinkedHashMap<String, MealFactoryGroupedClasses>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.elementUtils = processingEnv.getElementUtils();
        this.filer = processingEnv.getFiler();
        this.messager = processingEnv.getMessager();
        typeUtils = processingEnv.getTypeUtils();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(MealFactory.class.getCanonicalName());
    }

    // 只有这个方法时抽象的，其它都是重写
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        try {
            // 扫描类中的所有注解
            for (Element element : roundEnvironment.getElementsAnnotatedWith(MealFactory.class)) {
                // 因为这个注解只能在类上使用，所以如果在其它地方使用，需要提醒
                if (element.getKind() != ElementKind.CLASS) {
                    throw new ProcessingException(element, "Only classes can be annotated with @%s", MealFactory.class.getSimpleName());
                }
                handleElementClass((TypeElement) element);
            }
            for (MealFactoryGroupedClasses value : factoryClasses.values()) {
                messager.printMessage(Diagnostic.Kind.NOTE, "-------annotation generate :" + value.getSuperQualifiedClassName());
                value.generateCode(elementUtils, filer);
            }
            factoryClasses.clear();
            return true;
        } catch (Exception e) {
            printErr(e);
        }
        return false;
    }

    private void handleElementClass(TypeElement element) throws ProcessingException {
        // 检查该注解是否符合我们定义的规则
        MealFactoryAnnotatedClass mealFactoryAnnotatedClass = new MealFactoryAnnotatedClass(element);
        isValidClass(mealFactoryAnnotatedClass);
        // 保存信息，因为要将所有使用了相同注解的类的子类都搜索出来，然后统一在if-else 里面生成模板代码，所以需要现保存该类信息
        MealFactoryGroupedClasses mealFactoryGroupedClasses = factoryClasses.get(mealFactoryAnnotatedClass.getSuperQualifiedGroupClassName());
        if (null == mealFactoryGroupedClasses) {
            // 没有保存该类，比如没有扫描到 Meal.class
            mealFactoryGroupedClasses = new MealFactoryGroupedClasses(mealFactoryAnnotatedClass.getSuperQualifiedGroupClassName());
            factoryClasses.put(mealFactoryAnnotatedClass.getSuperQualifiedGroupClassName(), mealFactoryGroupedClasses);
        }
        mealFactoryGroupedClasses.add(mealFactoryAnnotatedClass);
    }

    /**
     * 因为我们要动态的创建对象，所以该类是可以被访问，并且可以被直接new出来，这里通过无参的构造方法来创建，所以也需要检查
     *
     * @param mealFactoryAnnotatedClass 使用了注解的类
     * @throws ProcessingException 异常信息
     */
    private void isValidClass(MealFactoryAnnotatedClass mealFactoryAnnotatedClass) throws ProcessingException {
        TypeElement classElement = mealFactoryAnnotatedClass.getElement();
        // 类没有申明成public
        if (!classElement.getModifiers().contains(Modifier.PUBLIC)) {
            throw new ProcessingException(classElement, "The class %s is not public.",
                    classElement.getQualifiedName().toString());
        }

        // 如果是抽象类，也是不行的
        if (classElement.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new ProcessingException(classElement,
                    "The class %s is abstract. You can't annotate abstract classes with @%",
                    classElement.getQualifiedName().toString(), MealFactory.class.getSimpleName());
        }

        // 检查继承：这个类必须要继承他标记的父类 @MealFactory.type();
        // 可以通过类的全路径老创建Element
        TypeElement superClassElement = elementUtils.getTypeElement(mealFactoryAnnotatedClass.getSuperQualifiedGroupClassName());
        if (superClassElement.getKind() == ElementKind.INTERFACE) {
            // 检查接口的方法是否完全实现
            if (!classElement.getInterfaces().contains(superClassElement.asType())) {
                throw new ProcessingException(classElement,
                        "The class %s annotated with @%s must implement the interface %s",
                        classElement.getQualifiedName().toString(), MealFactory.class.getSimpleName(),
                        mealFactoryAnnotatedClass.getSuperQualifiedGroupClassName());
            }
        } else {
            // 检查当前类是否继承于父类 @MealFactor.type。这里通过取处父类的名称来校验
            TypeElement currentClass = classElement;
            while (true) {
                TypeMirror superclass = currentClass.getSuperclass();
                if (superclass.getKind() == TypeKind.NONE) {
                    // 扫描到了最顶层的类(java.lang.Object)，所以应该结束循环了。说明该类没有继承父类
                    throw new ProcessingException(classElement,
                            "The class %s annotated with @%s must inherit from %s",
                            classElement.getQualifiedName().toString(), MealFactory.class.getSimpleName(),
                            mealFactoryAnnotatedClass.getSuperQualifiedGroupClassName());
                }
                // 继承了父类，它继承了或者它父类继承了,反正就是继承了
                if (superclass.toString().contains(mealFactoryAnnotatedClass.getSuperQualifiedGroupClassName())) {
                    break;
                }
                currentClass = (TypeElement) typeUtils.asElement(superclass);
            }
        }

        // 最后检查这个类有没有无参的共有的构造方法
        for (Element enclosedElement : classElement.getEnclosedElements()) {
            if (enclosedElement.getKind() == ElementKind.CONSTRUCTOR) {
                ExecutableElement executableElement = (ExecutableElement) enclosedElement;
                if (executableElement.getModifiers().contains(Modifier.PUBLIC) && executableElement.getParameters().size() == 0) {
                    //找到了
                    return;
                }
            }
        }
        // 没有找到无参构造方法
        throw new ProcessingException(classElement,
                "The class %s must provide an public empty default constructor",
                classElement.getQualifiedName().toString());
    }

    private void printErr(Exception e) {
        if (e instanceof ProcessingException) {
            ProcessingException exception = (ProcessingException) e;
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage(), exception.getElement());
        } else {
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage(), null);
        }
    }
}