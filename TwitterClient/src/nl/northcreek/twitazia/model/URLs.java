package nl.northcreek.twitazia.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class URLs extends Entity {

	private String url;
	private String display_url;

	public URLs(JSONObject entityObject) {
		super(entityObject);
		try {
			this.url = entityObject.getString("url");
			this.display_url = entityObject.getString("display_url");
		} catch (JSONException e) {
			Log.i("JSONException", "bij URL");
		}
	}

	public String getUrl() {
		return url;
	}

	public String getDisplay_url() {
		return display_url;
	}
}
