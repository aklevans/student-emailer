package alex.emailer.IO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import alex.emailer.Student.Student;

/**
 * Handles the interpreting of an excel file containing student information
 * @author Alex
 *
 */
public class ExcelReader {

	
	/**
	 * Reads student information from an excel file and creates a list of students with the corresponding information
	 * @param fileLocation the location of the excel file
	 * @param startRow the first row with student information, 1-indexed to match excel row number
	 * @param nameColumn the column in which names will be. Zero indexed
	 * @param emailColumn the column in which emails will be. Zero indexed
	 * @return the list of students 
	 * @throws IOException if file is invalid
	 */
	public static ArrayList<Student> readStudentData(String fileLocation, int startRow, int nameColumn, int emailColumn) throws IOException {
		FileInputStream file = new FileInputStream(new File(fileLocation));


		Workbook workbook = new XSSFWorkbook(file);
		
		Sheet sheet = workbook.getSheetAt(0);

		ArrayList<Student> students = new ArrayList<Student>();
		
		// Goes throw all rows in sheet starting at start row. Skips blank lines
		for (int i = startRow - 1; i <= sheet.getLastRowNum(); i++) {
			try {
				Row row = sheet.getRow(i); 
				String name = row.getCell(nameColumn).getStringCellValue();
				String email = row.getCell(emailColumn).getStringCellValue();
			 
				if(isValidEmail(email) && isValidName(name)) {
					String firstName = null;
					String lastName = null;
					if(name.contains(",")) { // if names are in lastname,firstname format
						String[] lastFirstName = name.split(", ?"); // index zero is last name, index 1 is first name
						try {
							firstName = lastFirstName[1];
						} catch (ArrayIndexOutOfBoundsException e) {
							// skip
						}
						lastName = lastFirstName[0];

					}
					else { // assumes names are in firstname lastname format
						String[] firstLastName = name.split(" "); // index zero is first name, index 1 is last name
						firstName = firstLastName[0];
						if (firstLastName.length > 1) {
							lastName = firstLastName[1];
						}
					}

					Student s = new Student(firstName, lastName, email);
					students.add(s);
				}
			}
			catch(NullPointerException e) {
				// skip line
			}
		}
		
		workbook.close();
		return students;
	}
	
	/**
	 * Checks if email is valid
	 * @param string
	 * @return
	 */
	private static boolean isValidEmail(String string) {
		return string.length() != 0;
	}
	
	/**
	 * checks if name is valid
	 * @param name
	 * @return
	 */
	private static boolean isValidName(String name) {
		return name.length() != 0;
	}
}


