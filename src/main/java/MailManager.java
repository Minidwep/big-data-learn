import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailManager {
    public static void sendEmail() throws Exception {
        Properties prop = new Properties();
        prop.setProperty("mail.host", "smtp.qq.com"); //// �����ʼ�������
        prop.setProperty("mail.transport.protocol", "smtp"); // �ʼ�����Э��
        prop.setProperty("mail.smtp.auth", "true"); // ��Ҫ��֤�û�������

        Session session = Session.getDefaultInstance(prop, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                //�������ʼ��û�������Ȩ��
                return new PasswordAuthentication("wangguanrui0209@qq.com", "iwzxomztfecbbeej");
            }
        });
        //2��ͨ��session�õ�transport����
        Transport ts = session.getTransport();

        //3��ʹ��������û�������Ȩ�������ʼ�������
        ts.connect("smtp.qq.com", "wangguanrui0209@qq.com", "iwzxomztfecbbeej");

        //4�������ʼ�
        //�����ʼ�����
        MimeMessage message = new MimeMessage(session);

        //ָ���ʼ��ķ�����
        message.setFrom(new InternetAddress("wangguanrui0209@qq.com"));

        //ָ���ʼ����ռ��ˣ����ڷ����˺��ռ�����һ���ģ��Ǿ����Լ����Լ���
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("943490180@qq.com"));

        //�ʼ��ı���
        message.setSubject("������������123123");

        //�ʼ����ı�����
        message.setContent("message456", "text/html;charset=UTF-8");

        ts.sendMessage(message, message.getAllRecipients());

        ts.close();

    }
}