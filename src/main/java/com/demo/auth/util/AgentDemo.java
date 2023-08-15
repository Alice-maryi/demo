package com.demo.auth.util;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.RedissonLock;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.instrument.Instrumentation;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class AgentDemo {


    private static String className = "8";
    private static String methodName = "8";

    public static void main(String[] args) {


    }

    public static void agentMain(String args, Instrumentation instrumentation) {
        try {
            List<Class<?>> classList = new LinkedList<>();
            Class<?>[] loadedClasses = instrumentation.getAllLoadedClasses();
            for (Class<?> loadedClass : loadedClasses) {
                if (loadedClass.getName().equals(className)) {
                    classList.add(loadedClass);
                }
            }
            instrumentation.addTransformer(new TestTransformer(className, methodName));
            instrumentation.retransformClasses(classList.toArray(new Class[0]));
        } catch (Exception e) {

        }
    }

    public static void premain(String args, Instrumentation instrumentation) {
        instrumentation.addTransformer(new TestTransformer(className, methodName));
    }

}
