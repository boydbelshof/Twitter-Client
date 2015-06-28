package nl.northcreek.twitazia.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public abstract class Entity {
	private int[] indices;
	
	public Entity(JSONObject entityObject){
		indices = new int[2];
		try {
			JSONArray indices = entityObject.getJSONArray("indices");
			this.indices[0] = indices.getInt(0);
			this.indices[1] = indices.getInt(1);
		} catch (JSONException e) {
			Log.i("JSONExeption", "Werkt niet");
		}
	}
	
	
	public int getindice(int index){
		if(!(index >= 0 ||index < 2)){
			throw new IllegalArgumentException();
		}
		return indices[index];
	}
}
