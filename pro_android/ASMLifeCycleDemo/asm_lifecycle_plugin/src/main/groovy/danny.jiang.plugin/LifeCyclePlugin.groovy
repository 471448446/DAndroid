package danny.jiang.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

public class LifeCyclePlugin implements Plugin<Project> {
    void apply(Project project) {
        System.out.println("hello world")
        def android = project.extensions.getByType(AppExtension)
        println 'register auto transform for lifecycle'
        android.registerTransform(new LifeCycleTransform())
    }
}