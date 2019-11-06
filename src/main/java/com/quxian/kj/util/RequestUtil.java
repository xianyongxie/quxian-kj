package com.quxian.kj.util;

import org.dom4j.DocumentException;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class RequestUtil {

    /**
     * 向目的URL发送post请求
     * @param url       目的url
     * @param params    发送的参数
     */
    public static Map sendPostRequest(String url, MultiValueMap<String, String> params){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.POST;
        // 以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //将请求头部和参数合成一个请求
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        String response = restTemplate.postForObject(url,requestEntity,String.class);
        System.out.println("请求返回结果：" + response);
        Map map = new HashMap();
        try {
            //将XML解析成map返回
            map = Xml2Map.xml2map(response);
            System.out.println("");
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return map;
    }
}
