

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogToFile {

	public static void log(String file, String content) {

		File f = null;
		String fileName = file + ".txt";
		try {
			// create new file
			f = new File(fileName);

			// tries to create new file in the system
			if (!f.exists()) {
				f.createNewFile();
			}

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			String newline = System.getProperty("line.separator");
			FileWriter fw = new FileWriter(f.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.newLine();
			bw.write(newline + dateFormat.format(date) + " -> " + content);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
