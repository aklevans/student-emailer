package alex.emailer.Student;

import java.io.File;

/**
 * Class representing a Student
 * @author Alex
 *
 */
public class Student {
	
	/** The Student's first name */
	private String firstName;
	
	/** The Student's last name */
	private String lastName;
	
	/** The Student's email */
	private String email;

	/** The Student's report file */
	private File report;
		
	/** 
	 * Constructs a Student 
	 * @param firstName the firstName
	 * @param lastName the lastName
	 * @param email the email
	 */
	public Student(String firstName, String lastName, String email) {
		setFirstName(firstName);
		setLastName(lastName);
		setEmail(email);
	}

	/**
	 * Gets the student's first name
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the students first name
	 * @param firstName the first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the Student's last name
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the Student's last name
	 * @param lastName the last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the Student's email
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the Student's email
	 * @param email the email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/** Gets the Student's report file
	 * 
	 * @return the report file
	 */
	public File getReport() {
		return report;
	}
	
	/**
	 * Gets the name of the report file that the student has
	 * @return the report's name, null if no report is attached to the student
	 */
	public String getReportName() {
		if(report == null) {
			return null;
		}
		else return report.getName();
	}
	/**
	 * Set's the Student's report file
	 * @param file the report file
	 */
	public void setReport(File file) {
		this.report = file;
	}
	
	/**
	 * Removes the Student's report file
	 */
	public void unmatch() {
		this.report = null;
	}
	
	/**
	 * Checks whether the Student has been matched with a file
	 * @return true if student is matched with a file, false if not
	 */
	public boolean isMatched() {
		return report != null;
	}

	/**
	 * Gets the student as a string containing first and last name separated with a space
	 */
	@Override
	public String toString() {
		return firstName + " " + lastName;
	}
	

	
}
