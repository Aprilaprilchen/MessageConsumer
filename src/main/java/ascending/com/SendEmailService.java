package ascending.com;

import com.amazonaws.services.dynamodbv2.xspec.S;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Properties;

public class SendEmailService {

    private static Properties properties;
    private static Session session;
    private static MimeMessage mailMessage;

    public void sendEmail(Email email) {
        try (InputStream input = new FileInputStream("src/main/java/ascending/com/application.properties")) {
            properties = new Properties();
            properties.load(input);

        }catch (IOException e){
            e.printStackTrace();
        }

        /*properties = new Properties();
        properties.put("mail.smtp.host", System.getProperty("mail.smtp.host"));
        properties.put("mail.smtp.auth", System.getProperty("mail.smtp.auth"));
        properties.put("mail.smtp.starttls.enable", System.getProperty("mail.smtp.starttls.enable"));
        properties.put("mail.smtp.port", System.getProperty("mail.smtp.port"));
        properties.put("mail.user.name", System.getProperty("mail.user.name"));
        properties.put("mail.user.password", System.getProperty("mail.user.password"));
        */



        System.out.println(">>>>> " + properties);

        String mailServer = properties.getProperty("mail.smtp.host");
        String mailServerPort = properties.getProperty("mail.smtp.port");
        String mailUserName = properties.getProperty("mail.user.name");
        String mailUserPassword = properties.getProperty("mail.user.password");

        System.out.println("mail server is: " + mailServer + "mail server port is: " + mailServerPort + "mail user name is: " + mailUserName);
//        String mailServer = "smtp.sendgrid.net";
//        String mailServerPort = "465";
//        String mailUserName = "apikey";
//        String mailUserPassword = "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";
//
//        properties = new Properties();
//        properties.put("mail.smtp.host", mailServer);
//        properties.put("mail.smtp.auth", "true");
//        properties.put("mail.smtp.port", mailServerPort);
//        properties.put("mail.smtp.starttls.enable", "true");
        if (mailServerPort.equalsIgnoreCase("465")) properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        session = Session.getInstance(properties, null);

        try {
            mailMessage = new MimeMessage(session);
            InternetAddress addressFrom = new InternetAddress(email.getFrom());
            mailMessage.setFrom(addressFrom);
            mailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getReplyTo()));
//        mailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("daveywang@live.com"));
//        mailMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress("yutong@gwu.edu"));
            mailMessage.setSubject(email.getSubject());
            String emailBody = "Test to send email via SendGrid by April Chen." + email.getText() + email.getSendTime();
            mailMessage.setContent(emailBody, "text/html");


            Transport transport = session.getTransport("smtp");
            transport.connect(mailServer, mailUserName, mailUserPassword);
            transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
            transport.close();

            System.out.println("Email has been sent out");

        }catch (MessagingException e){
            e.printStackTrace();
        }
    }
}
class Email{
    private String subject;
    private String from;
    private String replyTo;
    private String text;
    private LocalDateTime sendTime = LocalDateTime.now();

    public Email(String subject, String from, String replyTo, String text){
        this.subject = subject;
        this.from = from;
        this.replyTo = replyTo;
        this.text = text;
    }

    public String getSubject(){
        return subject;
    }

    public String getFrom(){
        return from;
    }

    public String getReplyTo(){
        return replyTo;
    }

    public String getText(){
        return text;
    }

    public LocalDateTime getSendTime(){
        return sendTime;
    }

    public void setText(String message){
        this.text = message;
    }
 }
