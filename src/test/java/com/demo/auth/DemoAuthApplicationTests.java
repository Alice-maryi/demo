package com.demo.auth;

import com.demo.auth.util.AgentDemo;
import com.demo.auth.util.Client;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.instrument.Instrumentation;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
public class DemoAuthApplicationTests {

    @Autowired
    private Redisson redisson;

    @Test
    public void contextLoad() {
        RLock lock = redisson.getLock("testLock");
        try {
            lock.tryLock(10, 30, TimeUnit.SECONDS);
            System.out.println("lock success");
        } catch (Exception ignored) {
            System.out.println("exception");
        } finally {
//            lock.unlock();
        }
    }

    public static void main(String[] args) {
        Locale locale = Locale.CHINA;
        System.out.println(locale.getLanguage());
        System.out.println(locale.getCountry());

    }


}

