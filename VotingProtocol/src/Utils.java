

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Utils {

	public static Map<String, String> addInfo(Map<String, String> knowledge,
			String key, String value) {
		knowledge.put(key, value);
		return knowledge;
	}
	
	public static String getMachineName(String nodeId, Map<String, String> nodeInfo){
		for (Map.Entry<String, String> entry : nodeInfo.entrySet()){
			if(entry.getKey().equals(nodeId)){
				String parts[] = entry.getValue().split("@");
				return parts[0];
			}
		}
		return null;
	}
	
	public static int  getPort(String nodeId, Map<String, String> nodeInfo){
		for (Map.Entry<String, String> entry : nodeInfo.entrySet()){
			if(entry.getKey().equals(nodeId)){
				String parts[] = entry.getValue().split("@");
				int port = Integer.valueOf(parts[1]);
				return port;
			}
		}
		return 0;
	}
	
	public static Map<String, String> compareMaps(Map<String, String> home, Map<String, String> away) {

		Set<String> keysInAway = new HashSet<String>(away.keySet());
		Set<String> keysInHome = new HashSet<String>(home.keySet());

		// Keys in Away and not in Home
		Set<String> inAwayNotHome = new HashSet<String>(keysInAway);
		inAwayNotHome.removeAll(keysInHome);
		for (String s : inAwayNotHome) {
			home.put(s, away.get(s));
		    
		}
		return home;
	}
	
	/**
	 * Following main method used for testing purposes only
	 */
	public static void main(String args[]) {
	      // create hash map
	     Map <String, String>newmap = new HashMap<String, String>();
	      Utils utils = new Utils();
	      ReadConfigFile read = new ReadConfigFile();
	      newmap = read.getNodesInfo(Config.CONFIG_FILE, Config.NODES);
	      // populate hash map
//	      newmap.put("n1", "dc01@6670");
//	      newmap.put("n2", "dc02@6671");
//	      newmap.put("n3", "dc03@6672");
	      System.out.println(utils.getMachineName("n2", newmap));
	      System.out.println(utils.getPort("n2", newmap));
	}
}
