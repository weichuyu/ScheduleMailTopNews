package com.yu.tools.util;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
public class MyEmailUtil {
    private static Logger logger = Logger.getLogger(MyEmailUtil.class);

    @Autowired
    private JavaMailSender javaMailSender;

    public Boolean sendSimpleMail(String from,String to,String subject,String content) {
        MimeMessage message = null;
        try {
            message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);

            StringBuffer sb = new StringBuffer();
            sb.append(content);
            helper.setText(sb.toString(), true);
//            FileSystemResource fileSystemResource=new FileSystemResource(new File("D:\76678.pdf"))
//            helper.addAttachment("电子发票"，fileSystemResource);
            javaMailSender.send(message);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return false;
        }
        return true;
    }
}
