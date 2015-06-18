package nl.northcreek.twitazia.adapter;

import java.util.Collections;
import java.util.List;

import nl.northcreek.twitazia.CircleImageView;
import nl.northcreek.twitazia.R;
import nl.northcreek.twitazia.TwitterClient;
import nl.northcreek.twitazia.adapter.TweetAdapter.Clicklistener;
import nl.northcreek.twitazia.model.Tweet;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class FollowerAdapter extends
		RecyclerView.Adapter<FollowerAdapter.MyViewHolder> {

	private LayoutInflater inflater;
	private TwitterClient app;
	private Clicklistener clicklistener;

	List<Tweet> tweets = Collections.emptyList();

	public FollowerAdapter(Context context, List<Tweet> tweets) {
		inflater = LayoutInflater.from(context);
		this.tweets = tweets;

	}

	@Override
	public int getItemCount() {

		return tweets.size();
	}

	@Override
	public void onBindViewHolder(FollowerAdapter.MyViewHolder holder,
			int position) {
		Tweet singleTweetItem = tweets.get(position);
		holder.followerPf.setImageResource(R.drawable.applogo);
		holder.followerUsername.setText(singleTweetItem.getUser().getName());
		holder.followerScreenname.setText(singleTweetItem.getUser().getScreen_name());
		holder.followerUsername.setText(singleTweetItem.getText());
		holder.followerScreenname.setText(singleTweetItem.getUser().getName());
		holder.followButton.setVisibility(View.VISIBLE);

	}

	public void setListener(Clicklistener clicklistener) {
		this.clicklistener = clicklistener;
	}

	@Override
	public FollowerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
			int viewType) {
		View view = inflater
				.inflate(R.layout.follower_list_item, parent, false);
		MyViewHolder holder = new MyViewHolder(view);

		app = (TwitterClient) view.getContext();
		return holder;
	}

	class MyViewHolder extends RecyclerView.ViewHolder implements
			OnClickListener {

		TextView followerScreenname, followerUsername;
		CircleImageView followerPf;
		ImageButton followButton;

		public MyViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			followerPf = (CircleImageView) itemView
					.findViewById(R.id.followerPf);
			followerScreenname = (TextView) itemView
					.findViewById(R.id.followerScreenname);
			followerUsername = (TextView) itemView
					.findViewById(R.id.followerUsername);
			followButton = (ImageButton) itemView
					.findViewById(R.id.followButton);

		}

		@Override
		public void onClick(View v) {
			if (clicklistener != null) {
				clicklistener.itemClicked(v, getPosition());
			}

		}

	}

}
