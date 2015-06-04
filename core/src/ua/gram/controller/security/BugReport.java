package ua.gram.controller.security;

import com.badlogic.gdx.Gdx;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.MailcapCommandMap;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by Gram on 12/1.
 */
public class BugReport {

    private final String TO;
    private final String FROM;
    private final String PASS;
    private final String HOST;
    private final short PORT;
    private HashMap<String, String> device;

    public BugReport(HashMap<String, String> device) {
        this.device = device;
        TO = "gram7gram@gmail.com";
        FROM = "divinedefensegame@gmail.com";
        PASS = "cxmmjGtdLmucADN";
        HOST = "smtp.gmail.com";
        PORT = 465;
    }

    public boolean sendReport(String text) {
        String content = "";
        for (String key : device.keySet()) {
            content += key + ": " + device.get(key) + "\n";
        }
        content += text;
        try {
            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
            CommandMap.setDefaultCommandMap(mc);

            Properties props = new Properties();
            props.put("mail.smtp.host", HOST);
            props.put("mail.smtp.user", FROM);
            props.put("mail.smtp.pass", PASS);
            props.put("mail.smtp.port", PORT);
            props.put("mail.smtp.auth", true);
            props.put("mail.smtp.starttls.enable", true);
            props.put("mail.smtp.socketFactory.port", PORT);
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", false);
            props.put("mail.debug", false);

            Session mailSession = Session.getInstance(props, null);
            DataHandler handler = new DataHandler(new ByteArrayDataSource("".getBytes(), "text/plain"));
            mailSession.setDebug(false);

            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(FROM));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(TO));
            msg.setDataHandler(handler);
            msg.setContent(new MimeMultipart());
            msg.setSubject("Divine Defense Bug Report");
            msg.setText(content);

            Transport transport = mailSession.getTransport("smtp");
            transport.connect(HOST, PORT, FROM, PASS);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();

            Gdx.app.log("INFO", "Email send successfully!");
            return true;
        } catch (MessagingException exc) {
            Gdx.app.error("ERR", "Could not send email " + exc);
            return false;
        }
    }
}