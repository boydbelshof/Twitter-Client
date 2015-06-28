package nl.northcreek.twitazia.adapter;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import nl.northcreek.twitazia.CircleImageView;
import nl.northcreek.twitazia.R;
import nl.northcreek.twitazia.TwitterClient;
import nl.northcreek.twitazia.model.User;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

	private LayoutInflater inflater;
	private TwitterClient app;

	List<User> users = Collections.emptyList();

	public UserAdapter(Context context, List<User> users) {
		inflater = LayoutInflater.from(context);
		this.users = users;

	}

	@Override
	public int getItemCount() {

		return users.size();
	}

	@Override
	public void onBindViewHolder(UserAdapter.MyViewHolder holder, int position) {
		User singleUserItem = users.get(position);
		holder.tweetUserProfileName.setText(singleUserItem.getScreen_name());
		holder.tweetUserScreenName.setText(singleUserItem. getName());
		new DownloadImageTask(holder.tweetUserProfilePicture)
				.execute(singleUserItem.getProfile_image_url());
	}

	@Override
	public UserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
			int viewType) {
		View view = inflater.inflate(R.layout.user_list_item, parent, false);
		MyViewHolder holder = new MyViewHolder(view);

		app = (TwitterClient) view.getContext();

		return holder;
	}

	class MyViewHolder extends RecyclerView.ViewHolder {
		TextView tweetUserScreenName, tweetUserProfileName;
		CircleImageView tweetUserProfilePicture;

		public MyViewHolder(View itemView) {
			super(itemView);

			tweetUserProfilePicture = (CircleImageView) itemView
					.findViewById(R.id.tweetUserProfilePicture);
			tweetUserScreenName = (TextView) itemView
					.findViewById(R.id.tweetUserScreenName);
			tweetUserProfileName = (TextView) itemView
					.findViewById(R.id.tweetUserProfileName);
		}
	}

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

