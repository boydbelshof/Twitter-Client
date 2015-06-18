package nl.northcreek.twitazia.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import nl.northcreek.twitazia.TwitterClient;
import nl.northcreek.twitazia.adapter.TweetAdapter;
import nl.northcreek.twitazia.model.Model;
import nl.northcreek.twitazia.model.Tweet;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;

public class SearchTweetsTask extends AsyncTask<String, Void, String> {

	public final static String consumerKey = "iAnfBl7eztTCmKzJ4ScGM05t2";
	public final static String consumerSecret = "nTT9gYWIpsgfV71gWLTt8BGbNjbO7QCGvbA4rNx85hG1Np1dkb";
	final static String TwitterTokenURL = "https://api.twitter.com/oauth2/token";
	final static String screenName = "lfpoelman";
	final static String TwitterStreamURL = "https://api.twitter.com/1.1/search/tweets.json?q=";
	private String searchWord = "saxion";
	private TwitterClient app = new TwitterClient();
	private Model model;
	private Context context;
	private TweetAdapter tweetAdapter;
	private RecyclerView lvTweets;
	private ProgressDialog pDialog;

	public SearchTweetsTask(Context context, String searchWord) {
		this.context = context;
		this.searchWord = searchWord;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(context);
		pDialog.setMessage("Searching....");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();

	}

	protected String doInBackground(String... params) {

		String results = null;
		app = (TwitterClient) context.getApplicationContext();
		model = app.getModel();
		// Step 1: Encode consumer key and secret
		try {

			// URL encode the consumer key and secret
			String urlApiKey = URLEncoder.encode(consumerKey, "UTF-8");
			String urlApiSecret = URLEncoder.encode(consumerSecret, "UTF-8");

			// Concatenate the encoded consumer key, a colon character, and the
			// encoded consumer secret
			String combined = urlApiKey + ":" + urlApiSecret;

			// Base64 encode the string
			String base64Encoded = Base64.encodeToString(combined.getBytes(),
					Base64.NO_WRAP);

			// Step 2: Obtain a bearer token
			HttpPost httpPost = new HttpPost(TwitterTokenURL);
			httpPost.setHeader("Authorization", "Basic " + base64Encoded);
			httpPost.setHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			httpPost.setEntity(new StringEntity("grant_type=client_credentials"));
			String rawAuthorization = getResponseBody(httpPost);
			System.out.println(rawAuthorization);
			Authenticated auth = jsonToAuthenticated(rawAuthorization);

			if (auth != null && auth.getToken_type().equals("bearer")) {

				// Step 3: Authenticate API requests with bearer token
				HttpGet httpGet = new HttpGet(TwitterStreamURL
						+ Uri.encode(searchWord));

				// construct a normal HTTPS request and include an
				// Authorization
				// header with the value of Bearer <>
				httpGet.setHeader("Authorization",
						"Bearer " + auth.getAccess_token());
				httpGet.setHeader("Content-Type", "application/json");
				// update the results with the body of the response
				results = getResponseBody(httpGet);
			}
			JSONObject jsonObject = new JSONObject(results);
			JSONArray tempOBJ = jsonObject.getJSONArray("statuses");
			for (int i = 0; i < tempOBJ.length(); i++) {
				JSONObject tweetOBJ = tempOBJ.getJSONObject(i);
				Tweet tweet = new Tweet(tweetOBJ);
				model.addTweet(tweet);

			}

		} catch (JSONException e) {
		} catch (UnsupportedEncodingException ex) {
		} catch (IllegalStateException ex1) {
		}

		return results;
	}

	@Override
	public void onPostExecute(String result) {
		if (pDialog != null && pDialog.isShowing()) {
			pDialog.dismiss();
		}
		if (model.getTweets().size() == 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage("Nothing has been found, try again");
			builder.setPositiveButton("Okay", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.show();
		}
		tweetAdapter = new TweetAdapter(app.getApplicationContext(),
				model.getTweets());
		lvTweets = new RecyclerView(app.getApplicationContext());
		model.update();
		lvTweets.setAdapter(tweetAdapter);
		tweetAdapter.notifyDataSetChanged();
		super.onPostExecute(result);
	}

	private Authenticated jsonToAuthenticated(String rawAuthorization)
			throws JSONException {
		Authenticated auth = new Authenticated();
		if (rawAuthorization != null && rawAuthorization.length() > 0) {
			try {
				JSONObject bearerToken = new JSONObject(rawAuthorization);
				String tokenType = auth.setToken_type(bearerToken
						.getString("token_type"));
				String bearTok = auth.setAccess_token(bearerToken
						.getString("access_token"));
				Log.d("TOKEN TYPE", tokenType);
				Log.d("BEARER TOKEN JSON", bearTok);
			} catch (IllegalStateException ex) {
				// just eat the exception
			}
		}
		return auth;
	}

	private String getResponseBody(HttpRequestBase request) {
		StringBuilder sb = new StringBuilder();
		try {

			DefaultHttpClient httpClient = new DefaultHttpClient(
					new BasicHttpParams());
			HttpResponse response = httpClient.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			String reason = response.getStatusLine().getReasonPhrase();

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
