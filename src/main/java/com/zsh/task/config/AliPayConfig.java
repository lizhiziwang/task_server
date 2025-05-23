//package com.zsh.task.config;
//
//import com.alipay.easysdk.factory.Factory;
//import com.alipay.easysdk.kernel.Config;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//
//import javax.annotation.PostConstruct;
//
//
//@Data
//@Slf4j
////@Configuration // 一定不要忽略此注解
//public class AliPayConfig {
//    @Value("${alipay.appId}")
//    private String appId;
//    @Value("${alipay.appPrivateKey}")
//    private String appPrivateKey;
//    @Value("${alipay.alipayPublicKey}")
//    private String alipayPublicKey;
//    @Value("${alipay.notifyUrl}")
//    private String notifyUrl;
//
//
//    @PostConstruct
//    public void init() {
//        // 设置参数（全局只需设置一次）
//        Config config = new Config();
//        config.protocol = "https";
//        config.gatewayHost = "openapi.alipaydev.com";
//        config.signType = "RSA2";
//        config.appId = this.appId;
//        config.merchantPrivateKey = this.appPrivateKey;
//        config.alipayPublicKey = this.alipayPublicKey;
//        config.notifyUrl = this.notifyUrl;
//        Factory.setOptions(config);
//        System.out.println("=======支付宝SDK初始化成功=======");
//    }
//
//
//}