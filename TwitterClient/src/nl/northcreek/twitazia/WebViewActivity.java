package nl.northcreek.twitazia;

import nl.northcreek.twitazia.model.Model;
import nl.northcreek.twitazia.network.AccessTokenRequest;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity {
	private String authUrl;
	private WebView mWebView;
	private TwitterClient app;
	private CommonsHttpOAuthConsumer httpOauthConsumer;
	private DefaultOAuthProvider httpOauthProvider;
	private SharedPreferences prefs;
	private Model model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_activity);
		app = (TwitterClient) getApplicationContext();
		model = app.getModel();
		httpOauthConsumer = app.getCommonsHttpOAuthConsumer();
		httpOauthProvider = app.getHttpOauthprovider();
		mWebView = (WebView) findViewById(R.id.webViewLogin);
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("http://twitter.com")) {
					Log.d("SUCCESVOL INGELOGD", url);
					String oAuthToken = url.substring(32, 59);
					String oAuthVerifier = url.substring(75, url.length());
					prefs = app.getPrefs();
					SharedPreferences.Editor editor = prefs.edit();
					editor.clear();
					editor.putString("oauthVerifier", oAuthVerifier);
					editor.putString("oAuthToken", oAuthToken);
					editor.commit();
					model.setoAuthToken(oAuthToken);
					model.setoAuthVerifier(oAuthVerifier);
					Log.d("TOKENS", oAuthToken + "     +     " + oAuthVerifier);
					AccessTokenRequest tokenRequest = new AccessTokenRequest(
							app, httpOauthConsumer, httpOauthProvider, prefs, oAuthVerifier);
					tokenRequest.execute();
					finish();

				}

				return super.shouldOverrideUrlLoading(view, url);
			}

		});
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			authUrl = extras.getString("URL");
		}
		Log.d("URL GET", authUrl);
		mWebView.loadUrl(authUrl);
	}
}
