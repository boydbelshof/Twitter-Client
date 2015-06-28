package nl.northcreek.twitazia.fragment;

import java.util.Observable;
import java.util.Observer;

import nl.northcreek.twitazia.R;
import nl.northcreek.twitazia.TwitterClient;
import nl.northcreek.twitazia.adapter.TweetAdapter;
import nl.northcreek.twitazia.adapter.TweetAdapter.Clicklistener;
import nl.northcreek.twitazia.model.Model;
import nl.northcreek.twitazia.network.Tweet_Get_Mentions;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

public class MentionsFragment extends Fragment implements Observer,
		OnRefreshListener, Clicklistener {
	private TweetAdapter tweetAdapter;
	private RecyclerView lvMentions;
	private View rootView;
	private Model model;
	private TwitterClient app;
	private SwipeRefreshLayout swipeToRefresh;

	public MentionsFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (TwitterClient) getActivity().getApplicationContext();
		model = app.getModel();
		model.addObserver(this);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		model.clear();
		rootView = inflater.inflate(R.layout.fragment_mentions, container,
				false);
		swipeToRefresh = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swiperefreshlayout);
		swipeToRefresh.setOnRefreshListener(this);
		swipeToRefresh.setColorScheme(android.R.color.holo_blue_light,
				android.R.color.holo_blue_dark);
		lvMentions = (RecyclerView) rootView
				.findViewById(R.id.mentionsListView);
		tweetAdapter = new TweetAdapter(getActivity().getApplicationContext(), model.getTweets());
		lvMentions.setLayoutManager(new LinearLayoutManager(getActivity()));
		Tweet_Get_Mentions getMentions = new Tweet_Get_Mentions(app);
		getMentions.execute();
		tweetAdapter.notifyDataSetChanged();
		lvMentions.setAdapter(tweetAdapter);

		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.menu_main, menu);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onRefresh() {

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Tweet_Get_Mentions getMentions = new Tweet_Get_Mentions(app);
				getMentions.execute();
				swipeToRefresh.setRefreshing(false);
			}
		}, 1000);

	}

	@Override
	public void itemClicked(View view, int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Observable observable, Object data) {
		tweetAdapter.notifyDataSetChanged();

	}
}
