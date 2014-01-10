package com.fenixedu.freeroomsfenix;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class TabListenerImpl<T extends Fragment> implements TabListener {

	private android.support.v4.app.Fragment fragment;
	private final SherlockFragmentActivity activity;
	private final String tag;
	private final Class<T> clazz;

	public TabListenerImpl(SherlockFragmentActivity activity, String tag,
			Class<T> clazz) {
		this.activity = activity;
		this.tag = tag;
		this.clazz = clazz;
	}

	public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {

	}

	public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
		// Check if the fragment is already initialized
		if (fragment == null) {
			// If not, instantiate and add it to the activity
			fragment = SherlockFragment.instantiate(activity, clazz.getName());
			fragmentTransaction.add(android.R.id.content, fragment, tag);
		} else {
			// If it exists, simply attach it in order to show it
			fragmentTransaction.attach(fragment);
		}
	}

	public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {
		if (fragment != null) {
			// Detach the fragment, because another one is being attached
			fragmentTransaction.detach(fragment);
		}
	}

}
