package nl.northcreek.twitazia.model;

import java.util.Observable;

import org.json.JSONException;
import org.json.JSONObject;

public class Tweet extends Observable {
	
	private String created_at;
	private int id;
	private String id_str;
	private String text;
	User user;
	Entity entity;
	private int favorite_count;
	private int retweet_count;

	public Tweet(JSONObject TweetObject){
		JSONObject userObject;
		JSONObject entityObject;
		try {
			text = TweetObject.getString("text");
			userObject = TweetObject.getJSONObject("user");
			user = new User(userObject);
			entityObject = TweetObject.getJSONObject("entities");
			entity = new Entity(entityObject);
			created_at = TweetObject.getString("created_at");
			id = TweetObject.getInt("id");
			id_str = TweetObject.getString("id_str");
			favorite_count = TweetObject.getInt("favorite_count");
			retweet_count = TweetObject.getInt("retweet_count");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
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
