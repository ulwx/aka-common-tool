package com.ulwx.tool;

import com.sun.mail.util.MailSSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 邮件收发工具类
 * 
 * @author yuxiaowei
 * 
 */
public class MailUtil {

	private static Logger log = LoggerFactory.getLogger(MailUtil.class);

	public static class Key{
		public static final String FROM_NAME="FROM_NAME";
		public static final String CC="CC";
		public static final String BCC="BCC";
	}

	public static void send(String smtp,String port, String title, String content, String filename, byte[] attach,
			final String from, String to, final String sendPassword,Map<String,String> properties) {
		List flist=new ArrayList();
		flist.add(filename);
		List alist=new ArrayList();
		alist.add(attach);
		send(smtp,port, title, content, flist, alist, from, to, sendPassword, "text/plain;charset=utf-8",properties);
	}
	public static void send(String smtp,String port, String title, String content,
							List<String> filenameList, List<byte[]> attachList,
							final String from, String to, final String sendPassword,Map<String,String> properties){
		send(smtp,port, title, content, filenameList, attachList, from, to, sendPassword, "text/plain;charset=utf-8",properties);
	}


	private static Address[] getAddress(String to)throws Exception{
		String[] tos = ArrayUtils.trim(to.split("，|,|;|；"));
		List<Address> addressList = new ArrayList<>();
		for (int i = 0; i < tos.length; i++) {
			// 定义邮件信息
			if(tos[i]==null || tos[i].isEmpty()){
				continue;
			}
			Address address = new InternetAddress(StringUtils.trim(tos[i]), StringUtils.trim(tos[i]), "UTF-8");
			addressList.add(address);
		}
		return addressList.toArray(new Address[0]);
	}
	/**
	 * 
	 * @param smtp
	 *            邮件服务器
	 * @param title
	 *            邮件标题
	 * @param content
	 *            邮件内容
	 * @param filenameList
	 *            附件名
	 * @param attachList
	 *            附件数据 byte[]
	 * @param from
	 *            发件人
	 * @param to
	 *            收件人，多人以,分隔
	 * @param sendPassword
	 *            发件人邮箱密码
	 * @param contentType 发送的内容的Content-Type
	 */
	public static void send(String smtp, String port, String title, String content, List<String> filenameList, List<byte[]> attachList,
							final String from, String to, final String sendPassword, String contentType,
							Map<String,String> properties) {
		System.setProperty("mail.mime.encodefilename", "true");
		// 创建Properties 对象
		Properties props = System.getProperties();

		// 添加smtp服务器属性
		if(StringUtils.hasText(smtp)) {
			props.put("mail.smtp.host", smtp);
		}
		if(StringUtils.hasText(port)) {
			props.put("mail.smtp.port", port);
		}
		props.put("mail.smtp.auth", "true"); // 163的stmp不是免费的也不公用的，需要验证

		MailSSLSocketFactory sf = null;
		try {
			sf = new MailSSLSocketFactory();
			sf.setTrustAllHosts(true);
			props.put("mail.smtp.ssl.enable", "true");
			props.put("mail.smtp.ssl.socketFactory", sf);
		} catch (Exception e) {
			log.error("", e);
		}

		// 创建邮件会话
		Session session = Session.getDefaultInstance(props, new Authenticator() { // 验账账户
			@Override
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, sendPassword);
			}
		});
		try {
			//String[] tos = ArrayUtils.trim(to.split("，|,|;|；"));
			MimeMultipart mcon = new MimeMultipart(); // 创建邮件体对象

			if (mcon == null)
				return;
			MimeBodyPart part = new MimeBodyPart(); // 创建文本部分对象
			part.setText(content, "utf-8");
			part.setHeader("Content-Type", contentType); //
			mcon.addBodyPart(part);

			// 设置抄送收件人
			// 设置暗抄送人
			// message.addRecipients(Message.RecipientType.BCC,new
			// InternetAddress());

			if (filenameList!=null  && filenameList.size()>0) {
				for(int i = 0; i<filenameList.size(); i++) {
					try {
						// 设置附件
						part = new MimeBodyPart();// 创建MIME对象
						DataSource fds = new ByteArrayDataSource(attachList.get(i), "application/octet-stream"); // 创建文件流对象
						part.setDataHandler(new DataHandler(fds));
						part.setFileName(filenameList.get(i));
						mcon.addBodyPart(part); // 添加二进制编码至邮件体中
					} catch (Exception e) {
						// TODO Auto-generated catch block
						log.error("", e);
					}
				}
			}

			MimeMessage message = new MimeMessage(session);
			String cc=properties.get(Key.CC);
			if(StringUtils.hasText(cc)) {
				message.addRecipients(Message.RecipientType.CC,getAddress(cc));
			}
			//new InternetAddress ("test@chinas.com", "这里是需要的昵称", "UTF-8")
			String bcc=properties.get(Key.BCC);
			if(StringUtils.hasText(bcc)) {
				message.addRecipients(Message.RecipientType.BCC,getAddress(bcc));
			}
			message.setFrom(new InternetAddress(from,properties.get(Key.FROM_NAME),"utf-8"));
			Address[] addressArray=getAddress(to);
			message.addRecipients(Message.RecipientType.TO, addressArray);

			message.setSubject(title, "utf-8");
			// message.setText(content);
			message.setContent(mcon); // 添加文本至邮件中
			// 发送消息
			// session.getTransport("smtp").send(message);
			// //也可以这样创建Transport对象
			Transport.send(message);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void receive(String host, String username, String password) {
		Store store = null;

		Folder folder = null;

		try {
			// 获取默认会话
			Properties props = new Properties();

			// Session类提供的getDefaultInstance()这个静态工厂方法获得一个默认的Session对象
			Session session = Session.getDefaultInstance(props, null);

			// 使用POP3会话机制，连接服务器
			store = session.getStore("pop3");

			// 服务器、 用户名 、密码
			store.connect(host, username, password);

			// 首先从Store中获得INBOX这个Folder（对于POP3协议只有一个名为INBOX的Folder有效）
			folder = store.getFolder("INBOX");

			// 以读写的方式打开邮件
			folder.open(Folder.READ_WRITE);

			// 接收新邮件列表
			Message[] message = folder.getMessages();

			// 循环读取邮件
			for (int i = 0, n = message.length; i < n; i++) {
				String from = ((InternetAddress) message[i].getFrom()[0]).getPersonal();
				log.info("from:" + from);

				String mail = (String) ((InternetAddress) message[i].getFrom()[0]).getAddress();
				log.info("mail:" + mail);
				// 邮件主题
				String title = (String) message[i].getSubject();
				// System.out.println(title);
				// 获取信息对象
				Part messagePart = message[i];
				Object content = messagePart.getContent();

				// 附件
				if (content instanceof Multipart) {
					messagePart = ((Multipart) content).getBodyPart(0);
					Multipart mp = (Multipart) message[i].getContent();
					for (int j = 0, num = mp.getCount(); j < num; j++) {
						Part part = mp.getBodyPart(j);
						String disposition = part.getDisposition();

						if (disposition != null
								&& (disposition.equals(Part.ATTACHMENT) || disposition.equals(Part.INLINE))) {
							log.info(part.getFileName() + " " + part.getContent() + " " + part.getInputStream());

						}
					}
				}
				// 获取content类型
				String contentType = messagePart.getContentType();
				// 如果邮件内容是纯文本或者是HTML，那么打印出信息
				String thisLine = null;
				StringBuffer str = new StringBuffer();
				if (contentType.startsWith("text/plain") || contentType.startsWith("text/html")) {
					InputStream is = messagePart.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));
					thisLine = reader.readLine();
					while (thisLine != null) {
						// 一行一行读取
						str.append(thisLine + "\n");
						thisLine = reader.readLine();

					}
				}
				// System.out.println(str.toString());

				// 删掉当前邮件
				// message[i].setFlag(Flags.Flag.DELETED, true);
			}
		} catch (Exception e) {
			log.error("", e);
		}

		finally {
			// 释放资源
			try {
				if (folder != null)
					folder.close(false);
				if (store != null)
					store.close();
			} catch (Exception ex2) {
				log.error("", ex2);
			}
		}

	}

	public static void main(String[] args) {
		String host = "smtp.qq.com"; // 指定的smtp服务器
		String from = "2550746642@qq.com"; // 邮件发送人的邮件地址
		String to = "2550746642@qq.com，hongyueyuan@pj-logistics.com"; // 邮件接收人的邮件地址
		final String password = "nansdwaojqtodjfa"; // 发件人的邮件密码

		String title = "测试邮件";
		String content = "测试邮件";
		String filename = "测试邮件.csv";
		try {
			send(host,"465", title, content, filename, content.getBytes("gbk"), from, to, password,new HashMap<>());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		System.out.println(Integer.MAX_VALUE);
	}

}
