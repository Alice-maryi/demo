package com.example.demo1;

import com.train.po.Excel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: Y.C
 * @Date: 2021/6/22 6:07 下午
 */
@Slf4j
public class ExcelTest extends Thread{

    @SneakyThrows
    public void run(){
        KieServices kieServices = KieServices.Factory.get();
        //获得kie容器对象
        //默认自动加载META-INF/kmodule.xml
        //从kieServices中获取kieContainer实例，会加载kmodule.xml文件并load规则文件
        KieContainer kieContainer = kieServices.newKieClasspathContainer();
        //从kie容器对象中获取会话对象
        KieSession session = kieContainer.newKieSession();

        System.out.println();
        System.out.println("睡眠");
        Thread.sleep(5000);
        System.out.println("睡眠完毕");
        //Fact对象，实例对象
        Order order = new Order();
        order.setOriginalPrice(200d);

        //order对象插入规则
        session.insert(order);

        int count = session.fireAllRules();
        System.out.println(count);


        //关闭会话
        session.dispose();

        System.out.println("优惠后的价格：" + order.getRealPrice());
        System.out.println(order.getOriginalPrice());
    }

    @Test
    public void test2(){
        ExcelTest threae = new ExcelTest();
        threae.run();

    }


}
