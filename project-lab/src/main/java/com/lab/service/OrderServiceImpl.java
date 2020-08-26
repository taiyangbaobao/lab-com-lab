package com.lab.service;

import org.springframework.stereotype.Service;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
    @Override
    public String createOrder(String orderId) {
        System.out.println("create-->"+orderId);
        return orderId+"--end";
    }
}
