package nl.northcreek.twitazia.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Mentions extends Entity {

	private String id_str;
	private String screen_name;
	private String name;

	public Mentions(JSONObject entityObject) {
		super(entityObject);
		try {
			this.id_str = entityObject.getString("id_str");
			this.screen_name = entityObject.getString("screen_name");
			this.name = entityObject.getString("name");
		} catch (JSONException e) {
			Log.i("JSONException", "Mentions");
		}

	}

	public String getScreen_name() {
		return screen_name;
	}

	public String getId_str() {
		return id_str;
	}

	public String getName() {
		return name;
	}
}
