package com.mjrj.lzh.pms.activemq;

import com.mjrj.lzh.pms.dao.SysUserDOMapper;
import com.mjrj.lzh.pms.redis.RedisComponent;
import com.mjrj.lzh.pms.dto.StatusEnum;
import com.mjrj.lzh.pms.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import java.util.HashMap;
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
public class Producer {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private RedisComponent redisComponent;
    @Autowired
    private SysUserDOMapper sysUserDOMapper;

    /**
     * activemq 生产者(点对点和订阅模式，点对点就是一个消费者对应一个生产者，订阅是一个对多个，场景不同
     * 如：我希望有一个消费者发短信，一个消费者发邮件，这时就可以以订阅模式)
     *
     * @param queueName 队列名称
     * @param phone     电话号码
     */
    public StatusEnum sendMessage(String queueName, String phone) {

        if (!redisComponent.exitKeyString(CommonUtil.PHONE_PRE + phone)) {
            Destination destination = new ActiveMQQueue(queueName);
            Map map = new HashMap();
            String s = CommonUtil.randomNumnber();
            map.put("code", s);
            log.info(s +  " : 短信验证码");
            map.put("phone", phone);
            jmsMessagingTemplate.convertAndSend(destination, map);
            return StatusEnum.SEND_SUCCESS;
        }
            return StatusEnum.SEND_FAIL;

    }


}
