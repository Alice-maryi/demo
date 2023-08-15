package com.demo.auth.util;

import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class TestTransformer implements ClassFileTransformer {

    private String targetClassName;
    private String targetVMClassName;
    private String targetMethodName;

    public TestTransformer(String className, String methodName) {
        this.targetVMClassName = className.replaceAll("\\.", "\\/");
        this.targetClassName = className;
        this.targetMethodName = methodName;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className.equals(targetVMClassName)) {
            return classfileBuffer;
        }
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass cls = pool.get(this.targetClassName);
            CtMethod ctMethod = cls.getDeclaredMethod(this.targetMethodName);
            ctMethod.insertBefore("{ System.out.println(\"start\"); }");
            ctMethod.insertAfter("{ System.out.println(\"end\"); }");
            return cls.toBytecode();
        } catch (Exception e) {
            return classfileBuffer;
        }
    }
}
