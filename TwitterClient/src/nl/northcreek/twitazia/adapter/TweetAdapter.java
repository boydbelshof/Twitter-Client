package nl.northcreek.twitazia.adapter;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import nl.northcreek.twitazia.CircleImageView;
import nl.northcreek.twitazia.R;
import nl.northcreek.twitazia.TwitterClient;
import nl.northcreek.twitazia.model.Tweet;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * De class van de TweetAdapter, hier word de adapter gevuld met de tweets.
 * @author boyd
 */
public class TweetAdapter extends
		RecyclerView.Adapter<TweetAdapter.MyViewHolder> {

	private LayoutInflater inflater;
	private TwitterClient app;
	private Clicklistener clicklistener;

	List<Tweet> tweets = Collections.emptyList();

	public TweetAdapter(Context context, List<Tweet> tweets) {
		inflater = LayoutInflater.from(context);
		this.tweets = tweets;

	}

	@Override
	public int getItemCount() {

		return tweets.size();
	}

	/**
	 * Hier word de ViewHolder ingevuld met tweets.
	 */
	public void onBindViewHolder(TweetAdapter.MyViewHolder holder, int position) {
		Tweet singleTweetItem = tweets.get(position);
		holder.tweetUserProfilePicture.setImageResource(R.drawable.applogo);
		holder.tweetUserProfileName
				.setText(singleTweetItem.getUser().getName());
		holder.tweetUserScreenName.setText("@" + singleTweetItem.getUser()
				.getScreen_name());
		holder.tweetText.setText(singleTweetItem.getText());
		holder.singleTweetFavoritesCount.setText("" + singleTweetItem.getFavorite_count());
		holder.singleTweetRetweetCount.setText("" + singleTweetItem.getRetweet_count());
		// Hier roept hij de DownloadImageTask aan en haalt hij de foto op
		new DownloadImageTask(holder.tweetUserProfilePicture)
				.execute(singleTweetItem.getUser().getProfile_image_url());
		// Hier haalt hi jde media op
		if (singleTweetItem.getEntities().getMedia().size() != 0) {
			if (singleTweetItem.getEntities().getMedia().get(0).getImage() == null) {
				holder.imageView.setVisibility(View.GONE);
			} else {
			holder.imageView.setVisibility(View.VISIBLE);
			holder.imageView.setImageBitmap(singleTweetItem.getEntities().getMedia().get(0).getImage());
		}
	} else {
		holder.imageView.setVisibility(View.GONE);
		}

	}

	public void setListener(Clicklistener clicklistener) {
		this.clicklistener = clicklistener;
	}

	/**
	 * Hier word de viewholder voor de eerste keer aangemaakt
	 */

	public TweetAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
			int viewType) {
		View view = inflater.inflate(R.layout.tweet_list_item, parent, false);
		MyViewHolder holder = new MyViewHolder(view);

		app = (TwitterClient) view.getContext();

		return holder;
	}

	/**
	 * De viewholder van de adapter, hier staat alles in opgeslagen zodat het
	 * niet steeds opnieuw word geinflate.
	 */
	class MyViewHolder extends RecyclerView.ViewHolder implements
			OnClickListener {
		TextView tweetUserScreenName, tweetUserProfileName, tweetText,
				tweetTime, singleTweetRetweetCount, singleTweetFavoritesCount, singleTweetReplyCount;
		CircleImageView tweetUserProfilePicture;
		ImageView imageView;

		public MyViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			tweetUserProfilePicture = (CircleImageView) itemView
					.findViewById(R.id.tweetUserPF);
			tweetUserScreenName = (TextView) itemView
					.findViewById(R.id.tweetUserScreenName);
			tweetUserProfileName = (TextView) itemView
					.findViewById(R.id.tweetUserProfileName);
			tweetText = (TextView) itemView.findViewById(R.id.tweetText);
			singleTweetRetweetCount = (TextView) itemView.findViewById(R.id.singleTweetRetweetCount);
			singleTweetFavoritesCount = (TextView) itemView.findViewById(R.id.singleTweetFavoritesCount);
			singleTweetReplyCount = (TextView) itemView.findViewById(R.id.singleTweetReplyCount);
			imageView = (ImageView) itemView.findViewById(R.id.media);
			tweetTime = (TextView) itemView.findViewById(R.id.tweetTime);
			tweetText.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
				}
			});
		}

		@Override
		public void onClick(View v) {
			if (clicklistener != null) {
				clicklistener.itemClicked(v, getPosition());
			}

		}

	}

	public interface Clicklistener {
		public void itemClicked(View view, int position);
	}

	/**
	 * De DownloadImageTask, hierin word de foto van de twitteraar opgehaald en
	 * weergegeven, dit is een AsyncTask omdat je hier te maken hebt met
	 * netwerkverkeer.
	 * 
	 * @author Boyd
	 *
	 */
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(CircleImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}

}
