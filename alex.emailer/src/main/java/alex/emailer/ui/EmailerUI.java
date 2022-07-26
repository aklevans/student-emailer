package alex.emailer.ui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;

import alex.emailer.IO.Emailer;
import alex.emailer.Student.Student;
import alex.emailer.Student.StudentManager;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
 
public class EmailerUI {
	
	public static void main(String[] args) {

		JFrame f = new JFrame("Emailer");
		f.setSize(1200, 800);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		// Menu Bar
	    
	    JMenuBar menuBar = new JMenuBar();
	    JMenu editMenu = new JMenu("Edit");
	    

	     
		JPanel pane = new JPanel(new GridBagLayout());

		f.setContentPane(pane);
		
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.NORTH;
		
		
		JTextField usernameField = new JTextField(20);
	    JTextField passwordField = new JTextField(10);
	    JTextField hostField = new JTextField(10);
	    JTextField portField = new JTextField(5);
	    
	    Properties loginProp = new Properties();
	    try(InputStream input = new FileInputStream(Emailer.LOGIN_PROP)){
	    	loginProp.load(input);
	    }
	    catch(IOException e) {
		    Emailer.setUp();
	    }
	    
	    if(loginProp.getProperty("user") == null || loginProp.getProperty("user").length() == 0 || 
	    		loginProp.getProperty("pw") == null || loginProp.getProperty("pw").length() == 0) {
	    	JPanel loginPanel = new JPanel();
		    loginPanel.add(new JLabel("username:"));
		    loginPanel.add(usernameField);
		    loginPanel.add(Box.createHorizontalStrut(15)); // a spacer
		    loginPanel.add(new JLabel("password:"));
		    loginPanel.add(passwordField);
		    loginPanel.add(Box.createHorizontalStrut(15)); // a spacer

		    
			JOptionPane.showMessageDialog(f, loginPanel);
			
			Emailer.setLoginInfo(usernameField.getText(), passwordField.getText());
	   }
	    

	    
	
				
		
		
		// Main Table
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Name");
		model.addColumn("Email");
		model.addColumn("File");
		JTable table = new JTable(model) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.getTableHeader().setReorderingAllowed(false);

		JScrollPane sp = new JScrollPane(table);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		pane.add(sp, c);
		

		// Unmatched Files table
		DefaultTableModel unmatchedModel = new DefaultTableModel();
		unmatchedModel.addColumn("unmatchedFiles");
		JTable table2 = new JTable(unmatchedModel) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JScrollPane sp2 = new JScrollPane(table2);
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		pane.add(sp2, c);
		
		
		// Unzip Button
		JButton unzipButton = new JButton("Unzip Files");
		unzipButton.setEnabled(false);
		unzipButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				JFileChooser fc = new JFileChooser();
				int r = fc.showOpenDialog(null);
				if(r == JFileChooser.APPROVE_OPTION) {
					StudentManager.getInstance().unzipFiles(fc.getSelectedFile().getAbsolutePath());
					StudentManager.getInstance().matchStudents();
					ArrayList<Student> students = StudentManager.getInstance().getStudents();
					for(int i = 0; i < students.size(); i++) {
						Student student = students.get(i);
						if(student.isMatched()) {
							table.setValueAt(students.get(i).getReport().getName(), i, 2);
						}
					}
					
					for(int i = 0; i < unmatchedModel.getRowCount(); i++) {
						unmatchedModel.removeRow(0);
					}
					ArrayList<File> unmatchedFiles = StudentManager.getInstance().getUnmatchedFiles();
					for(File file : unmatchedFiles) {
						Object[] fileInfo = {file};
						unmatchedModel.addRow(fileInfo);
					}
				}
				
			}
			
		});
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 1;
		pane.add(unzipButton, c);
		
		
		
		
		// ImportExcelButton
		JButton importExcelButton = new JButton("Import Excel");
		importExcelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	

				JOptionPane.showMessageDialog(f, "Remember, names must be in column " + StudentManager.getInstance().getNameColumnAsChar() + " and emails must be in column "
													+ StudentManager.getInstance().getEmailColulmnAsChar() + "!");
				JFileChooser fc = new JFileChooser();
				int r = fc.showOpenDialog(null);
				if(r == JFileChooser.APPROVE_OPTION) {
					try {
						StudentManager.getInstance().loadStudents(fc.getSelectedFile().getAbsolutePath());
						ArrayList<Student> students = StudentManager.getInstance().getStudents();
						for(Student student : students) {
							Object[] studentInfo = {student, student.getEmail(), null};
							if(student.isMatched()) {
								studentInfo[2] = student.getReport().getName();

							}
							model.addRow(studentInfo);
						}
						unzipButton.setEnabled(true);
					} catch(NotOfficeXmlFileException a) {
						JOptionPane.showMessageDialog(f, a.getMessage());

					}
					
					
				}
			}
		});
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 1;
		pane.add(importExcelButton, c);
		
		
		
		// Main buttons
		
		
		// Add match file button
		JButton matchFileButton = new JButton("Match file");
		matchFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int tableRow = table.getSelectedRow();
				int table2Row = table2.getSelectedRow();
				if(table.getSelectedRowCount() > 1) {
					JOptionPane.showMessageDialog(f, "Please select only one student");
				}
				else if (table2.getSelectedRowCount() > 1) {
					JOptionPane.showMessageDialog(f, "Please select only one file");

				}
				else if(tableRow == -1 && table2Row == -1) {
					JOptionPane.showMessageDialog(f, "Please select a student and a file from the unmatched files table");
				}
				else if(tableRow == -1) {
					JOptionPane.showMessageDialog(f, "Please select a student");
				}
				else if (table2Row == -1) {
					JOptionPane.showMessageDialog(f, "Please select a file from the unmatched files table");

				}
				else {
					Student student = (Student) table.getValueAt(tableRow, 0);
					int a = JOptionPane.YES_OPTION;
					if(student.isMatched()) {
						a = JOptionPane.showConfirmDialog(f, "This student is already matched with a file. Are you sure you want to proceed?");
					}
					if(a == JOptionPane.YES_OPTION) {
						student.setReport((File) table2.getValueAt(table2Row, 0));
						table.setValueAt(student.getReport().getName(), tableRow, 2);
						unmatchedModel.removeRow(table2Row);
					}

				}
			}
		});
		c.gridx = 2;
		c.gridy = 3;
		c.gridwidth = 2;
		c.gridheight = 1;
		pane.add(matchFileButton, c);
		
		
		
		// Edit Student button
		JTextField firstNameField = new JTextField(10);
		JTextField lastNameField = new JTextField(10);
		JTextField emailField = new JTextField(20);
		
		JButton editStudentButton = new JButton("Edit Student");
		editStudentButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int tableRow = table.getSelectedRow();
				if(table.getSelectedRowCount() > 1) {
					JOptionPane.showMessageDialog(f, "Please select only one student");
				}
				else if(tableRow == -1) {
					JOptionPane.showMessageDialog(f, "Please select a student");
				}
				else {
					Student selected = (Student) table.getValueAt(tableRow, 0);
					firstNameField.setText(selected.getFirstName());
					lastNameField.setText(selected.getLastName());
					emailField.setText(selected.getEmail());
					JPanel newStudentPanel = new JPanel();
					newStudentPanel.add(new JLabel("First Name:"));
					newStudentPanel.add(firstNameField);
					newStudentPanel.add(Box.createHorizontalStrut(15)); // a spacer
					newStudentPanel.add(new JLabel("Last Name:"));
					newStudentPanel.add(lastNameField);
					newStudentPanel.add(Box.createHorizontalStrut(15)); // a spacer
					newStudentPanel.add(new JLabel("Email:"));
					newStudentPanel.add(emailField);
					JOptionPane.showMessageDialog(f, newStudentPanel);
					
					selected.setFirstName(firstNameField.getText());
					selected.setLastName(lastNameField.getText());
					selected.setEmail(emailField.getText());
					table.setValueAt(selected, tableRow, 0);
					table.setValueAt(selected.getEmail(), tableRow, 1);

				}
			}
			
		});
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		pane.add(editStudentButton, c);
		
		// Remove Student Button
		JButton removeStudentButton = new JButton("Remove Student");
		removeStudentButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int tableRow = table.getSelectedRow();
				if(table.getSelectedRowCount() > 1) {
					JOptionPane.showMessageDialog(f, "Please select only one student");

				}
				else if(tableRow == -1) {
					JOptionPane.showMessageDialog(f, "Please select a student");
				}
				else {
					Student removed = (Student) table.getValueAt(tableRow, 0);
					model.removeRow(tableRow);
					StudentManager.getInstance().removeStudent(removed.getFirstName(), removed.getLastName());
				}
				
				
			}
			
		});
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		pane.add(removeStudentButton, c);
		
		
		// Add Student Button
		
		JButton addStudentButton = new JButton("Add Student");
		addStudentButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel newStudentPanel = new JPanel();
				newStudentPanel.add(new JLabel("First Name:"));
				newStudentPanel.add(firstNameField);
				newStudentPanel.add(Box.createHorizontalStrut(15)); // a spacer
				newStudentPanel.add(new JLabel("Last Name:"));
				newStudentPanel.add(lastNameField);
				newStudentPanel.add(Box.createHorizontalStrut(15)); // a spacer
				newStudentPanel.add(new JLabel("Email:"));
				newStudentPanel.add(emailField);
				JOptionPane.showMessageDialog(f, newStudentPanel);
				
				Student newStudent = new Student(firstNameField.getText(), lastNameField.getText(), emailField.getText());
				StudentManager.getInstance().addStudent(newStudent);
				Object[] studentInfo = {newStudent, newStudent.getEmail(), null};
				model.addRow(studentInfo);
			}
			
		});
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		c.gridheight = 1;
		pane.add(addStudentButton, c);
		
		// Add Removed Student Button
		JButton addRemovedStudentButton = new JButton("Re-add removed student");
		addRemovedStudentButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
	
				JPanel removedStudentsPanel = new JPanel();
				
				DefaultTableModel removedModel = new DefaultTableModel();
				removedModel.addColumn("Name");
				removedModel.addColumn("Email");
				removedModel.addColumn("File");
				JTable table = new JTable(removedModel) {
					private static final long serialVersionUID = 1L;
					public boolean isCellEditable(int row, int column) {
						return false;
					}
				};
				table.getTableHeader().setReorderingAllowed(false);
				JScrollPane sp = new JScrollPane(table);
				ArrayList<Student> removedStudents = StudentManager.getInstance().getRemovedStudents();
				for(int i = 0; i < removedStudents.size(); i++) {
					Object[] studentInfo = {removedStudents.get(i), removedStudents.get(i).getEmail(), removedStudents.get(i).getReportName()};
					removedModel.addRow(studentInfo);
				}
				removedStudentsPanel.add(sp);
				
				String[] options = new String[3];
				options[0] = "Re-Add selected";
				options[1] = "Re-Add all";
				options[2] = "Cancel";
				int selection = JOptionPane.showOptionDialog(f, removedStudentsPanel, "Removed Students", 0, JOptionPane.INFORMATION_MESSAGE, null, options, null);
				if(selection == 0) {
					int[] selectedRows = table.getSelectedRows();
					for(int i = 0; i < selectedRows.length; i++) {
						Student student = (Student) table.getValueAt(selectedRows[i], 0); 
						StudentManager.getInstance().reAddStudent(student);
						Object[] row = {student, student.getEmail(), student.getReportName()};
						model.addRow(row);
					}	
				}
				if(selection == 1) {
					for(int i = 0; i < removedStudents.size(); i++) {
						Object[] studentInfo = {removedStudents.get(i), removedStudents.get(i).getEmail(), removedStudents.get(i).getReportName()};
						model.addRow(studentInfo);
					}
					StudentManager.getInstance().reAddAll();
				}
			}
		});

		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 1;
		c.gridheight = 1;
		pane.add(addRemovedStudentButton, c);
		
		
		// Clear Students Button
		JButton clearStudentsButton = new JButton("Clear Students");
		clearStudentsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StudentManager.getInstance().clearStudents();
				int totalRows = model.getRowCount();
				for(int i = 0; i < totalRows; i++) {
					model.removeRow(0);
				}
			}
			
		});
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 1;
		c.gridheight = 1;
		pane.add(clearStudentsButton, c);

		
		// Attach Single file button 
		JButton manualFileButton = new JButton("Manually attach file");
		manualFileButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int tableRow = table.getSelectedRow();
				if(tableRow == -1) {
					JOptionPane.showMessageDialog(f, "Please select a student");
				}
				else if(table.getSelectedRowCount() > 1) {
					JOptionPane.showMessageDialog(f, "Please select only one student.");
				}
				else {
					JFileChooser fc = new JFileChooser();
					int r = fc.showOpenDialog(null);
					if(r == JFileChooser.APPROVE_OPTION) {
						Student selected = (Student) table.getValueAt(tableRow, 0);
						selected.setReport(fc.getSelectedFile());
						table.setValueAt(selected.getReport().getName(), tableRow, 2);

					}
				}
				
			}
			
		});
		c.gridx = 1;
		c.gridy = 5;
		c.gridwidth = 1;
		c.gridheight = 1;
		pane.add(manualFileButton , c);
		
		// Login Info Display
		
		try(InputStream input = new FileInputStream(Emailer.LOGIN_PROP)){
		    loginProp.load(input);
		}
		catch(IOException e) {
			
		}
		
		JLabel username = new JLabel("Sending from: " + loginProp.getProperty("user"));
		c.gridx = 2;
		c.gridy = 4;
		c.gridwidth = 1;
		c.gridheight = 1;
		pane.add(username, c);
		

		// Advanced Email Settings
		JButton advancedSettings = new JButton("Advanced Email Settings");
		advancedSettings.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try(InputStream input = new FileInputStream(Emailer.LOGIN_PROP)){
			    	loginProp.load(input);
			    }
			    catch(IOException f) {
			    	
			    }
				hostField.setText(loginProp.getProperty("host"));
				portField.setText(loginProp.getProperty("port"));
				JPanel advancedPanel = new JPanel();
				advancedPanel.add(new JLabel("host address:"));
				advancedPanel.add(hostField);
				advancedPanel.add(Box.createHorizontalStrut(15)); // a spacer
				advancedPanel.add(new JLabel("port:"));
				advancedPanel.add(portField);
				advancedPanel.add(Box.createHorizontalStrut(15)); // a spacer
				JOptionPane.showMessageDialog(f, advancedPanel);
				
				Emailer.setHost(hostField.getText());
				Emailer.setPort(portField.getText());;
				
			}
			
		});
		c.gridx = 3;
		c.gridy = 4;
		c.gridwidth = 1;
		c.gridheight = 1;
		pane.add(advancedSettings, c);
		
		
		// Edit Login Info button

		JButton editLoginInfo = new JButton("Edit login info");
		editLoginInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try(InputStream input = new FileInputStream(Emailer.LOGIN_PROP)){
			    	loginProp.load(input);
			    }
			    catch(IOException f) {
			    	
			    }
				usernameField.setText(loginProp.getProperty("user")); 
				passwordField.setText(loginProp.getProperty("pw"));
				JPanel loginPanel = new JPanel();
			    loginPanel.add(new JLabel("username:"));
			    loginPanel.add(usernameField);
			    loginPanel.add(Box.createHorizontalStrut(15)); // a spacer
			    loginPanel.add(new JLabel("password:"));
			    loginPanel.add(passwordField);
			    loginPanel.add(Box.createHorizontalStrut(15)); // a spacer
				JOptionPane.showMessageDialog(f, loginPanel);
				
				username.setText("Sending from: " + usernameField.getText());
				Emailer.setLoginInfo(usernameField.getText(), passwordField.getText());
			}
		});
		c.gridx = 2;
		c.gridy = 5;
		c.gridwidth = 2;
		c.gridheight = 1;
		pane.add(editLoginInfo, c);
		

		
		// Send Email button
		JButton sendButton = new JButton("Send Emails");
		sendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selection = JOptionPane.showConfirmDialog(f, "Are you sure you want to send?", "Confirm", 2, 0, null);
				if(selection == 0) {
					try {
						ArrayList<Student> unsentStudents = StudentManager.getInstance().emailStudents();
						if(unsentStudents.size() == 0) {
							JOptionPane.showMessageDialog(f, "All emails sent successfully!");
	
	
						}
						else {
							JOptionPane.showMessageDialog(f, "Emails sent with " + unsentStudents.size() + " failures. Student Email or login info may be incorrect.");
	
						}
						StudentManager.getInstance().resetStudents();
						int totalRows = model.getRowCount();
						for(int i = 0; i < totalRows; i++) {
							model.removeRow(0);
						}
						for(Student student : unsentStudents) {
							StudentManager.getInstance().addStudent(student);
							Object[] studentInfo = {student, student.getEmail(), student.getReport().getName()};
							model.addRow(studentInfo);
						}
					}
					catch(IllegalArgumentException p) {
						JOptionPane.showMessageDialog(f, p.getMessage());
					} 
				}
			}
			 
		});
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 4;
		c.gridheight = 1;
		pane.add(sendButton, c);
		

		f.setVisible(true);
		
	}
}
