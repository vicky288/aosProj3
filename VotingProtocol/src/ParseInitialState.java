import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParseInitialState {
	public static String HOME_PATH = System.getProperty("user.home");
	public static final String VAR_PATH = "/AOS/";

	public static void main(String[] args) {

		parse("initial");
	}
	static void parse(String fileName) {

		try {
			PrintWriter pw = new PrintWriter(HOME_PATH + VAR_PATH + fileName+".txt");

			ReadConfigFile f = new ReadConfigFile();

			String line;
			BufferedReader in;
			int x = 1;

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			pw.print(dateFormat.format(date));
			pw.println();

			// This is where the files are located. However, each file is in
			// their
			// own folder.
			File[] files = null;

			int z = Integer.parseInt(f.getData(HOME_PATH + VAR_PATH+ "VotingProtocol/config.txt",
					"#Number of nodes"));
			int a = Integer.parseInt(f.getData(HOME_PATH + VAR_PATH+ "VotingProtocol/config.txt",
					"#Number of files in the system"));

			for (int i = 1; i <= z; i++) {
				files = new File(HOME_PATH +  VAR_PATH+ "/n" + i + "/")
						.listFiles();

				
					pw.print(files[i-1].getName().toString());
					pw.println();
					try {
						in = new BufferedReader(new FileReader(
								files[i-1].toString()));
						while ((line = in.readLine()) != null && x != 3) {
							pw.write(line);
							pw.println();
							x++;
						}
						x = 1;

					} catch (IOException e) {
						e.printStackTrace();
					}
				
			}

			String requests = f.getData(
					HOME_PATH + VAR_PATH+ "VotingProtocol/config.txt", "#Requests");
//			String requests = f.getData(HOME_PATH+VAR_PATH, "#Requests");
			pw.print("~~~~~~~~~~~~~~~~~~~~~Node Request Info~~~~~~~~~~~~~~~~~~~~~");
			pw.println();
			pw.print(requests.toString());
			pw.println();
			pw.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

}