package com.better.app.meal_compiler;

import com.better.app.meal_annotation.MealFactory;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public class MealFactoryGroupedClasses {
    // 要生成的Factor后缀
    private static final String SUFFIX = "Factory";
    /**
     * {@link MealFactory#type()} 类信息
     */
    private final String superQualifiedClassName;
    /**
     * 所有的注解类
     */
    private Map<String, MealFactoryAnnotatedClass> itemsMap =
            new LinkedHashMap<String, MealFactoryAnnotatedClass>();

    public MealFactoryGroupedClasses(String superQualifiedClassName) {
        this.superQualifiedClassName = superQualifiedClassName;
    }

    public String getSuperQualifiedClassName() {
        return superQualifiedClassName;
    }

    public void add(MealFactoryAnnotatedClass toBeInsert) throws ProcessingException {
        // 因为设计上，根据id来做if-else，所以是不能重复的
        if (null != itemsMap.get(toBeInsert.getId())) {
            // 已经存在了
            throw new ProcessingException(toBeInsert.getElement(),
                    "Conflict: The class %s is annotated with @%s with id ='%s' but %s already uses the same id",
                    toBeInsert.getElement().getQualifiedName().toString(), MealFactory.class.getSimpleName(),
                    toBeInsert.getId(), toBeInsert.getElement().getQualifiedName().toString());
        }
        itemsMap.put(toBeInsert.getId(), toBeInsert);
    }

    /**
     * 生成Java代码
     *
     * @param elementUtils
     * @param filer
     */
    public void generateCode(Elements elementUtils, Filer filer) throws IOException, ProcessingException {
        TypeElement typeElement = elementUtils.getTypeElement(superQualifiedClassName);
        String factorClassName = typeElement.getSimpleName() + SUFFIX;
        PackageElement packageElement = elementUtils.getPackageOf(typeElement);
        if (packageElement.isUnnamed()) {
            throw new ProcessingException(packageElement, "fail get package name", "");
        }
        String packageName = packageElement.getQualifiedName().toString();

        MethodSpec.Builder create = MethodSpec.methodBuilder("create")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(String.class, "id")
                .returns(TypeName.get(typeElement.asType()));
        // 检查id 是否为null
        create.beginControlFlow("if (id == null)")
                .addStatement("throw new IllegalArgumentException($S)", "id is null!")
                .endControlFlow();
        // 生成if-else
        for (MealFactoryAnnotatedClass value : itemsMap.values()) {
            create.beginControlFlow("if ( $S.equals(id) )", value.getId())
                    .addStatement("return new $L()", value.getElement().getQualifiedName().toString())
                    .endControlFlow();
        }
        create.addStatement("throw new IllegalArgumentException($S + id)", "Unknown id = ");

        TypeSpec typeSpec = TypeSpec.classBuilder(factorClassName).addMethod(create.build()).build();
        JavaFile.builder(packageName, typeSpec).build().writeTo(filer);
    }
}
