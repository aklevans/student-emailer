package alex.emailer.IO;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import alex.emailer.Student.StudentManager;

public class ZipReaderTest {

	public static final String ZIP_LOCATION = "src/test/resources/test-files/ExampleZip.zip";
	
	/** Location where zip file of student reports are to be unzipped to. This is the java temp directory **/
	public static final String ZIP_DESTINATION = System.getProperty("java.io.tmpdir");
	
	/**
	 * Tests unzip method. Visually check ZIP_DESTINATION to confirm files have appeared
	 */
	@Test
	public void testUnzip() {
		try {
			ZipReader.unzip(ZIP_LOCATION, ZIP_DESTINATION);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

}
