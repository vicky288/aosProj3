

public class Config {
	public static final String HOME_PATH = System.getProperty("user.home");
	public static final String FILENAME = "D:/in Dallas/Classes S15/Advanced Operating Systems/Assignments/TestFile.txt";
	public static final String LOG_FILE = "Log.txt";
	public static final String CONFIG_FILE = "config.txt";
	public static final String NUMBER_OF_NODES = "#Number of nodes";
	public static final String NODES = "#Nodes";
	public static final String VOTES = "#Votes";
	public static final String NUMBER_OF_FILES = "#Number of files in the system";
	public static final String NUMBER_OF_OPS = "#Number of operations per node";
	public static final String EXP_BACKOFF = "#Parameters of exp backoff";
    public static final String CONIG_REQUESTS = "#Requests";
    
	/**
	 * Following constants used for testing purposes only
	 */
	public static final String FILE_PATH = "D:/in Dallas/Classes S15/Advanced Operating Systems/Assignments/";
	
	/*
	 * */
	public static final String REQUEST_READ = "READ_REQUEST";
	public static final String REQUEST_WRITE = "WRITE_REQUEST";
	
	public static final String MESSAGE_LOCK_REQUEST = "MESSAGE_LOCK_REQUEST";
	public static final String MESSAGE_LOCK_GRANT = "MESSAGE_LOCK_GRANT";
	public static final String MESSAGE_READ_LOCK = "MESSAGE_READ_LOCK";

	public static final String MESSAGE_READ_GRANT = "MESSAGE_READ_GRANT";
//	public static final String MESSAGE_LOCK_RELEASE = "3";
//	public static final String MESSAGE_LOCK_DENIED = "4";
	public static final String MESSAGE_LOCK_REQUEST_REMOVE = "MESSAGE_LOCK_REQUEST_REMOVE";	
	public static final String MESSAGE_READ_REQUEST_REMOVE = "MESSAGE_READ_REQUEST_REMOVE";	
	
	public static final String MESSAGE_WRITE_SUCCESSFUL = "MESSAGE_WRITE_SUCCESSFUL";
	public static final String MESSAGE_READ_SUCCESSFUL = "MESSAGE_READ_SUCCESSFUL";
//	public static final String MESSAGE_WRITE_UNSUCCESSFUL = "MESSAGE_WRITE_UNSUCCESSFUL";
	
	
	//
	public static final String THREAD_START_CONDITION = "THREAD_START_CONDITION";
	
	//
	public static final String KEY_FILE_VESRION_NUMBER = "FILE_VESRION_NUMBER";
	public static final String KEY_FILE_REPLICAS_UPDATED = "FILE_REPLICAS_UPDATED";
	public static final String KEY_FILE_CONTENT_UPDATED = "KEY_FILE_CONTENT_UPDATED";
	public static final String KEY_FILE_DISTINGUISHED_SITE = "KEY_FILE_DISTINGUISHED_SITE";
	
	//
	public static final int DURATION_GRANT_MESSAGE_WAIT= 7000;
	public static final int DURATION_BUFFER= 3000;
	public static final int DURATION_GRANT_RESPONSE_WAIT= 15000;
	
	//
	public static final String VERSION_NUMBER = "Version Number:";
    public static final String REPLICATED_FILES = "Replicated Files:";



}
