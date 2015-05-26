package nl.northcreek.twitazia.drawer;

import java.util.ArrayList;
import java.util.List;

import nl.northcreek.twitazia.AccountInfo;
import nl.northcreek.twitazia.CircleImageView;
import nl.northcreek.twitazia.R;
import nl.northcreek.twitazia.TwitterClient;
import nl.northcreek.twitazia.adapter.NavigationDrawerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class FragmentDrawer extends Fragment {

	private RecyclerView recyclerView;
	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;
	private NavigationDrawerAdapter adapter;
	private View containerView;
	private static String[] titles = null;
	private FragmentDrawerListener drawerListener;
	private TwitterClient app;
	private LinearLayoutManager layoutManager;

	public FragmentDrawer() {

	}

	public void setDrawerListener(FragmentDrawerListener listener) {
		this.drawerListener = listener;
	}

	public static List<NavDrawerItem> getData() {
		List<NavDrawerItem> data = new ArrayList<NavDrawerItem>();

		// preparing navigation drawer items
		for (int i = 0; i < titles.length; i++) {
			NavDrawerItem navItem = new NavDrawerItem();
			navItem.setTitle(titles[i]);
			if (titles[i].contains("Timeline")) {
				navItem.setImageIcon(R.drawable.ic_receipt_black_48dp);
				navItem.setShowNotify(true);
			} else if (titles[i].contains("Messages")) {
				navItem.setImageIcon(R.drawable.ic_question_answer_black_48dp);
			} else if (titles[i].contains("Discover")) {
				navItem.setImageIcon(R.drawable.ic_public_black_48dp);
			} else if (titles[i].contains("Activity")) {
				navItem.setImageIcon(R.drawable.ic_people_black_48dp);
			} else if (titles[i].contains("Trending")) {
				navItem.setImageIcon(R.drawable.ic_whatshot_black_48dp);
			} else if (titles[i].contains("Drafts")) {
				navItem.setImageIcon(R.drawable.ic_insert_drive_file_black_48dp);
			} else if (titles[i].contains("Lists")) {
				navItem.setImageIcon(R.drawable.ic_list_black_48dp);
			} else if (titles[i].contains("Settings")) {
				navItem.setImageIcon(R.drawable.ic_settings_applications_black_48dp);
			}
			data.add(navItem);
		}
		return data;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// drawer labels
		titles = getActivity().getResources().getStringArray(
				R.array.nav_drawer_labels);

		app = (TwitterClient) getActivity().getApplicationContext();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflating view layout
		View layout = inflater.inflate(R.layout.fragment_navigation_drawer,
				container, false);
		
		CircleImageView drawerPic = (CircleImageView) layout.findViewById(R.id.tweetUserProfilePicture);
		drawerPic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(app, AccountInfo.class);
				startActivity(intent);
				
			}
		});

		recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
		adapter = new NavigationDrawerAdapter(app, getData());

		recyclerView.addOnItemTouchListener(new RecyclerTouchListener(app,
				recyclerView, new ClickListener() {
					@Override
					public void onClick(View view, int position) {
						drawerListener.onDrawerItemSelected(view, position);

						mDrawerLayout.closeDrawer(containerView);
					}

					@Override
					public void onLongClick(View view, int position) {

					}
				}));

		layoutManager = new LinearLayoutManager(app);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setAdapter(adapter);
		return layout;
	}

	public void setUp(int fragmentId, DrawerLayout drawerLayout,
			final Toolbar toolbar) {
		containerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;
		mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,
				toolbar, R.string.drawer_open, R.string.drawer_close) {
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActivity().invalidateOptionsMenu();
			}
			
			@Override
			public void onConfigurationChanged(Configuration newConfig) {
				super.onConfigurationChanged(newConfig);
				getActivity().invalidateOptionsMenu();
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				getActivity().invalidateOptionsMenu();
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				super.onDrawerSlide(drawerView, slideOffset);
				toolbar.setAlpha(1 - slideOffset / 2);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);
	}

	public static interface ClickListener {
		public void onClick(View view, int position);

		public void onLongClick(View view, int position);
	}

	static class RecyclerTouchListener implements
			RecyclerView.OnItemTouchListener {

		private GestureDetector gestureDetector;
		private ClickListener clickListener;

		public RecyclerTouchListener(Context context,
				final RecyclerView recyclerView,
				final ClickListener clickListener) {
			this.clickListener = clickListener;
			gestureDetector = new GestureDetector(context,
					new GestureDetector.SimpleOnGestureListener() {
						@Override
						public boolean onSingleTapUp(MotionEvent e) {
							return true;
						}

						@Override
						public void onLongPress(MotionEvent e) {
							View child = recyclerView.findChildViewUnder(
									e.getX(), e.getY());
							if (child != null && clickListener != null) {
								clickListener.onLongClick(child,
										recyclerView.getChildPosition(child));
							}
						}
					});
		}

		@Override
		public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

			View child = rv.findChildViewUnder(e.getX(), e.getY());
			if (child != null && clickListener != null
					&& gestureDetector.onTouchEvent(e)) {
				clickListener.onClick(child, rv.getChildPosition(child));
			}
			return false;
		}

		@Override
		public void onTouchEvent(RecyclerView rv, MotionEvent e) {
		}
	}

	public interface FragmentDrawerListener {
		public void onDrawerItemSelected(View view, int position);
	}
}