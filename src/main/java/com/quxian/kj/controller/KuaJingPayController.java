package com.quxian.kj.controller;

import cn.hutool.crypto.SecureUtil;
import com.quxian.kj.service.KuaJingPayService;
import com.quxian.kj.util.DateUtil;
import com.quxian.kj.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class KuaJingPayController {

    @Autowired
    KuaJingPayService kuaJingPayService;

    /**
     * 获取商品
     * @return
     */
    @RequestMapping("/getGoods")
    public Map<String,Object> getGoods(){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        Map map = kuaJingPayService.getGoods();
        if (map.get("resultCode").equals("000000")){
            List<Map<String,Object>> goodsList = (List<Map<String, Object>>) ((Map)map.get("list")).get("item");
            resultMap.put("code","1");
            resultMap.put("data",goodsList);
        }else{
            resultMap.put("code",0);
            resultMap.put("resultMsg",map.get("resultMsg"));
        }
        return resultMap;
    }

    /**
     * 获取汇率
     * @return
     */
    @RequestMapping("/getRate")
    public Map<String,Object> getRate(){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        Map map = kuaJingPayService.getRate();
        if (map.get("resultCode").equals("000000")){
            List<Map<String,Object>> goodsList = (List<Map<String, Object>>) ((Map)map.get("list")).get("item");
            resultMap.put("code","1");
            resultMap.put("data",goodsList);
        }else{
            resultMap.put("code",0);
            resultMap.put("resultMsg",map.get("resultMsg"));
        }
        return resultMap;
    }


    /**
     * H5 交易预下单
     * @return
     */
    @RequestMapping("/orderPay")
    public Map<String,Object> orderPay(HttpServletRequest request){
        String ordAmt = request.getParameter("ordAmt");  //交易金额
        return null;
    }

}
