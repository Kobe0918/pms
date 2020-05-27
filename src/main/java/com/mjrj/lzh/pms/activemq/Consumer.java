package com.mjrj.lzh.pms.activemq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.activemq
 * @Author: lzh
 * @CreateTime: 2020-03-05 15:58
 * @Description: ${Description}
 */
@Component
@Slf4j
public class Consumer {

    @Autowired
    private AliSendMessage aliSendMessage;
    @Autowired
    private MailSender mailSender;

    /**
     * 消费者
     * @SendTo("sendMail") 双向队列，当sendsms处理完，结果返回到sendmail中去
     * @param map 存储电话号码和验证码
     * @return
     */
    @JmsListener(destination = "sendSms")
    @SendTo("sendMail")
    public String sendSms(Map <Object, String> map) {
        return aliSendMessage.sendMessage(map.get("phone"), map.get("code"));
    }

    /**
     * 判断是否发送短信出问题，是，发送邮件到相应人员
     * @param message
     */
    @JmsListener(destination = "sendMail")
    public void sendMail(String message){
        if(!StringUtils.isEmpty(message)){
            mailSender.sendMail(message);
        }
    }
}
