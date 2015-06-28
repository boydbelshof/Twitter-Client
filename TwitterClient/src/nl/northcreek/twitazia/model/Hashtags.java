package nl.northcreek.twitazia.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Hashtags extends Entity {

	private String text;
	
	public Hashtags(JSONObject entityObject) {
		super(entityObject);
		try {
			this.text = entityObject.getString("text");
		} catch (JSONException e) {
			Log.i("JSONException", "Hashtag fout");
		}
	}

	public String getText(){
		return text;
	}
}
