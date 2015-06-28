package nl.northcreek.twitazia.network;

import nl.northcreek.twitazia.TwitterClient;
import oauth.signpost.OAuth;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;

public class AccessTokenRequest extends AsyncTask<String, Void, String> {
	private CommonsHttpOAuthConsumer httpOauthConsumer;
	private DefaultOAuthProvider httpOauthprovider;
	private TwitterClient app;
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
		prefs = app.getPrefs();
		oauthVerifier = prefs.getString(OAuth.OAUTH_VERIFIER, null);
		Log.d(oauthVerifier, oauthVerifier);
		httpOauthConsumer.setTokenWithSecret(oauthVerifier, httpOauthConsumer.getTokenSecret());
		try {
			httpOauthprovider.retrieveAccessToken(httpOauthConsumer,
					oauthVerifier);
			final Editor edit1 = prefs.edit();
			edit1.putString(OAuth.OAUTH_TOKEN, httpOauthConsumer.getToken());
			edit1.putString(OAuth.OAUTH_TOKEN_SECRET,
					httpOauthConsumer.getTokenSecret());
			edit1.commit();
			
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
		return results;
	}

	@Override
	protected void onPostExecute(String results) {
		Tweet_Get_HomeTimeline getHomeTimelineTweets = new Tweet_Get_HomeTimeline(
				app);
		getHomeTimelineTweets.execute();

		super.onPostExecute(results);
	}

}

