package ascending.com;

import com.amazonaws.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jms.*;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public class SQSMessageListener implements MessageListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private SendEmailService ses = new SendEmailService();
    private Email email = new Email("Hello", "yutong@gwu.edu", "yutong@gwu.edu", "Just say Hi");

    @Override
    public void onMessage(Message message) {
        try {
            handleMessage(message);
            message.acknowledge();
            logger.info("Acknowledged message " + message.getJMSMessageID());
        }
        catch (JMSException e) {
            logger.error("Error processing message: " + e.getMessage());
        }
    }
    private void handleMessage(Message message) throws JMSException {
        logger.info("Got message " + message.getJMSMessageID());
        if (message instanceof TextMessage) {
            TextMessage txtMessage = (TextMessage)message;
            email.setText(txtMessage.getText());
            ses.sendEmail(email);
            logger.info("Content: " + txtMessage.getText());
        }
        else if (message instanceof BytesMessage){
            BytesMessage byteMessage = (BytesMessage)message;
            byte[] bytes = new byte[(int)byteMessage.getBodyLength()];
            byteMessage.readBytes(bytes);
            email.setText(Base64.encodeAsString(bytes));
            ses.sendEmail(email);
            logger.info("Content: " +  Base64.encodeAsString(bytes));
        }
        else if (message instanceof ObjectMessage) {
            ObjectMessage objMessage = (ObjectMessage) message;
            email.setText(objMessage.getObject().toString());
            ses.sendEmail(email);
            logger.info("Content: " + objMessage.getObject());
        }
    }
}