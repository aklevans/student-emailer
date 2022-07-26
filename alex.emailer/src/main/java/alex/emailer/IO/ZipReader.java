package alex.emailer.IO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
/**
 * Reads zip directory
 * @author Alex
 *
 */
public class ZipReader {
	
	/**
	 * Unzips a zip directory to a location
	 * modified from from https://www.baeldung.com/java-compress-and-uncompress
	 * @param filename location of zip directory to unzip
	 * @param destination file location of where to put uncompressed files
	 * @throws IOException if file is not fount
	 */
	public static void unzip(String filename, String destination) throws IOException {
		File destDir = new File(destination);
		byte[] buffer = new byte[1024];
		ZipInputStream zis = new ZipInputStream(new FileInputStream(filename));
		ZipEntry zipEntry = zis.getNextEntry();
		while(zipEntry != null) {
			 File newFile = newFile(destDir, zipEntry);
		     if (zipEntry.isDirectory()) {
		         if (!newFile.isDirectory() && !newFile.mkdirs()) {
		             throw new IOException("Failed to create directory " + newFile);
		         }
		     } else {
		         // fix for Windows-created archives
		         File parent = newFile.getParentFile();
		         if (!parent.isDirectory() && !parent.mkdirs()) {
		             throw new IOException("Failed to create directory " + parent);
		         }
		         
		         // write file content
		         FileOutputStream fos = new FileOutputStream(newFile);
		         int len;
		         while ((len = zis.read(buffer)) > 0) {
		             fos.write(buffer, 0, len);
		         }
		         fos.close();
		     }
		 zipEntry = zis.getNextEntry();
		}
		
		zis.closeEntry();
		zis.close();
				
	}

	private static File newFile(File destDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destDir, zipEntry.getName());

	    String destDirPath = destDir.getCanonicalPath();
	    String destFilePath = destFile.getCanonicalPath();

	    if (!destFilePath.startsWith(destDirPath + File.separator)) {
	        throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
	    }

	    return destFile;
	} 
}
