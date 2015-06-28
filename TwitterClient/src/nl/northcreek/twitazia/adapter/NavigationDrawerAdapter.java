package nl.northcreek.twitazia.adapter;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import nl.northcreek.twitazia.CircleImageView;
import nl.northcreek.twitazia.R;
import nl.northcreek.twitazia.TwitterClient;
import nl.northcreek.twitazia.drawer.NavDrawerItem;
import nl.northcreek.twitazia.model.Model;
import nl.northcreek.twitazia.model.User;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class NavigationDrawerAdapter extends
		RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
	List<NavDrawerItem> data = Collections.emptyList();
	private LayoutInflater inflater;
	private Context context;
	private TwitterClient app;
	private Model model;

	public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.data = data;
	}

	public void delete(int position) {
		data.remove(position);
		notifyItemRemoved(position);
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
		MyViewHolder holder = new MyViewHolder(view);
		app = (TwitterClient) context.getApplicationContext();
		model = app.getModel();
		return holder;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		NavDrawerItem current = data.get(position);
		holder.title.setText(current.getTitle());
		holder.icon.setImageResource(current.getImageIcon());
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	class MyViewHolder extends RecyclerView.ViewHolder {
		TextView title, totalTweetsData, totalFollowersData,
				totalFollowingData, userName, userScreenName;
		ImageView icon;
		public MyViewHolder(View itemView) {
			super(itemView);
			title = (TextView) itemView.findViewById(R.id.title);
			icon = (ImageView) itemView.findViewById(R.id.drawerIcon);
			

		}
	}

}