import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailManager {
    public static void sendEmail() throws Exception {
        Properties prop = new Properties();
        prop.setProperty("mail.host", "smtp.qq.com"); //// 设置邮件服务器
        prop.setProperty("mail.transport.protocol", "smtp"); // 邮件发送协议
        prop.setProperty("mail.smtp.auth", "true"); // 需要验证用户名密码

        Session session = Session.getDefaultInstance(prop, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                //发件人邮件用户名、授权码
                return new PasswordAuthentication("wangguanrui0209@qq.com", "iwzxomztfecbbeej");
            }
        });
        //2、通过session得到transport对象
        Transport ts = session.getTransport();

        //3、使用邮箱的用户名和授权码连上邮件服务器
        ts.connect("smtp.qq.com", "wangguanrui0209@qq.com", "iwzxomztfecbbeej");

        //4、创建邮件
        //创建邮件对象
        MimeMessage message = new MimeMessage(session);

        //指明邮件的发件人
        message.setFrom(new InternetAddress("wangguanrui0209@qq.com"));

        //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("943490180@qq.com"));

        //邮件的标题
        message.setSubject("含有敏感数据123123");

        //邮件的文本内容
        message.setContent("message456", "text/html;charset=UTF-8");

        ts.sendMessage(message, message.getAllRecipients());

        ts.close();

    }
}