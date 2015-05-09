

import java.util.ArrayList;
import java.util.Map;




public class Application implements Runnable{
	
	
	Map<String, String> nodesInfo;
	int numberOfFiles;
	ReadConfigFile readConfigFile = new ReadConfigFile();
	Node node;
	static String nodeId;
	String threadStartParam;
	String fileName;
	String operation;
	String content;
	
	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}



	public String getFileName() {
		return fileName;
	}



	public void setFileName(String fileName) {
		this.fileName = fileName;
	}



	public String getOperation() {
		return operation;
	}



	public void setOperation(String operation) {
		this.operation = operation;
	}



	public Map<String, String> getNodesInfo() {
		return nodesInfo;
	}



	public void setNodesInfo(Map<String, String> nodesInfo) {
		this.nodesInfo = nodesInfo;
	}



	public int getNumberOfFiles() {
		return numberOfFiles;
	}



	public void setNumberOfFiles(int numberOfFiles) {
		this.numberOfFiles = numberOfFiles;
	}



	public ReadConfigFile getReadConfigFile() {
		return readConfigFile;
	}



	public void setReadConfigFile(ReadConfigFile readConfigFile) {
		this.readConfigFile = readConfigFile;
	}



	public Node getNode() {
		return node;
	}



	public void setNode(Node node) {
		this.node = node;
	}



	public static String getNodeId() {
		return nodeId;
	}



	public static void setNodeId(String nodeId) {
		Application.nodeId = nodeId;
	}



	public String getThreadStartParam() {
		return threadStartParam;
	}



	public void setThreadStartParam(String threadStartParam) {
		this.threadStartParam = threadStartParam;
	}



	public void startFunction(String nodeId){
		ArrayList<Application> appList = new ArrayList<Application>();
		
		nodesInfo = readConfigFile.getNodesInfo(Config.CONFIG_FILE, Config.NODES);
		numberOfFiles = Integer.parseInt(readConfigFile.getData(Config.CONFIG_FILE, Config.NUMBER_OF_FILES));
		LogToFile.log(nodeId, "Initializing node");
		System.out.println("Thread started for node: "+nodeId);

		
		node = Node.getNodes(nodeId, nodesInfo, false, numberOfFiles);
		node.printNodeInfo();
		node.initializeServer();
		Thread nodeThread = new Thread(node);
		try {
			Thread.currentThread().sleep(500);
			nodeThread.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		try {
			Thread.currentThread().sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//LogToFile.log(nodeId, "NodeId ->>>>>>>>>>>>>>>"+nodeId+" "+Thread.currentThread().getName());


		
		this.setNode(node);
	
		ReadConfigFile read = new ReadConfigFile();
		String requests= read.getData(Config.CONFIG_FILE, Config.CONIG_REQUESTS);
		String requestArray[] = requests.split(",");
		LogToFile.log(nodeId, "Request Array String ->>>>>>>>>>>>>>>"+requests);
		
		
		if(nodeId.equals("n3")){
			try {
				Thread.currentThread().sleep(8000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		for(String str:requestArray) {
			//LogToFile.log(nodeId, "Request is ->>>>>>>>>>>>>>>"+str);
			//System.out.println(str);
			String[] requestEntities = str.split("-");
			//System.out.println(requestEntities[0]);
			if(requestEntities[0].equals(nodeId)) {
				LogToFile.log(nodeId, "This request is for this node ->>>>>>>>>>>>>>>"+requestEntities[0]+"....Request is>>>"+str);
				Application app = new Application();
				app.setNode(node);				
				app.setFileName(requestEntities[1]);
				LogToFile.log(nodeId, "File Name is ->>>>>>>>>>>>>>>"+requestEntities[1]);
				app.setOperation(requestEntities[2]);
				LogToFile.log(nodeId, "File Operation to perform ->>>>>>>>>>>>>>>"+requestEntities[2]);
				app.setContent(requestEntities[4]);
				LogToFile.log(nodeId, "Content to add ->>>>>>>>>>>>>>>"+requestEntities[4]);

				appList.add(app);
				Thread thread = new Thread(app);
				thread.start();
				
				int timeToSleep = Integer.valueOf(requestEntities[3].trim());
				try {
					Thread.currentThread().sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}
		
		

		
		System.out.println( "End of Application  for ->>>>>>>>>>>>>>>"+nodeId);
		
/*		if(nodeId.equalsIgnoreCase("n1")) {
			System.out.println("--------->>>>>>>>Write on f1 by n1");
			//node.write("f1", "myke yong");
			//String content = node.read("f1");
			//System.out.println(content);
		}

		try {
			Thread.currentThread().sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if(nodeId.equalsIgnoreCase("n2")) {
			//System.out.println("--------->>>>>>>>Write on f1 by n2");
			//node.write("f1", "rick mick");
			//String content = node.read("f1");
			//System.out.println(content);
		}
		
*/	}
	
	
	
	public static void main(String[] args) throws Exception {
		nodeId = args[0].trim();
		Application test = new Application();
		//System.out.println("Something is running");
		test.startFunction(nodeId);
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		LogToFile.log(nodeId, "####################Starting Application Request Thread for --->"+nodeId+" Object is-->"+this);

		String fileName = this.getFileName();
		String operation = this.getOperation();
		Node node = this.getNode();
		
		if(operation.equals("r")) {
			LogToFile.log(nodeId, "This is a read operation at --->"+nodeId);
			String readContent = "----Read didn't happen---";
			//node.write(fileName, content);Name, "oxoxoxoooxoxooxo");
			readContent = node.read(fileName);
			System.out.println(readContent+">>>"+nodeId);
		}
		if(operation.equals("w")) {
			LogToFile.log(nodeId, "This is a write operation at --->"+nodeId);
			String contentToWrite = this.getContent();
			node.write(fileName, contentToWrite);
		}
	}

	
}