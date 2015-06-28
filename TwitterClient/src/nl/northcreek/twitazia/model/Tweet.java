package nl.northcreek.twitazia.model;

import java.util.ArrayList;
import java.util.Observable;

import nl.northcreek.twitazia.TwitterClient;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * De tweetclass, aan deze class geef je een JSON Object mee, deze zet hij vervolgens om in kleinere Objecten voor bijvoorbeel de user en de entities, dit 
 * word daarin verder ontleed. Ook slaat hij hier al een aantal dingen als de favorite count en retweet count op. Onderin word de text spannable gemaakt.
 * @author boyd
  */
public class Tweet extends Observable {

	private String created_at;
	private int id;
	private String id_str;
	private Spannable text;
	private User user;
	private Context context;
	private Entities entities;
	private int favorite_count;
	private int retweet_count;
	private Model model;

	public Tweet(JSONObject TweetObject) {
		JSONObject userObject;
		JSONObject entityObject;
		try {
			userObject = TweetObject.getJSONObject("user");
			user = new User(userObject);
			entityObject = TweetObject.getJSONObject("entities");
			entities = new Entities(entityObject);
			created_at = TweetObject.getString("created_at");
			id = TweetObject.getInt("id");
			id_str = TweetObject.getString("id_str");
			favorite_count = TweetObject.getInt("favorite_count");
			retweet_count = TweetObject.getInt("retweet_count");
			text = spanText(TweetObject.getString("text"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		TwitterClient app = (TwitterClient) model.context
				.getApplicationContext();
		model = app.getModel();

	}

	private Spannable spanText(String text) {
		Spannable WordtoSpan = null;

		ArrayList<URLs> urls = entities.getURLs();
		if (urls.size() == 0 || urls == null) {
			WordtoSpan = new SpannableString(text);
		} else {
			for (int j = 0; j < urls.size(); j++) {
				URLs url = urls.get(j);
				Log.i("Tweet.java", url.getUrl());
				int begin = url.getindice(0);
				int end = url.getindice(1);

				WordtoSpan = new SpannableString(text);

				ClickableSpan clickableSpan = new ClickableSpan() {

					@Override
					public void onClick(View widget) {
						TextView tv = (TextView) widget;
						Spanned s = (Spanned) tv.getText();
						int start = s.getSpanStart(this);
						int end = s.getSpanEnd(this);
						Spannable url = (Spannable) s.subSequence(start, end);
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse(url.toString()));
						context.startActivity(i);

					}
				};

				WordtoSpan.setSpan(clickableSpan, begin, end,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			}

		}

		ArrayList<Hashtags> hashtags = entities.getHashtags();

		if (hashtags != null) {
			for (int h = 0; h < hashtags.size(); h++) {
				Hashtags hashtag = hashtags.get(h);
				int begin = hashtag.getindice(0);
				int end = hashtag.getindice(1);
				WordtoSpan.setSpan(new ForegroundColorSpan(Color.CYAN), begin,
						end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}

		ArrayList<Mentions> userMentions = entities.getMentions();

		if (userMentions != null) {
			for (int h = 0; h < userMentions.size(); h++) {
				Mentions userMention = userMentions.get(h);
				int begin = userMention.getindice(0);
				int end = userMention.getindice(1);
				WordtoSpan.setSpan(new ForegroundColorSpan(Color.CYAN), begin,
						end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}

		return WordtoSpan;

	}

	public Spannable getText() {
		return text;
	}

	public void setText(Spannable text) {
		this.text = text;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getId_str() {
		return id_str;
	}

	public void setId_str(String id_str) {
		this.id_str = id_str;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Entities getEntities() {
		return entities;
	}

	public void setEntities(Entities entities) {
		this.entities = entities;
	}

	public int getFavorite_count() {
		return favorite_count;
	}

	public void setFavorite_count(int favorite_count) {
		this.favorite_count = favorite_count;
	}

	public int getRetweet_count() {
		return retweet_count;
	}

	public void setRetweet_count(int retweet_count) {
		this.retweet_count = retweet_count;
	}
}
