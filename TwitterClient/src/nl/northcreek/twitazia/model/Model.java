package nl.northcreek.twitazia.model;

import java.util.ArrayList;
import java.util.Observable;

import nl.northcreek.twitazia.model.Tweet;

public class Model extends Observable {
	private String oAuthVerifier;
	private String oAuthToken;

	public void update() {
		notifyObservers();
		setChanged();
		super.setChanged();
	}

	private ArrayList<Tweet> tweets;

	public Model() {
		this.tweets = new ArrayList<Tweet>();
	}

	public void addTweet(Tweet tweet) {
		this.tweets.add(tweet);
	}

	public ArrayList<Tweet> getTweets() {
		return this.tweets;
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