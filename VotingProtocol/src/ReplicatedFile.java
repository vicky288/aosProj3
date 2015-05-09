

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

public class ReplicatedFile implements Runnable{
	private static ReplicatedFile instance = null;
	String fileName;
	String fileContent;
	Long version;
	String parentNode;
	int replicasUpdated;
	boolean isBusy=false;
	boolean requestRemove=false;
	boolean readRequestRemove=false;
	
	boolean timedOut = false;
	private ArrayList<MessageFormat> messages = new ArrayList<MessageFormat>();
	private MessageFormat writeOperationResult=null;
	private MessageFormat readOperationResult=null;
	
	private NetworkOperations networkObject = NetworkOperations.getNetworkOperations();

	private ReplicatedFile() {

	}
	public static ReplicatedFile getReplicatedFile(String fileName, String parentNode) {
		//System.out.println("ReplicatedFile Instance is null");
		instance = new ReplicatedFile();
		instance.fileName = fileName;
		instance.version = FileOperations.getFileVersionNumber(parentNode, fileName);
		instance.replicasUpdated = FileOperations.getNumberOfReplicatedFiles(parentNode, fileName);
		instance.fileContent = FileOperations.getFileContent(parentNode, fileName);
		instance.parentNode = parentNode;
		instance.isBusy = false;
		return instance;
	}



	public boolean isReadRequestRemove() {
		return readRequestRemove;
	}
	public void setReadRequestRemove(boolean readRequestRemove) {
		this.readRequestRemove = readRequestRemove;
	}
	public MessageFormat getReadOperationResult() {
		return readOperationResult;
	}
	public void setReadOperationResult(MessageFormat readOperationResult) {
		this.readOperationResult = readOperationResult;
	}
	public String getFileContent() {
		return fileContent;
	}
	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}
	public boolean isTimedOut() {
		return timedOut;
	}
	public void setTimedOut(boolean timedOut) {
		this.timedOut = timedOut;
	}
	public boolean isRequestRemove() {
		return requestRemove;
	}
	public void setRequestRemove(boolean requestRemove) {
		this.requestRemove = requestRemove;
	}
	public MessageFormat getWriteOperationResult() {
		return writeOperationResult;
	}


	public void setWriteOperationResult(MessageFormat writeOperationResult) {
		this.writeOperationResult = writeOperationResult;
	}


	public static ReplicatedFile getInstance() {
		return instance;
	}


	public static void setInstance(ReplicatedFile instance) {
		ReplicatedFile.instance = instance;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public Long getVersion() {
		return version;
	}


	public void setVersion(Long version) {
		this.version = version;
	}


	public String getParentNode() {
		return parentNode;
	}


	public void setParentNode(String parentNode) {
		this.parentNode = parentNode;
	}


	public int getReplicasUpdated() {
		return replicasUpdated;
	}


	public void setReplicasUpdated(int replicasUpdated) {
		this.replicasUpdated = replicasUpdated;
	}
	public boolean isBusy() {
		return isBusy;
	}
	public void setBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}
	public NetworkOperations getNetworkObject() {
		return networkObject;
	}
	public void setNetworkObject(NetworkOperations networkObject) {
		this.networkObject = networkObject;
	}
	public ArrayList<MessageFormat> getMessages() {
		return messages;
	}


	public void setMessages(ArrayList<MessageFormat> messages) {
		this.messages = messages;
	}


	public String logName() {
		String logName = this.parentNode + "_" +this.fileName;
		return logName;
	}
	@Override
	public void run() {
		LogToFile.log(this.logName(), "File Thread Started...............!!!!!!!!!!!!!!!"+this+"-----"+this.fileName);

		while(true) {
			ArrayList<MessageFormat> messageList = getMessages();
			int messageListSize = messageList.size();

			try {
				Thread.currentThread().sleep(10);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			while(messageList.size() > 0) {
				LogToFile.log(this.logName(), "Message is there in the list...............");	
				MessageFormat msg = messageList.get(0);
				//If Message is a Read Request
				if(msg.isReadRequest()) {
					LogToFile.log(this.logName(), "~~~~~~~~~~~~~~Message Is a Read request~~~~~~~~~~~~~~~~~");
					if (isBusy()) {
						LogToFile.log(this.logName(), "File is busy");	
						LogToFile.log(this.logName(), "File is busy....Can't Send Grant Message To Read");	
						messageList.remove(0);

					} else {
						LogToFile.log(this.logName(), "File is not busy... Processing Read Request");	
						this.setTimedOut(false);
						this.setRequestRemove(false);
						long lStartTime = new Date().getTime();
						long lEndTime = new Date().getTime();
						long difference = 0;
						// create MESSAGE_READ_GRANT 
						MessageFormat message = msg.createReadGrantMessage(msg, this.version, this.replicasUpdated,Node.getNode(parentNode).isDistinguishedSite,this.fileContent);
						
						//print the Message
						//message.printMessage(this.logName(), message);
						
						//Send Grant Message to Requesting Node
						LogToFile.log(this.logName(), "Sending Grant Message to->"+message.getDestId());	

						try {
							networkObject.sendToNode(message.getDestServer(), message.getDestPort(), message);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							System.out.println("Exception at sendToNode....run of ReplicatedFile.....");
							e1.printStackTrace();
						}
						
						//wait till readOperationResult value has not be updated
						LogToFile.log(this.logName(), "Before while loop to check Read operation result");	
						while (getReadOperationResult() == null) {
							lEndTime = new Date().getTime();
							difference = lEndTime - lStartTime;
							if (difference > Config.DURATION_GRANT_RESPONSE_WAIT ) {
								setTimedOut(true);
								break;
							}
							if(isReadRequestRemove()==true) {
								break;
								//if time out break
							}
							try {	
								Thread.currentThread().sleep(50);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
							}
						}

						if(isTimedOut() == true) {
							LogToFile.log(this.logName(), "Time Out......Removeing Read Request");
							messageList.remove(0);
						}
						
						if(isReadRequestRemove()==true) {
							LogToFile.log(this.logName(), "Read Request Remove Request......Removeing Read Request");
							messageList.remove(0);
						}
						if(getReadOperationResult() != null) {
							LogToFile.log(this.logName(), "$$$$$$$$$$Read Operation was Executed Successfully Updating File$$$$$$$$$$");

							messageList.remove(0);
						}
						setBusy(false);
					}
				}

				//If Message is a lock Write Request
				if(msg.isLockRequest()) {
					LogToFile.log(this.logName(), "Message Is a lock request");	
					if (isBusy()) {
						LogToFile.log(this.logName(), "File is busy");	
						LogToFile.log(this.logName(), "File is busy....Can't Send Grant Message To Read");	

						//Remove Messages from List
						messageList.remove(0);

					} else {
						LogToFile.log(this.logName(), "File is not busy... Processing Write Request");	
						if(msg.getRequestType().equals(Config.REQUEST_WRITE)) {
							this.setTimedOut(false);
							this.setRequestRemove(false);
							long lStartTime = new Date().getTime();
							long lEndTime = new Date().getTime();
							long difference = 0;
							// create MESSAGE_WRITE_GRANT 
							MessageFormat message = msg.createLockGrantMessage(msg, Config.MESSAGE_LOCK_GRANT, this.version, this.replicasUpdated,Node.getNode(parentNode).isDistinguishedSite,this.fileContent);

							//print the Message
							//message.printMessage(this.logName(), message);

							//set isBusy true
							setBusy(true);

							//Send Grant Message to Requesting Node
							LogToFile.log(this.logName(), "Sending Grant Message to->"+message.getDestId());	

							try {
								networkObject.sendToNode(message.getDestServer(), message.getDestPort(), message);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								System.out.println("Exception at sendToNode....run of ReplicatedFile.....");
								e1.printStackTrace();
							}

							//wait till writeOperationResult value has not be updated
							LogToFile.log(this.logName(), "Before while loop to check Write operation result");	
							while (getWriteOperationResult() == null) {
								lEndTime = new Date().getTime();
								difference = lEndTime - lStartTime;
								if (difference > Config.DURATION_GRANT_RESPONSE_WAIT ) {
									setTimedOut(true);
									break;
								}
								if(isRequestRemove()==true) {
									break;
									//if time out break
								}
								try {	
									Thread.currentThread().sleep(50);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
								}
							}
							if(isTimedOut() == true) {
								LogToFile.log(this.logName(), "Time Out......Removeing Write Lock Request");
								messageList.remove(0);
							}

							if(isRequestRemove()==true) {
								LogToFile.log(this.logName(), "Lock Request Remove Request......Removeing Lock Request");
								messageList.remove(0);
							}
							if(getWriteOperationResult() != null) {
								LogToFile.log(this.logName(), "**************Write Operation was Executed Successfully Updating File***********************");
								//Update File Parameters
								MessageFormat quorumReply = getWriteOperationResult();
								TreeMap<String, String> updatedValues = quorumReply.getValuesUpdated();
								long latestversion = Long.valueOf(updatedValues.get(Config.KEY_FILE_VESRION_NUMBER));
								int noOfUpdatedReplicas = Integer.valueOf(updatedValues.get(Config.KEY_FILE_REPLICAS_UPDATED));
								String content = updatedValues.get(Config.KEY_FILE_CONTENT_UPDATED);


								//File Absolute Path
								String filePath = FileOperations.getAbsoluteFilePath(parentNode, quorumReply.getFileName());

								LogToFile.log(this.logName(), "Absolute Path of File->"+filePath);
								LogToFile.log(this.logName(), "--------Below Values Wille be updated in the file--------");
								LogToFile.log(this.logName(), "latest Version->"+latestversion);
								LogToFile.log(this.logName(), "No of Replicas Updated->"+noOfUpdatedReplicas);
								LogToFile.log(this.logName(), "Content->"+content);

								// Update Version number and replicas updated parameters
								FileOperations.changeFileControlLines(filePath, Config.VERSION_NUMBER, String.valueOf(latestversion));
								FileOperations.changeFileControlLines(filePath, Config.REPLICATED_FILES, String.valueOf(noOfUpdatedReplicas));
								this.setVersion(latestversion);
								this.setReplicasUpdated(noOfUpdatedReplicas);

								//Update content
								FileOperations.modifyFile(filePath, content);

								//set writeOperationResult to null
								setWriteOperationResult(null);

								//Remove Messages from List
								messageList.remove(0);
							}
							//setBusy(false);
							setBusy(false);
						} 
					}
				}
				LogToFile.log(this.logName(), "End of while loop (checking messageList size)....now the file's isBusy return value is ->"+isBusy());
			}
		}

	}


	public void addMessage(MessageFormat message) {
		if(message.isLockRequest()) {
			LogToFile.log(this.logName(), "Adding Lock Request to List");	
			messages.add(message);			
		}
		if(message.isWriteLockRequestRemove()) {
			LogToFile.log(this.logName(), "Setting Write Lock REMOVE Variable");	
			this.setRequestRemove(true);
		}
		if(message.isMessageWriteSuccessful()) {
			LogToFile.log(this.logName(), "Adding Write Successful Message to writeOperationResult");
			this.setWriteOperationResult(message);
		}
		if(message.isReadRequest()){
			LogToFile.log(this.logName(), "This is A read Request");	
			messages.add(message);
		}
		if(message.isReadLockRequestRemove()) {
			LogToFile.log(this.logName(), "Setting Read Lock REMOVE Variable");	
			this.setReadRequestRemove(true);
		}
		if(message.isMessageReadSuccessful()) {
			LogToFile.log(this.logName(), "Adding Write Successful Message to writeOperationResult");
			this.setReadOperationResult(message);
		}
	}


}
