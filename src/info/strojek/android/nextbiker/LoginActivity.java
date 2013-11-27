package info.strojek.android.nextbiker;

import info.strojek.android.nextbiker.service.BikeService;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {

	protected EditText e_phone;
	protected EditText e_pin;

	protected Button b_login;

	protected CheckBox c_remember;

	protected Menu menu;

	private String session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login);

		SharedPreferences settings = getSharedPreferences(
				BikeService.SETTINGS_NAME, 0);

		e_phone = (EditText) findViewById(R.id.e_phone);
		e_phone.setText(settings.getString("phone", ""));

		e_pin = (EditText) findViewById(R.id.e_pin);
		e_pin.setInputType(InputType.TYPE_CLASS_NUMBER);
		e_pin.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		e_pin.setOnEditorActionListener(new EditText.OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					login();
					return true;
				}
				return false;
			}
		});

		if (!e_phone.getText().equals("")) {
			e_pin.requestFocus();
		}

		b_login = (Button) findViewById(R.id.b_login);
		// login.getBackground().setColorFilter(0xFF00DD00,
		// PorterDuff.Mode.MULTIPLY);
		b_login.setOnClickListener(this);

		c_remember = (CheckBox) findViewById(R.id.remember);
		c_remember.setChecked(settings.getBoolean("remember", false));

		session = settings.getString("session", "");

		if (c_remember.isChecked() && !session.equals("")) {
			lookupActivity(e_phone.getText().toString(), session);
		}

	}

	@Override
	public void onStop() {
		super.onStop();

		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		SharedPreferences settings = getSharedPreferences(
				BikeService.SETTINGS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("remember", c_remember.isChecked());
		if (c_remember.isChecked()) {
			editor.putString("phone", e_phone.getText().toString());
		} else if (settings.contains("phone")) {
			editor.remove("phone");
		}

		// Commit the edits!
		editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.login, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.login_menu_register:
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.login_register_hint),
					Toast.LENGTH_LONG).show();

			TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://nextbike.net/"
							+ tm.getNetworkCountryIso().toLowerCase()
							+ "/m/register"));
			startActivity(browserIntent);

			return true;
		case R.id.login_menu_lost_pin:
			FrameLayout l = new FrameLayout(getApplicationContext());
			l.setPadding(15, 0, 15, 0);

			final EditText input = new EditText(this);
			input.setInputType(InputType.TYPE_CLASS_PHONE);
			input.setText(e_phone.getText());

			l.addView(input);

			new AlertDialog.Builder(this)
					.setTitle("Lost PIN")
					.setMessage(
							"Please enter phone number for witch you want to recovery your PIN number. You will receive new PIN number by text message")
					.setView(l)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									Editable value = input.getText();
									e_phone.setText(value);

									e_pin.requestFocus();
								}
							}).setNegativeButton("Cancel", null).show();
			return true;
		case R.id.login_menu_i_dont_trust_you:
			Intent intent = new Intent();
			intent.setClass(LoginActivity.this, WebLoginActivity.class);
			startActivity(intent);
			return true;
		}

		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void onClick(View v) {
		if (b_login.equals(v)) {
			login();
		}
	}

	protected void login() {
		SharedPreferences settings = getSharedPreferences(
				BikeService.SETTINGS_NAME, 0);
		
		

		/*if (!settings.getBoolean("acceptedlicense", false)) {

			final AlertDialog d = new AlertDialog.Builder(this)
					.setPositiveButton("Yes", null)
					.setNegativeButton("No", null)
					.setIcon(R.drawable.icon)
					// .setMessage(Html.fromHtml("<a href=\"http://www.google.com\">Check this link out</a>"))
					.setMessage(
							Html.fromHtml(getString(R.string.login_license)))
					.create();
			d.show();
			// Make the textview clickable. Must be called after show()
			((TextView) d.findViewById(android.R.id.message))
					.setMovementMethod(LinkMovementMethod.getInstance());

			return;
		}*/

		final ProgressDialog dialog = ProgressDialog.show(LoginActivity.this,
				getString(R.string.app_name),
				getString(R.string.login_loading), true);
		dialog.show();

		/*new Thread(new Runnable() {
			public void run() {
				try {
					Map<String, String> result = BikeService.getInstance()
							.login(e_phone.getText().toString(),
									e_pin.getText().toString());
					session = result.get("session");
					dialog.dismiss();

					lookupActivity(e_phone.getText().toString(), session);
				} catch (LoginException ex) {
					dialog.dismiss();

					session = "";

					LoginActivity.this.runOnUiThread(new Runnable() {

						public void run() {
							Toast.makeText(LoginActivity.this, R.string.login_bad_phone_or_pin,
									Toast.LENGTH_LONG).show();

						}
					});

				}

			}
		}).start();*/
	
		
		dialog.dismiss();
		
		lookupActivity(e_phone.getText().toString(), "");
	}

	protected void lookupActivity(String phone, String session) {
		if (c_remember.isChecked()) {
			SharedPreferences settings = getSharedPreferences(
					BikeService.SETTINGS_NAME, 0);
			settings.edit().putString("session", session).commit();
		}

		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), LookupActivity.class);
		intent.putExtra("phone", phone);
		intent.putExtra("session", session);
		startActivityForResult(intent, 0);
	}

}
