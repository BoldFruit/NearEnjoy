package com.example.fw_annotations_compiler;

import com.example.fw_annotations.BindPath;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

/**
 * 注解处理器（生成装入ARouter的代码）
 * @author YangZhaoxin.
 * @since 2020/1/18 19:58.
 * email yangzhaoxin@hrsoft.net.
 */
@SupportedAnnotationTypes("com.example.fw_annotations.BindPath")
@AutoService(Processor.class)   // 注册注解处理器
public class AnnotationCompiler extends AbstractProcessor {

    // 生成文件的对象
    Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
    }

    /**
     * 声明当前的注解处理器支持的java源版本
     * 设置最新的java的源版本
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

//    /**
//     * 声明当前的注解处理器需要处理的注解
//     * @return
//     */
//    @Override
//    public Set<String> getSupportedAnnotationTypes() {
//        Set<String> types = new HashSet<>();
//        types.add(BindPath.class.getCanonicalName());
//        return types;
//    }


    /**
     * 生成文件代码
     * @param annotations   使用了支持处理注解的节点集合
     * @param roundEnv  表示当前或是之前的运行环境,可以通过该对象查找找到的注解。
     * @return true表示后续处理器不会再处理(已经处理)
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!annotations.isEmpty()) {
            // 获取到当前模块中所有标注了BindPath的节点
            // TypeElement 类节点；    VariableElement 成员变量的节点；    ExecutableElement：方法节点
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(BindPath.class);
            Map<String, String> map = new HashMap<>();
            for (Element element : elements) {
                TypeElement typeElement = (TypeElement) element;
                // 通过类节点获取到上面的注解
                BindPath annotation = typeElement.getAnnotation(BindPath.class);
                String key = annotation.value();
                // 获取到类节点的类名(带包名)
                String activityName = typeElement.getQualifiedName().toString();
                map.put(key, activityName + ".class");
            }

            // 创建文件写入代码
            if (map.size() > 0) {
                // 1. 用Writer写文件
                Writer writer = null;
                // 创建生成文件的类名
                String activityName = "ActivityUtil" + System.currentTimeMillis();
                try {
                    // 创建并返回一个java文件对象
                    JavaFileObject sourceFile = filer.createSourceFile("com.example.annotations.util." + activityName);
                    writer = sourceFile.openWriter();
                    writer.write("package com.example.annotations.util;\n");
                    writer.write("import com.example.arouter.ARouter;\n");
                    writer.write("import com.example.arouter.IRouter;\n");
                    writer.write("public class " + activityName + " implements IRouter {\n");
                    writer.write("@Override\n");
                    writer.write("public void putActivity() {\n");

                    for (String key : map.keySet()) {
                        String value = map.get(key);
                        writer.write(" ARouter.getInstance().putActivity(\"" + key + "\", " + value + ");\n");
                    }
                    writer.write("}\n}\n");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
}
