

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;



public class Node implements Runnable{
	private static Node instance = null;

	boolean timedOut = false;
	Timer timer;
	String nodeId;
	String machineName;
	int numberOfFiles;
	int port;
	boolean isDistinguishedSite;
	NetworkOperations networkObject = NetworkOperations.getNetworkOperations();

	String threadStartCondition;
	private TreeMap<String, ReplicatedFile> replicatedFileMap = new TreeMap<String, ReplicatedFile>();
	private TreeMap<String, String> nodesKnowledge = new TreeMap<String, String>();
	private ArrayList<MessageFormat> grantMessageList = new ArrayList<MessageFormat>();
	private ArrayList<MessageFormat> readGrantMessageList = new ArrayList<MessageFormat>();


	boolean isFail;
	boolean timerStarted = false;

	static Initialize init = new Initialize();


	private Node() {

	}
	//Singleton Class // Easy to use the same object throughout
	public static Node getNodes(String nodeId, Map<String, String> nodeInfo, boolean isDistinguishedSite, int numberOfFiles) {
		if(instance == null) {
			//System.out.println("Node Instance is null");
			instance = new Node();
			instance.isDistinguishedSite = isDistinguishedSite;
			instance.numberOfFiles = numberOfFiles;
			instance.setNodeId(nodeId);
			init.initializeNode(nodeId, instance, nodeInfo);

			//Starting file Threads
			Collection<ReplicatedFile> col=instance.replicatedFileMap.values();
			Iterator<ReplicatedFile> itNew=col.iterator();
			while(itNew.hasNext()){
				ReplicatedFile fileObject = itNew.next();
				Thread fileThread = new Thread(fileObject);
				try {
					Thread.currentThread().sleep(10);
					fileThread.start();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return instance;
	}
	public static Node getNode(String nodeId){
		if(instance == null){
			return null;
		}
		return instance;
	}
	public void printNodeInfo(){
		LogToFile.log(this.nodeId, "####################Start of Printing Node Info###################");	
		LogToFile.log(this.nodeId,"nodeId->"+this.nodeId);
		LogToFile.log(this.nodeId,"Server->"+this.machineName);
		LogToFile.log(this.nodeId,"port->"+this.port);
		LogToFile.log(this.nodeId,"Distinguished Site->"+this.isDistinguishedSite);
		LogToFile.log(this.nodeId,"numberOfFiles->"+this.numberOfFiles);

		LogToFile.log(this.nodeId,"Nodes Knowledge->");
		Set<String> setKey=nodesKnowledge.keySet();
		Iterator<String> it=setKey.iterator();
		while(it.hasNext()){
			//System.out.println(it.next());
			LogToFile.log(this.nodeId,it.next());

		}
		Collection<String> col=nodesKnowledge.values();
		Iterator<String> itNew=col.iterator();
		while(itNew.hasNext()){
			LogToFile.log(this.nodeId,itNew.next());
		}

		//LogToFile.log(this.nodeId,"->"+this.);

		LogToFile.log(this.nodeId, "####################End of Printing Node Info###################");	

	}

	public void clearReadGrantMessageList() {
		this.readGrantMessageList.clear();
	}

	public void clearReadGrantMessageListForFile(String fileName) {
		Iterator<MessageFormat> iterator = this.grantMessageList.iterator();
		while(iterator.hasNext())
		{
			MessageFormat value = iterator.next();
			if (value.getFileName().equals(fileName))
			{
				iterator.remove();
			}
		}
	}
	public ArrayList<MessageFormat> getReadGrantMessageList() {
		return readGrantMessageList;
	}
	public void setReadGrantMessageList(
			ArrayList<MessageFormat> readGrantMessageList) {
		this.readGrantMessageList = readGrantMessageList;
	}
	public int getNumberOfFiles() {
		return numberOfFiles;
	}

	public void setNumberOfFiles(int numberOfFiles) {
		this.numberOfFiles = numberOfFiles;
	}

	public TreeMap<String, String> getNodesKnowledge() {
		return nodesKnowledge;
	}

	public void setNodesKnowledge(TreeMap<String, String> nodesKnowledge) {
		this.nodesKnowledge = nodesKnowledge;
	}

	public void clearGrantMessageList() {
		this.grantMessageList.clear();
	}

	public void clearGrantMessageListForFile(String fileName) {
		Iterator<MessageFormat> iterator = this.grantMessageList.iterator();
		while(iterator.hasNext())
		{
			MessageFormat value = iterator.next();
			if (value.getFileName().equals(fileName))
			{
				iterator.remove();
			}
		}
	}


	public void clearGrantMessageList(String fileName) {
		this.grantMessageList.clear();
	}
	public ArrayList<MessageFormat> getGrantMessageList() {
		return grantMessageList;
	}

	public void setGrantMessageList(ArrayList<MessageFormat> grantMessageList) {
		this.grantMessageList = grantMessageList;
	}

	public boolean isTimerStarted() {
		return timerStarted;
	}

	public void setTimerStarted(boolean timerStarted) {
		this.timerStarted = timerStarted;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isDistinguishedSite() {
		return isDistinguishedSite;
	}

	public void setDistinguishedSite(boolean isDistinguishedSite) {
		this.isDistinguishedSite = isDistinguishedSite;
	}

	public NetworkOperations getNetworkObject() {
		return networkObject;
	}

	public void setNetworkObject(NetworkOperations networkObject) {
		this.networkObject = networkObject;
	}

	public String getThreadStartCondition() {
		return threadStartCondition;
	}

	public void setThreadStartCondition(String threadStartCondition) {
		this.threadStartCondition = threadStartCondition;
	}

	public TreeMap<String, ReplicatedFile> getReplicatedFileMap() {
		return replicatedFileMap;
	}

	public void setReplicatedFileMap(
			TreeMap<String, ReplicatedFile> replicatedFileMap) {
		this.replicatedFileMap = replicatedFileMap;
	}

	public boolean isFail() {
		return isFail;
	}

	public void setFail(boolean isFail) {
		this.isFail = isFail;
	}

	public Initialize getInit() {
		return init;
	}

	public void setInit(Initialize init) {
		this.init = init;
	}

	public void run() {
		LogToFile.log(this.nodeId, "Node Thread Started...............!!!!!!!!!!!!!!!");
		//if(this.threadStartCondition.equals(Config.THREAD_START_CONDITION)){
		receiveData();
		//}


	}

	
	public boolean isTimedOut() {
		return timedOut;
	}
	public void setTimedOut(boolean timedOut) {
		this.timedOut = timedOut;
	}
	public void initializeServer() {
		networkObject.startServer(this.port);
	}
	public void receiveData() {
		this.threadStartCondition = "";
		while(true) {
			MessageFormat messageReceived = networkObject.receiveMessage();
			String fileName = messageReceived.getFileName();
			LogToFile.log(this.nodeId, "Some Message Received................."+isTimerStarted());
			messageReceived.printMessage(nodeId, messageReceived);
			ReplicatedFile replicatedFileInstance = replicatedFileMap.get(fileName);//Aurko: shouldn't you get the replicated file depending on the node?
			if(!messageReceived.isLockGrant()){
				LogToFile.log(this.nodeId, "Message Is a not Lock Grant.....Dispatching it to file thread"+fileName);
				//				LogToFile.log(this.nodeId, "Message type is:"+messageReceived.getMessageType());
				//				LogToFile.log(this.nodeId, "Message source is: "+messageReceived.getSourceServer());
				replicatedFileInstance.addMessage(messageReceived);				
			}
			if(messageReceived.isLockGrant() && isTimerStarted()) {
				LogToFile.log(this.nodeId, "Message Is a Lock Grant--------Adding it to grantMessageList");
				grantMessageList.add(messageReceived);
			}

			if(messageReceived.isReadGrant() && isTimerStarted()) {
				LogToFile.log(this.nodeId, "Message Is a Read Grant==========Adding it to readGrantMessageList");
				readGrantMessageList.add(messageReceived);
			}
		}
	}

	public String read(String fileName) {

		String readContent = "~~~~~~~~~~Content Blank~~~~~~~~~";
		LogToFile.log(this.nodeId, "Received Read Request to read from file->"+this.nodeId+"==="+fileName);


		//For exponential back off
		ReadConfigFile readConfigFile = new ReadConfigFile();
		int initialBackoff = Integer.parseInt(readConfigFile.getMinExpBackoff(Config.CONFIG_FILE, Config.EXP_BACKOFF));
		int maxRetries = Integer.parseInt(readConfigFile.getMaxTries(Config.CONFIG_FILE, Config.EXP_BACKOFF));
		int numberOfRetries = 0;
		int delayTime = 1;

		do {
			LogToFile.log(this.nodeId, "*************************************Do while Starts Here*************************************>>"+numberOfRetries);

			//Iterate Nodes knowledge and Send Quorum Create request all nodes
			TreeMap<String, String> nodesInfoMap = this.getNodesKnowledge();
			Collection<String> nodeInfoCollection=nodesInfoMap.keySet();
			Iterator<String> nodeInfosIterator=nodeInfoCollection.iterator();

			
			//Start timer
			LogToFile.log(this.nodeId, "Timer Started");
			setTimerStarted(true);
			//timer = new Timer();
			//timer.schedule(new RemindTask(), Config.DURATION_GRANT_MESSAGE_WAIT);

			while(nodeInfosIterator.hasNext()){
				//LogToFile.log(this.nodeId, "Timer value");

				// Find dest Id,server, port
				String destId = nodeInfosIterator.next();
				if(destId.equals(this.nodeId)){
					//continue;			//commented to consider itself in the Quorum
				}
				String destServerName = Utils.getMachineName(destId, nodesInfoMap);
				int destPort = Utils.getPort(destId, nodesInfoMap);

				LogToFile.log(this.nodeId, "Create READ Quorum request to node->"+destId+ " timer value-->"+isTimerStarted());	    	
				//Create Message to send
				MessageFormat messageToSend = new MessageFormat();
				messageToSend = messageToSend.createReadRequest(this.nodeId, this.machineName, this.port, destId, destServerName, destPort, fileName);

				//Print Message
				//messageToSend.printMessage(this.nodeId, messageToSend);

				//Send Message
				try {
					networkObject.sendToNode(messageToSend.getDestServer(), messageToSend.getDestPort(), messageToSend);
				} catch (Exception e) {
					LogToFile.log(this.nodeId, "Message sending to node->"+destId+" failed.");
					e.printStackTrace();
				}
				LogToFile.log(this.nodeId, "READ Quorum Create Message for "+ fileName+ " sent to node->"+destId);
			}

			try {
				Thread.currentThread().sleep(Config.DURATION_GRANT_MESSAGE_WAIT + Config.DURATION_BUFFER);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			setTimerStarted(false);

			
			try {
				Thread.currentThread().sleep(Config.DURATION_BUFFER);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//Collect all Read Grant Messages Received in time Frame
			LogToFile.log(this.nodeId, "Total Read Grant Messages Received.........>>>>>"+getReadGrantMessageList().size());
			ArrayList<MessageFormat> receivedReadGrantMessages = getReadGrantMessageList();
			ArrayList<MessageFormat> receivedReadGrantMessagesInTimeFrame = new ArrayList<MessageFormat>();
			for(MessageFormat receivedMessage:receivedReadGrantMessages) {
				if(receivedMessage.getFileName().equals(fileName)) {
					receivedReadGrantMessagesInTimeFrame.add(receivedMessage);
				} else {
					LogToFile.log(this.nodeId, "^^^^^^^^^^^^^^^^^^^No Grant Messages Added for File Name.....>>>>>"+receivedMessage.getFileName()+"But operation was performed on file....>>>>"+fileName);

				}
			}

			//Clear the Grant Message List
			//clearGrantMessageList();
			clearReadGrantMessageListForFile(fileName);


			LogToFile.log(this.nodeId, "Total Read Grant Messages Received In TimeFrame.........>>>>>"+receivedReadGrantMessagesInTimeFrame.size());

			//Print Grant MessageList
			for(MessageFormat message:receivedReadGrantMessagesInTimeFrame){
				LogToFile.log(this.nodeId, ">>>>>>Now printing received Grant Messages Received for "+ fileName+ " In TimeFrame.........");
				//message.printMessage(this.nodeId, message);
			}
			if (receivedReadGrantMessagesInTimeFrame.size() == 0){
				LogToFile.log(this.nodeId, "No Read Grant Messages received.........");
				LogToFile.log(this.nodeId, "----------------Read Operation didn't happen on "+ fileName+ " As voting protocol can't be called-------------------");
				//return readContent;
			}

			LogToFile.log(this.nodeId, "^^^^^^^^^^^^^^^^^^^^^Starting Dynamic Voting Protocol For Read on "+ fileName+ " ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

			MessageFormat readResultMessage = null;
			if(receivedReadGrantMessagesInTimeFrame.size()>0) {
				votingProtocolOperations protolObject = new votingProtocolOperations();
				readResultMessage = protolObject.executeProtocol(receivedReadGrantMessagesInTimeFrame, fileName, this, "");
			}

			if(readResultMessage != null) {
				LogToFile.log(this.nodeId, "----------------Read Operation Successful  on "+ fileName+ " as it got Quorum-------------------");
				readContent = readResultMessage.getValuesUpdated().get(Config.KEY_FILE_CONTENT_UPDATED);
				System.out.println("Read Successful at>>"+nodeId);
				return readContent;
			} else {
				//LogToFile.log(this.nodeId, "----------------Write Operation Failed on "+ fileName+ " As Quorum couldn't be created-------------------");
				LogToFile.log(this.nodeId, "----------------Read Operation didn't happen on "+ fileName+ " As Quorum couldn't be created-----------Entering Exponential Back off--------noofreTries>>"+numberOfRetries);
				try {
					Thread.currentThread().sleep(2*delayTime*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			delayTime = delayTime *2;
			LogToFile.log(this.nodeId, "*************************************Do while Ends Here*************************************>>"+numberOfRetries);

		} while ((++numberOfRetries) <= maxRetries);
		LogToFile.log(this.nodeId, "----------------Read Operation Failed  on "+ fileName+ " As Quorum couldn't be created-------------------");
		System.out.println("Read Operation Failed at>>"+nodeId +" on file->"+fileName);
		return readContent;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public void write(String fileName, String content){
		LogToFile.log(this.nodeId, "Received Write Request to write in file->"+this.nodeId+"==="+fileName);


		//Added by aurko for incremental back off
		ReadConfigFile readConfigFile = new ReadConfigFile();
		int initialBackoff = Integer.parseInt(readConfigFile.getMinExpBackoff(Config.CONFIG_FILE, Config.EXP_BACKOFF));
		int maxRetries = Integer.parseInt(readConfigFile.getMaxTries(Config.CONFIG_FILE, Config.EXP_BACKOFF));
		int numberOfRetries = 0;
		int delayTime = 1;
		
		do {
			LogToFile.log(this.nodeId, "*************************************Do while Starts Here*************************************>>"+numberOfRetries);
			//Iterate Nodes knowledge and Send Quorum Create request all nodes
			TreeMap<String, String> nodesInfoMap = this.getNodesKnowledge();
			Collection<String> nodeInfoCollection=nodesInfoMap.keySet();
			Iterator<String> nodeInfosIterator=nodeInfoCollection.iterator();
			
			//Start timer
			LogToFile.log(this.nodeId, "Timer Started");
			setTimerStarted(true);
			//timer = new Timer();
			//timer.schedule(new RemindTask(), Config.DURATION_GRANT_MESSAGE_WAIT);

			while(nodeInfosIterator.hasNext()){
				// Find dest Id,server, port
				String destId = nodeInfosIterator.next();
				if(destId.equals(this.nodeId)){
					//continue;			//commented to consider itself in the Quorum
				}
				String destServerName = Utils.getMachineName(destId, nodesInfoMap);
				int destPort = Utils.getPort(destId, nodesInfoMap);

				LogToFile.log(this.nodeId, "Create Quorum request for "+ fileName+ " to node->"+destId + " timer value-->"+isTimerStarted());	    		    	
				//Create Message to send
				MessageFormat messageToSend = new MessageFormat();
				messageToSend = messageToSend.createLockRequest(this.nodeId, this.machineName, this.port, destId, destServerName, destPort, fileName);

				//Print Message
				//messageToSend.printMessage(this.nodeId, messageToSend);

				//Send Message
				try {
					networkObject.sendToNode(messageToSend.getDestServer(), messageToSend.getDestPort(), messageToSend);
				} catch (Exception e) {
					LogToFile.log(this.nodeId, "Message sending to node->"+destId+" failed.");
					e.printStackTrace();
				}
				LogToFile.log(this.nodeId, "Quorum Create Message for "+ fileName+ " sent to node->"+destId);
			}


			try {
				Thread.currentThread().sleep(Config.DURATION_GRANT_MESSAGE_WAIT + Config.DURATION_BUFFER);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			setTimerStarted(false);
			
			try {
				Thread.currentThread().sleep(Config.DURATION_BUFFER);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			//Collect all Grant Messages Received in time Frame
			LogToFile.log(this.nodeId, "Total Grant Messages Received.........>>>>>"+grantMessageList.size());
			ArrayList<MessageFormat> receivedGrantMessages = getGrantMessageList();
			ArrayList<MessageFormat> receivedGrantMessagesInTimeFrame = new ArrayList<MessageFormat>();
			for(MessageFormat receivedMessage:receivedGrantMessages) {
				if(receivedMessage.getFileName().equals(fileName)) {
					receivedGrantMessagesInTimeFrame.add(receivedMessage);
				}
			}

			//Clear the Grant Message List
			clearGrantMessageListForFile(fileName);

			LogToFile.log(this.nodeId, "Total Grant Messages Received In TimeFrame.........>>>>>"+receivedGrantMessagesInTimeFrame.size());

			//Print Grant MessageList
			for(MessageFormat message:receivedGrantMessagesInTimeFrame){
				LogToFile.log(this.nodeId, ">>>>>>Now printing received Grant Messages Received  for "+ fileName+ " In TimeFrame.........");
				//message.printMessage(this.nodeId, message);
			}
			if (receivedGrantMessagesInTimeFrame.size() == 0){
				LogToFile.log(this.nodeId, "No Write Grant Messages received.........");
				//LogToFile.log(this.nodeId, "----------------Write Operation  on "+ fileName+ " Failed As voting protocol can't be called-------------------");
				LogToFile.log(this.nodeId, "----------------Write Operation didn't happen on "+ fileName+ " As voting protocol can't be called-----------");
				//return;

			}

			LogToFile.log(this.nodeId, "^^^^^^^^^^^^^^^^^^^^^Starting Dynamic Voting Protocol For Write on "+ fileName+ " ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

			MessageFormat writeResultMessage = null;
			//Call to Voting Protocol
			if (receivedGrantMessagesInTimeFrame.size() > 0){
				votingProtocolOperations protolObject = new votingProtocolOperations();
				writeResultMessage = protolObject.executeProtocol(receivedGrantMessagesInTimeFrame, fileName, this, content);
				
			}

			if(writeResultMessage != null){
				LogToFile.log(this.nodeId, "----------------Write Operation Successful on "+ fileName+ " as it got Quorum-------------------");
				//Send Messages to Members replied to Quorum Request
				for(MessageFormat message:receivedGrantMessagesInTimeFrame){
					MessageFormat messageToSend = message.createWriteResponseMessage(message, writeResultMessage);

					//Print Message
					//messageToSend.printMessage(this.nodeId, messageToSend);

					//Send Message
					try {
						networkObject.sendToNode(messageToSend.getDestServer(), messageToSend.getDestPort(), messageToSend);
					} catch (Exception e) {
						LogToFile.log(this.nodeId, "Write Result Message  on "+ fileName+ " sending to node->"+messageToSend.getDestId()+" failed.");
						e.printStackTrace();
					}
					LogToFile.log(this.nodeId, "Write Result Message  on "+ fileName+ " sent to node->"+messageToSend.getDestId());
				}
				System.out.println("Write Successful at>>"+nodeId);
				return;
			} else {
				//LogToFile.log(this.nodeId, "----------------Write Operation Failed on "+ fileName+ " As Quorum couldn't be created-------------------");
				LogToFile.log(this.nodeId, "----------------Write Operation didn't happen on "+ fileName+ " As Quorum couldn't be created-----------Entering Exponential Back off--------noofreTries>>"+numberOfRetries);
				try {
					Thread.currentThread().sleep(2*delayTime*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			delayTime = delayTime *2;
			LogToFile.log(this.nodeId, "*************************************Do while Ends Here*************************************>>"+numberOfRetries);
		} while ((++numberOfRetries) <= maxRetries);
		LogToFile.log(this.nodeId, "----------------Write Operation Failed on "+ fileName+ " As Quorum couldn't be created-------------------");			
		System.out.println("Write Operation Failed at>>"+nodeId +" on file->"+fileName);
	}


	class RemindTask extends TimerTask {
		public void run() {
			//setTimerStarted(false);
			System.out.println("Time's up!");
			LogToFile.log(getNodeId(), "Timer Stopped");
			timer.cancel(); // Terminate the timer thread
		}
	}

}
