package nl.northcreek.twitazia.network;

import nl.northcreek.twitazia.TwitterClient;
import nl.northcreek.twitazia.WebViewActivity;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class OAuthAccessTokenRequest extends AsyncTask<String, Void, String> {
	private CommonsHttpOAuthConsumer httpOauthConsumer;
	private DefaultOAuthProvider httpOauthprovider;
	private String callbackURL = "http://twitter.com";
	public String authUrl = "";
	private TwitterClient app;

	public OAuthAccessTokenRequest(TwitterClient app,
			CommonsHttpOAuthConsumer httpOauthConsumer,
			DefaultOAuthProvider httpOauthprovider) {
		this.app = app;
		this.httpOauthConsumer = httpOauthConsumer;
		this.httpOauthprovider = httpOauthprovider;
	}

	@Override
	protected String doInBackground(String... params) {
		app = (TwitterClient) app.getApplicationContext();
		httpOauthprovider.setOAuth10a(true);
		try {
			authUrl = httpOauthprovider.retrieveRequestToken(httpOauthConsumer,
					callbackURL);

		} catch (OAuthMessageSignerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthNotAuthorizedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!authUrl.startsWith("https://") && !authUrl.startsWith("http://")) {
			authUrl = "https://" + authUrl;
		}
		return authUrl;
	}

	@Override
	protected void onPostExecute(String authUrl) {
		super.onPostExecute(authUrl);
		Intent i = new Intent(app, WebViewActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra("URL", authUrl);
		app.startActivity(i);
		Log.d("ON POST EXECUTE", authUrl);
	}

	public String getAuthUrl() {
		return authUrl;
	}

	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}

}
