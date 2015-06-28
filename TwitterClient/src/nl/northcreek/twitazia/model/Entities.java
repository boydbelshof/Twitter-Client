/**
 * @author Boyd
 * Het model van de Entities, hier word alles gergeld
 */
package nl.northcreek.twitazia.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Entities {

	private ArrayList<Entity> entitiesList;

	/**
	 * Leest alles uit het JSON Object en zet het in aparte objecten zodat dit
	 * vervolgens weer makkelijk opgehaald kan worden.
	 * 
	 * @param entities
	 *            Is het JSON Object waar alles in zit
	 */
	public Entities(JSONObject entities) {
		entitiesList = new ArrayList<Entity>();

		try {
			JSONArray urls = entities.getJSONArray("urls");
			for (int counter = 0; counter < urls.length(); counter++) {
				JSONObject obj = urls.getJSONObject(counter);
				this.entitiesList.add(new URLs(obj));
			}
		} catch (JSONException e) {
			Log.e("JSONException", "Mislukt");
		}

		try {
			JSONArray media = entities.getJSONArray("media");

			for (int counter = 0; counter < media.length(); counter++) {
				JSONObject obj = media.getJSONObject(counter);
				this.entitiesList.add(new Media(obj));
			}
		} catch (JSONException e) {
			Log.e("JSONException", "Mislukt");
		}

		try {
			JSONArray user_mentions = entities.getJSONArray("user_mentions");
			for (int counter = 0; counter < user_mentions.length(); counter++) {
				JSONObject obj = user_mentions.getJSONObject(counter);
				this.entitiesList.add(new Mentions(obj));
			}

		} catch (JSONException e) {
			Log.e("JSONException", "Mislukt");
		}

		try {
			JSONArray hashtags = entities.getJSONArray("hashtags");
			for (int counter = 0; counter < hashtags.length(); counter++) {
				JSONObject obj = hashtags.getJSONObject(counter);
				this.entitiesList.add(new Hashtags(obj));
			}
		} catch (JSONException e) {
			Log.e("JSONException", "Mislukt");
		}

	}

	
	/**
	 * Kijk in de lijst met alle entitys of hier Hashtags inzitten, deze plaatst hij vervolgens in een lege lijst en deze returnt hij uiteindelijk
	 * met alle values erin.
	 * @return een ArrayList met alle Hashtags
	 */
	public ArrayList<Hashtags> getHashtags() {
		ArrayList<Hashtags> hashtagsList = new ArrayList<Hashtags>();
		for (Entity entity : entitiesList) {
			if (entity instanceof Hashtags) {
				hashtagsList.add((Hashtags) entity);
			}
		}
		return hashtagsList;
	}

	/**
	 * Kijk in de lijst met alle entitys of hier URLS inzitten, deze plaatst hij vervolgens in een lege lijst en deze returnt hij uiteindelijk
	 * met alle values erin.
	 * @return een ArrayList met alle URLs
	 */
	public ArrayList<URLs> getURLs() {
		ArrayList<URLs> urlsList = new ArrayList<URLs>();
		for (Entity entity : entitiesList) {
			if (entity instanceof URLs) {
				urlsList.add((URLs) entity);
			}
		}
		return urlsList;
	}
	
	/**
	 * Kijk in de lijst met alle entitys of hier Media inzit, deze plaatst hij vervolgens in een lege lijst en deze returnt hij uiteindelijk
	 * met alle values erin.
	 * @return een ArrayList met alle Media
	 */
	public ArrayList<Media> getMedia() {
		ArrayList<Media> mediaList = new ArrayList<Media>();
		for (Entity entity : entitiesList) {
			if (entity instanceof Media) {
				mediaList.add((Media) entity);
			}
		}
		return mediaList;
	}
	
	/**
	 * Kijk in de lijst met alle entitys of hier Mentions inzitten, deze plaatst hij vervolgens in een lege lijst en deze returnt hij uiteindelijk
	 * met alle values erin.
	 * @return een ArrayList met alle Mentions
	 */
	public ArrayList<Mentions> getMentions() {
		ArrayList<Mentions> mentionsList = new ArrayList<Mentions>();
		for (Entity entity : entitiesList) {
			if (entity instanceof Mentions) {
				mentionsList.add((Mentions) entity);
			}
		}
		return mentionsList;
	}
}
