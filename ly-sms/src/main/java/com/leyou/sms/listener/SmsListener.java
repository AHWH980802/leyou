package com.leyou.sms.listener;

import com.leyou.sms.util.SmsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 17:46 2019/5/14
 */
@Component
public class SmsListener {

    @Autowired
    private SmsUtils smsUtils;

    /**
     * 发短信验证码
     * @param phone
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "sms.verify.code.queue", durable = "true"),
            exchange = @Exchange(name="ly.sms.exchange",type = ExchangeTypes.TOPIC),
            key = "sms.verify.code"
    ))
    public void listenSms(String phone) {
        if (StringUtils.isBlank(phone)) {
            return;
        }
        smsUtils.getCode(phone);
    }
}
