package com.quxian.kj.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.quxian.kj.service.KuaJingPayService;
import com.quxian.kj.util.DateUtil;
import com.quxian.kj.util.RequestUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class KuaJingPayServiceImpl implements KuaJingPayService {

    @Value("${kuajing.url.goods}")
    String getGoodsUrl;
    @Value("${kuajing.url.rate}")
    String getRateUrl;

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
}
