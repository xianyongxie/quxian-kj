package com.quxian.kj.util;

import org.dom4j.*;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestUtil {

    /**
     * 向目的URL发送post请求
     * @param url       目的url
     * @param params    发送的参数
     */
    public static Map sendPostRequest(String url, MultiValueMap<String, String> params){
        RestTemplate restTemplate = restTemplate();

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

    /**
     * 支付回调
     * 请求参数转map
     * @param request
     * @return
     */
    public static HashMap<String, String> verifyParameters(HttpServletRequest request)
    {
        HashMap<String, String> params = new HashMap<String, String>();
        BufferedInputStream is = null;
        try
        {
            is = new BufferedInputStream(request.getInputStream());
            byte[] buffer = new byte[request.getContentLength()];
            int len = 0;
            if ((len = is.read(buffer)) == -1) System.out.println("参数不正确");
            String xml = new String(buffer, 0, len, "UTF-8");
            Document document = DocumentHelper.parseText(xml);
            Element root = (Element)document.getRootElement();
            for (int i = 0; i < root.nodeCount(); i++)
            {
                Node child = (Node)root.node(i);
                String name = child.getName();
                if (null == name) continue;
                String val = child.getText();

                params.put(name, val);
            }

            if (!"SUCCESS".equals(params.get("return_code"))) throw new Exception(params.get("return_msg"));

            return params;
        }
        catch(Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 获取restTemplate，解决中文乱码问题
     * @return
     */
    public static RestTemplate restTemplate(){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(5000);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(factory);
        restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(Charset.forName("GBK")));
        return restTemplate;
    }
}
