package com.microsoft.device.display.samples.masterdetail;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Surface;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.microsoft.device.display.samples.masterdetail.ItemsListFragment.OnItemSelectedListener;
import com.microsoft.device.display.samples.utils.DualScreenDetectionListener;
import com.microsoft.device.display.samples.utils.DualScreenHelper;

public class MainActivity extends AppCompatActivity implements OnItemSelectedListener, DualScreenDetectionListener {

	private static final String TAG = "MainActivity";
	public static final String FRAGMENT_KEY = "fragment";
	public static final String POSITION_KEY = "position";

	private boolean isTwoPane = false;
	private DualScreenHelper mDualScreenHelper;
	private int currentSelectedPosition;
	//Which fragment should be shown on the top
	private String currentFragment = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(null);
		Log.d(TAG,"onCreate");
		currentSelectedPosition = -1;
		if(savedInstanceState != null) {
			currentSelectedPosition = savedInstanceState.getInt(POSITION_KEY, -1);
			currentFragment = savedInstanceState.getString(FRAGMENT_KEY,"");
			Log.d(TAG,"isTwoPane = " + isTwoPane);
			Log.d(TAG,"position = " + currentSelectedPosition);
			Log.d(TAG,"fragment = " + currentFragment);
		}
		mDualScreenHelper = new DualScreenHelper();
		// // Adds a listener for layout changes (single or spanned)
		boolean isDuo = mDualScreenHelper.initialize(this, getWindow().getDecorView().getRootView());
		if(!isDuo) {
			useSingleScreenMode(DualScreenHelper.getRotation(this));
		} else {
			mDualScreenHelper.addListener(this);
		}
	}

	@Override
	public void onItemSelected(Item item, int position) {
		currentSelectedPosition = position;
		if (isTwoPane) {
			// Showing ItemDetailFragment on the right screen when the app is in spanned mode
			showFragment(ItemDetailFragment.newInstance(item), R.id.detail, false);
		} else {
			showFragment(ItemDetailFragment.newInstance(item), R.id.master_single, false);
		}
	}

	@Override
	public void onInit(Item item, int position) {
		if (isTwoPane) {
			// Showing ItemDetailFragment on the right screen
			showFragment(ItemDetailFragment.newInstance(item), R.id.detail, false);
		} else {
			if(currentFragment.equals(ItemDetailFragment.class.getName())) {
				showFragment(ItemDetailFragment.newInstance(item), R.id.master_single, false);
			}
		}
	}

	@Override
	public void useSingleScreenMode(int rotation) {
		Log.d(TAG,"useSingleScreenMode " + " position = " + currentSelectedPosition);
		// Setting layout for single portrait
		setContentView(R.layout.activity_items_single_portrait);
		// If app is in landscape mode , detail is always shown on the top.
		if(rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
			if (currentSelectedPosition == -1) {
				currentSelectedPosition = 0;
			}
			currentFragment = ItemDetailFragment.class.getName();
		}
		if(currentSelectedPosition != -1) {
			showFragment(ItemsListFragment.newInstance(currentSelectedPosition), R.id.master_single, true);
		} else {
			showFragment(ItemsListFragment.newInstance(), R.id.master_single, true);
		}
		isTwoPane = false;
	}

	@Override
	public void useDualScreenMode(int rotation, Rect screenRect1, Rect screenRect2) {
		Log.d(TAG,"useDualScreenMode " + " position = " + currentSelectedPosition);
		switch (rotation) {
			case Surface.ROTATION_90:
			case Surface.ROTATION_270:
				// Setting layout for double landscape
				useSingleScreenMode(rotation);
				break;
			default:
				// Setting layout for double portrait
				setContentView(R.layout.activity_items_dual_portrait);
				if (currentSelectedPosition == -1) {
					currentSelectedPosition = 0;
				}
				if(currentSelectedPosition != -1) {
					// Showing ListView on the left screen
					showFragment(ItemsListFragment.newInstance(currentSelectedPosition), R.id.master_dual, true);
				}
				isTwoPane = true;

				break;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		final FragmentManager fragmentManager = getSupportFragmentManager();
		if(!isTwoPane) {
			if(fragmentManager.getBackStackEntryCount() > 0) {
				String theLastFragment = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
				currentFragment = theLastFragment;
			} else {
				currentFragment = "";
			}
		}

		if(currentSelectedPosition != -1) {
			outState.putInt(POSITION_KEY, currentSelectedPosition);
			outState.putString(FRAGMENT_KEY, currentFragment);
		}
		
		super.onSaveInstanceState(outState);
	}

	private void showFragment(Fragment fragment, int id, boolean isInit) {
		final FragmentManager fragmentManager = getSupportFragmentManager();
		final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		final Fragment showFragment = fragmentManager.findFragmentById(id);
		if (!fragment.isAdded()) {
			if(showFragment == null) {
				fragmentTransaction.add(id, fragment);
			} else {
				fragmentTransaction.hide(showFragment).add(id, fragment);
			}
		} else {
			fragmentTransaction.hide(showFragment).show(fragment);
		}

		if(!isTwoPane) {
			if(!isInit) {
				fragmentTransaction.addToBackStack(fragment.getClass().getName());
			}
			// Showing back on the actionbar
			if(fragment instanceof ItemDetailFragment) {
				ActionBar actionbar = getSupportActionBar();
				actionbar.setDisplayHomeAsUpEnabled(true);
				actionbar.setHomeButtonEnabled(true);
			}
		}
		fragmentTransaction.commit();
	}


	@Override
	public void onBackPressed() {
		if(getSupportFragmentManager().getBackStackEntryCount() == 0 || isTwoPane) {
			super.onBackPressed();
		} else{
			getSupportFragmentManager().popBackStack();
			getSupportFragmentManager().executePendingTransactions();
			if(!isTwoPane) {
				// Do not show back on the actionbar when current fragment is ItemsListFragment
				final Fragment showFragment = getSupportFragmentManager().findFragmentById(R.id.master_single);
				if(showFragment != null && showFragment instanceof ItemsListFragment) {
					getSupportActionBar().setDisplayHomeAsUpEnabled(false);
				}
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG,"onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG,"onPause");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG,"onDestroy");
	}
}