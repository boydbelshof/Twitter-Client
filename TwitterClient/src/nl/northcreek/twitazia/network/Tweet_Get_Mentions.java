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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class Tweet_Get_Mentions extends AsyncTask<String, Void, String> {
	private TwitterClient app;
	private Model model;
	private CommonsHttpOAuthConsumer httpOauthConsumer;
	private HttpGet httpGet;
	private int statusCode;
	private SharedPreferences prefs;
	private TweetAdapter tweetAdapter;
	private RecyclerView lvMentions;
	
	public Tweet_Get_Mentions(TwitterClient app) {
		this.app = app;
	}
	@Override
	protected String doInBackground(String... params) {
	app = (TwitterClient) app.getApplicationContext();
	model = app.getModel();
	httpOauthConsumer = app.getCommonsHttpOAuthConsumer();
	String results = null;
	prefs = app.getPrefs();
	String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
	String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
	httpOauthConsumer.setTokenWithSecret(token, secret);
	try {
		httpGet = new HttpGet(
				"https://api.twitter.com/1.1/statuses/mentions_timeline.json");
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
		model.update();
		tweetAdapter = new TweetAdapter(app, model.getTweets());
		lvMentions = new RecyclerView(app);
		lvMentions.setAdapter(tweetAdapter);
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
