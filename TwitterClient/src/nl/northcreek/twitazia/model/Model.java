package nl.northcreek.twitazia.model;

import java.util.ArrayList;
import java.util.Observable;

import nl.northcreek.twitazia.model.Tweet;

public class Model extends Observable {
	
	
	private ArrayList<Tweet> tweets;
	
    public Model(){
        this.tweets = new ArrayList<Tweet>();
    }

    public void addTweet(Tweet tweet){
        this.tweets.add(tweet);
    }

    public ArrayList<Tweet> getTweets(){
        return this.tweets;
    }

    public Tweet getTweetsAtPosition(int id){
        return tweets.get(id);
    }
}