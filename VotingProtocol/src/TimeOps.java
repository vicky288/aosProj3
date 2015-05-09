

import java.util.Timer;
import java.util.TimerTask;

public class TimeOps {

	Timer timer;
	static boolean val = false;


	public TimeOps(int seconds) {
		timer = new Timer();
		timer.schedule(new RemindTask(), seconds * 1000);
	}

	class RemindTask extends TimerTask {
		public void run() {
			System.out.println("Time's up!");
			val = true;
			timer.cancel(); // Terminate the timer thread
		}
	}
	
/**
 * Following function used to test timer only.
 * @param args
 */
	public static void main(String args[]) {
		new TimeOps(5);
		System.out.println("Task scheduledaaaaaa.");
		System.out.println("Task scheduled.");
		System.out.println("Started---"+val);
		
		System.out.println("Ended---"+val);
	}
}
