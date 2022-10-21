package com.xm.lua_apt;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.xm.lua_annotation.LuaInterfaceForClass;
import com.xm.lua_annotation.LuaInterfaceForObj;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.xm.lua_annotation.LuaInterfaceForObj")
public class LuaProcessor extends AbstractProcessor {
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * package com.xm.lua_inject
     * public class LuaInject{
     * public static void inject(com.xm.luajava.LuaState lua){
     * lua.pushJavaObject(lua, new classFullName());
     * lua.setGlobal(luaName);
     * }
     * }
     *
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        // 根据注解获取信息
        Set<? extends Element> objElement = roundEnvironment.getElementsAnnotatedWith(LuaInterfaceForObj.class);
        Set<? extends Element> classElement = roundEnvironment.getElementsAnnotatedWith(LuaInterfaceForClass.class);

        if (!(objElement != null && objElement.size() > 0) && !(classElement != null && classElement.size() > 0)) {
            return false;
        }

        Map<String, String> luaInjectObjMap = new HashMap<>();
        for (Element element : objElement) {
            if (element instanceof TypeElement) {
                TypeElement typeElement = (TypeElement) element;
                String luaName = typeElement.getAnnotation(LuaInterfaceForObj.class).luaName();
                String className = typeElement.getQualifiedName().toString();
                luaInjectObjMap.put(luaName, className);
            }
        }

        Map<String, String> luaInjectClassMap = new HashMap<>();
        for (Element element : classElement) {
            if (element instanceof TypeElement) {
                TypeElement typeElement = (TypeElement) element;
                String luaName = typeElement.getAnnotation(LuaInterfaceForClass.class).luaName();
                String className = typeElement.getQualifiedName().toString();
                luaInjectClassMap.put(luaName, className);
            }
        }

        // 生成inject方法
        MethodSpec.Builder builder = MethodSpec.methodBuilder("execute")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(String.class)
                .addParameter(String.class, "luaScript");

        builder.addCode(
                "com.xm.luajava.LuaState lua = com.xm.luajava.LuaStateFactory.newLuaState();\n" +
                        "lua.openLibs();\n"
        );
        for (String luaName : luaInjectObjMap.keySet()) {
            builder.addCode("lua.pushJavaObject(new " + luaInjectObjMap.get(luaName) + "());\n");
            builder.addCode("lua.setGlobal(\"" + luaName + "\");\n");
        }

        for (String luaName : luaInjectClassMap.keySet()) {
            builder.addCode("lua.pushJavaClass(" + luaInjectClassMap.get(luaName) + ".class);\n");
            builder.addCode("lua.setGlobal(\"" + luaName + "\");\n");
        }

        builder.addCode(
                "lua.LdoString(luaScript);\n" +
                        "\n" +
                        "int jsonType = -1;\n" +
                        "try {\n" +
                        "    Class.forName(\"com.xiaomi.gson.Gson\");\n" +
                        "    jsonType = 1;\n" +
                        "} catch (ClassNotFoundException e) {\n" +
                        "    e.printStackTrace();\n" +
                        "}\n" +
                        "\n" +
                        "if (jsonType == -1) {\n" +
                        "    try {\n" +
                        "        Class.forName(\"com.google.gson.Gson\");\n" +
                        "        jsonType = 2;\n" +
                        "    } catch (ClassNotFoundException e) {\n" +
                        "        e.printStackTrace();\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "String re = \"\";\n" +
                        "try {\n" +
                        "    Object o = lua.toJavaObject(-1);\n" +
                        "    if (jsonType == 1) {\n" +
                        "        Class<?> xiaomiGsonClass = Class.forName(\"com.xiaomi.gson.Gson\");\n" +
                        "        Object xiaomiGson = xiaomiGsonClass.newInstance();\n" +
                        "        java.lang.reflect.Method xiaomiTojson = xiaomiGsonClass.getDeclaredMethod(\"toJson\", Object.class);\n" +
                        "        re = (String) xiaomiTojson.invoke(xiaomiGson, o);\n" +
                        "    } else if (jsonType == 2) {\n" +
                        "        Class<?> gsonClass = Class.forName(\"com.google.gson.Gson\");\n" +
                        "        Object gson = gsonClass.newInstance();\n" +
                        "        java.lang.reflect.Method toJson = gsonClass.getDeclaredMethod(\"toJson\", Object.class);\n" +
                        "        re = (String) toJson.invoke(gson, o);\n" +
                        "    } else {\n" +
                        "        re = o.toString();\n" +
                        "    }\n" +
                        "    lua.pop(1);\n" +
                        "} catch (Exception e) {\n" +
                        "    e.printStackTrace();\n" +
                        "}\n" +
                        "lua.close();\n" +
                        "\n" +
                        "return re;"
        );

        MethodSpec luaInjectMethod = builder.build();

        // 生成LuaInject类
        TypeSpec luaInjectClass = TypeSpec.classBuilder("LuaExecutor")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(luaInjectMethod)
                .build();

        // 生成java文件
        JavaFile javaFile = JavaFile.builder("com.xm.lua_inject", luaInjectClass).build();

        // 写入java文件
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}