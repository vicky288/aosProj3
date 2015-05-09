import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class votingProtocolOperations {

	Timer timer;
	FileOperations fileOperations = new FileOperations();

	public String logName(String nodeid) {
		String logName = nodeid + "_q";
		return logName;
	}
	
	public ArrayList<MessageFormat> createQuorum(ArrayList<MessageFormat> messageList, String fileKey ,String nodeId){
		ArrayList<MessageFormat> quorum = new ArrayList<MessageFormat>();
//		quorum.add(messageList.get(0));
		int versionNumberToBeCompared = Integer.parseInt(messageList.get(0).getValuesUpdated().get(Config.KEY_FILE_VESRION_NUMBER));
		for(MessageFormat message : messageList){
			if(Integer.parseInt(message.getValuesUpdated().get(Config.KEY_FILE_VESRION_NUMBER)) > versionNumberToBeCompared){
				quorum.clear();
				quorum.add(message);
				versionNumberToBeCompared = Integer.parseInt(message.getValuesUpdated().get(Config.KEY_FILE_VESRION_NUMBER));
			}else if(Integer.parseInt(message.getValuesUpdated().get(Config.KEY_FILE_VESRION_NUMBER)) == versionNumberToBeCompared) {
				quorum.add(message);
			}
		}
		LogToFile.log(logName(nodeId), "Quorum size = "+quorum.size());
		LogToFile.log(logName(nodeId), "Members of the quorum are :");
		for(MessageFormat q : quorum){
			LogToFile.log(logName(nodeId),q.getSourceId());
		}
		return quorum;
	}

	public boolean isOperationAllowed(ArrayList<MessageFormat> quorumList, String fileKey, Node node){

		int N = Integer.parseInt(quorumList.get(0).getValuesUpdated().get(Config.KEY_FILE_REPLICAS_UPDATED));
		double modQShouldBeGreaterThan = (N/2);
		if(quorumList.size() == modQShouldBeGreaterThan){
			
			for(MessageFormat quorum : quorumList){
				if((quorum.getValuesUpdated().get(Config.KEY_FILE_DISTINGUISHED_SITE)).equalsIgnoreCase("true")){
					return true;
				}
			}
			return false;
		}
		return(quorumList.size() > modQShouldBeGreaterThan);
	}

	public void propagateChanges(ArrayList<Node>nodeList, long versionNumber, int replicasUpdated, String edittedFileName, String nodeId, String fileKey){
		for(Node node : nodeList){
			if(node.nodeId != nodeId){
				String fileName = node.getReplicatedFileMap().get(fileKey).fileName;
				FileOperations.deleteFile(fileName);
				FileOperations.copyFile(edittedFileName, fileName);
				node.getReplicatedFileMap().get(fileKey).version = versionNumber;
				node.getReplicatedFileMap().get(fileKey).replicasUpdated = replicasUpdated;
				//change isLocked variable
			}

		}
	}

	public boolean doIHaveLatest(Node node , ArrayList<MessageFormat> quorum, String fileKey){
		return(node.getReplicatedFileMap().get(fileKey).version >= Integer.parseInt(quorum.get(0).getValuesUpdated().get(Config.KEY_FILE_VESRION_NUMBER)));
	}

	public void getLatestFileForUpdate(Node node, ArrayList<MessageFormat>quorum, String fileKey){
		String fileName = FileOperations.getAbsoluteFilePath(node.nodeId, fileKey);
		LogToFile.log(logName(node.nodeId), "Using the following Q member to update itself.");
		LogToFile.log(logName(node.nodeId), "VersionNumber:"+quorum.get(0).getValuesUpdated().get(Config.KEY_FILE_VESRION_NUMBER));
		LogToFile.log(logName(node.nodeId), "Replicas Updated :"+quorum.get(0).getValuesUpdated().get(Config.KEY_FILE_REPLICAS_UPDATED));
		long versionNumber = Integer.parseInt(quorum.get(0).getValuesUpdated().get(Config.KEY_FILE_VESRION_NUMBER));
		FileOperations.deleteFile(fileName);
		FileOperations.modifyFile(fileName, Config.VERSION_NUMBER + quorum.get(0).getValuesUpdated().get(Config.KEY_FILE_VESRION_NUMBER)+"     ");
		node.getReplicatedFileMap().get(fileKey).setVersion(versionNumber);
		FileOperations.modifyFile(fileName, Config.REPLICATED_FILES + quorum.get(0).getValuesUpdated().get(Config.KEY_FILE_REPLICAS_UPDATED)+"    ");
		FileOperations.modifyFile(fileName, quorum.get(0).getValuesUpdated().get(Config.KEY_FILE_CONTENT_UPDATED));
		LogToFile.log(logName(node.nodeId),"Latest version acquired by node "+node.nodeId);
	}


	public MessageFormat executeProtocol(ArrayList<MessageFormat> messageList, String fileKey, Node node, String content){

		//send request to all nodes
		//accept responses
		//timer stops
		//received responses stored in arraylist
//		ArrayList <Node> nodeList = new ArrayList<Node>();
		ArrayList<MessageFormat> quorum = createQuorum(messageList, fileKey, node.nodeId);
		if(isOperationAllowed(quorum, fileKey, node)){
			LogToFile.log(logName(node.nodeId),node.nodeId + "has got a quorum.");
			
			String fileName = FileOperations.getAbsoluteFilePath(node.nodeId, fileKey);
			//FileOperations.modifyFile(fileName, content);
			LogToFile.log(logName(node.nodeId),node.nodeId + "Creating Messages After Successful Quorum Creation");
			
			//Create Success Read Message 
			MessageFormat sendBackMessage = new MessageFormat();
			if(messageList.get(0).getRequestType().equalsIgnoreCase(Config.REQUEST_READ)){
				String readContent = quorum.get(0).getValuesUpdated().get(Config.KEY_FILE_CONTENT_UPDATED);
				sendBackMessage.getValuesUpdated().put(Config.KEY_FILE_CONTENT_UPDATED, readContent);
			}else{
				//Create Success Write Message 
				if(!(doIHaveLatest(node, quorum, fileKey))){
					LogToFile.log(logName(node.nodeId),node.nodeId +"has a stale copy. Updating copy to latest version.");
					getLatestFileForUpdate(node, quorum, fileKey);
				}
				sendBackMessage.getValuesUpdated().put(Config.KEY_FILE_VESRION_NUMBER,  (++node.getReplicatedFileMap().get(fileKey).version).toString());
				sendBackMessage.getValuesUpdated().put(Config.KEY_FILE_REPLICAS_UPDATED, String.valueOf(messageList.size()));
				sendBackMessage.getValuesUpdated().put(Config.KEY_FILE_CONTENT_UPDATED, content);
				node.getReplicatedFileMap().get(fileKey).replicasUpdated = messageList.size();
				String edittedFileName = node.getReplicatedFileMap().get(fileKey).fileName;
//				propagateChanges(nodeList, versionNumber, replicasUpdated, edittedFileName, node.nodeId, fileKey);
//				LogToFile.log(logName(node.nodeId),"All changes propagated.");
			}
			
			
			return sendBackMessage;
			
		}else{
			LogToFile.log(logName(node.nodeId),node.nodeId + "does not have quorum to perform operation.");
		}
		return null;
	}

	public ReplicatedFile incrementVersion(ReplicatedFile expediente){
		expediente.version +=1;
		return expediente;
	}
	/**
	 * Used for testing purposes only
	 * @param args
	 */
	public static void main(String args[]){
		int N = 4;
		double q = 3.5;
		System.out.println(N<q);
	}
}