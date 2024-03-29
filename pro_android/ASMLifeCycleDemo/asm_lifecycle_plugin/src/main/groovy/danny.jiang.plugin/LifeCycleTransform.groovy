package danny.jiang.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import danny.jiang.asm.LifecycleClassVisitor
import groovy.io.FileType
import org.apache.commons.io.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

class LifeCycleTransform extends Transform {

    /**
     * 设置我们自定义的 Transform 对应的 Task 名称。
     * Gradle 在编译的时候，会将这个名称显示在控制台上。
     * 比如：Task :app:transformClassesWithXXXForDebug。
     */
    @Override
    String getName() {
        return "LifeCycleTransform"
    }

    /**
     * 在项目中会有各种各样格式的文件，通过 getInputType 可以设置 LifeCycleTransform 接收的文件类型，
     * 此方法返回的类型是 Set<QualifiedContent.ContentType> 集合。
     * 1. CLASSES：代表只检索 .class 文件；
     * 2. RESOURCES：代表检索 java 标准资源文件。
     */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    /**
     * 这个方法规定自定义 Transform 检索的范围，具体有以下几种取值：
     */
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.PROJECT_ONLY
    }

    /**
     * 表示当前 Transform 是否支持增量编译，我们不需要增量编译，所以直接返回 false 即可。
     */
    @Override
    boolean isIncremental() {
        return false
    }
    /**
     * inputs：inputs 中是传过来的输入流，其中有两种格式，一种是 jar 包格式，一种是 directory（目录格式）。
     *
     * outputProvider：outputProvider 获取到输出目录，最后将修改的文件复制到输出目录，这一步必须做，否则编译会报错
     */
    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        // 获取所有的class文件
        def transformInputs = transformInvocation.inputs
        transformInputs.each { TransformInput input ->
            /**
             * directoryInputs 表示以源码的方式编译项目里面所有的源文件
             * 比如R.class BuildConfig.class MainActivity.class
             */
            input.directoryInputs.each { DirectoryInput directoryInput ->
                def fileDir = directoryInput.file
                if (fileDir) {
                    // 只查找 .class 文件,注意写法 ~/.*\.class/
                    fileDir.traverse(type: FileType.FILES, nameFilter: ~/.*\.class/) { File file ->
                        System.out.println("findClass " + file.name)
                        //对class文件进行读取与解析
                        ClassReader classReader = new ClassReader(file.bytes)
                        //对class文件的写入
                        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                        //访问class文件相应的内容，解析到某一个结构就会通知到ClassVisitor的相应方法
                        ClassVisitor classVisitor = new LifecycleClassVisitor(classWriter)
                        //依次调用 ClassVisitor接口的各个方法
                        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
                        //toByteArray方法会将最终修改的字节码以 byte 数组形式返回。
                        byte[] bytes = classWriter.toByteArray()

                        //通过文件流写入方式覆盖掉原先的内容，实现class文件的改写。
                        //FileOutputStream outputStream = new FileOutputStream( file.parentFile.absolutePath + File.separator + fileName)
                        FileOutputStream outputStream = new FileOutputStream(file.path)
                        outputStream.write(bytes)
                        outputStream.close()
                    }
                }

                //处理完输入文件后把输出传给下一个文件
                def dest = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes,
                        directoryInput.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(directoryInput.file, dest)
            }
        }

    }
}
