package nl.northcreek.twitazia.fragment;

import java.util.Observable;
import java.util.Observer;

import nl.northcreek.twitazia.CircleImageView;
import nl.northcreek.twitazia.R;
import nl.northcreek.twitazia.TwitterClient;
import nl.northcreek.twitazia.adapter.TweetAdapter;
import nl.northcreek.twitazia.adapter.TweetAdapter.Clicklistener;
import nl.northcreek.twitazia.floatingactionbutton.FloatingActionButton;
import nl.northcreek.twitazia.model.Model;
import nl.northcreek.twitazia.network.SearchTweetsTask;
import nl.northcreek.twitazia.network.Tweet_Get_HomeTimeline;
import nl.northcreek.twitazia.network.Tweet_Post_Status;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnCloseListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Dit is de timeline van de gebruiker, hier kan hij alle recente tweets zien.
 * 
 * @author boyd
 *
 */
public class TimelineFragment extends Fragment implements Observer,
		OnRefreshListener, Clicklistener,
		android.support.v7.widget.SearchView.OnQueryTextListener {

	private TweetAdapter tweetAdapter;
	private RecyclerView lvTweets;
	private View rootView;
	private Model model;
	private TwitterClient app;
	private SwipeRefreshLayout swipeToRefresh;
	private FloatingActionButton floatingActionButton;
	private SearchView searchView;
	private MenuItem menuItem;
	private CommonsHttpOAuthConsumer httpOauthConsumer;
	private DefaultOAuthProvider httpOauthprovider;
	private SharedPreferences prefs;
	private String oauthVerifier;

	public TimelineFragment() {
		// Required empty public constructor
	}

	/**
	 * Wanneer het fragment voor het eerst word aangeroepen.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		app = (TwitterClient) getActivity().getApplicationContext();
		model = app.getModel();
		model.addObserver(this);
		httpOauthConsumer = app.getCommonsHttpOAuthConsumer();
		httpOauthprovider = app.getHttpOauthprovider();
		prefs = PreferenceManager.getDefaultSharedPreferences(app);
		oauthVerifier = prefs.getString("oauthVerifier", null);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.searchview_in_menu, menu);
		menuItem = menu.findItem(R.id.menu_search);
		searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
		searchView.setOnQueryTextListener(this);
		searchView.setOnCloseListener(new OnCloseListener() {

			@Override
			public boolean onClose() {
				model.clear();
				Tweet_Get_HomeTimeline getHomeTimelineTweets = new Tweet_Get_HomeTimeline(
						app);
				getHomeTimelineTweets.execute();
				return false;
			}
		});
		super.onCreateOptionsMenu(menu, inflater);

	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		Toast.makeText(app.getApplicationContext(),
				"Searching tweets with: " + query, Toast.LENGTH_SHORT).show();
		SearchTweetsTask oa = new SearchTweetsTask(getActivity(), query);
		oa.execute();
		return false;
	}

	/**
	 * Hier word de floating action button aangemaakt waarop je moet klikken om
	 * een nieuwe tweet de wereld in te sturen.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_timeline, container,
				false);
		model.clear();
		swipeToRefresh = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swiperefreshlayout);
		swipeToRefresh.setOnRefreshListener(this);
		swipeToRefresh.setColorScheme(android.R.color.holo_blue_light,
				android.R.color.holo_blue_dark);
		lvTweets = (RecyclerView) rootView.findViewById(R.id.tweetListView);
		floatingActionButton = (FloatingActionButton) rootView
				.findViewById(R.id.floatingActionButton1);
		floatingActionButton.setColorNormal(Color.rgb(3, 169, 244));
		floatingActionButton.setColorPressed(Color.rgb(2, 119, 189));
		floatingActionButton.setIcon(R.drawable.posttweeticon);
		floatingActionButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				messageDialog(getActivity(), rootView);
			}
		});
		tweetAdapter = new TweetAdapter(getActivity().getApplicationContext(),
				model.getTweets());
		Tweet_Get_HomeTimeline getHomeTimelineTweets = new Tweet_Get_HomeTimeline(
				app);
		getHomeTimelineTweets.execute();
		lvTweets.setLayoutManager(new LinearLayoutManager(getActivity()));
		tweetAdapter.notifyDataSetChanged();
		lvTweets.setAdapter(tweetAdapter);

		return rootView;
	}

	/**
	 * Dit is het menu waar je vervolgens in terecht komt.
	 * 
	 * @param activity
	 * @param rootView
	 */
	public void messageDialog(final Context activity, final View rootView) {

		final Dialog myDialog = new Dialog(activity);
		myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		myDialog.setContentView(R.layout.newtweetdialog);
		myDialog.setCancelable(true);
		myDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		Button postTweetbutton = (Button) myDialog.findViewById(R.id.button1);
		final EditText edtInput = (EditText)	myDialog.findViewById(R.id.edtInput);

		CircleImageView circleImageView = (CircleImageView) myDialog
				.findViewById(R.id.followerPf);

		postTweetbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String tweetText = edtInput.getText().toString();
				Tweet_Post_Status tweet = new Tweet_Post_Status(app);
				tweet.execute(tweetText);
				myDialog.dismiss();

			}
		});

		myDialog.show();
		floatingActionButton.invalidate();

	}

	@Override
	public void onAttach(Activity activity) {
		setHasOptionsMenu(true);
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void update(Observable observable, Object data) {

		tweetAdapter.notifyDataSetChanged();

	}

	@Override
	public void onRefresh() {

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Tweet_Get_HomeTimeline getHomeTimelineTweets = new Tweet_Get_HomeTimeline(
						app);
				getHomeTimelineTweets.execute();
				swipeToRefresh.setRefreshing(false);
			}
		}, 1000);

	}

	@Override
	public void itemClicked(View view, int position) {
		Toast t = new Toast(getActivity());
		t.setText("you clicked: " + position);
		t.show();
	}

}
