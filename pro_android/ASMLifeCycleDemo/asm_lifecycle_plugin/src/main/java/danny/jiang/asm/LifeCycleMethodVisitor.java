package danny.jiang.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created by better on 2020/5/31 10:26 PM.
 */
public class LifeCycleMethodVisitor extends MethodVisitor {
    private String className;
    private String methodName;

    public LifeCycleMethodVisitor(MethodVisitor methodVisitor, String className, String methodName) {
        super(Opcodes.ASM5, methodVisitor);
        this.className = className;
        this.methodName = methodName;
    }

    //方法执行前插入
    @Override
    public void visitCode() {
        super.visitCode();
        /**
         * 可以看出 ASM 都是直接以字节码指令的方式进行操作的，所以如果想使用 ASM，需要程序员对字节码有一定的理解。
         * 如果对字节码不是很了解，也可以借助三方工具 ASM Bytecode Outline 来生成想要的字节码。¡¡
         */
        System.out.println("MethodVisitor visitCode------");

        mv.visitLdcInsn("TAG");
        mv.visitLdcInsn(className + "---->" + methodName);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(Opcodes.POP);
    }
}
