package nl.northcreek.twitazia.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Entity {
	
	// Entity class
	
	private JSONArray hashtags;
	private JSONArray urls;
	private JSONArray symbols;
	private JSONArray user_mentions;
	private JSONArray media;

	public Entity(JSONObject entityObject) {
		try {
			hashtags = entityObject.getJSONArray("hashtags");
			urls = entityObject.getJSONArray("urls");
			symbols = entityObject.getJSONArray("symbols");
			user_mentions = entityObject.getJSONArray("user_mentions");
			media = entityObject.getJSONArray("media");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	public JSONArray getHashtags() {
		return hashtags;
	}

	public JSONArray getUrls() {
		return urls;
	}

	public JSONArray getSymbols() {
		return symbols;
	}

	public JSONArray getUser_mentions() {
		return user_mentions;
	}

	public JSONArray getMedia() {
		return media;
	}
	
	

}
