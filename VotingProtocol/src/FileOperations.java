

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class FileOperations{
	public static void modifyFile(String filename, String data){
		File file =new File(filename);
		try {
			if(!file.exists()){
				file.createNewFile();
			}

			//true = append file
			FileWriter fileWritter = new FileWriter(file.getAbsoluteFile(),true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			String newline = System.getProperty("line.separator");
			bufferWritter.write(newline+data);
			bufferWritter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void copyFile(String fileName, String newFileName){
		InputStream inStream = null;
		OutputStream outStream = null;
	 
	    	try{
	 
	    	    File afile =new File(fileName);
	    	    File bfile =new File(newFileName);
	 
	    	    inStream = new FileInputStream(afile);
	    	    outStream = new FileOutputStream(bfile);
	 
	    	    byte[] buffer = new byte[1024];
	 
	    	    int length;
	    	    //copy the file content in bytes 
	    	    while ((length = inStream.read(buffer)) > 0){
	 
	    	    	outStream.write(buffer, 0, length);
	 
	    	    }
	 
	    	    inStream.close();
	    	    outStream.close();
	    	    LogToFile.log(Config.LOG_FILE,"File copied successfully!");
	    	    System.out.println("File is copied successfully!");
	 
	    	}catch(IOException e){
	    		e.printStackTrace();
	    	}
	}
	
	public static void renameFile(String fileName, String newName){
		
		File oldfile =new File(fileName);
		File newfile =new File(newName);
 
		if(oldfile.renameTo(newfile)){
			System.out.println("Rename succesful");
		}else{
			System.out.println("Rename failed");
		}
	}
	
	public static void deleteFile(String fileName){
		
		File file = new File(fileName);
		file.delete();
	}
	
	/**
	 * Following method to be used for testing functions in this class only.
	 * @param args
	 */
	public static long getFileVersionNumber(String nodeId, String fileName) {
		String path = Config.HOME_PATH + "/AOS/" + nodeId + "/"+ fileName +".txt";
		String lineIWant = "";
		//System.out.println(path);
		FileInputStream fs;
		String vesrionString = "";
		long versionNumber = 0;
		try {
			fs = new FileInputStream(path);
			BufferedReader br = new BufferedReader(new InputStreamReader(fs));
			lineIWant = br.readLine();
			String[] lineContents = lineIWant.split(":");
			vesrionString = lineContents[1];
			versionNumber = Long.valueOf(vesrionString.trim()); 
			//System.out.println(versionNumber);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return versionNumber;
	}

	public static int getNumberOfReplicatedFiles(String nodeId, String fileName) {
		String path = Config.HOME_PATH + "/AOS/" + nodeId + "/"+ fileName +".txt";
		String lineIWant = "";
		//System.out.println(path);
		FileInputStream fs;
		String replicatedFileString = "";
		int numberOfReplicatedFiles = 0;
		try {
			fs = new FileInputStream(path);
			BufferedReader br = new BufferedReader(new InputStreamReader(fs));
			br.readLine();
			lineIWant = br.readLine();
			String[] lineContents = lineIWant.split(":");
			replicatedFileString = lineContents[1];
			numberOfReplicatedFiles = Integer.valueOf(replicatedFileString.trim()); 
			//System.out.println(numberOfReplicatedFiles);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return numberOfReplicatedFiles;
	}
	/**
	 * 
	 * @param fileName
	 * @param controlPhrase Replicated File/Version Number
	 * @param value
	 */
	public static void changeFileControlLines(String fileName, String controlPhrase, String value){
		String replaceString = controlPhrase+value+"    ";
		try {
			RandomAccessFile raFile = new RandomAccessFile(fileName, "rw");
			if(controlPhrase.equalsIgnoreCase(Config.REPLICATED_FILES)){
				String line = raFile.readLine();
				//System.out.println(line.length());
				raFile.seek(line.length()+1);
			}else{
				raFile.seek(0);
			}
				
			raFile.writeBytes(replaceString);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getAbsoluteFilePath(String nodeId, String fileName){
		String fileAbsolutePath = Config.HOME_PATH + "/AOS/" +nodeId + "/" + fileName + ".txt";
		return fileAbsolutePath;
	}
	public static String getFileContent(String nodeId, String fileName) {
		String fileContent = "";
		String filename = getAbsoluteFilePath(nodeId, fileName);
		String newline = System.getProperty("line.separator");
		
		try (BufferedReader br = new BufferedReader(new FileReader(filename)))
		{
 
			String sCurrentLine;
			br.readLine();
			br.readLine();
			while ((sCurrentLine = br.readLine()) != null) {
				
				fileContent += (sCurrentLine+newline);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return fileContent;

	}
	public static void main(String args[]){
		String filePath = "/home/004/b/bx/bxp131030/AOS/n2/f2.txt";
		FileOperations.changeFileControlLines(filePath, Config.VERSION_NUMBER, "11");
		FileOperations.changeFileControlLines(filePath, Config.REPLICATED_FILES, "22");

	}


}
