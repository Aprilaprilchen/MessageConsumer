package ascending.com;

import com.sendgrid.SendGrid;

public class EmailService {
    private SendGrid sg;

    public EmailService(SendGrid sendGrid){
        this.sg = sendGrid;
    }

    public void sendEmail(){

    }
}
