package alex.emailer.IO;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import javax.mail.MessagingException;

import org.junit.Test;

import alex.emailer.Student.Student;

public class EmailerTest {

	/** Email to send from */
	private static final String SENDER_ADRESS = "";
	
	/** email password */
	private static final String PASSWORD = "";
	
	/**
	 * Tests if sendEmail() works. update values to get a test that works for you 
	 */
//	@Test
//	public void testSendEmail() throws MessagingException {
//		Student s = new Student("firstname", "lastname", "email@email.com");
//		File attachment = new File("src/test/resources/test-files/test-attachment.txt");
//		s.setReport(attachment);
//		Emailer.setLoginInfo(SENDER_ADRESS, PASSWORD);
//		Emailer.setHost("smtp.gmail.com");
//		try {
//			Emailer.sendEmail(s);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			fail();
//		}
//	}
}
