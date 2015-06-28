package nl.northcreek.twitazia;

import nl.northcreek.twitazia.model.Model;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class TwitterClient extends Application {

	private Model model;
	private CommonsHttpOAuthConsumer httpOauthConsumer;
	public final static String consumerKey = "kYJXFIBWYMrWJh1SvKwHS2MdA";
	public final static String consumerSecret = "pG4NvY8lowQu0F32JSSon9hOKiYWwKbiQAwqC4rr0PQSCjm82a";
	private DefaultOAuthProvider httpOauthprovider;
	private SharedPreferences prefs;

	@Override
	public void onCreate() {
		model = new Model(this.getApplicationContext());
		httpOauthConsumer = new CommonsHttpOAuthConsumer(consumerKey,
				consumerSecret);
		httpOauthprovider = new DefaultOAuthProvider(
				"https://api.twitter.com/oauth/request_token",
				"https://api.twitter.com/oauth/access_token",
				"https://api.twitter.com/oauth/authorize");
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		super.onCreate();
	}

	public CommonsHttpOAuthConsumer getCommonsHttpOAuthConsumer() {
		return httpOauthConsumer;
	}

	public Model getModel() {
		return model;
	}

	public DefaultOAuthProvider getHttpOauthprovider() {
		return httpOauthprovider;
	}

	public SharedPreferences getPrefs() {
		return prefs;
	}

}
