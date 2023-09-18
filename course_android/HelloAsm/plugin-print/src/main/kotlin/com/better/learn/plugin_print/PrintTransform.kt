package com.better.learn.plugin_print

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.FileOutputStream

class PrintTransform : Transform(), Plugin<Project> {

    override fun apply(project: Project) {
        println(">>>>>> this is a log just from PrintTransform")
        val appExtension = project.extensions.getByType(AppExtension::class.java)
        appExtension.registerTransform(this)
    }

    // 指明 Transform 的名字，也对应了该 Transform 所代表的 Task 名称，例如当返回值为 InjectTransform 时，编译后可以看到名为transformClassesWithInjectTransformForxxx 的 task。
    override fun getName(): String {
        return "KotlinPrintTransform"
    }

    //指明 Transform 处理的输入类型，在 TransformManager 中定义了很多类型：
    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    //指明 Transform 输入文件所属的范围, 因为 gradle 是支持多工程编译的。
    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT //所有project
    }

    //指明该 Transform 是否支持增量编译。有时即使返回 true, 在某些情况下它还是会当作 false 返回。
    override fun isIncremental(): Boolean {
        return false
    }

    //transform是一个空实现，input的内容将会打包成一个 TransformInvocation 对象。
    override fun transform(transformInvocation: TransformInvocation?) {
        super.transform(transformInvocation)
        val inputs = transformInvocation?.inputs
        val outputProvider = transformInvocation?.outputProvider

        if (!isIncremental) {
            outputProvider?.deleteAll()
        }
        // 根据条件遍历所有的class文件，在应用变更。写法差不多的
        inputs?.forEach { it ->
            it.directoryInputs.forEach {
                if (it.file.isDirectory) {
                    FileUtils.getAllFiles(it.file).forEach { file ->
                        val name = file.name
                        if (name.endsWith(".class") && name != ("R.class")
                            && !name.startsWith("R\$") && name != ("BuildConfig.class")
                        ) {

                            val classPath = file.absolutePath
                            println(">>>>>> classPath :$classPath")

                            val cr = ClassReader(file.readBytes())
                            val cw = ClassWriter(cr, ClassWriter.COMPUTE_MAXS)
                            val visitor = PrintClassVisitor(cw)
                            cr.accept(visitor, ClassReader.EXPAND_FRAMES)

                            val bytes = cw.toByteArray()

                            val fos = FileOutputStream(classPath)
                            fos.write(bytes)
                            fos.close()
                        }
                    }
                }

                val dest = outputProvider?.getContentLocation(
                    it.name,
                    it.contentTypes,
                    it.scopes,
                    Format.DIRECTORY
                )
                FileUtils.copyDirectoryToDirectory(it.file, dest)
            }

            //  !!!!!!!!!! !!!!!!!!!! !!!!!!!!!! !!!!!!!!!! !!!!!!!!!!
            //使用androidx的项目一定也注意jar也需要处理，否则所有的jar都不会最终编译到apk中，千万注意
            //导致出现ClassNotFoundException的崩溃信息，当然主要是因为找不到父类，因为父类AppCompatActivity在jar中
            it.jarInputs.forEach {
                val dest = outputProvider?.getContentLocation(
                    it.name,
                    it.contentTypes,
                    it.scopes,
                    Format.JAR
                )
                FileUtils.copyFile(it.file, dest)
            }
        }

    }
}