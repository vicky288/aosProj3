import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;





public class TestClass {
	ArrayList<String> l1 = new ArrayList<String>();
	


	public ArrayList<String> getL1() {
		return l1;
	}

	public void setL1(ArrayList<String> l1) {
		this.l1 = l1;
	}

	public void readAFile(){
		//String fileName = Config.HOME_PATH +"/AOS/n1/f2.txt";
		String fileName = "config.txt";
		String data ="new content";
		FileOperations.modifyFile(fileName, data);
	}

	public static long readFileVersionNumber(String nodeId, String fileName) {
		String path = Config.HOME_PATH + "/AOS/" + nodeId + "/"+ fileName +".txt";
		String lineIWant = "";
		System.out.println(path);
		FileInputStream fs;
		String vesrionString = "";
		long versionNumber = 0;
		try {
			fs = new FileInputStream(path);
			BufferedReader br = new BufferedReader(new InputStreamReader(fs));
			lineIWant = br.readLine();
			String[] lineContents = lineIWant.split(":");
			vesrionString = lineContents[1];
			versionNumber = Long.valueOf(vesrionString); 
			System.out.println(versionNumber);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return versionNumber;
	}

	public static void main(String[] args) throws IOException {
		//		String nodeId = "n1";
		//		String fileName ="f1";
		//		String path = Config.HOME_PATH + "/AOS/" + nodeId + "/"+ fileName +".txt";
		//		System.out.println(path);
		//		FileInputStream fs= new FileInputStream(path);
		//		//FileInputStream fs= new FileInputStream("C:\\Users\\vicky288\\workspace\\VotingProtocol\\VotingProtocol\\bin\\config.txt");
		//		BufferedReader br = new BufferedReader(new InputStreamReader(fs));
		//		br.readLine();
		//		String lineIWant = br.readLine();
		//		String[] lineContents = lineIWant.split(":");
		//		String vesrionString = lineContents[1];
		//		long versionNumber = 0; 
		//		System.out.println(vesrionString);

	ArrayList<String> list = new ArrayList<String>();
	list.add("yyyy");
	list.add("zzzz");	
	list.add("aaabbb");
	list.add("aaabbb");
	list.add("aaabbbyyy");
	list.add("aaabbbzzz");
	list.add("bbb");
	list.add("bbbccc");
	list.add("sssaaLL");
	list.add("bbbddd");
	
	Iterator<String> iterator = list.iterator();
	while(iterator.hasNext())
	{
	    String value = iterator.next();
	    if (value.contains("aa"))
	    {
	        iterator.remove();
	        
	    }
	}
	
	for(String str:list) {
		System.out.println(str);
	}
		
	
	}

}
