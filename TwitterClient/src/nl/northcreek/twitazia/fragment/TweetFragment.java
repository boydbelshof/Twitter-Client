package nl.northcreek.twitazia.fragment;

import nl.northcreek.twitazia.CircleImageView;
import nl.northcreek.twitazia.R;
import nl.northcreek.twitazia.TwitterClient;
import nl.northcreek.twitazia.model.Model;
import nl.northcreek.twitazia.model.Tweet;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.widget.TextView;

public class TweetFragment extends ActionBarActivity {
	private TwitterClient app;
	private Model model;
	private int tweetId;
	private LayoutInflater inflater;

	public TweetFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_tweet);
		Intent intent = getIntent();
		tweetId = intent.getIntExtra("tweetId", 0);
		Tweet tweet = model.getTweetsAtPosition(tweetId);

		CircleImageView tweetUserProfilePicture = (CircleImageView) findViewById(R.id.singleTweetUserProfilePicture);
		TextView tweetUserScreenName = (TextView) findViewById(R.id.singleTweetUserScreenName);
		TextView tweetUserProfileName = (TextView) findViewById(R.id.singleTweetUserProfileName);
		TextView tweetText = (TextView) findViewById(R.id.singleTweetText);
		TextView tweetTime = (TextView) findViewById(R.id.singleTweetTime);
		TextView tweetFavoritesCount = (TextView) findViewById(R.id.singleTweetFavoritesCount);
		TextView tweetRetweetCount = (TextView) findViewById(R.id.singleTweetRetweetCount);

		tweetUserScreenName.setText(tweet.getUser().getScreen_name());
		tweetUserProfileName.setText(tweet.getUser().getName());
		tweetText.setText(tweet.getText());
		tweetTime.setText(tweetTime.getText());
		tweetFavoritesCount.setText(tweetFavoritesCount.getText());
		tweetRetweetCount.setText(tweetRetweetCount.getText());
		

	}

}
