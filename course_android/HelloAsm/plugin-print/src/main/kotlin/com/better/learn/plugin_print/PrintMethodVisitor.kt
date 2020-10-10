package com.better.learn.plugin_print

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes


class PrintMethodVisitor(p1: MethodVisitor?) : MethodVisitor(Opcodes.ASM5, p1) {
    override fun visitCode() {
        super.visitCode()

        mv.visitLdcInsn("TAG")
        mv.visitLdcInsn("===== This is just a test message =====")
        mv.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            "android/util/Log",
            "e",
            "(Ljava/lang/String;Ljava/lang/String;)I",
            false
        )
        mv.visitInsn(Opcodes.POP)
    }
}