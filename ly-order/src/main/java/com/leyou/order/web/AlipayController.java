package com.leyou.order.web;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.leyou.order.dto.OrderDto;
import com.leyou.order.pojo.Order;
import com.leyou.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.alipay.api.AlipayConstants.APP_ID;
import static org.apache.catalina.manager.Constants.CHARSET;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 16:43 2019/5/18
 */
@Slf4j
@Controller
@RequestMapping("alipay")
public class AlipayController {

    @Autowired
    private OrderService orderService;

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016091700533496";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDABZx38aujiX5dLaPojUSnm+zdQg675qdfL71TzwhaFkQX84FUNJOSA9wIGzUpKEQy/8Isrsox6OvLpsuYjxjN0zqYgP7sGis+p0GWTV2kMjAtEeaxkqtifsdDRa8aO6z22n0mty2RkRE98T/fl9m4iokkW3g4Lc7f4Tm3RR1ooZ4+LigYgaisSkD5DRY0jglC2DjVnK/4kNFEiCuedP503wrWzh9PIzImu5bZVMk82Xhu04p9/0rN3c77yVulYs7pdS+oxjjheG9OksdACQml/e787L7FzYCZOQ/lKpd+P0lrU7dXxvcZIgHuwDrlr7UMTozd6j4LmMW2jeb5smEvAgMBAAECggEASihgmznsDYZmFNoxePhF0W04duKyCgM0FGC9f4ZvNUtwFMfn5HtBmpx+92p6h2CTphZ8CQGf+NJ5bfcGne51p9exw3/d85TPqqyxebh7N5RiYqWNMHryUWGg1uwsibcOP2918ePAZ31c6U0oW9OrM8fc2vuwXBHk2TWEMrEwgF1vG/bE7gRqFQ10jOuMkhkXEtcHYMSYvwLem6GQzMPC7YrFR4YgnplAVSliNB90QPuqIDI+MH9XXhY5Hqq6wKlo1k8BIDRJUrAE3Z1Y7Qebm0tgtojRziTwQypmew279XH8zaUU4Z8KQ2XdzSEtEpTTEZDRdJwBf2D4XiG2ap/noQKBgQDfKLF2MrnbOXVkk4rtk6q2mMNcPd1Xwqf/XHYpkBm1NameCSxBGA4k5T6b9VRmRlTYaZajNt6EPkJPJ2ijR+Ndk5JvooPcXE4ETDeR8p4iHfgSyHs0JNhwXyb7i/PPocktSLMsBGzat0pdxil8JCsZGvZvhYa4FFxZ0yEwTyPytwKBgQDcR9viYQC/IbedE1D8nQcTgnjFc+GjTiCtL39sVjRzAcaZ86YdXP40Ic/tDxKmUlbLeV6peGQJ+GYKrG0/djIsImd1h0ln62kXqzT4Og2KWMHzaJlZXGnSbPsP8a3Rrj0AHyDu8nSC1qTqJDBxucqgNQg4J5n6p13XstqIN3gtSQKBgQCTCO/sjj2xOSmlLCW0pcAscXj/FO/7PBGPCq/8cKrPn9zzeN3EwgHHw5g+ECYdASg0M1YCkkuI3LC6EKA8PUiMxogMsAqrrdHluQZATphsqExkq92pnMFTQQ6hXTQ20UWJZcZQMVY7wdro8IEy/226K+7Fyjfan+e9c4QBnZN0/wKBgDhsg6s586oBpfmrH/yE8HIT4oGZTDu7YG1cFESY1Dh9RPTcTzmIP2j1HEB5suo9fxQBk7jPKLjREro7LoSp7vIVIfEd5TSkPTgd16SZoixEzfFeYqFcJZML7UdJPZ/PBLsKPjJWkneNKZL8EBihYYCl9OQFaNCmnEgK/SUVKEhZAoGBALX9dgKDh74sMcBvJjpyEz6FHGl8QfgtVZUP1IZP2wnyRGXPUwO6cw7BtW/kqpp65IGnFEDwdYFxEMYlNUZ+JXDpPyrvxi3YxDr3/SxqrZx2E60dLdJyjGtwMK/RM56QEHInLCrKc3/wIDymdD97YxM1TumGnm7Oq18HikW8+bSr";
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwAWcd/Gro4l+XS2j6I1Ep5vs3UIOu+anXy+9U88IWhZEF/OBVDSTkgPcCBs1KShEMv/CLK7KMejry6bLmI8YzdM6mID+7BorPqdBlk1dpDIwLRHmsZKrYn7HQ0WvGjus9tp9JrctkZERPfE/35fZuIqJJFt4OC3O3+E5t0UdaKGePi4oGIGorEpA+Q0WNI4JQtg41Zyv+JDRRIgrnnT+dN8K1s4fTyMyJruW2VTJPNl4btOKff9Kzd3O+8lbpWLO6XUvqMY44XhvTpLHQAkJpf3u/Oy+xc2AmTkP5SqXfj9Ja1O3V8b3GSIB7sA65a+1DE6M3eo+C5jFto3m+bJhLwIDAQAB";
    public static String alipay_public = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqSfpEIgEHrw8XLKvDSOoyVkxC/8ydXZrchhSfEoeyDpv3DgqzFICdzkthtuqheIFQ42WIk6zxc9yAqErUB8uMBRhE/JpPE0aKkvHzx6d1pSTSa1hgBkLJoEaensaI34Z7yK0vOCUXfjVscC+jozA58mTC0COQ4LK3kqEcqBZWEBMAXS78IX2TSsqNOxA903rhRE/wB4HE7cLcRUhjo2L8mnp7c3GU5gWI7PXiCVpqBHBal2yjLX+foSXcRT1yqRS9DDK653mIKh/y0NVlRTsDMa1a/Z4M4yXGUOGafkw60t0/O5wYxVHwELGjF2offijEJ+luOzI3gPqGQ74NolgIQIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://api.leyou.com/api/order-service/alipay/returnUrl";
//	public static String notify_url = "http://localhost:8080/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://api.leyou.com/api/order-service/alipay/notifyUrl";
//	public static String return_url = "http://localhost:8080/pay_success.do";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";

    // 仅支持JSON
    public static String format = "JSON";

    @PostMapping("/pay")
    public String pay(@RequestBody OrderDto orderDto, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception {
        System.out.println("进来了");
        //  新增订单
        try {
            String orderId = orderService.createOrder(orderDto);

            //  根据id查询订单
            Order order = orderService.queryOrderById(orderId);
            AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, app_id, merchant_private_key, format, charset,
                    alipay_public_key, sign_type); // 获得初始化的AlipayClient
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();// 创建API对应的request
            alipayRequest.setReturnUrl(return_url);
            alipayRequest.setNotifyUrl(notify_url);// 在公共参数中设置回跳和通知地址
            alipayRequest.setBizContent("{" + "    \"out_trade_no\":\"" + order.getOrderId() + "\","
                    + "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," + "    \"total_amount\":" + order.getTotalPay() + ","
                    + "    \"subject\":\"" + order.getOrderDetails().get(0).getTitle().substring(0, 15) + "..." + "\"," + "    \"body\":\"" + order.getBuyerMessage() + "\","
                    + "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\","
                    + "    \"extend_params\":{" + "    \"sys_service_provider_id\":\"2088511833207846\"" + "    }" + "  }");// 填充业务参数
            String form = "";
            try {
                form = alipayClient.pageExecute(alipayRequest).getBody(); // 调用SDK生成表单
            } catch (AlipayApiException e) {
                e.printStackTrace();
            }
            httpResponse.setContentType("text/html;charset=" + charset);
            httpResponse.getWriter().write(form);// 直接将完整的表单html输出到页面
            httpResponse.getWriter().flush();
            httpResponse.getWriter().close();
            String result = alipayClient.pageExecute(alipayRequest).getBody();
            return result;
        } catch (Exception e) {
            log.error("[订单服务] 创建订单失败 {}", e);
        }
        return null;
       /* AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do", app_id, merchant_private_key, "json", charset, alipay_public, "RSA2");
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        request.setBizModel(model);
        model.setOutTradeNo(String.valueOf(System.currentTimeMillis()));
        model.setTotalAmount("88.88");
        model.setSubject("Iphone6 16G");
        AlipayTradePrecreateResponse response = alipayClient.execute(request);
        System.out.print(response.getBody());
        System.out.print(response.getQrCode());*/

    }

    @RequestMapping(value = "/returnUrl", method = RequestMethod.GET)
    public void returnUrl(HttpServletRequest request, HttpServletResponse response)
            throws IOException, AlipayApiException {
        System.out.println("=================================同步回调=====================================");

        // 获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        System.out.println(params);
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipay_public_key, charset, sign_type);

        // ——请在这里编写您的程序（以下代码仅作参考）——
        if (signVerified) {
            // 商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            // 支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            // 付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

            System.out.println("商户订单号=" + out_trade_no);
            System.out.println("支付宝交易号=" + trade_no);
            System.out.println("付款金额=" + total_amount);

            response.getWriter().write(
                    "trade_no:" + trade_no + "<br/>out_trade_no:" + out_trade_no + "<br/>total_amount:" + total_amount);
        } else {
            response.getWriter().write("验签失败");
        }
        response.getWriter().flush();
        response.getWriter().close();
    }

    @RequestMapping(value = "/notifyUrl", method = RequestMethod.POST)
    public void notifyUrl(HttpServletRequest request, HttpServletResponse response)
            throws AlipayApiException, IOException {
        System.out.println("#################################异步回调######################################");

        // 获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        System.out.println(params);
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipay_public_key, charset, sign_type); // 调用SDK验证签名

        // ——请在这里编写您的程序（以下代码仅作参考）——

        /*
         * 实际验证过程建议商户务必添加以下校验： 1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
         * 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额）， 3、校验通知中的seller_id（或者seller_email)
         * 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
         * 4、验证app_id是否为该商户本身。
         */
        if (signVerified) {// 验证成功
            // 商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            // 支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            // 交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");

            System.out.println("商户订单号=" + out_trade_no);
            System.out.println("支付宝交易号=" + trade_no);
            System.out.println("交易状态=" + trade_status);
            if (trade_status.equals("TRADE_FINISHED")) {
                // 判断该笔订单是否在商户网站中已经做过处理
                // 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                // 如果有做过处理，不执行商户的业务程序

                // 注意：
                // 退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            } else if (trade_status.equals("TRADE_SUCCESS")) {
                // 判断该笔订单是否在商户网站中已经做过处理
                // 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                // 如果有做过处理，不执行商户的业务程序

                // 注意：
                // 付款完成后，支付宝系统发送该交易状态通知
            }

            System.out.println("异步回调验证成功");
            response.getWriter().write("success");

        } else {// 验证失败
            System.out.println("异步回调验证失败");
            response.getWriter().write("fail");

            // 调试用，写文本函数记录程序运行情况是否正常
            // String sWord = AlipaySignature.getSignCheckContentV1(params);
            // AlipayConfig.logResult(sWord);
        }
        response.getWriter().flush();
        response.getWriter().close();
    }

}
