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
import android.widget.TextView;

public class FollowingAdapter extends
		RecyclerView.Adapter<FollowingAdapter.MyViewHolder> {

	private LayoutInflater inflater;
	private TwitterClient app;
	private Clicklistener clicklistener;

	List<Tweet> tweets = Collections.emptyList();

	public FollowingAdapter(Context context, List<Tweet> tweets) {
		inflater = LayoutInflater.from(context);
		this.tweets = tweets;

	}

	@Override
	public int getItemCount() {

		return tweets.size();
	}

	@Override
	public void onBindViewHolder(FollowingAdapter.MyViewHolder holder,
			int position) {
		Tweet singleTweetItem = tweets.get(position);
		holder.followingPf.setImageResource(R.drawable.applogo);
		holder.followingUsername.setText(singleTweetItem.getUserProfileName());
		holder.followingScreenname.setText(singleTweetItem.getUserName());
	}

	public void setListener(Clicklistener clicklistener) {
		this.clicklistener = clicklistener;
	}

	@Override
	public FollowingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
			int viewType) {
		View view = inflater.inflate(R.layout.following_list_item, parent,
				false);
		MyViewHolder holder = new MyViewHolder(view);

		app = (TwitterClient) view.getContext();
		return holder;
	}

	class MyViewHolder extends RecyclerView.ViewHolder implements
			OnClickListener {

		TextView followingScreenname, followingUsername;
		CircleImageView followingPf;

		public MyViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			followingPf = (CircleImageView) itemView
					.findViewById(R.id.followingPf);
			followingScreenname = (TextView) itemView
					.findViewById(R.id.followingScreenname);
			followingUsername = (TextView) itemView
					.findViewById(R.id.followingUsername);

		}

		@Override
		public void onClick(View v) {
			if (clicklistener != null) {
				clicklistener.itemClicked(v, getPosition());
			}

		}

	}

}
