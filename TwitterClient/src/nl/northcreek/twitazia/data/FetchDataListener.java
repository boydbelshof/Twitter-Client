package nl.northcreek.twitazia.data;

import java.util.List;

import nl.northcreek.twitazia.model.Tweet;

public interface FetchDataListener {
    public void onFetchComplete(List<Tweet> tweets);
    public void onFetchFailure(String msg);
}
