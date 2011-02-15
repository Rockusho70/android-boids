package se.uncle.boidsdemo;

import se.uncle.boidsdemo.example.MetaBallsBoids;
import se.uncle.boidsdemo.example.PlainBoids;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BoidsDemo extends ListActivity {
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		String[] examples = new String[] { "Plain", "Metaballs"};
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, examples));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent;
		switch (position) {
		default:
		case 0: // PlainBoids
			intent = new Intent(this, PlainBoids.class);
			startActivity(intent);
			break;
		case 1: // MetaBalls
			intent = new Intent(this, MetaBallsBoids.class);
			startActivity(intent);
			break;
		}
	}
}