package com.quxian.kj.service;

import org.springframework.stereotype.Service;

import java.util.Map;

public interface KuaJingPayService {
    Map getGoods();

    Map getRate();

    Map orderPay(Map<String, Object> params);

    Map getOrderDetail(String ordId);
}
