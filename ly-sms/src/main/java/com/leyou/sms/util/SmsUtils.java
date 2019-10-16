package com.leyou.sms.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.leyou.sms.config.SmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 17:36 2019/5/14
 */
@Slf4j
@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsUtils {

    private static final String QUERY_PATH = "https://api.miaodiyun.com/20150822/industrySMS/sendSMS";

    private static final String KEY_PREFIX = "sms:phone:";
    private static final long SMS_MIN_INTERVAL_IN_MILLIS = 120000;

    private static final String CODE_PREFIX = "user:verify:phone:";


    @Autowired
    private SmsProperties smsProperties;

    @Autowired
    private StringRedisTemplate redisTemplate;


    // 根据相应的手机号发送验证码
    public String getCode(String phone) {
        String key_phone = KEY_PREFIX + phone;
        String key_code = CODE_PREFIX + phone;
        //  读取时间
        String lastTime = redisTemplate.opsForValue().get(key_phone);
        if(StringUtils.isNotBlank(lastTime)){
            Long last = Long.valueOf(lastTime);
            if(System.currentTimeMillis() - last < SMS_MIN_INTERVAL_IN_MILLIS){
                log.info("[短信服务] 发送短信频率过高，被拦截，手机号码{}",phone);
                return null;
            }
        }

        String rod = smsCode();
        String timestamp = getTimestamp();
        String sig = getMD5(smsProperties.getACCOUNT_SID(), smsProperties.getAUTH_TOKEN(), timestamp);
        String tamp = "【一旬科技】您的验证码为：" + rod + "，请于"+2+"分钟内正确输入，如非本人操作，请忽略此短信。";
        OutputStreamWriter out = null;
        BufferedReader br = null;
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(QUERY_PATH);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);// 设置是否允许数据写入
            connection.setDoOutput(true);// 设置是否允许参数数据输出
            connection.setConnectTimeout(5000);// 设置链接响应时间
            connection.setReadTimeout(10000);// 设置参数读取时间
            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            // 提交请求
            out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            String args = getQueryArgs(smsProperties.getACCOUNT_SID(), tamp, phone, timestamp, sig, "JSON");
            System.out.println(args);
            out.write(args);
            out.flush();
            // 读取返回参数

            br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String temp = "";
            while ((temp = br.readLine()) != null) {
                result.append(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject json = new JSONObject(result.toString());
        String respCode = json.getString("respCode");
        String defaultRespCode = "00000";
        if (defaultRespCode.equals(respCode)) {
            //  发送短信成功后写入redis  同时指定生存时间   同时保存验证码
            log.info("[短信服务] 发送短信成功，手机号码{}",phone);
            redisTemplate.opsForValue().set(key_phone,String.valueOf(System.currentTimeMillis()),2, TimeUnit.MINUTES);
            redisTemplate.opsForValue().set(key_code,rod,2, TimeUnit.MINUTES);
            return rod;
        } else {
            return defaultRespCode;
        }
    }


    // 定义一个请求参数拼接方法
    public static String getQueryArgs(String accountSid, String smsContent, String to, String timestamp, String sig,
                                      String respDataType) {
        return "accountSid=" + accountSid + "&smsContent=" + smsContent + "&to=" + to + "&timestamp=" + timestamp
                + "&sig=" + sig + "&respDataType=" + respDataType;
    }

    // 获取时间戳
    public static String getTimestamp() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    // sing签名
    public static String getMD5(String sid, String token, String timestamp) {

        StringBuilder result = new StringBuilder();
        String source = sid + token + timestamp;
        // 获取某个类的实例
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            // 要进行加密的东西
            byte[] bytes = digest.digest(source.getBytes());
            for (byte b : bytes) {
                String hex = Integer.toHexString(b & 0xff);
                if (hex.length() == 1) {
                    result.append("0" + hex);
                } else {
                    result.append(hex);
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    // 创建验证码
    public static String smsCode() {
        String random = (int) ((Math.random() * 9 + 1) * 100000) + "";
        return random;
    }

}
