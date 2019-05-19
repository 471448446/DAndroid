package com.better.learn.compiler;

import com.better.learn.annoation.BindView;
import com.better.learn.annoation.OnClick;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

/**
 * Created by better on 2019-05-19 11:24.
 */
//@AutoService(Processor.class) //触发注解，生成Java文件 not work
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MyProcess extends AbstractProcessor {
    private Elements elementUtil;// 操作Elements
    private Filer filer;// 创建源文件

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtil = processingEnvironment.getElementUtils();
        filer = processingEnvironment.getFiler();
        System.out.println("______init");
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        //支持的注解类型
        Set<String> support = new LinkedHashSet<>();
        support.add(BindView.class.getCanonicalName());
        support.add(OnClick.class.getCanonicalName());
        return support;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        //Processor支持的版本。使用注解或者重载方法返回
        return SourceVersion.latest();
    }

    /**
     * @param set              参数是要处理的注解的类型集合
     * @param roundEnvironment 表示运行环境，可以通过这个参数获得被注解标注的代码块
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("_______process()");
        if (null == set || set.size() == 0) {
            return false;
        }
        // 将所有注解分类找出来
        // bindView
        Set<? extends Element> fieldSet = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        // onClick
        Set<? extends Element> methodSet = roundEnvironment.getElementsAnnotatedWith(OnClick.class);

        Map<String, List<VariableElement>> fieldViews = new HashMap<>();
        for (Element element : fieldSet) {
            if (element.getKind().isField()) {
                VariableElement variableElement = (VariableElement) element;
                String activityName = getActivityName(variableElement);
                List<VariableElement> list = fieldViews.get(activityName);
                if (null == list) {
                    list = new ArrayList<>();
                    fieldViews.put(activityName, list);
                }
                list.add(variableElement);
            }
        }
        Map<String, List<ExecutableElement>> methodViews = new HashMap<>();
        for (Element element : methodSet) {
            if (element.getKind() == ElementKind.METHOD) {
                ExecutableElement variableElement = (ExecutableElement) element;
                String activityName = getActivityName(variableElement);
                List<ExecutableElement> list = methodViews.get(activityName);
                if (null == list) {
                    list = new ArrayList<>();
                    methodViews.put(activityName, list);
                }
                list.add(variableElement);
            }
        }
        System.out.println("create file_____" + fieldViews.size() + "," + methodViews.size());
        for (String activityName : fieldViews.keySet()) {

            List<VariableElement> variableElements = fieldViews.get(activityName);
            List<ExecutableElement> executableElements = methodViews.get(activityName);
            try {
                JavaFileObject sourceFile = filer.createSourceFile(activityName + "_ViewBinder");
                String pkg =
                        elementUtil.getPackageOf(variableElements.get(0).getEnclosingElement()).getQualifiedName().toString();
                Writer writer = sourceFile.openWriter();

                String simpleName = variableElements.get(0).getEnclosingElement().getSimpleName().toString() + "_ViewBinder";

                writer.write("package " + pkg + ";\n");
                writer.write("import com.better.learn.library.ButterKnife;\n");
                writer.write("import com.better.learn.library.DebugOnClickListener;\n");
                writer.write("import com.better.learn.library.ViewBinder;\n");
                writer.write("import android.view.View;\n");
                writer.write("import android.util.Log;\n");

                writer.write("public class " + simpleName + " implements ViewBinder<" + activityName + "> {\n");
                //生成bind()方法
                writer.write("public void bind(final " + activityName + " target) {\n");
                writer.write("Log.e(\"Better\",\"bind start\");");

                //生成每个控件的代码
                for (VariableElement element : variableElements) {
                    // 控件属性名称
                    String fieldName = element.getSimpleName().toString();
                    // 控件的注解
                    BindView bindView = element.getAnnotation(BindView.class);
                    // 注解的值，即View的ID
                    int id = bindView.value();
                    // 绑定View
                    writer.write("target." + fieldName + " = " + "target.findViewById(" + id + ");\n");
                }
                //为控件注入事件
                // 没有OnClick？？
                for (ExecutableElement element : executableElements) {
                    // 方法名称
                    String methodName = element.getSimpleName().toString();
                    // 注解
                    OnClick onClick = element.getAnnotation(OnClick.class);
                    int[] ids = onClick.value();
                    List<? extends VariableElement> parameters = element.getParameters();
                    for (int id : ids) {
                        writer.write("target.findViewById(" + id + ")" +
                                ".setOnClickListener(new DebugOnClickListener() {\n");
                        writer.write("public void doClick(View view) {\n");
                        if (parameters.isEmpty()) {
                            writer.write("target." + methodName + "();");
                        } else {
                            writer.write("target." + methodName + "(view);");
                        }
                        writer.write("\n}\n});\n");
                    }
                }


                writer.write("}\n}\n");

                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return true;
    }

    private String getActivityName(VariableElement element) {
        Element parent = element.getEnclosingElement();
        String name = parent.getSimpleName().toString();
        String pkg = elementUtil.getPackageOf(parent).getQualifiedName().toString();
        System.out.println("name:" + name + " pkg:" + pkg);
        return pkg + "." + name;
    }

    private String getActivityName(ExecutableElement element) {
        Element parent = element.getEnclosingElement();
        String name = parent.getSimpleName().toString();
        String pkg = elementUtil.getPackageOf(parent).getQualifiedName().toString();
        System.out.println("name:" + name + " pkg:" + pkg);
        return pkg + "." + name;
    }
}