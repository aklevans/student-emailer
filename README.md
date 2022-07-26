# student-emailer
This program was created to automatically email students a file that corresponds to them. The student information would be from an excel file and all of the attachments would be in a zip file with filenames that match the student names. 

The program runs from /alex.emailer/src/main/java/alex/emailer/ui/EmailerUI.java

Workflow:
1. Run EmailerUI
2. If it is the first time running, it will ask for your email, password, email host address, and port (more on email settings later).
3. Click "Import Excel" button. Select an excel file containing student information (more on formatting later). After this you should see a table containing student names and emails from the file.
4. Click "Unzip Files" button and select the zip file containing all of the attachments. The program will then try to automatically match all of the files to the students that have already been loaded in, which will be seen on the left side of the screen. All files that were unable to be matched will be in the "unmatchedFiles" table on the right side of the screen. You can then select a file from this table and a student from the left table and click the "Match file" button to manually match a file to a student.
5. If there is any incorrect information in the student table, you can correct it with the many buttons available: "Edit student" lets you change the first and last name of a student as well as their email. "Remove student" removes a student. If you accidentally removed a student, you can click "Re-add removed student" and see all of the students you have removed and select the ones you wish to add back. "Clear students" removes all of the students from the list. If you wish to attach a file to a student that was not from the zip file, you can select a student and click "Manually attach file".
6. You can edit your email login info by pressing the "Edit login info" button. "Advanced Email Settings" allows you to change your email host address and port.
7. Click "Send Emails" to send all emails. After attempting this, any students who were unable to have an email sent to them will remain in the list.


Excel Format:
The default column for names is B (StudentManager.DEFAULT_NAME_COLUMN = 1) and can be written as "firstname lastname" with a space or "lastname, firstname"
The default column for emails is C (StudentManager.DEFAULT_Name_COLUMN = 2) 
Currently there is no functionality in the GUI to change these values, though there are the methods setNameColumn() and setEmailColumn() in StudentManager that would allow for that functionality. If you wish to change the default format, I would recommend just changing StudentManager.DEFAULT_NAME_COLUMN and  StudentManager.DEFAULT_Name_COLUMN

Email Settings:
Many email servers including gmail require you to use an app specific password for programs like these. You can generate an app password through the account managing page of your email program. Host address and port can easily be found on the internet. For example, gmails address is smtp.gmail.com and its port is 465.

Email information will be saved on a local properties file called "emailer.properties". When starting up, if the program cannot find your properties file, it will create one in the same directory that the program is saved.

