package info.strojek.android.nextbiker;

import info.strojek.android.nextbiker.service.BikeService;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LookupActivity extends Activity implements OnClickListener {
	
	protected String session;
	protected String phone;
	
	protected EditText bike;
	protected Button rent;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lookup);
		
		bike = (EditText) findViewById(R.id.e_bike);
		
		rent = (Button) findViewById(R.id.b_rent);
		rent.setOnClickListener(this);
		
		Bundle extras = getIntent().getExtras();
		if( extras != null ) {
			session = extras.getString("session");
			phone = extras.getString("phone");
			Log.d("NextBiker", "Session ID: " + session);
			Log.d("NextBiker", "Phone: " + phone);
		}
		else {
			Log.d("NextBiker", "Can't retrive extra informations!");
		}
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

		inflater.inflate(R.menu.lookup, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.logout:
			Toast.makeText(getApplicationContext(), "Logging out",
					Toast.LENGTH_LONG).show();

			SharedPreferences settings = getSharedPreferences(
					BikeService.SETTINGS_NAME, 0);
			settings.edit().remove("session").commit();

			finish();

			return true;
		case R.id.email:
			Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not
																// ACTION_SEND
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_SUBJECT, "Subject of email");
			intent.putExtra(Intent.EXTRA_TEXT, "Body of email");
			intent.setData(Uri.parse("mailto:default@recipient.com"));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);

			return true;
		case R.id.browser:
			TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://nextbike.net/"
							+ tm.getNetworkCountryIso().toLowerCase()
							+ "/m/home?PHPSESSID="+session));
			startActivity(browserIntent);
		}

		return false;
	}

	public void onClick(View v) {
		if(v == rent) {
			if(bike.getText().length() == 0) {
				
				Toast.makeText(getApplicationContext(),
						R.string.lookup_bike_numer_not_entered,
						Toast.LENGTH_LONG).show();
				
				return;
			}
			
			int bike_no = Integer.parseInt(bike.getText().toString());
			int lock_code = 9999;
			
			// TODO Temporarly random value, change to service!
			lock_code = new Random().nextInt(10000);
			// TODO REMEMBER!
			
			rentialActivity(bike_no, lock_code);
		}
	}
	
	public void rentialActivity(int bike_no, int lock_code) {
		Intent intent = new Intent();
		intent.setClass(LookupActivity.this, RentialActivity.class);
		intent.putExtra("bike_no", bike_no);
		intent.putExtra("lock_code", lock_code);
		
		startActivityForResult(intent, 0);
	}
}