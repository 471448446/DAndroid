package com.better.app.meal_compiler;

import com.better.app.meal_annotation.MealFactory;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

/**
 * 用于获取使用了 {@link com.better.app.meal_annotation.MealFactory} 注解后的类的一些信息
 */
public class MealFactoryAnnotatedClass {
    private final TypeElement element;
    /**
     * {@link MealFactory#id()}
     */
    private final String id;
    /**
     * 被注解的父类类全名
     */
    private String superQualifiedGroupClassName;
    /**
     * 被注解的父类类名
     */
    private String superSimpleFactoryGroupName;

    public MealFactoryAnnotatedClass(TypeElement element) throws ProcessingException {
        this.element = element;
        MealFactory annotation = element.getAnnotation(MealFactory.class);

        // 当在id方法处返回null时，会有语法提示错误：attribute must be constant
        if (null == annotation.id() || annotation.id().length() == 0) {
            throw new ProcessingException(element,
                    "id() in @%s for class %s is null or empty! that's not allowed",
                    MealFactory.class.getSimpleName(), element.getQualifiedName().toString());
        }
        id = annotation.id();

        // 因为annotation.type() 获取的Class信息
        // 这里需要注意，因为注解是在编译时期进行的，所以获取的类信息，可能还未被编译，也可能已经被编译了，这里需要处理
        // 1. 如果已经被编译了，try 语句里面是能正常获取到Class信息
        // 2. 如果获取的类还未被编译会抛出MirroredTypeException异常。而这里MealFactory注解只能被用于类上面进行注解，在获取的时候已经判断过了。
        // 所以在catch语句块里面，直接进行了强制转换成DeclaredType并通过TypeElement来获取类信息
        try {
            Class<?> type = annotation.type();
            superQualifiedGroupClassName = type.getCanonicalName();
            superSimpleFactoryGroupName = type.getSimpleName();
        } catch (MirroredTypeException e) {
            DeclaredType typeMirror = (DeclaredType) e.getTypeMirror();
            TypeElement typeElement = (TypeElement) typeMirror.asElement();
            superQualifiedGroupClassName = typeElement.getQualifiedName().toString();
            superSimpleFactoryGroupName = typeElement.getSimpleName().toString();
        }
    }

    /**
     * @return 原始的使用了{@link MealFactory}注解的Element，
     */
    public TypeElement getElement() {
        return element;
    }

    /**
     * @return {@link MealFactory} 注解里面的id
     */
    public String getId() {
        return id;
    }

    /**
     * @return {@link MealFactory#type()} 注解里面的Class的类全路基名称
     */
    public String getSuperQualifiedGroupClassName() {
        return superQualifiedGroupClassName;
    }

    /**
     * @return {@link MealFactory#type()} 注解里面的Class的类名
     */
    public String getSuperSimpleFactoryGroupName() {
        return superSimpleFactoryGroupName;
    }
}
