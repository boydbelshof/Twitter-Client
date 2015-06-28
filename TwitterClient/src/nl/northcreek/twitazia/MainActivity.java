package nl.northcreek.twitazia;

import nl.northcreek.twitazia.drawer.FragmentDrawer;
import nl.northcreek.twitazia.fragment.MentionsFragment;
import nl.northcreek.twitazia.fragment.FollowersFragment;
import nl.northcreek.twitazia.fragment.SettingsFragment;
import nl.northcreek.twitazia.fragment.TimelineFragment;
import nl.northcreek.twitazia.model.Model;
import nl.northcreek.twitazia.network.OAuthAccessTokenRequest;
import nl.northcreek.twitazia.network.Tweet_Get_HomeTimeline;
import oauth.signpost.OAuth;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

public class MainActivity extends ActionBarActivity implements
		FragmentDrawer.FragmentDrawerListener {
	private Toolbar mToolbar;
	private FragmentDrawer drawerFragment;
	private FragmentManager fragmentManager = getSupportFragmentManager();
	private Fragment myFragment;
	private int fragPos = 0;
	private TimelineFragment fragmentTweets = new TimelineFragment();
	private Model model;
	private TwitterClient app;
	private OAuthAccessTokenRequest mRequest;
	private CommonsHttpOAuthConsumer httpOauthConsumer;
	private DefaultOAuthProvider httpOauthprovider;

	SharedPreferences mPrefs;
	final String accessToken = "";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		app = (TwitterClient) getApplicationContext();
		httpOauthConsumer = app.getCommonsHttpOAuthConsumer();
		httpOauthprovider = app.getHttpOauthprovider();
		mPrefs = app.getPrefs();
		model = app.getModel();
		String hasAccessToken = mPrefs.getString(OAuth.OAUTH_TOKEN_SECRET,
				null);
		if (hasAccessToken == null) {
			mRequest = new OAuthAccessTokenRequest(app, httpOauthConsumer,
					httpOauthprovider);
			mRequest.execute();
		} else {
			Tweet_Get_HomeTimeline getHomeTimelineTweets = new Tweet_Get_HomeTimeline(
					app);
			getHomeTimelineTweets.execute();
		}
		
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		drawerFragment = (FragmentDrawer) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_navigation_drawer);
		drawerFragment.setUp(R.id.fragment_navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
		drawerFragment.setDrawerListener(this);
		displayView(fragPos);
		

	}

	public boolean onClose() {
		return false;
	}

	protected boolean isAlwaysExpanded() {
		return false;
	}

	@Override
	public void onDrawerItemSelected(View view, int position) {
		displayView(position);

	}

	private void displayView(int position) {
		myFragment = new Fragment();
		String title = getString(R.string.app_name);
		switch (position) {
		case 0:
			myFragment = new TimelineFragment();
			title = getString(R.string.title_timeline);
			fragPos = 0;
			break;
		case 1:
			myFragment = new MentionsFragment();
			title = getString(R.string.title_mentions);
			fragPos = 1;
			break;
		case 2:
			myFragment = new FollowersFragment();
			title = getString(R.string.title_retweets);
			fragPos = 2;
			break;
		case 3:
			myFragment = new SettingsFragment();
			title = getString(R.string.title_settings);
			fragPos = 3;
			break;
		default:
			break;
		}

		if (myFragment != null) {

			fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.replace(R.id.container_body, myFragment);
			fragmentTransaction.commit();

			// set the toolbar title
			getSupportActionBar().setTitle(title);
		}
	}

}
