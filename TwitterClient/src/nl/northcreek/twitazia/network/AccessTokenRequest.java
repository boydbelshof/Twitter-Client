package nl.northcreek.twitazia.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import nl.northcreek.twitazia.TwitterClient;
import nl.northcreek.twitazia.adapter.TweetAdapter;
import nl.northcreek.twitazia.model.Model;
import nl.northcreek.twitazia.model.Tweet;
import oauth.signpost.OAuth;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

public class AccessTokenRequest extends AsyncTask<String, Void, String> {
	private CommonsHttpOAuthConsumer httpOauthConsumer;
	private DefaultOAuthProvider httpOauthprovider;
	private TwitterClient app;
	private Model model;
	private TweetAdapter tweetAdapter;
	private RecyclerView lvTweets;
	private HttpGet httpGet;
	private int statusCode;
	private SharedPreferences prefs;
	private String oauthVerifier;

	public AccessTokenRequest(TwitterClient app,
			CommonsHttpOAuthConsumer httpOauthConsumer,
			DefaultOAuthProvider httpOauthprovider, SharedPreferences prefs,
			String oauthVerifier) {
		this.app = app;
		this.httpOauthConsumer = httpOauthConsumer;
		this.httpOauthprovider = httpOauthprovider;
		this.prefs = prefs;
		this.oauthVerifier = oauthVerifier;
	}

	@Override
	protected String doInBackground(String... params) {
		String results = null;
		app = (TwitterClient) app.getApplicationContext();
		model = app.getModel();
		prefs = PreferenceManager.getDefaultSharedPreferences(app);
		oauthVerifier = prefs.getString("oauthVerifier", null);
		try {
			httpOauthprovider.retrieveAccessToken(httpOauthConsumer,
					oauthVerifier);
			final Editor edit1 = prefs.edit();
			edit1.clear();
			edit1.putString(OAuth.OAUTH_TOKEN, httpOauthConsumer.getToken());
			edit1.putString(OAuth.OAUTH_TOKEN_SECRET,
					httpOauthConsumer.getTokenSecret());
			edit1.commit();

			String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
			String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
			Log.d("toker", token);
			Log.d("Secret", secret);
			httpOauthConsumer.setTokenWithSecret(token, secret);
		} catch (OAuthMessageSignerException e) {
			e.printStackTrace();
		} catch (OAuthNotAuthorizedException e) {
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			Log.d("ACCESS TOKEN", e.getMessage());
			e.printStackTrace();
		}

		try {
			httpGet = new HttpGet(
					"https://api.twitter.com/1.1/statuses/home_timeline.json");
			httpOauthConsumer.sign(httpGet);
			results = getResponseBody(httpGet);
			if (statusCode == 200) {
				model.clear();
				JSONArray jsonObject = new JSONArray(results);
				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject tweetOBJ = jsonObject.getJSONObject(i);
					Tweet tweet = new Tweet(tweetOBJ);
					model.addTweet(tweet);
				}
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
		Log.d("UPDATE ADAPTER", "UPDATE VAN ADAPTER");
		model.update();
		tweetAdapter = new TweetAdapter(app, model.getTweets());
		lvTweets = new RecyclerView(app);
		lvTweets.setAdapter(tweetAdapter);
		tweetAdapter.notifyDataSetChanged();
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
