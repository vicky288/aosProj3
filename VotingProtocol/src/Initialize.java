

import java.util.Map;

public class Initialize {
	FileOperations fileOperations = new FileOperations();

	public Node initializeNode(String nodeId, Node node, Map<String, String> nodeInfo){
		node.machineName = Utils.getMachineName(nodeId, nodeInfo);
		node.port = Utils.getPort(nodeId, nodeInfo);
		node.isFail = false;
		node.getNodesKnowledge().putAll(nodeInfo);
		Node initializedNode = replicateFile(node, node.getNumberOfFiles());
		return initializedNode;
	}

	public Node replicateFile(Node node, int numberOfFiles){
		for(int i=1; i<=5; i++){
			String key = "f"+i;
			String fileName = "f"+i;		
			//String fileNameAbsolute = Config.HOME_PATH + "/AOS/" + node.nodeId + "/" + key + ".txt";
			ReplicatedFile r =ReplicatedFile.getReplicatedFile(fileName, node.nodeId);
			node.getReplicatedFileMap().put(key, r);
		}
		return node;
	}


}