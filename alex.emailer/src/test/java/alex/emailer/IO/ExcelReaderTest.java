package alex.emailer.IO;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import alex.emailer.IO.ExcelReader;
import alex.emailer.Student.Student;


/**
 * Tests the ExcelReader class
 * @author Alex
 *
 */
public class ExcelReaderTest {

	/** File location for excel file with List of 6 names, all emails are email@gmail.com */
	public static final String EMAILS_1 = "src/test/resources/test-files/emails1.xlsx";
	
	/** File location for excel file with List of 5 names with a blank line */
	public static final String EMAILS_2 = "src/test/resources/test-files/emails2.xlsx";
	
	/** File location for excel file with List of 6 names, one name is missing space after comma 
	 * and one name is in firstname lastname format - should still work
	 */
	public static final String EMAILS_3 = "src/test/resources/test-files/emails3.xlsx";

	/** File location for excel file with List of 6 names, one student doesnt have a last name */
	public static final String EMAILS_4 = "src/test/resources/test-files/emails4.xlsx";

	
	/** Row where names will start. 1-indexed, use the excel row number */
	public static final int START_ROW = 1;
	
	/** Column number where names will be. Zero indexed.*/
	public static final int NAME_COLUMN = 0;
	
	/** Column number where email will be. Zero indexed. */
	public static final int EMAIL_COLUMN = 1;
	
	/**
	 * Tests reading students from /test-files/emails1.xlsx
	 * List of 6 names, all emails are email@gmail.com
	 */
	@Test
	public void testReadEmails1() {
		try {
			ArrayList<Student> students = ExcelReader.readStudentData(EMAILS_1, START_ROW, NAME_COLUMN, EMAIL_COLUMN);
		
			assertEquals(6, students.size());
			assertEquals("Thomas", students.get(0).getFirstName());
			assertEquals("Wayne", students.get(0).getLastName());
			assertEquals("email@gmail.com", students.get(0).getEmail());
			
			assertEquals("Robert", students.get(1).getFirstName());
			assertEquals("Ware", students.get(1).getLastName());
			assertEquals("email@gmail.com", students.get(2).getEmail());
			
			assertEquals("Alex", students.get(2).getFirstName());
			assertEquals("Blouse", students.get(2).getLastName());
			assertEquals("email@gmail.com", students.get(2).getEmail());

			assertEquals("Gregory", students.get(3).getFirstName());
			assertEquals("Thomas", students.get(3).getLastName());
			assertEquals("email@gmail.com", students.get(3).getEmail());
			
			assertEquals("James", students.get(4).getFirstName());
			assertEquals("House", students.get(4).getLastName());
			assertEquals("email@gmail.com", students.get(4).getEmail());
			
			assertEquals("Tom", students.get(5).getFirstName());
			assertEquals("McDonald", students.get(5).getLastName());
			assertEquals("email@gmail.com", students.get(5).getEmail());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}	
	}
	
	/**
	 * Tests reading students from /test-files/emails2.xlsx
	 * List of 5 names with a blank line
	 */
	@Test
	public void testReadEmails2(){
		try {
			ArrayList<Student> students = ExcelReader.readStudentData(EMAILS_2, START_ROW, NAME_COLUMN, EMAIL_COLUMN);
			
			assertEquals(5, students.size());
			assertEquals("Thomas", students.get(0).getFirstName());
			assertEquals("Wayne", students.get(0).getLastName());
			assertEquals("email@gmail.com", students.get(0).getEmail());
			
			assertEquals("Alex", students.get(1).getFirstName());
			assertEquals("Blouse", students.get(1).getLastName());
			assertEquals("email@gmail.com", students.get(1).getEmail());

			assertEquals("Gregory", students.get(2).getFirstName());
			assertEquals("Thomas", students.get(2).getLastName());
			assertEquals("email@gmail.com", students.get(2).getEmail());
			
			assertEquals("James", students.get(3).getFirstName());
			assertEquals("House", students.get(3).getLastName());
			assertEquals("email@gmail.com", students.get(3).getEmail());
			
			assertEquals("Tom", students.get(4).getFirstName());
			assertEquals("McDonald", students.get(4).getLastName());
			assertEquals("email@gmail.com", students.get(4).getEmail());

			
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * Tests reading students from /test-files/emails3.xlsx
	 * List of 6 names, one name is missing space after comma and one name is in firstname lastname format
	 */
	@Test
	public void testReadEmails3() {
		try {
			ArrayList<Student> students = ExcelReader.readStudentData(EMAILS_3, START_ROW, NAME_COLUMN, EMAIL_COLUMN);
		
			assertEquals(6, students.size());
			assertEquals("Thomas", students.get(0).getFirstName());
			assertEquals("Wayne", students.get(0).getLastName());
			assertEquals("email@gmail.com", students.get(0).getEmail());
			
			assertEquals("Robert", students.get(1).getFirstName());
			assertEquals("Ware", students.get(1).getLastName());
			assertEquals("email@gmail.com", students.get(2).getEmail());
			
			assertEquals("Alex", students.get(2).getFirstName());
			assertEquals("Blouse", students.get(2).getLastName());
			assertEquals("email@gmail.com", students.get(2).getEmail());

			assertEquals("Gregory", students.get(3).getFirstName());
			assertEquals("Thomas", students.get(3).getLastName());
			assertEquals("email@gmail.com", students.get(3).getEmail());
			
			assertEquals("James", students.get(4).getFirstName());
			assertEquals("House", students.get(4).getLastName());
			assertEquals("email@gmail.com", students.get(4).getEmail());
			
			assertEquals("Tom", students.get(5).getFirstName());
			assertEquals("McDonald", students.get(5).getLastName());
			assertEquals("email@gmail.com", students.get(5).getEmail());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}	
	}
	
	/**
	 * Tests reading students from /test-files/emails4.xlsx
	 * List of 6 names, one student doesnt have a last name
	 */
	@Test
	public void testReadEmails4() {
		try {
			ArrayList<Student> students = ExcelReader.readStudentData(EMAILS_4, START_ROW, NAME_COLUMN, EMAIL_COLUMN);
		
			assertEquals(6, students.size());
			assertEquals("Thomas", students.get(0).getFirstName());
			assertEquals(null, students.get(0).getLastName());
			assertEquals("email@gmail.com", students.get(0).getEmail());
			
			assertEquals("Robert", students.get(1).getFirstName());
			assertEquals("Ware", students.get(1).getLastName());
			assertEquals("email@gmail.com", students.get(2).getEmail());
			
			assertEquals("Alex", students.get(2).getFirstName());
			assertEquals("Blouse", students.get(2).getLastName());
			assertEquals("email@gmail.com", students.get(2).getEmail());

			assertEquals("Gregory", students.get(3).getFirstName());
			assertEquals("Thomas", students.get(3).getLastName());
			assertEquals("email@gmail.com", students.get(3).getEmail());
			
			assertEquals("James", students.get(4).getFirstName());
			assertEquals("House", students.get(4).getLastName());
			assertEquals("email@gmail.com", students.get(4).getEmail());
			
			assertEquals("Tom", students.get(5).getFirstName());
			assertEquals("McDonald", students.get(5).getLastName());
			assertEquals("email@gmail.com", students.get(5).getEmail());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}	
	}

}
