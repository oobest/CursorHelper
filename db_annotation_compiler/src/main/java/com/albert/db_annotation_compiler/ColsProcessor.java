package com.albert.db_annotation_compiler;

import com.albert.lib_db_annotation.AptCursorWrapper;
import com.albert.lib_db_annotation.Cols;
import com.albert.lib_db_annotation.Constants;
import com.albert.lib_db_annotation.IParseCursor;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class ColsProcessor extends AbstractProcessor {

    private Types mTypeUtils;

    private Elements mElementsUtils;

    private Filer mFiler;

    private Messager mMessager;

    private static final List<String> TYPES;

    static {
        TYPES = Arrays.asList("int", "long", "float", "double", "float", "java.lang.String");
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        mTypeUtils = processingEnvironment.getTypeUtils();
        mElementsUtils = processingEnvironment.getElementUtils();
        mFiler = processingEnvironment.getFiler();
        mMessager = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set == null || set.isEmpty()) {
            info(">>> set is null...<<<");
            return true;
        }
        info(">>> Found field, start... <<<");
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(AptCursorWrapper.class);
        if (elements == null || elements.isEmpty()) {
            info(">>> elements is null...");
            return true;
        }
        try {
            Map<TypeElement, List<FieldBinding>> targetMap = getTargetMap(elements);
            createJavaFile(targetMap.entrySet());
            info(">>>>>>>>>>>>>>>>>>>end");
        } catch (ColsException e) {
            error(e.getElement(), e.getMessage());
            return true;
        } catch (IOException e) {
            error(e.getMessage());
            return true;
        }

        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(AptCursorWrapper.class.getCanonicalName());
        return annotations;
    }

    private Map<TypeElement, List<FieldBinding>> getTargetMap(Set<? extends Element> elements) throws ColsException {
        Map<TypeElement, List<FieldBinding>> targetMap = new HashMap<>();
        for (Element element : elements) {
            if (element.getKind() != ElementKind.CLASS) {
                throw new ColsException(element, String.format("Only field can be annotated with @%s", Cols.class.getSimpleName()));
            }
            TypeElement typeElement = (TypeElement) element; //获取文件

            List<FieldBinding> list = getFieldBinding(typeElement);
            if (list != null && !list.isEmpty()) {
                targetMap.put(typeElement, list);
            }
        }

        return targetMap;
    }

    private List<FieldBinding> getFieldBinding(TypeElement typeElement) throws ColsException {
        TypeMirror parentMirror = typeElement.getSuperclass();
        List<FieldBinding> result = null;
        if (parentMirror != null) {
            TypeElement parentElement = (TypeElement) mTypeUtils.asElement(typeElement.getSuperclass());
            String fullClassName = parentElement.getQualifiedName().toString();
            if (fullClassName.equals("java.lang.Object")) {
                result = new ArrayList<>();
            } else {
                result = getFieldBinding(parentElement);
            }
        }

        List<? extends Element> elements = typeElement.getEnclosedElements();
        for (Element e : elements) {
            if (e.getKind() == ElementKind.FIELD) {
                Cols annotation = e.getAnnotation(Cols.class);
                if (annotation != null) {
                    String fieldName = e.getSimpleName().toString();
                    TypeMirror fieldType = e.asType();
                    String typeString = fieldType.toString();
                    if (!TYPES.contains(typeString)) {
                        throw new ColsException(e, String.format("Field's type Not support for %s, can use int, float,double,String", typeString));
                    }
                    result.add(new FieldBinding(fieldName, fieldType, annotation.value()));
                }
            }
        }
        return result;
    }

    private void createJavaFile(Set<Map.Entry<TypeElement, List<FieldBinding>>> entries) throws IOException {
        final ParameterSpec cursorParameterSpec = ParameterSpec.builder(ClassName.bestGuess("android.database.Cursor"), "cursor")
                .build();
        final ClassName interfaceName = ClassName.get(IParseCursor.class);
        final ClassName superClassName = ClassName.bestGuess("android.database.CursorWrapper");

        for (Map.Entry<TypeElement, List<FieldBinding>> entry : entries) {
            TypeElement typeElement = entry.getKey();
            List<FieldBinding> list = entry.getValue();
            if (list == null || list.size() == 0) {
                continue;
            }

            //
            String packageName = mElementsUtils.getPackageOf(typeElement).getQualifiedName().toString();
            //
            String fullClassName = typeElement.getQualifiedName().toString();
            String className = fullClassName.substring(packageName.length() + 1);
            String newClassName = className + Constants.SUFFIX;
            ClassName ItemClassName = ClassName.bestGuess(fullClassName);

            //
            MethodSpec.Builder constructorMethodBuilder = MethodSpec.constructorBuilder()
                    .addParameter(cursorParameterSpec)
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("super(cursor)");


            //
            TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(newClassName)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addSuperinterface(ParameterizedTypeName.get(interfaceName, ItemClassName))
                    .superclass(superClassName);


            CodeBlock.Builder codeBlock = CodeBlock.builder();
            codeBlock.addStatement("$T item= new $T()", ItemClassName, ItemClassName);
            codeBlock.addStatement("int index = -1");
            for (FieldBinding fieldBinding : list) {
                codeBlock.addStatement("index=$N(\"$L\")", "getColumnIndex", fieldBinding.getColsName());
                codeBlock.beginControlFlow("if(index>-1)");

                switch (fieldBinding.getType().getKind()) {
                    case DECLARED:
                        codeBlock.addStatement("item.$L=isNull(index) ? null : getString(index)", fieldBinding.getName());
                        break;
                    case INT:
                        codeBlock.addStatement("item.$L=isNull(index) ? 0 : getInt(index)", fieldBinding.getName());
                        break;
                    case LONG:
                        codeBlock.addStatement("item.$L=isNull(index) ? 0 : getLong(index)", fieldBinding.getName());
                        break;
                    case DOUBLE:
                        codeBlock.addStatement("item.$L=isNull(index) ? 0 : getDouble(index)", fieldBinding.getName());
                        break;
                    case FLOAT:
                        codeBlock.addStatement("item.$L=isNull(index)? 0 : getFloat(index)", fieldBinding.getName());
                    default:
                        break;
                }
                codeBlock.endControlFlow();
            }
            codeBlock.addStatement("return item");
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("getItem")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(ItemClassName)
                    .addCode(codeBlock.build());

            TypeSpec typeSpec = typeSpecBuilder.addMethod(constructorMethodBuilder.build())
                    .addMethod(methodBuilder.build()).build();

            JavaFile javaFile = JavaFile.builder(packageName, typeSpec).build();
            javaFile.writeTo(mFiler);
        }

        info(">>> analysisAnnotated is finish... <<<");
    }


    private void error(Element e, String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }

    private void error(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    private void info(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }
}