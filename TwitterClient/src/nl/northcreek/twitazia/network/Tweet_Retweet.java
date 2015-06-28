package nl.northcreek.twitazia.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import nl.northcreek.twitazia.TwitterClient;
import nl.northcreek.twitazia.model.Model;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpResponse;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.os.AsyncTask;
import android.widget.Toast;

public class Tweet_Retweet extends AsyncTask<String, Void, Void> {
	private CommonsHttpOAuthConsumer consumer;
	private HttpResponse response;
	private Model model;
	private TwitterClient app;

	public Tweet_Retweet(TwitterClient app) {
		this.app = app;
	}

	@Override
	protected Void doInBackground(String... params) {
		app = (TwitterClient) app.getApplicationContext();
		model = app.getModel();
		consumer = app.getCommonsHttpOAuthConsumer();
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"https://api.twitter.com/1.1/statuses/retweet/" + params[0]
						+ ".json");

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("status", params[0]));
		try {
			post.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e1) {
		}

		try {
			consumer.sign(post);
		} catch (OAuthMessageSignerException e) {
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			e.printStackTrace();
		}

		try {
			response = (HttpResponse) client.execute(post);
			//Toast.makeText(app.getApplicationContext(), "Geretweet!", Toast.LENGTH_SHORT);
		} catch (ClientProtocolException e) {
		int statusCode = ((org.apache.http.HttpResponse) response)
				.getStatusLine().getStatusCode();
		} catch (IOException e) {
		}

		model.update();
		return null;

	}
}
