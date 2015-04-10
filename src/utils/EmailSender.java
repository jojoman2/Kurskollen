package utils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Created by Fredrik on 2015-03-19.
 */
public class EmailSender {

    public static void sendEmail(String activationCode,String name, String receivingEmail){
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            //msg.setFrom(new InternetAddress("no-reply@kurskollenapp.appspotmail.com", "Kurskollen"));
            msg.setFrom(new InternetAddress("freddewallen@gmail.com"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(receivingEmail));
            msg.setSubject("Kurskollen registreringskod");
            msg.setText("Hej och tack för att du registrerade dig på Kurskollen. \n \n Din kod är: " + activationCode);
            Transport.send(msg);

        } catch (AddressException e) {
            throw(new RuntimeException("dfsdfsd"));
        } catch (MessagingException e) {
            throw (new RuntimeException("dfsdfsd"));
        }
    }
}
