package nl.northcreek.twitazia.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Observable;

import android.graphics.Bitmap;

public class Tweet extends Observable {
	private ArrayList<Contributor> contributor;
	private Coordinate coordinates;
	private String created_at;
	private Array[] hastags;
	private Array[] urls;
	private Array[] userMentions;
	private int favoriteCount;
	private boolean favorited;
	private long id;
	private String id_str;
	private String in_reply_to_screen_name;
	private long in_reply_to_status_id;
	private String in_reply_to_status_id_str;
	private String lang;
	private Place place;
	private boolean possibly_sensitive;
	private int retweet_count;
	private boolean retweeted;
	private String source;

	private boolean truncated;
	private User user;
	private boolean withheld_copyright;
	private ArrayList<String> withheld_in_countries;

	private String text;
	private String userName;
	private String userProfileName;
	private Bitmap userProfilePicture;
	private String timeAgo;

	public Tweet(String userName, String userProfileName, String text,
			Bitmap userProfilePicture, String timeAgo) {
		this.setText(text);
		this.setUserProfileName(userProfileName);
		this.setUserName(userName);
		this.setUserProfilePicture(userProfilePicture);
		this.setTimeAgo(timeAgo);
	}

	public Tweet() {
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserProfileName() {
		return userProfileName;
	}

	public void setUserProfileName(String userProfileName) {
		this.userProfileName = userProfileName;
	}

	public Bitmap getUserProfilePicture() {
		return userProfilePicture;
	}

	public void setUserProfilePicture(Bitmap userProfilePicture) {
		this.userProfilePicture = userProfilePicture;
	}

	public String getTimeAgo() {
		return timeAgo;
	}

	public void setTimeAgo(String timeAgo) {
		this.timeAgo = timeAgo;
	}

}
