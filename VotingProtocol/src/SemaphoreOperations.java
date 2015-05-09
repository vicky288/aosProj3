

import java.util.concurrent.Semaphore;

public class SemaphoreOperations {

	public void getWriteLock(Semaphore writeLock){
		try {
			writeLock.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void releaseWriteLock(Semaphore writeLock){
		writeLock.release();
	}
	
	public void getReadLock(Semaphore readLock){
		try {
			readLock.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void releaseReadLock(Semaphore readLock){
		readLock.release();
	}
	
	
	/**
	 * Using this to test the semaphore operations
	 * @param args
	 */
	public static void main(String args[]){
		SemaphoreOperations so = new SemaphoreOperations();
		FileOperations fo  = new FileOperations();
		Semaphore writeLock = new Semaphore(1);
		Semaphore readLock = new Semaphore(1);
		System.out.println(writeLock.availablePermits());
		so.getWriteLock(writeLock);
		LogToFile.log(Config.LOG_FILE,"write lock acquired.");
		fo.modifyFile(Config.FILENAME, "Test 1");
		
//		so.getWriteLock(writeLock);
		System.out.println(writeLock.availablePermits());
		so.releaseWriteLock(writeLock);
		LogToFile.log(Config.LOG_FILE,"write lock released.");
		System.out.println("Program execution over!!");
	}
}
