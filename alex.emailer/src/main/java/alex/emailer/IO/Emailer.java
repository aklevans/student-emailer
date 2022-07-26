package alex.emailer.IO;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import alex.emailer.Student.Student;

/**
 * Sends emails
 * @author Alex
 *
 */
public class Emailer {

	
	/** Default host smtp address */
	public static final String HOST = "smtp.gmail.com";
	
	/** Default host port address */
	public static final String PORT = "465";
	
	/** Properties file location for login information */
	public static final String LOGIN_PROP = "emailer.properties";

	public static void setUp() {
		Properties loginProp = new Properties();
		try (OutputStream output = new FileOutputStream(LOGIN_PROP)) {
	        
	        loginProp.setProperty("host", HOST);
	        loginProp.setProperty("port", PORT);
	        loginProp.store(output, null);
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    }	
	}
	
	/** 
	 * Set the user login info
	 * @param username the user's email
	 * @param password the user's password
	 */
	public static void setLoginInfo(String username, String password) {
		Properties loginProp = new Properties();
		try (InputStream input = new FileInputStream(LOGIN_PROP)) {
	        loginProp.load(input);

	    } catch (IOException e) {
	    	throw new IllegalArgumentException("User information missing");
	    }
		
		try (OutputStream output = new FileOutputStream(LOGIN_PROP)) {
	        
	        loginProp.setProperty("user", username);
	        loginProp.setProperty("pw", password);
	        
	        loginProp.store(output, null);
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Set the host address for the user's email server
	 * @param host the host address
	 */
	public static void setHost(String host) {
		Properties loginProp = new Properties();
		try (InputStream input = new FileInputStream(LOGIN_PROP)) {
	        loginProp.load(input);

	    } catch (IOException e) {
	    	throw new IllegalArgumentException("User information missing");
	    }
		
		try (OutputStream output = new FileOutputStream(LOGIN_PROP)) {
	        
	        loginProp.setProperty("host", host);
	        
	        loginProp.store(output, null);
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Set the port number for the user's email server
	 * @param port the port number
	 */
	public static void setPort(String port) {
		Properties loginProp = new Properties();
		try (InputStream input = new FileInputStream(LOGIN_PROP)) {
	        loginProp.load(input);

	    } catch (IOException e) {
	    	throw new IllegalArgumentException("User information missing");
	    }
		
		try (OutputStream output = new FileOutputStream(LOGIN_PROP)) {
	        
	        loginProp.setProperty("port", port);
	        
	        loginProp.store(output, null);
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		
	}
	
	/**
	 * Sends an email to a student with the student's associated report as an attachment
	 * @param student the student to send the email to
	 * @throws IOException if attachment cannot be found
	 * @throws MessagingException if error in message construction
	 */
	public static void sendEmail(Student student) throws MessagingException, IOException {
		Properties loginProp = new Properties();
		try (InputStream input = new FileInputStream(LOGIN_PROP)) {
	        loginProp.load(input);

	        
	    } catch (IOException e) {
	    	throw new IllegalArgumentException("User information missing");
	    }
		
		String subject = student.toString() + " Leadership Report";
		String body = "Hello " + student.getFirstName() + ", \n please find attached your I-OPT report.";  
		
		
		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", loginProp.getProperty("host"));
		properties.put("mail.smtp.port", loginProp.getProperty("port"));
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		
		
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(loginProp.getProperty("user"), loginProp.getProperty("pw"));
			}
		});

		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(loginProp.getProperty("user")));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(student.getEmail()));
		message.setSubject(subject);

		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(body, "text/html; charset=utf-8");

		MimeBodyPart attachmentBodyPart = new MimeBodyPart();
		attachmentBodyPart.attachFile(student.getReport());

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(mimeBodyPart);
		multipart.addBodyPart(attachmentBodyPart);

		message.setContent(multipart);

		System.out.println("Sending...");
		Transport.send(message);
		System.out.println("Message sent successfully.");
	}
	

}
