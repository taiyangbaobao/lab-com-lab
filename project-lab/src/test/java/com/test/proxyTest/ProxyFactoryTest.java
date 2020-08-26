package com.test.proxyTest;

import com.lab.service.OrderService;
import com.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProxyFactoryTest extends BaseTest {
    @Autowired
    OrderService orderServiceP;

    @Test
    public void test(){
        String a = orderServiceP.createOrder("23q341");
        System.out.println("a--->"+a);
    }
}
