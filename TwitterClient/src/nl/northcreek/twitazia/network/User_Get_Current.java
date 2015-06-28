package nl.northcreek.twitazia.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import nl.northcreek.twitazia.TwitterClient;
import nl.northcreek.twitazia.model.Model;
import nl.northcreek.twitazia.model.User;
import oauth.signpost.OAuth;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class User_Get_Current extends AsyncTask<String, Void, String> {
	private TwitterClient app;
	private Model model;
	private CommonsHttpOAuthConsumer httpOauthConsumer;
	private HttpGet httpGet;
	private int statusCode;
	private SharedPreferences prefs;



	public User_Get_Current(TwitterClient app) {
		this.app = app;
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		app = (TwitterClient) app.getApplicationContext();
	}
	@Override
	protected String doInBackground(String... params) {
		model = app.getModel();
		model.clear();
		httpOauthConsumer = app.getCommonsHttpOAuthConsumer();
		String results = null;
		prefs = app.getPrefs();
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		httpOauthConsumer.setTokenWithSecret(token, secret);
		try {
			httpGet = new HttpGet(
					"https://api.twitter.com/1.1/account/verify_credentials.json");
			httpOauthConsumer.sign(httpGet);
			results = getResponseBody(httpGet);
			if (statusCode == 200) {
				JSONObject jsonObject = new JSONObject(results);
				User user = new User(jsonObject);
				model.addCurrentUser(user);
				Log.d("USER ADDED",model.getCurrentUser().size() + "");
				}
			
		} catch (OAuthMessageSignerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}

	@Override
	protected void onPostExecute(String results) {
		model.update();
		super.onPostExecute(results);
	}

	private String getResponseBody(HttpRequestBase request) {
		StringBuilder sb = new StringBuilder();
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient(
					new BasicHttpParams());
			HttpResponse response = httpClient.execute(request);
			statusCode = response.getStatusLine().getStatusCode();
			String reason = response.getStatusLine().getReasonPhrase();
			Log.d("STATUS CODE", Integer.toString(statusCode));
			Log.d("STATUS REASON", reason);

			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream inputStream = entity.getContent();

				BufferedReader bReader = new BufferedReader(
						new InputStreamReader(inputStream, "UTF-8"), 8);
				String line = null;
				while ((line = bReader.readLine()) != null) {
					sb.append(line);
				}
			} else {
				sb.append(reason);
			}
		} catch (UnsupportedEncodingException ex) {
		} catch (ClientProtocolException ex1) {
		} catch (IOException ex2) {
		}
		return sb.toString();
	}

}
