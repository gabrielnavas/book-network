package io.github.gabrielnavas.book_network_api.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;


    @Async
    public void sendEmail(
            String to,
            String username,
            EmailTemplate emailTemplate,
            String confirmationUrl,
            String activationCode,
            String subject) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        Map<String, Object> properties = loadProperties(username, confirmationUrl, activationCode);

        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom("noreply@book-network.com");
        helper.setTo(to);
        helper.setSubject(subject);

        String template = loadTemplate(emailTemplate.name(), context);
        helper.setText(template, true);

        javaMailSender.send(mimeMessage);
    }

    private Map<String, Object> loadProperties(String username, String confirmationUrl, String activationCode) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("confirmationUrl", confirmationUrl);
        properties.put("activationCode", activationCode);
        return properties;
    }

    private String loadTemplate(String templateName, Context context) {
        return templateEngine.process(templateName.toLowerCase(), context);
    }
}
