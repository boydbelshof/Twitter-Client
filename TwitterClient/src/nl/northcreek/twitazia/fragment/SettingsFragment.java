package nl.northcreek.twitazia.fragment;

import nl.northcreek.twitazia.MainActivity;
import nl.northcreek.twitazia.R;
import nl.northcreek.twitazia.TwitterClient;
import nl.northcreek.twitazia.model.Model;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SettingsFragment extends Fragment {
	private Model model;
	private TwitterClient app;
	public SettingsFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (TwitterClient) getActivity().getApplicationContext();
		model = app.getModel();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.fragment_settings, container,
				false);

		Button logOut = (Button) rootView.findViewById(R.id.buttonLogOut);
		logOut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			model.setoAuthVerifier("");
			model.setoAuthToken("");
			app.getPrefs().edit().clear().apply();
			startActivity(new Intent(app.getApplicationContext(), MainActivity.class));
				
			}
		});
		return rootView;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.menu_main, menu);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}
}
