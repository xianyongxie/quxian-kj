package com.quxian.kj.controller;

import cn.hutool.crypto.SecureUtil;
import com.quxian.kj.util.DateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class PayController {

    @Value("${kuajing.url.goods}")
    String getGoodsUrl;
    @Value("${kuajing.merid}")
    String merId;
    @Value("${kuajing.md5Key}")
    String md5Key;
    @Value("${kuajing.version}")
    String version;

    RestTemplate restTemplate;

    /**
     * 获取商品
     * @return
     */
    @RequestMapping("/getGoods")
    public String getGoods(){
        HttpHeaders httpHeaders = new HttpHeaders();
        //请求头设置Form表单提交
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
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
        //请求接口
        String response = restTemplate.postForObject(getGoodsUrl,new HttpEntity<>(body,httpHeaders),String.class);
        System.out.println("请求返回结果：" + response);
        return response;
    }
}
