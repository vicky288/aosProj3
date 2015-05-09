import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParseFinalState {
	
	public static final String HOME_PATH = System.getProperty("user.home");
	
	public static final String VAR_PATH = "/AOS/";

	public static void main(String[] args) {
		parseAndCompare();
	}

	static void parseAndCompare() {
		BufferedReader in;
		String line;
		PrintWriter pw;
		int x = 1, y = 1;
		ReadConfigFile f = new ReadConfigFile();
		int z = Integer
				.parseInt(f.getData(HOME_PATH + VAR_PATH + "VotingProtocol/config.txt",
						"#Number of nodes"));
		

		try {
			pw = new PrintWriter(HOME_PATH + VAR_PATH+"afinal.txt");
			in = new BufferedReader(new FileReader(HOME_PATH + VAR_PATH + "initial.txt"));
			
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			pw.print(dateFormat.format(date));
			pw.println();

			while ((line = in.readLine()) != null) {
				if (x > 2 && x % 3 == 0 && x <= (z * 3) + 1) {
					pw.print("*******************BEFORE Execution*******************");
					pw.println();
				}
				pw.print(line);
				pw.println();
				if (x> 2 && (x - 1) % 3 == 0 && x <= (z * 3) + 1) {
					pw.print("-------------------AFTER Execution @ N"+y+"-------------------");
					pw.println();
					pw.print(printParse(y));
					pw.println();
					y++;
				}
				x++;
			}
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static String printParse(int y) {
		String line;	
		BufferedReader in;
		StringBuilder sb = new StringBuilder();
	
		String newLine = System.getProperty("line.separator");
		for(int i=1;i<=5;i++){
			File files = new File(HOME_PATH + VAR_PATH + "n" + y + "/f" + i +".txt");
			sb.append("f"+i+".txt" + newLine);
			int x = 1;
			try {
				in = new BufferedReader(new FileReader(files));
				while ((line = in.readLine()) != null && x != 3) {
					x++;
					sb.append(line + newLine);
					
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return sb.toString();
	}
}