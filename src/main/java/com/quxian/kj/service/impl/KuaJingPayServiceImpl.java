package com.quxian.kj.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.quxian.kj.service.KuaJingPayService;
import com.quxian.kj.util.DateUtil;
import com.quxian.kj.util.RequestUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class KuaJingPayServiceImpl implements KuaJingPayService {

    @Value("${kuajing.url.goods}")
    String getGoodsUrl;
    @Value("${kuajing.url.rate}")
    String getRateUrl;
    @Value("${kuajing.url.order-pay}")
    String orderPayUrl;
    @Value("${kuajing.url.order-detail}")
    String orderDetailUrl;

    @Value("${kuajing.merid}")
    String merId;
    @Value("${kuajing.md5Key}")
    String md5Key;
    @Value("${kuajing.version}")
    String version;

    RestTemplate restTemplate;

    @Override
    public Map getGoods() {

        String reqType = "ObtainWares";
        String reqDate = DateUtil.getDate();
        String reqTime = DateUtil.getTime();
        //签名参数组装
        String waitChkValue = new StringBuilder(version).append(reqType).
                append(reqDate).append(reqTime).
                append(merId).append(md5Key).toString();
        System.out.println("待加密的明文：" + waitChkValue);
        //参数签名加密
        String chkValue = SecureUtil.md5(waitChkValue);
        System.out.println("加密后的密文：" + chkValue);
        //开始参数封装
        MultiValueMap<String,String> body = new LinkedMultiValueMap<>(16);
        body.add("version",version);
        body.add("reqType",reqType);
        body.add("reqDate", reqDate);
        body.add("reqTime",reqTime );
        body.add("merId",merId);
        body.add("chkValue",chkValue);
        //发送Post数据并返回数据
        Map map = RequestUtil.sendPostRequest(getGoodsUrl, body);
        return map;
    }

    @Override
    public Map getRate() {
        String reqType = "ObtainForeignRate";
        String reqDate = DateUtil.getDate();
        String reqTime = DateUtil.getTime();
        //签名参数组装
        String waitChkValue = new StringBuilder(version).append(reqType).append(reqDate).append(reqTime).append(merId).append(md5Key).toString();
        System.out.println("待加密的明文：" + waitChkValue);
        //参数签名加密
        String chkValue = SecureUtil.md5(waitChkValue);
        System.out.println("加密后的密文：" + chkValue);
        //开始参数封装
        MultiValueMap<String,String> body = new LinkedMultiValueMap<>(16);
        body.add("version",version);
        body.add("reqType",reqType);
        body.add("reqDate", reqDate);
        body.add("reqTime",reqTime );
        body.add("merId",merId);
        body.add("chkValue",chkValue);

        //发送Post数据并返回数据
        Map map = RequestUtil.sendPostRequest(getRateUrl, body);
        return map;
    }

    @Override
    public String orderPay(Map<String, Object> params) {
        String version = "10";
        String reqType = "OrderPay";
        String reqDate = DateUtil.getDate();
        String reqTime = DateUtil.getTime();
        String ordId = RandomUtil.randomString(20);
        String ordAmt;
        String gateId = "ebth5pay";
        String merPriv = ""; //商户私有域，有值时参与验签 可空
        String retUrl = "aaa";
        String bgRetUrl = "bbb";
        String prepayId = "";//交易卡信息 可空
        String currencyCode = "GBP";//币种（商品获取接口返回）
        String pcs = "1";
        String exchangeRate = "9.0409";//交易汇率（汇率获取接口返回）
        String foreignAmt = "2.00";//外币金额
        String goodsNo = "GA19090903629116";//商品编号
        ordAmt = new BigDecimal(foreignAmt).multiply(new BigDecimal(exchangeRate)).setScale(2,BigDecimal.ROUND_UP).toString();
        System.out.println("订单ID：" + ordId + "，订单金额：" + ordAmt);
        //签名参数组装
        String waitChkValue = new StringBuilder(version).append(reqType).append(merId).append(reqDate).append(reqTime).append(ordId).append(ordAmt).append(gateId)
                .append(retUrl).append(bgRetUrl).append(currencyCode).append(pcs).append(exchangeRate).append(foreignAmt).append(goodsNo).append(md5Key).toString();
        System.out.println("待加密的明文：" + waitChkValue);
        //参数签名加密
        String chkValue = SecureUtil.md5(waitChkValue);
        System.out.println("加密后的密文：" + chkValue);
        //开始参数封装
        MultiValueMap<String,String> body = new LinkedMultiValueMap<>(32);
        body.add("version",version);
        body.add("reqType",reqType);
        body.add("merId",merId);
        body.add("reqDate", reqDate);
        body.add("reqTime",reqTime );
        body.add("ordId", ordId);
        body.add("ordAmt", ordAmt);
        body.add("gateId", gateId);
        body.add("merPriv", merPriv);
        body.add("retUrl", retUrl);
        body.add("bgRetUrl", bgRetUrl);
        body.add("prepayId", prepayId);
        body.add("currercyCode", currencyCode);
        body.add("pcs", pcs);
        body.add("exchangeRate", exchangeRate);
        body.add("foreignAmt", foreignAmt);
        body.add("goodsNo", goodsNo);
        body.add("chkValue",chkValue);

        //发送Post数据并返回数据
        String content = RequestUtil.sendPostRequestStr(orderPayUrl, body);
        return content;
    }

    /**
     * 查询订单详情
     * @return
     */
    @Override
    public Map getOrderDetail(String ordId) {
        String version = "10";
        String reqType = "ObtainForeignRate";
        //签名参数组装
        String waitChkValue = new StringBuilder(version).append(reqType).append(merId).append(ordId).append(md5Key).toString();
        System.out.println("待加密的明文：" + waitChkValue);
        //参数签名加密
        String chkValue = SecureUtil.md5(waitChkValue);
        System.out.println("加密后的密文：" + chkValue);
        //开始参数封装
        MultiValueMap<String,String> body = new LinkedMultiValueMap<>(16);
        body.add("version",version);
        body.add("reqType",reqType);
        body.add("ordId", ordId);
        body.add("merId",merId);
        body.add("chkValue",chkValue);

        //发送Post数据并返回数据
        Map map = RequestUtil.sendPostRequest(orderDetailUrl, body);
        return map;
    }
}
