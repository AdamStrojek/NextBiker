package info.strojek.android.nextbiker;

import info.strojek.android.nextbiker.service.BikeService;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebLoginActivity extends Activity {

	protected WebView view;
	protected WebViewClient client = new WebViewClient() {
		public void onPageFinished(WebView view, String url)
		{
			
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.weblogin);
		
		view = (WebView) findViewById(R.id.weblogin_view);
		
		WebSettings settings = view.getSettings();
		
		settings.setJavaScriptEnabled(true);
		settings.setGeolocationEnabled(true);
		settings.setSavePassword(false);
		
		Log.d("NextBiker", "User-Agent: " + settings.getUserAgentString() );
		
		//view.setWebViewClient(client);
		
		view.loadUrl(BikeService.LOGIN_URL);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
