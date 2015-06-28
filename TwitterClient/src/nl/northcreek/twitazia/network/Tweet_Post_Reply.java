package nl.northcreek.twitazia.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import nl.northcreek.twitazia.TwitterClient;
import nl.northcreek.twitazia.model.Model;

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
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpResponse;

public class Tweet_Post_Reply extends AsyncTask<String, Void, String> {
	private CommonsHttpOAuthConsumer consumer;
	private HttpResponse response;
	private Model model;
	private TwitterClient app;

	public Tweet_Post_Reply(TwitterClient app) {
		this.app = app;
	}

	protected String doInBackground(String... params) {
		app = (TwitterClient) app.getApplicationContext();
		model = app.getModel();
		consumer = app.getCommonsHttpOAuthConsumer();

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"https://api.twitter.com/1.1/statuses/update.json");

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
		} catch (ClientProtocolException e) {
			int statusCode = ((org.apache.http.HttpResponse) response)
					.getStatusLine().getStatusCode();
			return "" + statusCode;
		} catch (IOException e) {
			return "Internet";
		}

		return null;
		
		
	}
	@Override
	protected void onPostExecute(String result) {
		if (result != null) {
			if (result.equals("403")) {
				Toast.makeText(app.getBaseContext(),
						"You can't post 2 of the same tweets",
						Toast.LENGTH_LONG).show();
			} else if (result.equals("Internet")) {
				Toast.makeText(app.getApplicationContext(),
						"Je hebt geen internet", Toast.LENGTH_SHORT).show();
			} else {
				model.update();
				Toast.makeText(app.getBaseContext(), "Tweet has been sended",
						Toast.LENGTH_SHORT).show();
			}
		}


		model.update();
		super.onPostExecute(result);
	}

}
