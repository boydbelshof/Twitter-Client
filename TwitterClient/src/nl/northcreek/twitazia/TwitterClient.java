package nl.northcreek.twitazia;

import nl.northcreek.twitazia.model.Model;
import android.app.Application;

public class TwitterClient extends Application {

	private Model model;

	@Override
	public void onCreate() {
		model = new Model();
		super.onCreate();
	}

	public Model getModel() {
		return model;
	}

}

