package nl.northcreek.twitazia.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Entity {


	private JSONArray hashtags;
	private JSONArray urls;
	private JSONArray symbols;
	private JSONArray user_mentions;
	private JSONArray media;

	private int beginPosition, endPosition;

	public Entity(JSONObject entityObject) {
		try {
			JSONArray indices = entityObject.getJSONArray("indices");
			beginPosition = indices.getInt(0);
			endPosition = indices.getInt(1);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public int getBeginPosition() {
		return beginPosition;
	}

	public void setBeginPosition(int beginPosition) {
		this.beginPosition = beginPosition;
	}

	public int getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(int endPosition) {
		this.endPosition = endPosition;
	}

}
