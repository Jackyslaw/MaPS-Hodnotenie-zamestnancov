package com.hackag.fibimeter.mail;

import com.hackag.fibimeter.FibimeterApplication;
import com.hackag.fibimeter.util.StaticContextAccessor;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.hibernate.validator.constraints.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.net.ssl.SSLHandshakeException;
import javax.validation.constraints.NotNull;

/**
 * Helper for sending e-mail messages from a Fibimeter e-mail address.
 * <p>
 * Created by Steve on 21.10.2017.
 */
@Component
@PropertySource("classpath:mail.properties")
@ConfigurationProperties
@Validated
public class MailHelper {

    public static boolean EMAILS_DISABLED;

    private static Logger log = LoggerFactory.getLogger(MailHelper.class);

    @Email
    @NotNull
    private String fromEmail;
    @NotNull
    private String host;
    @NotNull
    private Integer port;
    @NotNull
    private Boolean useAuth;
    @NotNull
    private Boolean useSsl;
    @NotNull
    private Boolean useStarttls;
    @NotNull
    private Integer timeoutMs;
    // Trivia: previously used property name "username" caused a name of current user in OS to be injected instead
    @NotNull
    private String emailUsername;
    @NotNull
    private String emailPassword;

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setUseAuth(boolean useAuth) {
        this.useAuth = useAuth;
    }

    public void setUseSsl(Boolean useSsl) {
        this.useSsl = useSsl;
    }

    public void setUseStarttls(boolean useStarttls) {
        this.useStarttls = useStarttls;
    }

    public void setTimeoutMs(int timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

    public void setEmailUsername(String emailUsername) {
        this.emailUsername = emailUsername;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    /**
     * Sends e-mail to a recipient.
     *
     * @param to      Recipient's email.
     * @param subject Message subject.
     * @param body    Message body.
     */
    public void sendMail(String to, String subject, String body) {
        if (EMAILS_DISABLED) {
            log.error("E-MAIL SENDING IS DISABLED, mail not sent!");
            return;
        }
        try {
            log.trace("Trying to send email with subject: " + subject
                + " and body:\n" + body + "\nto address: " + to);
            org.apache.commons.mail.Email email = new SimpleEmail();
            email.setHostName(host);
            email.setSmtpPort(port);
            if (useAuth) {
                email.setAuthenticator(new DefaultAuthenticator(emailUsername, emailPassword));
            }
            email.setSSLOnConnect(useSsl);
            email.setStartTLSEnabled(useStarttls);
            email.setStartTLSRequired(useStarttls);
            email.setSocketConnectionTimeout(timeoutMs);
            email.setSocketTimeout(timeoutMs);
            email.setFrom(fromEmail);
            email.setSubject(subject);
            email.setMsg(body);
            email.addTo(to);
            email.send();
            log.info("Sent email to " + to + " successfully");
        } catch (EmailException e) {
            if (e.getCause() != null && e.getCause().getCause() instanceof SSLHandshakeException) {
                log.error("Certificate problem in sending email to " + to
                    + ".\nThis problem is most likely caused either by using Avast Antivirus with enabled SMTP "
                    + "testing in mail shield, or by cacerts in Java not validating the SMTP server.\n"
                    + "As a workaround you can try to call CertificateHelper.disableCertificateValidation(), "
                    + "or set Java property -Dcom.sun.net.ssl.checkRevocation=false.");
            } else {
                log.error("Error in sending email to " + to, e);
            }
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        // Mail send test
        SpringApplication.run(FibimeterApplication.class, args);
        String to = "jakub.ruzicka@student.tuke.sk";
        if ("my@mail.com".equals(to)) {
            throw new RuntimeException("Enter your own e-mail to test MailHelper");
        }
        StaticContextAccessor.getBean(MailHelper.class).sendMail(to, "TestSubject", "TestBody\nMessage");
    }
}
