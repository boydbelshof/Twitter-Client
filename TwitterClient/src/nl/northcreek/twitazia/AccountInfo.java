package nl.northcreek.twitazia;

import java.util.List;
import java.util.Vector;

import nl.northcreek.twitazia.adapter.FollowerAdapter;
import nl.northcreek.twitazia.adapter.FollowingAdapter;
import nl.northcreek.twitazia.adapter.TweetAdapter;
import nl.northcreek.twitazia.floatingactionbutton.FloatingActionButton;
import nl.northcreek.twitazia.model.Model;
import nl.northcreek.twitazia.model.Tweet;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class AccountInfo extends ActionBarActivity {
	private ViewPager mPager;
	private Context mContext;
	private SlidingTabLayout mTabs;
	private Toolbar mToolbar;
	private TwitterClient app;
	private static Model model;
	private TweetAdapter mTweetAdapter;
	private static int mPos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_info);
		mContext = this;
		mToolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setTitle(R.string.nav_header_username);

		app = (TwitterClient) getApplicationContext();
		model = app.getModel();

		mTweetAdapter = new TweetAdapter(this, model.getTweets());
		mPager = (ViewPager) findViewById(R.id.pager);

		mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
		mTabs.setDistributeEvenly(true);
		mTabs.setViewPager(mPager);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			finish();
		}
		return (super.onOptionsItemSelected(menuItem));
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {
		String[] tabs;

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			tabs = getResources().getStringArray(R.array.tabs);

		}

		public CharSequence getPageTitle(int position) {
			mPos = position;
			switch (position) {
			case 0:
				return model.getTweets().size() + " Tweets";
			case 1:
				return model.getTweets().size() + " Following";
			case 2:
				return model.getTweets().size() + " Followers";
			}
			return null;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				MyFragment myFragment = MyFragment.getInstance(position);
				return myFragment;
			case 1:
				MySecondFragment mySecondFragment = MySecondFragment
						.getInstance(position);
				return mySecondFragment;
			case 2:
				MyThirdFragment myThirdFragment = MyThirdFragment
						.getInstance(position);
				return myThirdFragment;
			}
			return null;
		}

		@Override
		public int getCount() {
			return 3;
		}

	}

	public static class MyFragment extends Fragment {
		public static MyFragment getInstance(int position) {
			MyFragment myFragment = new MyFragment();
			Bundle args = new Bundle();
			args.putInt("position", position);
			myFragment.setArguments(args);
			return myFragment;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View layout = inflater.inflate(R.layout.fragment_timeline,
					container, false);
			RecyclerView lvTweets = (RecyclerView) layout
					.findViewById(R.id.tweetListView);

			FloatingActionButton floatingActionButton = (FloatingActionButton) layout
					.findViewById(R.id.floatingActionButton1);

			floatingActionButton.setVisibility(View.INVISIBLE);

			TweetAdapter tweetAdapter = new TweetAdapter(getActivity()
					.getApplicationContext(), model.getTweets());
			lvTweets.setLayoutManager(new LinearLayoutManager(getActivity()));
			lvTweets.setAdapter(tweetAdapter);
			return layout;
		}

	}

	public static class MySecondFragment extends Fragment {
		public static MySecondFragment getInstance(int position) {
			MySecondFragment mySecondFragment = new MySecondFragment();
			Bundle args = new Bundle();
			args.putInt("position", position);
			mySecondFragment.setArguments(args);
			return mySecondFragment;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View layout = inflater.inflate(R.layout.fragment_following,
					container, false);
			RecyclerView lvFollowing = (RecyclerView) layout
					.findViewById(R.id.lvFollowing);

			FollowingAdapter followerAdapter = new FollowingAdapter(
					getActivity().getApplicationContext(), model.getTweets());

			lvFollowing
					.setLayoutManager(new LinearLayoutManager(getActivity()));
			lvFollowing.setAdapter(followerAdapter);

			return layout;
		}

	}

	public static class MyThirdFragment extends Fragment {
		public static MyThirdFragment getInstance(int position) {
			MyThirdFragment myThirdFragment = new MyThirdFragment();
			Bundle args = new Bundle();
			args.putInt("position", position);
			myThirdFragment.setArguments(args);
			return myThirdFragment;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View layout = inflater.inflate(R.layout.fragment_following,
					container, false);
			RecyclerView lvFollowing = (RecyclerView) layout
					.findViewById(R.id.lvFollowing);

			FollowerAdapter followingAdapter = new FollowerAdapter(
					getActivity().getApplicationContext(), model.getTweets());
			lvFollowing
					.setLayoutManager(new LinearLayoutManager(getActivity()));
			lvFollowing.setAdapter(followingAdapter);
			return layout;

		}

	}
}
