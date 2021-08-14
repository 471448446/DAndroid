package danny.jiang.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created by better on 2020/5/31 10:23 PM.
 */
public class LifecycleClassVisitor extends ClassVisitor {
    private String className;
    private String superName;

    public LifecycleClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.className = name;
        this.superName = superName;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        System.out.println("¡ClassVisitor visitMethod name-------" + name + ", superName is " + superName);
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        /**
         * 过滤出继承自 AppCompatActivity 的文件，并在 LifeCycleMethodVisitor.java 中对 onCreate 进行改造。
         */
//        if (superName.equals("android/support/v7/app/AppCompatActivity")) {
        if (superName.equals("androidx/appcompat/app/AppCompatActivity")) {
            if (name.startsWith("onCreate")) {
                //处理onCreate()方法
                return new LifeCycleMethodVisitor(mv, className, name);
            }
        }
        return mv;
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }

}
