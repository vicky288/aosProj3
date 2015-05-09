

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ReadConfigFile {

	Utils utils = new Utils();
	public void getNodes(){
		
	}
	
	public void getResources(){
		
	}
	
	public Map<String, String> getNodesInfo(String fileName, String marker){
		File file = new File(fileName);
		FileInputStream fileInputStream;
		String parts[]=null;
		Map<String, String> nodesInfo = new HashMap<String, String>(); 
		
		try {
			fileInputStream = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
			
			String line = null;
			while ((line = br.readLine()) != null) {
				if(!(line.equals(marker))){
					continue;
				}
				else if(line.equals(marker)){
					line = br.readLine();
					while(!(line.equals(marker))) {
						parts = line.split(":");
						nodesInfo = utils.addInfo(nodesInfo, parts[0], parts[1]);
						line = br.readLine();
					}
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nodesInfo;
	}
	
	public String getData(String fileName, String marker){
		File file = new File(fileName);
		FileInputStream fileInputStream;
		try{
			fileInputStream = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
			
			String line = null;
			
			while ((line = br.readLine()) != null) {
				if(!(line.equals(marker))){
					continue;
				}
				else if(line.equals(marker)){
					line = br.readLine();
					return line;
				}
			}
			
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String getMinExpBackoff(String fileName, String marker){
		String parts[] = null;
		String line = getData(fileName, marker);
		parts = line.split(":");
		return parts[0];
	}
	
	public String getMaxTries(String fileName, String marker){
		String parts[] = null;
		String line = getData(fileName, marker);
		parts = line.split(":");
		return parts[1];
	}
	
	/**
	 * Following method used to test functions in this class only
	 */
	public static void main(String args[]){
		ReadConfigFile test = new ReadConfigFile();
//		System.out.println(test.getMinExpBackoff(Config.CONFIG_FILE, Config.EXP_BACKOFF));
		Map<String, String> nodesInfo = test.getNodesInfo(Config.CONFIG_FILE, Config.NODES);
		for (Map.Entry <String, String> entry : nodesInfo.entrySet()){
			System.out.println(entry.getKey()+" "+entry.getValue());
		}
	}
}
