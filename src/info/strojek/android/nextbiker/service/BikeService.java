package info.strojek.android.nextbiker.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.alexd.jsonrpc.JSONRPCClient;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.google.common.base.Joiner;

public class BikeService {
	public static final String SETTINGS_NAME = "NextBikerSettings";

	public static final String LOGIN_URL = "https://nextbike.net/de/m/home";
	public static final String SERVICE_URL = "https://nextbikerapi.appspot.com/json";

	private JSONRPCClient client;

	private static BikeService instance = null;

	public static BikeService getInstance() {
		if (instance == null) {
			instance = new BikeService();
		}

		return instance;
	}

	private BikeService() {
		client = JSONRPCClient.create(BikeService.SERVICE_URL);
	}

	public String login(String phone, String pin)
			throws LoginException {
		
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter("http.useragent", "Mozilla/5.0 (Linux; U; Android 2.3; en-gb) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
		
		HttpPost request = new HttpPost(BikeService.LOGIN_URL);
		//request.addHeader("User-Agent", "Mozilla/5.0 (Linux; U; Android 2.3; en-gb) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
		
		try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
	        nameValuePairs.add(new BasicNameValuePair("action", "login"));
	        nameValuePairs.add(new BasicNameValuePair("bike_no", ""));
	        nameValuePairs.add(new BasicNameValuePair("mobile", phone));
	        nameValuePairs.add(new BasicNameValuePair("pin", pin));
	        request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = client.execute(request);
	        
	        String responseString = EntityUtils.toString(response.getEntity());
	        
	        List<String> cookies = new ArrayList<String>();
	        
	        for( Cookie c : client.getCookieStore().getCookies() ) {
	        	cookies.add(String.format("%s=%s", c.getName(), c.getValue()));
	        	Log.d("NextBiker", String.format("Cookie: %s=%s %s", c.getName(), c.getValue(), c.getPath()) );
	        }
	        
	        Joiner joiner = Joiner.on("; ");
	        
	        Document doc = Jsoup.parse(responseString);
	        
	        Elements el = doc.select("input[type=hidden][value=login]");
	        
	        if( el.isEmpty() ) {
	        	Log.d("NextBiker", "You are logged in!");
	        	return joiner.join(cookies);
	        }
	        else
	        {
	        	Log.d("NextBiker", "You are not logged in!");
	        	throw new LoginException();
	        }
	        
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    }
		
		return "";
		/*Map<String, String> params = new HashMap<String, String>();
		params.put("phone", phone);
		params.put("pin", pin);

		JSONObject resultObject = client.call("login", params);

		Map<String, String> result = new HashMap<String, String>();

		try {
			Iterator<String> iter = resultObject.keys();
			while (iter.hasNext()) {
				String key = iter.next();

				result.put(key, resultObject.get(key).toString());
			}
		} catch (JSONException ex) {
			throw new JSONRPCException("Cannot convert result", ex);
		}

		return result;*/
	}
}
