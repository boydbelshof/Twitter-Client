package nl.northcreek.twitazia.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import nl.northcreek.twitazia.TwitterClient;
import nl.northcreek.twitazia.model.Model;
import nl.northcreek.twitazia.model.Tweet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.os.AsyncTask;
import android.util.Log;

public class Populate extends AsyncTask<Void, Void, String> {
	private final FetchDataListener listener;
	private String msg;
	private Context context;
	private static Bitmap userProfilePicture;
	private Model model;
	private TwitterClient app;

	public Populate(FetchDataListener listener, Context context) {
		this.listener = listener;
		this.context = context;
	}

	@Override
	protected String doInBackground(Void... params) {

		app = (TwitterClient) context;
		model = app.getModel();
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {
			InputStream is = context.getAssets().open("searchresults",
					AssetManager.ACCESS_BUFFER);
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
			try {
				throw e;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		// Log.d("JSON TO STRING", sb.toString());
		return sb.toString();

	}

	@Override
	protected void onPostExecute(String json) {
		if (json == null) {
			if (listener != null)
				listener.onFetchFailure(msg);
			return;
		}
		try {
			// convert json string to json object
			JSONObject jsonObject = new JSONObject(json);
			JSONArray tempOBJ = jsonObject.getJSONArray("statuses");
			// create Tweets list
			String time = "";
			List<Tweet> tweets = model.getTweets();

			for (int i = 0; i < tempOBJ.length(); i++) {
				JSONObject tweetOBJ = tempOBJ.getJSONObject(i);
				//Log.d("JSONOBJ", "" + tweetOBJ.toString());
				JSONObject userOBJ = tweetOBJ.getJSONObject("user");
				//Log.d("JSONOBJ", "" + userOBJ.toString());
				Tweet tweet = new Tweet();
				tweet.setUserProfileName(userOBJ.getString("name"));
				tweet.setUserName(userOBJ.getString("screen_name"));
				tweet.setText(tweetOBJ.getString("text"));
//				String src = tweetOBJ.getString("profile_image_url");
//				tweet.setUserProfilePicture(getBitmapFromURL(src));
//				getTimeDifference(tweetOBJ.getString("created_at"), time);
//				tweet.setTimeAgo(time);

				System.out.println(tweetOBJ.getString("text"));
			//	Log.d("JSON", "json response:" + json);
				// add the tweet to tweets list
				tweets.add(tweet);

			}

			// notify the activity that fetch data has been complete
			if (listener != null)
				listener.onFetchComplete(tweets);
		} catch (JSONException e) {
			msg = "Invalid response";
			if (listener != null)
				listener.onFetchFailure(msg);
			return;}
//		} catch (java.text.ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	private void getTimeDifference(String pDate, String time)
			throws java.text.ParseException {
		int diffInDays = 0;
		SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("GMT + 2.00"));
		Calendar c = Calendar.getInstance();
		String formattedDate = format.format(c.getTime());

		Date d1 = null;
		Date d2 = null;
		try {

			d1 = format.parse(formattedDate);
			d2 = format.parse(pDate);
			long diff = d1.getTime() - d2.getTime();

			diffInDays = (int) (diff / (1000 * 60 * 60 * 24));
			if (diffInDays > 0) {
				if (diffInDays == 1) {
					time = diffInDays + " day ago";
				} else {
					time = diffInDays + " days ago";
				}
			} else {
				int diffHours = (int) (diff / (60 * 60 * 1000));
				if (diffHours > 0) {
					if (diffHours == 1) {
						time = (diffHours + " hr ago");
					} else {
						time = (diffHours + " hrs ago");
					}
				} else {

					int diffMinutes = (int) ((diff / (60 * 1000) % 60));
					if (diffMinutes == 1) {
						time = (diffMinutes + " min ago");
					} else {
						time = (diffMinutes + " mins ago");
					}

				}
			}

		} catch (ParseException e) {
			// System.out.println("Err: " + e);
			e.printStackTrace();
		}

	}

	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			userProfilePicture = BitmapFactory.decodeStream(input);
			return userProfilePicture;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
