package com.mjrj.lzh.pms.activemq;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Properties;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.util
 * @Author: lzh
 * @CreateTime: 2020-03-05 20:21
 * @Description: ${Description}
 */
@Component
@ConfigurationProperties(prefix = "mail")
@Getter
@Setter
public class MailSender {
    private String host;
    private String userName;
    private String passWord;
    private String reciver;
    private String title;

    /**
     * 实现JavaMailSender，自定义邮件发送器
     * @return
     */
    public JavaMailSenderImpl makeMail() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setUsername(userName);
        mailSender.setPassword(passWord);
        mailSender.setDefaultEncoding("UTF-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.auth", "true");
        mailSender.setJavaMailProperties(p);
        return mailSender;
    }

    /**
     * 发送邮件通知（当短信发送失败时）
     * @param errorMsg
     */
    public void sendMail(String errorMsg) {
        MimeMessage mimeMessage = makeMail().createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(userName);
            messageHelper.setTo(reciver);
            messageHelper.setSubject(title+ LocalDateTime.now());
            messageHelper.setText(errorMsg);
            makeMail().send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
