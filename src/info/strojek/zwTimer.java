package info.strojek;

import android.os.Handler;
import android.os.SystemClock;

public class zwTimer {
	private Runnable task;
	private int delay;
	
	private Handler handler = new Handler();
	
	private long uptimeMillis;
	
	private Runnable timeTask = new Runnable() {
		
		public void run() {
			uptimeMillis += delay;
			handler.postAtTime(timeTask, uptimeMillis);
			
			task.run();
		}
	};
	
	/**
	 * 
	 * @param delay in millisecs
	 * @param task
	 */
	public zwTimer(int delay, Runnable task) {
		this.delay = delay;
		this.task = task;
	}
	
	public void start() {
		if (uptimeMillis == 0L) {
			uptimeMillis = SystemClock.uptimeMillis() + delay;
			handler.removeCallbacks(timeTask);
			handler.postAtTime(timeTask, uptimeMillis);
			
			task.run();
		}
	}
	
	public void stop() {
		handler.removeCallbacks(timeTask);
	}
}
