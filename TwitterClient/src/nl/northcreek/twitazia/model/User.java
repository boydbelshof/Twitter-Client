package nl.northcreek.twitazia.model;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
	private int id;
	private String id_str;
	private String name;
	private String screen_name;
	private String profile_image_url;

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

}
