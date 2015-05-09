

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

public class MessageFormat implements Serializable{

	private String sourceServer;
	private String destServer;
	private String sourceId;
	private String destId;
	private int sourcePort;
	private int destPort;
	private String messageType; 	//grant/request/remove
	private String requestType;		//write/read
	private TreeMap<String, String> valuesUpdated = new TreeMap<String, String>();
	private String fileName;
	
	
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getSourceServer() {
		return sourceServer;
	}
	public void setSourceServer(String sourceServer) {
		this.sourceServer = sourceServer;
	}
	public String getDestServer() {
		return destServer;
	}
	public void setDestServer(String destServer) {
		this.destServer = destServer;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getDestId() {
		return destId;
	}
	public void setDestId(String destId) {
		this.destId = destId;
	}
	public int getSourcePort() {
		return sourcePort;
	}
	public void setSourcePort(int sourcePort) {
		this.sourcePort = sourcePort;
	}
	public int getDestPort() {
		return destPort;
	}
	public void setDestPort(int destPort) {
		this.destPort = destPort;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public TreeMap<String, String> getValuesUpdated() {
		return valuesUpdated;
	}
	public void setValuesUpdated(TreeMap<String, String> valuesUpdated) {
		this.valuesUpdated = valuesUpdated;
	}

	public boolean isReadRequest(){
		if(requestType.equalsIgnoreCase(Config.REQUEST_READ)) {
			return true;
		}
		return false;
	}
	public boolean isLockRequest() {
		if(messageType.equalsIgnoreCase(Config.MESSAGE_LOCK_REQUEST)) {
			return true;
		}
		return false;
	}
	public boolean isLockGrant() {
		if(messageType.equalsIgnoreCase(Config.MESSAGE_LOCK_GRANT)) {
			return true;
		}
		return false;
	}
	public boolean isReadGrant() {
		if(messageType.equalsIgnoreCase(Config.MESSAGE_READ_GRANT)) {
			return true;
		}
		return false;
	}
/*	public boolean isLockRelease() {
		if(messageType.equalsIgnoreCase(Config.MESSAGE_LOCK_RELEASE)) {
			return true;
		}
		return false;
	}	

	public boolean isLockDenied() {
		if(messageType.equalsIgnoreCase(Config.MESSAGE_LOCK_DENIED)) {
			return true;
		}
		return false;
	}	
*/
	public boolean isReadLockRequestRemove() {
		if(messageType.equalsIgnoreCase(Config.MESSAGE_READ_REQUEST_REMOVE)) {
			return true;
		}
		return false;
	}	
	
	public boolean isWriteLockRequestRemove() {
		if(messageType.equalsIgnoreCase(Config.MESSAGE_LOCK_REQUEST_REMOVE)) {
			return true;
		}
		return false;
	}	
	
	public boolean isMessageWriteSuccessful() {
		if(messageType.equalsIgnoreCase(Config.MESSAGE_WRITE_SUCCESSFUL)) {
			return true;
		}
		return false;
	}	
	public boolean isMessageReadSuccessful() {
		if(messageType.equalsIgnoreCase(Config.MESSAGE_READ_SUCCESSFUL)) {
			return true;
		}
		return false;
	}

//	public boolean isMessageWriteUnsuccessful() {
//		if(messageType.equalsIgnoreCase(Config.MESSAGE_WRITE_UNSUCCESSFUL)) {
//			return true;
//		}
//		return false;
//	}	

	public MessageFormat createLockRequest(String sourceId, String sourceServer, int sourcePort,String destId, String destServer, int destPort, String fileName) {
		MessageFormat newMessage = new MessageFormat();
		newMessage.setDestId(destId);
		newMessage.setDestPort(destPort);
		newMessage.setDestServer(destServer);
		
		newMessage.setSourceId(sourceId);
		newMessage.setSourcePort(sourcePort);
		newMessage.setSourceServer(sourceServer);
		
		newMessage.setFileName(fileName);
		
		newMessage.setRequestType(Config.REQUEST_WRITE);
		newMessage.setMessageType(Config.MESSAGE_LOCK_REQUEST);
		
		return newMessage;
	}
	public MessageFormat createReadRequest(String sourceId, String sourceServer, int sourcePort,String destId, String destServer, int destPort, String fileName) {
		MessageFormat newMessage = new MessageFormat();
		newMessage.setDestId(destId);
		newMessage.setDestPort(destPort);
		newMessage.setDestServer(destServer);
		
		newMessage.setSourceId(sourceId);
		newMessage.setSourcePort(sourcePort);
		newMessage.setSourceServer(sourceServer);
		
		newMessage.setFileName(fileName);
		
		newMessage.setRequestType(Config.REQUEST_READ);
		newMessage.setMessageType(Config.MESSAGE_READ_LOCK);
		
		return newMessage;
	}
	public MessageFormat createReadGrantMessage(MessageFormat message, long versionNumber, int noOfReplicatedFiles, boolean isDistinguishedSite, String content) {
		MessageFormat replyMessage = new MessageFormat();
		
		replyMessage.setDestId(message.getSourceId());
		replyMessage.setDestServer(message.getSourceServer());
		replyMessage.setDestPort(message.getSourcePort());

		replyMessage.setSourceId(message.getDestId());
		replyMessage.setSourceServer(message.getDestServer());
		replyMessage.setSourcePort(message.getDestPort());	
		
		replyMessage.setRequestType(Config.REQUEST_READ);
		replyMessage.setMessageType(Config.MESSAGE_READ_GRANT);
		
		replyMessage.setFileName(message.getFileName());
		
		TreeMap<String, String> fileInfo = new TreeMap<String, String>();
		fileInfo.put(Config.KEY_FILE_VESRION_NUMBER, String.valueOf(versionNumber));
		fileInfo.put(Config.KEY_FILE_REPLICAS_UPDATED, String.valueOf(noOfReplicatedFiles));
		fileInfo.put(Config.KEY_FILE_DISTINGUISHED_SITE, String.valueOf(isDistinguishedSite));
		fileInfo.put(Config.KEY_FILE_CONTENT_UPDATED, content);
		replyMessage.setValuesUpdated(fileInfo);
		
		return replyMessage;
	}

	public MessageFormat createLockGrantMessage(MessageFormat message, String lockType, long versionNumber, int noOfReplicatedFiles, boolean isDistinguishedSite, String content) {
		MessageFormat replyMessage = new MessageFormat();
		
		replyMessage.setDestId(message.getSourceId());
		replyMessage.setDestServer(message.getSourceServer());
		replyMessage.setDestPort(message.getSourcePort());

		replyMessage.setSourceId(message.getDestId());
		replyMessage.setSourceServer(message.getDestServer());
		replyMessage.setSourcePort(message.getDestPort());	
		
		replyMessage.setMessageType(lockType);
		replyMessage.setRequestType(Config.REQUEST_WRITE);
		
		replyMessage.setFileName(message.getFileName());
		
		TreeMap<String, String> fileInfo = new TreeMap<String, String>();
		fileInfo.put(Config.KEY_FILE_VESRION_NUMBER, String.valueOf(versionNumber));
		fileInfo.put(Config.KEY_FILE_REPLICAS_UPDATED, String.valueOf(noOfReplicatedFiles));
		fileInfo.put(Config.KEY_FILE_DISTINGUISHED_SITE, String.valueOf(isDistinguishedSite));
		fileInfo.put(Config.KEY_FILE_CONTENT_UPDATED, content);
		replyMessage.setValuesUpdated(fileInfo);
		
		return replyMessage;
	}
	
	public void printMessage(String nodeId, MessageFormat message) {
		LogToFile.log(nodeId, "####################Start of Printing Message###################");	
		LogToFile.log(nodeId, "Source Id->"+ message.getSourceId());
		LogToFile.log(nodeId, "Source Server->"+message.getSourceServer());
		LogToFile.log(nodeId, "Source Port->"+message.getSourcePort()+"");
		LogToFile.log(nodeId, "Dest Id->"+message.getDestId());
		LogToFile.log(nodeId, "Dest Server->"+message.getDestServer());
		LogToFile.log(nodeId, "Dest Port->"+message.getDestPort()+"");
		LogToFile.log(nodeId, "Request Type->"+message.getRequestType());
		LogToFile.log(nodeId, "Message Type->"+message.getMessageType());
		LogToFile.log(nodeId, "FileName->"+message.getFileName());
		LogToFile.log(nodeId, "Version Number->"+message.getValuesUpdated().get(Config.KEY_FILE_VESRION_NUMBER));
		LogToFile.log(nodeId, "Number of Replicas->"+message.getValuesUpdated().get(Config.KEY_FILE_REPLICAS_UPDATED));
		LogToFile.log(nodeId, "Distinguished Site->"+message.getValuesUpdated().get(Config.KEY_FILE_DISTINGUISHED_SITE));
		
		//LogToFile.log(nodeId, "->"+message.get);
//		LogToFile.log(nodeId, "->"+message.get);
		LogToFile.log(nodeId, "####################End of Printing Message###################");		

	}
	
	public MessageFormat createWriteResponseMessage(MessageFormat senderMessage, MessageFormat writeResultMessage){
		if(writeResultMessage == null) {
			writeResultMessage = new MessageFormat();
			writeResultMessage.setDestId(senderMessage.getSourceId());
			writeResultMessage.setDestServer(senderMessage.getSourceServer());
			writeResultMessage.setDestPort(senderMessage.getSourcePort());

			writeResultMessage.setSourceId(senderMessage.getDestId());
			writeResultMessage.setSourceServer(senderMessage.getDestServer());
			writeResultMessage.setSourcePort(senderMessage.getDestPort());	
			
			writeResultMessage.setMessageType(Config.MESSAGE_LOCK_REQUEST_REMOVE);
			writeResultMessage.setRequestType(Config.REQUEST_WRITE);

			writeResultMessage.setFileName(senderMessage.getFileName());
			
		} else {
			writeResultMessage.setDestId(senderMessage.getSourceId());
			writeResultMessage.setDestServer(senderMessage.getSourceServer());
			writeResultMessage.setDestPort(senderMessage.getSourcePort());

			writeResultMessage.setSourceId(senderMessage.getDestId());
			writeResultMessage.setSourceServer(senderMessage.getDestServer());
			writeResultMessage.setSourcePort(senderMessage.getDestPort());	
			writeResultMessage.setMessageType(Config.MESSAGE_WRITE_SUCCESSFUL);
			writeResultMessage.setRequestType(Config.REQUEST_WRITE);
			
			writeResultMessage.setFileName(senderMessage.getFileName());
			
		}
		return writeResultMessage;
	}
}
