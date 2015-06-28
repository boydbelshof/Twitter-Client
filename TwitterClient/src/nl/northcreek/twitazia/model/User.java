package nl.northcreek.twitazia.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

public class User {
	private int id;
	private String id_str;
	private String name;
	private String screen_name;
	private String profile_image_url;
	private Bitmap profile_image;

	public User(JSONObject userObject) {
		try {
			id = userObject.getInt("id");
			id_str = userObject.getString("id_str");
			name = userObject.getString("name");
			screen_name = userObject.getString("screen_name");
			profile_image_url = userObject.getString("profile_image_url");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScreen_name() {
		return screen_name;
	}

	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}

	public String getProfile_image_url() {
		return profile_image_url;
	}

	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}

}
