package alex.emailer.Student;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

import alex.emailer.IO.Emailer;
import alex.emailer.IO.ExcelReaderTest;
import alex.emailer.IO.ZipReaderTest;

public class StudentManagerTest {

	/**
	 * Tests loading students from /test-files/emails1.xlsx
	 */
	@Test
	public void testLoadStudents() {
		StudentManager sm = StudentManager.getInstance();
		sm.setStartRow(1);
		sm.setNameColumn('A');
		sm.setEmailColumn('B');
		sm.loadStudents(ExcelReaderTest.EMAILS_1);
		sm.unzipFiles(ZipReaderTest.ZIP_LOCATION);
		sm.matchStudents();
		
		assertEquals("Thomas", sm.getStudents().get(0).getFirstName());
		assertEquals("Wayne", sm.getStudents().get(0).getLastName());
		assertTrue(sm.getStudents().get(0).isMatched());
		
		assertEquals("Robert", sm.getStudents().get(1).getFirstName());
		assertEquals("Ware", sm.getStudents().get(1).getLastName());
		assertTrue(sm.getStudents().get(1).isMatched());
		
		assertEquals("Alex", sm.getStudents().get(2).getFirstName());
		assertEquals("Blouse", sm.getStudents().get(2).getLastName());
		assertTrue(sm.getStudents().get(2).isMatched());
		
		assertEquals("Gregory", sm.getStudents().get(3).getFirstName());
		assertEquals("Thomas", sm.getStudents().get(3).getLastName());
		assertTrue(sm.getStudents().get(3).isMatched());
		
		assertEquals("James", sm.getStudents().get(4).getFirstName());
		assertEquals("House", sm.getStudents().get(4).getLastName());
		assertTrue(sm.getStudents().get(4).isMatched());
		
		assertEquals("Tom", sm.getStudents().get(5).getFirstName());
		assertEquals("McDonald", sm.getStudents().get(5).getLastName());
		assertTrue(sm.getStudents().get(5).isMatched());		
	}
	
	/**
	 * Tests emailStudents()
	 * deactivated because real emails are not used in test files, you may change this to test 
	 */
//	@Test
//	public void testEmailStudents() {
//		StudentManager sm = StudentManager.getInstance();
//		sm.setStartRow(1);
//		sm.setEmailColumn('B');
//		sm.setNameColumn('A');
//		sm.unzipFiles(ZipReaderTest.ZIP_LOCATION);
//		sm.loadStudents(ExcelReaderTest.EMAILS_1);
//		sm.matchStudents();

//		
//		// Test if there is a student that is not matched
//		Student unmatched = new Student("fistname", "lastname", "email@gmail.com");
//		sm.addStudent(unmatched);
//		assertThrows(IllegalArgumentException.class, () -> sm.emailStudents());
//		
//		// Test remove unmatched student
//		sm.removeStudent("firstname", "lastname");
//		assertNull(sm.getStudentByName("firstname", "lastname"));
//		
//		sm.setLoginInfo("email@gmail.com", "password", "smtp.gmail.com");
//		sm.emailStudents();
//	
//	}
	
	/**
	 * Tests setLoginInfo()
	 */
	@Test
	public void testSetLoginInfo() {
		StudentManager sm = StudentManager.getInstance();
		
		sm.setLoginInfo("user", "pass");
		Properties prop = new Properties();
	    
	    try (InputStream input = new FileInputStream(Emailer.LOGIN_PROP)) {
	        prop.load(input);
	        System.out.println(prop);
	        
	    } catch (IOException e) {
	        fail();
	    }
	    
	    assertEquals("user", prop.getProperty("user"));
        assertEquals("pass", prop.getProperty("pw"));
	}

	
}
