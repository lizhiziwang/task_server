package com.zsh.task.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@Slf4j
public class EMailClient {
    @Resource
    private JavaMailSender jm;

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(String to, String title, String content) {
        try {
            MimeMessage message = jm.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(title);
            helper.setText(content, true);//不加参数默认是文本，加上true之后支持html格式文件
            jm.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            log.error("发送邮件失败:" + e.getMessage());
        }
    }
}
