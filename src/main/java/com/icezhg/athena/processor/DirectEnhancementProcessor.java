// DirectEnhancementProcessor.java
package com.example.trace.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.example.trace.Trace")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class DirectEnhancementProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                if (element instanceof TypeElement) {
                    enhanceOriginalClass((TypeElement) element);
                }
            }
        }
        return true;
    }

    private void enhanceOriginalClass(TypeElement classElement) {
        String packageName = getPackageName(classElement);
        String className = classElement.getSimpleName().toString();
        
        try {
            // 重新生成原始类，添加 trace 功能
            TypeSpec enhancedClass = rebuildClassWithTrace(classElement);
            JavaFile javaFile = JavaFile.builder(packageName, enhancedClass)
                .skipJavaLangImports(true)
                .build();
            
            // 直接覆盖原始文件（需要特殊配置）
            JavaFileObject sourceFile = processingEnv.getFiler()
                .createSourceFile(packageName + "." + className, classElement);
            
            try (Writer writer = sourceFile.openWriter()) {
                javaFile.writeTo(writer);
            }
            
        } catch (IOException e) {
            processingEnv.getMessager().printError("Failed to enhance class: " + e.getMessage());
        }
    }

    private TypeSpec rebuildClassWithTrace(TypeElement originalClass) {
        String className = originalClass.getSimpleName().toString();
        Trace traceAnnotation = originalClass.getAnnotation(Trace.class);
        String traceFieldName = traceAnnotation != null ? traceAnnotation.value() : "trace";
        
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className)
            .addModifiers(Modifier.PUBLIC);
        
        // 添加原始类的修饰符
        originalClass.getModifiers().forEach(classBuilder::addModifiers);
        
        // 添加父类
        if (originalClass.getSuperclass() != null) {
            classBuilder.superclass(TypeName.get(originalClass.getSuperclass()));
        }
        
        // 添加实现的接口
        originalClass.getInterfaces().forEach(interfaceType -> 
            classBuilder.addSuperinterface(TypeName.get(interfaceType)));
        
        // 添加 trace 字段
        FieldSpec traceField = FieldSpec.builder(
            ClassName.get("com.example.trace", "ClassTracer"),
            traceFieldName,
            Modifier.PRIVATE, Modifier.FINAL
        ).initializer("new $T()", ClassName.get("com.example.trace", "ClassTracer"))
         .build();
        classBuilder.addField(traceField);
        
        // 添加 getter 方法
        MethodSpec getter = MethodSpec.methodBuilder("get" + capitalize(traceFieldName))
            .addModifiers(Modifier.PUBLIC)
            .returns(ClassName.get("com.example.trace", "ClassTracer"))
            .addStatement("return this.$L", traceFieldName)
            .build();
        classBuilder.addMethod(getter);
        
        // 复制并增强原始方法
        for (Element enclosed : originalClass.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.METHOD) {
                ExecutableElement method = (ExecutableElement) enclosed;
                if (method.getModifiers().contains(Modifier.PUBLIC) && 
                    !method.getSimpleName().toString().startsWith("get" + capitalize(traceFieldName))) {
                    classBuilder.addMethod(enhanceMethod(method, traceFieldName));
                }
            } else if (enclosed.getKind() == ElementKind.FIELD) {
                // 复制原始字段
                VariableElement field = (VariableElement) enclosed;
                FieldSpec fieldSpec = FieldSpec.builder(
                    TypeName.get(field.asType()),
                    field.getSimpleName().toString(),
                    field.getModifiers().toArray(new Modifier[0])
                ).build();
                classBuilder.addField(fieldSpec);
            } else if (enclosed.getKind() == ElementKind.CONSTRUCTOR) {
                // 复制构造函数
                ExecutableElement constructor = (ExecutableElement) enclosed;
                MethodSpec constructorSpec = copyConstructor(constructor);
                classBuilder.addMethod(constructorSpec);
            }
        }
        
        return classBuilder.build();
    }

    private MethodSpec enhanceMethod(ExecutableElement originalMethod, String traceFieldName) {
        String methodName = originalMethod.getSimpleName().toString();
        TypeName returnType = TypeName.get(originalMethod.getReturnType());
        
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
            .addModifiers(Modifier.PUBLIC)
            .returns(returnType);
        
        // 添加参数
        for (VariableElement param : originalMethod.getParameters()) {
            methodBuilder.addParameter(
                TypeName.get(param.asType()),
                param.getSimpleName().toString()
            );
        }
        
        // 添加异常声明
        for (TypeMirror thrownType : originalMethod.getThrownTypes()) {
            methodBuilder.addException(TypeName.get(thrownType));
        }
        
        // 构建参数列表
        String paramList = originalMethod.getParameters().stream()
            .map(param -> param.getSimpleName().toString())
            .collect(java.util.stream.Collectors.joining(", "));
        
        // 方法体 - 添加追踪逻辑
        if (returnType.equals(TypeName.VOID)) {
            methodBuilder
                .addStatement("$L.enter($S, $L)", traceFieldName, methodName, 
                    paramList.isEmpty() ? "new Object[0]" : paramList)
                .beginControlFlow("try")
                .addStatement("// 原始方法逻辑")
                .addComment("TODO: 这里需要插入原始方法的具体实现")
                .nextControlFlow("catch (Exception e)")
                .addStatement("$L.error($S, e)", traceFieldName, methodName)
                .addStatement("throw e")
                .endControlFlow()
                .addStatement("$L.exit($S)", traceFieldName, methodName);
        } else {
            methodBuilder
                .addStatement("$L.enter($S, $L)", traceFieldName, methodName, 
                    paramList.isEmpty() ? "new Object[0]" : paramList)
                .beginControlFlow("try")
                .addStatement("// 原始方法逻辑")
                .addStatement("$T result = null", returnType)
                .addComment("TODO: 这里需要插入原始方法的具体实现和返回值")
                .addStatement("$L.exit($S, result)", traceFieldName, methodName)
                .addStatement("return result")
                .nextControlFlow("catch (Exception e)")
                .addStatement("$L.error($S, e)", traceFieldName, methodName)
                .addStatement("throw e")
                .endControlFlow();
        }
        
        return methodBuilder.build();
    }

    private MethodSpec copyConstructor(ExecutableElement constructor) {
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
            .addModifiers(constructor.getModifiers().toArray(new Modifier[0]));
        
        for (VariableElement param : constructor.getParameters()) {
            constructorBuilder.addParameter(
                TypeName.get(param.asType()),
                param.getSimpleName().toString()
            );
        }
        
        // 调用父类构造函数或其他初始化逻辑
        constructorBuilder.addStatement("// 构造函数逻辑");
        
        return constructorBuilder.build();
    }

    private String getPackageName(TypeElement type) {
        return processingEnv.getElementUtils()
            .getPackageOf(type).getQualifiedName().toString();
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}