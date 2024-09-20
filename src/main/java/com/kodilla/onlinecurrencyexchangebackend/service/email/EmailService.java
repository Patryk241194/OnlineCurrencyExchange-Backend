package com.kodilla.onlinecurrencyexchangebackend.service.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.kodilla.onlinecurrencyexchangebackend.security.log.LogMessages.EMAIL_SENDING_FAILURE_LOG;
import static com.kodilla.onlinecurrencyexchangebackend.security.log.LogMessages.EMAIL_SENT_SUCCESS_LOG;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void send(final Mail mail) {
        try {
            MimeMessagePreparator mailMessage = createMimeMessage(mail);
            javaMailSender.send(mailMessage);
            log.info(EMAIL_SENT_SUCCESS_LOG);
        } catch (MailException e) {
            log.error(EMAIL_SENDING_FAILURE_LOG + e.getMessage(), e);
        }
    }

    private MimeMessagePreparator createMimeMessage(final Mail mail) {
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setTo(mail.getMailTo());
            if (mail.getToCc() != null) {
                messageHelper.addCc(mail.getToCc());
            }
            messageHelper.setSubject(mail.getSubject());
            messageHelper.setText(mail.getMessage(), true);
        };
    }

}
