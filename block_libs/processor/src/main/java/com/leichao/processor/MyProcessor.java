package com.leichao.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class MyProcessor extends AbstractProcessor {

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(Override.class);// 添加自定义注解
        return annotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        StringBuilder classNames = new StringBuilder();
        for (Element element : roundEnv.getElementsAnnotatedWith(Override.class)) {
            try {
                TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
                Override override = element.getAnnotation(Override.class);
                String packageName = processingEnv.getElementUtils()
                        .getPackageOf(enclosingElement).getQualifiedName().toString();
                int packageLen = packageName.length() + 1;
                String className = enclosingElement.getQualifiedName().toString()
                        .substring(packageLen).replace('.', '$');
                ClassName bindingClassName = ClassName.get(packageName, className + "_ViewBinding");
                boolean isFinal = enclosingElement.getModifiers().contains(Modifier.FINAL);
                classNames.append(bindingClassName.toString()).append("\n");
            } catch (Exception e) {
                StringWriter stackTrace = new StringWriter();
                e.printStackTrace(new PrintWriter(stackTrace));
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        String.format("Unable to parse @%s binding.\n\n%s", element.getSimpleName(), stackTrace),
                        element);
            }
        }

        try {
            getJavaFile(classNames.toString()).writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private JavaFile getJavaFile(String classNames) {
        // 类名和包名
        TypeSpec finderClass = TypeSpec.classBuilder("MyCustomClass")
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("这是自定义类\n" + classNames)
                .addMethod(MethodSpec.methodBuilder("custom")
                        .addModifiers(Modifier.PUBLIC)
                        .addJavadoc("这是自定义方法\n")
                        .addComment("这是什么")
                        .addCode("int a = 1;\n")
                        .addCode("System.out.println(\"你好\" + a);\n")
                        .addStatement("$T file = new $T(\"\")", File.class, File.class)
                        .beginControlFlow("if (a != 0)")
                        .addStatement("file = new $T(\"\")", File.class)
                        .beginControlFlow("if (a == 1)")
                        .addStatement("file = new $T(\"1\")", File.class)
                        .nextControlFlow("else if (a == 2)")
                        .addComment("给你一个null值，拿好")
                        .addStatement("file = null")
                        .endControlFlow()
                        .endControlFlow()
                        .build())
                .addMethod(MethodSpec.methodBuilder("custom2")
                        .addModifiers(Modifier.PUBLIC)
                        .build())
                .build();
        // 创建Java文件
        return JavaFile.builder("com.leichao.haha", finderClass)
                .addFileComment("这是自动生成的类，请不要编辑!")
                .build();
    }

}
