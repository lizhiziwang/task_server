//package com.zsh.task.controller;
//
//import com.alipay.api.AlipayApiException;
//import com.alipay.api.AlipayClient;
//import com.alipay.api.DefaultAlipayClient;
//import com.alipay.api.domain.AlipayFundTransUniTransferModel;
//import com.alipay.api.domain.AlipayUserInfoAuthModel;
//import com.alipay.api.domain.Participant;
//import com.alipay.api.request.AlipayFundTransUniTransferRequest;
//import com.alipay.api.request.AlipayTradePagePayRequest;
//import com.alipay.api.request.AlipayUserInfoAuthRequest;
//import com.alipay.api.response.AlipayFundTransUniTransferResponse;
//import com.alipay.api.response.AlipayUserInfoAuthResponse;
//import com.alipay.easysdk.factory.Factory;
//import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
//import com.zsh.task.common.LoginUserThreatContext;
//import com.zsh.task.common.Result;
//import com.zsh.task.config.AliPayConfig;
//import com.zsh.task.entity.AliPay;
//import com.zsh.task.entity.MoneyRecord;
//import com.zsh.task.entity.Order;
//import com.zsh.task.service.MoneyRecordService;
//import com.zsh.task.service.OrderService;
//import com.zsh.task.service.UserService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.*;
//
//@Slf4j
//@RestController
//@RequestMapping("/alipay")
//public class AliPayController {
//    @Resource
//    AliPayConfig aliPayConfig;
//    @Resource
//    UserService us;
//    @Resource
//    MoneyRecordService mrs;
//    @Resource
//    UserCache uc;
//
//    //order
//    @Resource
//    OrderService os;
//
//    private static final String GATEWAY_URL ="https://openapi-sandbox.dl.alipaydev.com/gateway.do";
//    private static final String FORMAT ="JSON";
//    private static final String CHARSET ="utf-8";
//    private static final String SIGN_TYPE ="RSA2";
//
//    @PostMapping("/notify")  // 注意这里必须是POST接口
//    public String payNotify(HttpServletRequest request) throws Exception {
//        log.info("=========支付宝异步回调========");
//
//        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
//            Map<String, String> params = new HashMap<>();
//            Map<String, String[]> requestParams = request.getParameterMap();
//            for (String name : requestParams.keySet()) {
//                params.put(name, request.getParameter(name));
//                // System.out.println(name + " = " + request.getParameter(name));
//            }
//
//            String tradeNo = params.get("out_trade_no");
//            String gmtPayment = params.get("gmt_payment");
//            String alipayTradeNo = params.get("trade_no");
//            // 支付宝验签
//            if (Factory.Payment.Common().verifyNotify(params)) {
//                // 验签通过
//                log.info("交易名称: " + params.get("subject"));
//                log.info("交易状态: " + params.get("trade_status"));
//                log.info("支付宝交易凭证号: " + params.get("trade_no"));
//                log.info("商户订单号: " + params.get("out_trade_no"));
//                log.info("交易金额: " + params.get("total_amount"));
//                log.info("买家在支付宝唯一id: " + params.get("buyer_id"));
//                log.info("买家付款时间: " + params.get("gmt_payment"));
//                log.info("买家付款金额: " + params.get("buyer_pay_amount"));
//                // 更新订单未已支付
//                Order order = new Order();
//                order.setId(Long.valueOf(tradeNo));
//                order.setUpdateTime(new Date())
////                        .setState(OrderState.PAID.code);
////                order.setCheckoutTime(params.get("gmt_payment"));
//                os.updateById(order);
//            }
//        }
//        return "success";
//    }
//
//    //http://localhost:8062/alipay/pay?subject=%E6%B8%B8%E6%88%8F%E8%B4%A6%E5%8F%B7&traceNo=4564655465654645&totalAmount=100
//
//    //充值
//    @GetMapping("/pay") // &subject=xxx&traceNo=xxx&totalAmount=xxx
//    public void pay(AliPay aliPay, HttpServletResponse httpResponse) throws Exception {
//        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
//                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
//        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
//        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
//        request.setBizContent("{\"out_trade_no\":\"" + aliPay.getTraceNo() + "\","
//                + "\"total_amount\":\"" + aliPay.getTotalAmount() + "\","
//                + "\"subject\":\"" + aliPay.getSubject() + "\","
//                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
//        String form = "";
//        try {
//            // 调用SDK生成表单
//            form = alipayClient.pageExecute(request).getBody();
//        } catch (AlipayApiException e) {
//            e.printStackTrace();
//        }
//        httpResponse.setContentType("text/html;charset=" + CHARSET);
//        // 直接将完整的表单html输出到页面
//        httpResponse.getWriter().write(form);
//        httpResponse.getWriter().flush();
//        httpResponse.getWriter().close();
//
//        // 开发无法执行回调，再辞手动
//        us.purseUpOrDown(-aliPay.getTotalAmount(),aliPay.getCurrentUserId());
//        uc.updateCache(aliPay.getCurrentUserId());
//    }
//
//    //提现
//    @GetMapping("money")
//    public Result<Boolean> money(
//            @RequestParam(name = "orderId")String orderId,
//            @RequestParam(name = "amount")String amount,
//            @RequestParam(name = "aliId")String aliId
//                                            ){
//        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
//                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
//        // 构造请求参数以调用接口
//        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
//        AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();
//
//       try{
//           // 设置商家侧唯一订单号
//           model.setOutBizNo(orderId);
//
//           // 设置订单总金额
//           model.setTransAmount(amount);
//
//           // 设置描述特定的业务场景
//           model.setBizScene("DIRECT_TRANSFER");
//
//           // 设置业务产品码
//           model.setProductCode("TRANS_ACCOUNT_NO_PWD");
//
//           // 设置转账业务的标题
//           model.setOrderTitle("账户提现");
//
//           // 设置收款方信息
//           Participant payeeInfo = new Participant();
//
//           payeeInfo.setIdentity(aliId);
//           payeeInfo.setIdentityType("ALIPAY_USER_ID");
//           model.setPayeeInfo(payeeInfo);
//           // 设置业务备注
//           model.setRemark("201905代发");
//
//           // 设置转账业务请求的扩展参数
//           model.setBusinessParams("{\"payer_show_name_use_alias\":\"true\"}");
//
//           request.setBizModel(model);
//           AlipayFundTransUniTransferResponse response = alipayClient.execute(request);
//           log.info(response.getBody());
//
//           if (response.isSuccess()) {
//               // 设置用户钱包
//               us.purseUpOrDown(Double.parseDouble(amount),LoginUserThreatContext.getUser().getId());
//               uc.updateCache(LoginUserThreatContext.getUser().getId());
//
//               // 设置充值结果
//               UpdateWrapper<MoneyRecord> uw = new UpdateWrapper<>();
//               uw.set("update_time",new Date())
//                               .set("result","sucess")
//                                       .set("alipay_id",response.getOrderId())
//                                               .eq("id",orderId);
//               mrs.update(uw);
//               log.info("提现成功");
//           } else {
//               log.info("提现失败");
//           }
//       } catch (AlipayApiException e) {
//           log.error(e.getMessage());
//       }
//        return Result.succeed(true);
//    }
//
//    @GetMapping("/a")
//    public void a() throws AlipayApiException {
//        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
//                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
//        // 构造请求参数以调用接口
//        AlipayUserInfoAuthRequest request = new AlipayUserInfoAuthRequest();
//        AlipayUserInfoAuthModel model = new AlipayUserInfoAuthModel();
//
//        // 设置接口权限值
//        List<String> scopes = new ArrayList<String>();
//        scopes.add("auth_base");
//        model.setScopes(scopes);
//
//        model.setState("init");
//
//        request.setBizModel(model);
//        // 第三方代调用模式下请设置app_auth_token
//        // request.putOtherTextParam("app_auth_token", "<-- 请填写应用授权令牌 -->");
//
//        AlipayUserInfoAuthResponse response = alipayClient.pageExecute(request, "POST");
//        // 如果需要返回GET请求，请使用
//        // AlipayUserInfoAuthResponse response = alipayClient.pageExecute(request, "GET");
//        String pageRedirectionData = response.getBody();
//        System.out.println(pageRedirectionData);
//
//        if (response.isSuccess()) {
//            System.out.println("调用成功");
//        } else {
//            System.out.println("调用失败");
//            // sdk版本是"4.38.0.ALL"及以上,可以参考下面的示例获取诊断链接
//            // String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
//            // System.out.println(diagnosisUrl);
//        }
//
//    }
//}
