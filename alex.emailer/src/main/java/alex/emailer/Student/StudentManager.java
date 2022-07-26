package alex.emailer.Student;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import javax.mail.MessagingException;


import alex.emailer.IO.Emailer;
import alex.emailer.IO.ExcelReader;
import alex.emailer.IO.ZipReader;

/**
 * Manages all of the students in the classroom
 * @author Alex
 *
 */
public class StudentManager {
	/** Location where zip file of student reports are to be unzipped to. This is the java temp directory **/
	public static final String ZIP_DESTINATION = System.getProperty("java.io.tmpdir");

	/** Initial value for startRow*/
	public static final int DEFAULT_START_ROW = 2;
	
	/** initial value for nameColumn*/
	public static final int DEFAULT_NAME_COLUMN = 1;
	
	/** initial value for emailColumn */
	public static final int DEFAULT_EMAIL_COLUMN = 2;

	/** Row where names will start. 1-indexed, use the excel row number. Initially set to DEFAULT_START_ROW */
	private int startRow;
	
	/** Column number where names will be. Zero indexed. Initially set to DEFAULT_NAME_COLUMN*/
	private int nameColumn;
	
	/** Column number where email will be. Zero indexed. Initially set to DEFAULT_EMAIL_COLUMN */
	private int emailColumn;
	
	/** List of students in class **/
	private ArrayList<Student> students;
	
	/** List of removed students for backup */
	private ArrayList<Student> removedStudents;
	
	/** List of all students who have been sent emails */
	private ArrayList<Student> sentStudents;
	
	/** The path to where zip file will be unzipped to. Will be set to the java temp directory */
	private Path zipPath;
	
	/** Singleton instance of StudentManager */
	private static StudentManager instance = new StudentManager();
	
	
	/**
	 * Constructs a StudentManager.
	 */
	private StudentManager() {
		try {
			this.zipPath = Files.createTempDirectory("java-");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resetStartInfo();
		this.students = new ArrayList<Student>();
		this.removedStudents = new ArrayList<Student>();
		this.sentStudents = new ArrayList<Student>();
	}

	/** Gets the singleton instance of StudentManager 
	 * @return the instance
	 * */
	public static StudentManager getInstance() {
		return instance;
	}
	
	/**
	 * Gets the file location where the zip files will be unzipped to. Will be the java temp directory.
	 * @return the zip destination file location
	 */
	public String getZipDestination(){
		return zipPath.toAbsolutePath().toString();	
	}
	
	/** Gets the startRow. 1-indexed 
	 * @return the start row
	 * */
	public int getStartRow() {
		return startRow;
	}
	
	/**
	 * Set the row where the student information will begin in the file. 1-indexed
	 * @param startRow the start row
	 */
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	
	/**
	 * Gets the name column as an integer
	 * @return name column the name column
	 */
	public int getNameColumn() {
		return nameColumn;
	}
	
	/**
	 * Gets the name Column as a char
	 * @return the name column as a char
	 */
	public char getNameColumnAsChar() {
		return getCharFromNumber(nameColumn + 1);
	}
	
	/**
	 * Set the column where names will be in the file. Uses excel letter column names.
	 * @param nameColumn the name column
	 */
	public void setNameColumn(char nameColumn) {
		this.nameColumn = getAlphabeticalValue(Character.toLowerCase(nameColumn)) - 1;;
	}
	
	/**
	 * Gets the email column as an integer
	 * @return emailColumn the email column
	 */
	public int getEmailColumn() {
		return emailColumn;
	}
	
	/**
	 * Gets the email column as a char
	 * @return the email column as a char
	 */
	public char getEmailColulmnAsChar() {
		return getCharFromNumber(emailColumn + 1);
	}
	
	/**
	 * Set the column where emails will be in the file. Uses excel letter column names.
	 * @param emailColumn the email column
	 */
	public void setEmailColumn(char emailColumn) {
		this.emailColumn = getAlphabeticalValue(Character.toLowerCase(emailColumn)) - 1;
	}
	
	
	private char getCharFromNumber(int i) {
		return (char)(i + 64);
	}
	
	private int getAlphabeticalValue(char ch) {
		int temp = (int)ch;
		int temp_integer = 96;
		if(temp <= 122 && temp >= 97) {
			return temp - temp_integer;
		}
		else throw new IllegalArgumentException("Invalid column");
	}
	
	/**
	 * Resets startRow, nameColumn, and emailColumn to their default values.
	 */
	public void resetStartInfo() {
		setStartRow(DEFAULT_START_ROW);
		this.nameColumn = DEFAULT_NAME_COLUMN;
		this.emailColumn = DEFAULT_EMAIL_COLUMN;
	}
		
	/**
	 * Reads student information from an excel file and creates Student objects with
	 * the corresponding information. Students are then added to a list of students
	 * 
	 * @param filename the location of the excel file
	 */
	public void loadStudents(String filename) {
		if(nameColumn == emailColumn) {
			throw new IllegalArgumentException("Name Column and Email Column cannot be the same.");
		}
		try {
			students.addAll(ExcelReader.readStudentData(filename, startRow, nameColumn, emailColumn));
			//matchStudents();
		}
		catch(IOException E) {
			throw new IllegalArgumentException("Invalid file");
		}

	}

	/**
	 * Attempts to match all Students in students
	 * with a file located in zipPath that begins with "firstname lastname" Students
	 * that are matched are added to matchedStudents Students that are not matched
	 * are added to unmatchedStudents
	 * 
	 * @return true if all Students find a match, false if any Student does not have
	 *         a match
	 */
	public boolean matchStudents() {
		
		boolean allHaveMatches = true;
		File directory = new File(getZipDestination());
		for (final Student student : students) {
			File[] matches = directory.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.startsWith(student.getFirstName() + " " + student.getLastName());
				}
			});
			if (matches.length == 0) {
				allHaveMatches = false;
			} else {
				student.setReport(matches[0]);
			}

		}
		return allHaveMatches;
	}

	/**
	 * Unzips directory containing student report files to zipPath.
	 * 
	 * @param filename location of zip file to be unzipped
	 */
	public void unzipFiles(String filename) {
		try {
			ZipReader.unzip(filename, getZipDestination());
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Cannot import file");
		}
	}

	/**
	 * Gets a list of the files in zipPath that have not been matched to a student in students
	 * @return unmatchedFiles the array list of unmatched files
	 */
	public ArrayList<File> getUnmatchedFiles() {
		
		File directory = new File(getZipDestination());
		File[] files = directory.listFiles();
		ArrayList<File> unmatchedFiles = new ArrayList<File>();
		for(int i = 0; i < files.length ; i++) {
			boolean matched = false;
			for(Student student : students) {
				if(files[i].equals(student.getReport())) {
					matched = true;
				}
			}
			if (!matched) {
				unmatchedFiles.add(files[i]);
			}
		}

		return unmatchedFiles;
		
	}
	
	/**
	 * Sends email to all students in students with the corresponding file attachment.
	 * @return unsentStudents list of students who were unable to send to
	 * @throws IllegalArgumentException if not all students have been matched with a report.
	 */
	public ArrayList<Student> emailStudents() {
		if(students.size() == 0) {
			throw new IllegalArgumentException("Students have not been loaded yet.");
		}
		ArrayList<Student> unmatchedStudents = getUnmatchedStudents();
		ArrayList<Student> unsentStudents = new ArrayList<Student>();
		if (unmatchedStudents.size() != 0) {
			if(unmatchedStudents.size() == 1) {
				throw new IllegalArgumentException("One student has not been matched with a report. Manually add a report or remove the student without a report.");
			}
			else throw new IllegalArgumentException(unmatchedStudents.size() + " students have not been matched with a report. Manually add a report or remove students without a report.");
		}
		
		for (Student student : students) {
			try {
				Emailer.sendEmail(student);
				sentStudents.add(student);
			} catch (MessagingException | IOException e) {
				unsentStudents.add(student);
				e.printStackTrace();
			} 
		}
		System.out.println("Finished sending.");
		System.out.println(unsentStudents.size() + " emails not sent.");
		return unsentStudents;
	}

	/**
	 * Gets the list of students
	 * @return the list of students
	 **/
	public ArrayList<Student> getStudents() {
		return students;
	}

	/**
	 * Gets a Student in students based on first and last name
	 * 
	 * @param firstName the first name of the student
	 * @param lastName  the last name of the student
	 * @return the student with matching first and last name, null if no student is
	 *         found.
	 */
	public Student getStudentByName(String firstName, String lastName) {
		for (Student student : students) {
			if (student.getFirstName().equals(firstName) && student.getLastName().equals(lastName)) {
				return student;
			}
		}
		return null;
	}

	/**
	 * Gets a list of unmatched Students
	 * 
	 * @return unmatchedStudents the unmatched Students
	 */
	public ArrayList<Student> getUnmatchedStudents() {
		ArrayList<Student> unmatchedStudents = new ArrayList<Student>();
		for(Student student : students) {
			if(!student.isMatched()) {
				unmatchedStudents.add(student);
			}
		}
		return unmatchedStudents;
	}

	/**
	 * Gets the list of matched Students
	 * 
	 * @return matchedStudents the matched Students
	 */
	public ArrayList<Student> getMatchedStudents() {
		ArrayList<Student> matchedStudents = new ArrayList<Student>();
		for(Student student : students) {
			if(student.isMatched()) {
				matchedStudents.add(student);
			}
		}
		return matchedStudents;
	}

	/** Adds a student to the list of students 
	 * @param student the student to add
	 * */
	public void addStudent(Student student) {
		students.add(student);
	}

	/**
	 * Tries to remove a Student from students based on first and last name. Adds removed student to backup list of removed students
	 * @param firstName the first name of the student
	 * @param lastName the last name of the student
	 * @return true if student is found with matching names, false if no student is found
	 */
	public boolean removeStudent(String firstName, String lastName) {
		for(int i = 0; i < students.size(); i++) {
			if(students.get(i).getFirstName().equals(firstName) && students.get(i).getLastName().equals(lastName)) {
				removedStudents.add(students.get(i));
				students.remove(i);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removes all students from students and adds them all to removed students
	 */
	public void clearStudents() {
		for(Student student : students) {
			removedStudents.add(student);
		}
		students = new ArrayList<Student>();
	}
	
	/**
	 * Removes all students from students without adding them all to removed students
	 */
	public void resetStudents() {
		students = new ArrayList<Student>();
	}
	
	/**
	 * Gets a list of students that have been removed
	 * @return the removed students
	 */
	public ArrayList<Student> getRemovedStudents(){
		return removedStudents;
	}
	
	/**
	 * Set the username and password of the user
	 * 
	 * For gmail, password will have to be an app password (found under security in manage account). May be similar for other email hosts

	 * @param username the user's username/email
	 * @param password the user's password
	 */
	public void setLoginInfo(String username, String password) {
		Emailer.setLoginInfo(username, password);	
	}
	
	/**
	 * Sets the username, password, and host of the user
	 * 
	 * For gmail, password will have to be an app password (found under security in manage account). May be similar for other email hosts
	 * 
	 * @param username the user's username/email
	 * @param password the user's password
	 * @param host the host address
	 */
	public void setLoginInfo(String username, String password, String host) {
		Emailer.setLoginInfo(username, password);
		Emailer.setHost(host);
	}
	
	/**
	 * Adds a student from removedStudents
	 * @param student the student to be readded
	 * @return true if student is in removedStudents, false if student was not in removedStudents
	 */
	public boolean reAddStudent(Student student) {
		if(removedStudents.contains(student)) {
			students.add(student);
			removedStudents.remove(student);
			return true;
		}
		return false;
	}
	
	/**
	 * Re-adds all students in removedStudents.
	 */
	public void reAddAll() {
		for(Student student : removedStudents) {
			students.add(student);
		}
		removedStudents = new ArrayList<Student>();
	}
}

// :)

