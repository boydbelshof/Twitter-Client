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
			JSONObject jsonObject = new JSONObject(json);
			JSONArray tempOBJ = jsonObject.getJSONArray("statuses");
			for (int i = 0; i < tempOBJ.length(); i++) {
				JSONObject tweetOBJ = tempOBJ.getJSONObject(i);
				Tweet tweet = new Tweet(tweetOBJ);
				model.addTweet(tweet);
			}

		} catch (JSONException e) {
			msg = "Invalid response";
			if (listener != null)
				listener.onFetchFailure(msg);
			return;
		}

	}

}
