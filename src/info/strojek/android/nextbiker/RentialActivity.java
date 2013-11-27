package info.strojek.android.nextbiker;

import info.strojek.zwGradientCalculator;
import info.strojek.zwTimer;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RentialActivity extends Activity implements OnClickListener {

	protected WheelView hour, mins, secs;

	protected Button b_return;

	protected TextView v_bike_no;
	protected TextView v_lock_code;

	protected TextView v_cost;

	private zwTimer timer, bg_timer;
	private zwGradientCalculator gradient_start = new zwGradientCalculator();
	private zwGradientCalculator gradient_continue = new zwGradientCalculator();

	private long startTime;

	private int bike_no;
	private int lock_code;
	
	private boolean force_repaint = true;
	private boolean animate = true;

	public RentialActivity() {
		super();

		gradient_start.addColor(.7, 0xff70c656); // Green
		gradient_start.addColor(.9, Color.YELLOW); // Yellow
		gradient_start.addColor(1., Color.RED); // Red

		gradient_continue.addColor(.0, Color.RED); // Red
		gradient_continue.addColor(.0025, Color.YELLOW); // Yellow
		gradient_continue.addColor(.005, 0xff70c656); // Green
		gradient_continue.addColor(.7, 0xff70c656); // Green
		gradient_continue.addColor(.9, Color.YELLOW); // Yellow
		gradient_continue.addColor(1., Color.RED); // Red

	}

	private Runnable timerUpdateTask = new Runnable() {
		public void run() {
			long millis = System.currentTimeMillis() - startTime;

			int v_seconds = (int) (millis / 1000);
			int v_minutes = v_seconds / 60;
			int v_hours = v_minutes / 60;
			v_seconds %= 60;
			v_minutes %= 60;

			if (hour.getCurrentItem() != v_hours)
				hour.setCurrentItem(v_hours, animate);

			if (mins.getCurrentItem() != v_minutes)
				mins.setCurrentItem(v_minutes, animate);

			if (secs.getCurrentItem() != v_seconds)
				secs.setCurrentItem(v_seconds, animate);
		}
	};
	
	private final static long l20min =  1200000L;
	private final static double d20min =  1200000.d;
	
	private final static long l40min =  2400000L;
	private final static double d40min =  2400000.d;
	
	private final static long l60min =  3600000L;
	private final static double d60min =  3600000.d;

	private Runnable backgroundUpdateTask = new Runnable() {

		public void run() {
			long millis = System.currentTimeMillis() - startTime;

			int color = 0xff70c656;
			if (millis <= l20min) {
				double ratio = millis / d20min;
				
				if(ratio < .7 && !force_repaint)
					return;

				color = gradient_start.getColor(ratio);
			} else if (millis <= l60min) {
				double ratio = (millis - d20min) / d40min;
				
				if( .005 < ratio && ratio < .7 && !force_repaint)
					return;

				color = gradient_continue.getColor(ratio);
			} else {
				double ratio = (millis % l60min) / d60min;

				if( .005 < ratio && ratio < .7 && !force_repaint)
					return;
				
				color = gradient_continue.getColor(ratio);
			}

			hour.setBackgroundColor(color);
			mins.setBackgroundColor(color);
			secs.setBackgroundColor(color);
			
			force_repaint = false;
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rential);

		hour = (WheelView) findViewById(R.id.r_hour);
		hour.setViewAdapter(new NumericWheelAdapter(this, 0, 12));
		hour.setEnabled(false);

		mins = (WheelView) findViewById(R.id.r_mins);
		mins.setViewAdapter(new NumericWheelAdapter(this, 0, 59, "%02d"));
		mins.setCyclic(true);
		mins.setEnabled(false);

		secs = (WheelView) findViewById(R.id.r_secs);
		secs.setViewAdapter(new NumericWheelAdapter(this, 0, 59, "%02d"));
		secs.setCyclic(true);
		secs.setEnabled(false);

		b_return = (Button) findViewById(R.id.b_return);
		b_return.setOnClickListener(this);

		v_bike_no = (TextView) findViewById(R.id.v_bike_no);

		v_lock_code = (TextView) findViewById(R.id.v_lock_code);

		v_cost = (TextView) findViewById(R.id.v_cost);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

		startTimer();

		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			bike_no = extras.getInt("bike_no");
			lock_code = extras.getInt("lock_code");
		}

		if (savedInstanceState != null) {
			startTime = savedInstanceState.getLong("start_time", startTime);

			bike_no = savedInstanceState.getInt("bike_no");
			lock_code = savedInstanceState.getInt("lock_code");
		}

		v_bike_no.setText(String.format("%05d", bike_no));
		v_lock_code.setText(String.format("%04d", lock_code));
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		outState.putInt("bike_no", bike_no);
		outState.putInt("lock_code", lock_code);

		outState.putLong("start_time", startTime);

		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		force_repaint = true;
		
		animate = false;
		timerUpdateTask.run();
		animate = true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.rential, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.logout:
			return true;
		}

		return false;
	}

	public void onClick(View v) {
		if (v == b_return) {
			stopTimer();
		}
	}
	
	private void startTimer() {
		if (timer == null) {
			startTime = System.currentTimeMillis();

			timer = new zwTimer(1000, timerUpdateTask);
			timer.start();
			
			bg_timer = new zwTimer(100, backgroundUpdateTask);
			bg_timer.start();
		}
	}
	
	private void stopTimer() {
		if (timer != null) {
			timer.stop();
			timer = null;
		}
		
		if (bg_timer != null) {
			bg_timer.stop();
			bg_timer = null;
		}

		finish();
	}
}
