package nl.northcreek.twitazia.model;

import java.util.ArrayList;
import java.util.Observable;

import android.content.Context;

public class Model extends Observable {
	private String oAuthVerifier;
	private String oAuthToken;
	public static Context context;

	public void update() {
		notifyObservers();
		setChanged();
		super.setChanged();
	}

	private ArrayList<Tweet> tweets;
	private ArrayList<User> users;
	private ArrayList<User> currentUser;

	public Model(Context context) {
		this.context = context;
		this.tweets = new ArrayList<Tweet>();
		this.users = new ArrayList<User>();
		this.currentUser = new ArrayList<User>();
		
	}
	public void addCurrentUser(User user) {
		this.currentUser.add(user);
	}
	public void addTweet(Tweet tweet) {
		this.tweets.add(tweet);
	}
	
	public void addUser(User user) {
		this.users.add(user);
	}

	public ArrayList<Tweet> getTweets() {
		return this.tweets;
	}
	public ArrayList<User> getCurrentUser() {
		return this.currentUser;
	}
	public ArrayList<User> getUsers() {
		return this.users;
	}

	public Tweet getTweetsAtPosition(int id) {
		return tweets.get(id);
	}
	
	public void clear() {
		tweets.clear();
	}

	public String getoAuthVerifier() {
		return oAuthVerifier;
	}

	public void setoAuthVerifier(String oAuthVerifier) {
		this.oAuthVerifier = oAuthVerifier;
	}

	public String getoAuthToken() {
		return oAuthToken;
	}

	public void setoAuthToken(String oAuthToken) {
		this.oAuthToken = oAuthToken;
	}

}