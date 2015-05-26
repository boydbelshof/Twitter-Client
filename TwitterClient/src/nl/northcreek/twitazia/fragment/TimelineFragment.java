package nl.northcreek.twitazia.fragment;

import java.util.Observable;
import java.util.Observer;

import nl.northcreek.twitazia.R;
import nl.northcreek.twitazia.TwitterClient;
import nl.northcreek.twitazia.adapter.TweetAdapter;
import nl.northcreek.twitazia.adapter.TweetAdapter.Clicklistener;
import nl.northcreek.twitazia.data.FetchDataListener;
import nl.northcreek.twitazia.data.Populate;
import nl.northcreek.twitazia.floatingactionbutton.FloatingActionButton;
import nl.northcreek.twitazia.model.Model;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

public class TimelineFragment extends Fragment implements Observer,
		OnRefreshListener, Clicklistener {
	private TweetAdapter tweetAdapter;
	private View rootView;
	private Model model;
	private TwitterClient app;
	private FetchDataListener fdl;
	private SwipeRefreshLayout swipeToRefresh;
	private FloatingActionButton floatingActionButton;
	private AlertDialog dialog;

	public TimelineFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		app = (TwitterClient) getActivity().getApplicationContext();
		model = app.getModel();
		model.addObserver(this);

		Populate populate = new Populate(fdl, getActivity()
				.getApplicationContext());
		populate.execute();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_timeline, container,
				false);

		swipeToRefresh = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swiperefreshlayout);
		swipeToRefresh.setOnRefreshListener(this);

		RecyclerView lvTweets = (RecyclerView) rootView
				.findViewById(R.id.tweetListView);

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
		tweetAdapter.setListener(this);

		lvTweets.setLayoutManager(new LinearLayoutManager(getActivity()));
		lvTweets.setAdapter(tweetAdapter);
		return rootView;
	}

	public void messageDialog(final Context activity, final View rootView) {

		final Dialog myDialog = new Dialog(activity);
		myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		myDialog.setContentView(R.layout.newtweetdialog);
		myDialog.setCancelable(true);
		myDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

		Button postTweetbutton = (Button) myDialog.findViewById(R.id.button1);
		postTweetbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast t = new Toast(getActivity()).makeText(getActivity(),
						"Tweet posted!", Toast.LENGTH_SHORT);
				t.show();
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
				Populate populate = new Populate(fdl, getActivity()
						.getApplicationContext());
				populate.execute();
				swipeToRefresh.setRefreshing(false);
			}
		}, 3000);

	}

	@Override
	public void itemClicked(View view, int position) {
		Toast t = new Toast(getActivity());
		t.setText("you clicked: " + position);
		t.show();
		startActivity(new Intent(getActivity(), TweetFragment.class));
	}
}
