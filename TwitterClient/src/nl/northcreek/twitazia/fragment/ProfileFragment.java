package nl.northcreek.twitazia.fragment;

import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;

import nl.northcreek.twitazia.CircleImageView;
import nl.northcreek.twitazia.R;
import nl.northcreek.twitazia.TwitterClient;
import nl.northcreek.twitazia.model.Model;
import nl.northcreek.twitazia.model.User;
import nl.northcreek.twitazia.network.User_Get_Current;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Dit is de timeline van de gebruiker, hier kan hij alle recente tweets zien.
 * 
 * @author boyd
 *
 */
public class ProfileFragment extends Fragment implements Observer {
	private Model model;
	private TwitterClient app;
	private CircleImageView userProfilePicture;
	private TextView totalTweetsData, totalFollowersData, totalFollowingData,
			userName, userScreenName;
	private User currentUser;
	public ProfileFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (TwitterClient) getActivity().getApplicationContext();
		model = app.getModel();
		currentUser = model.getCurrentUser().get(0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_profile, container,
				false);
		
		totalTweetsData = (TextView) rootView.findViewById(R.id.countTweets);
		totalFollowersData = (TextView) rootView
				.findViewById(R.id.countFollowers);
		totalFollowingData = (TextView) rootView
				.findViewById(R.id.countFollowing);
		userName = (TextView) rootView.findViewById(R.id.txtUsername);
		userScreenName = (TextView) rootView.findViewById(R.id.txtUserScreenName);
		userProfilePicture = (CircleImageView) rootView
				.findViewById(R.id.tweetUserPF);
		userName.setText(currentUser.getName());
		userScreenName.setText("@" + currentUser.getScreen_name());
	//	totalTweetsData.setText(currentUser.getStatuses_count());
//		totalFollowersData.setText(currentUser.getFollowers_count());
//		totalFollowingData.setText(currentUser.getFriends_count());
		new DownloadImageTask(userProfilePicture).execute(currentUser.getProfile_image_url());
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
	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		
	}
}
