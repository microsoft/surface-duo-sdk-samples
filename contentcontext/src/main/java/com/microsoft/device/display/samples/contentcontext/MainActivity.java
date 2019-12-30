package com.microsoft.device.display.samples.contentcontext;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.microsoft.device.display.samples.utils.ScreenHelper;


public class MainActivity extends AppCompatActivity implements ItemsListFragment.OnItemSelectedListener {

	private static final String TAG = MainActivity.class.getSimpleName();
	public static final String POSITION_KEY = "position";

	private ScreenHelper screenHelper;
	private int currentSelectedPosition;
	// Which fragment should be shown on the top
	private String fragmentToBeShownOnInit = "";
	private View single;
	private View dual_portrait;
	private View dual_landscape;
	private boolean isDuo;
	private boolean dualMode = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(null);

		screenHelper = new ScreenHelper();
		isDuo = screenHelper.initialize(this);
		single = getLayoutInflater().inflate(R.layout.activity_items_single_portrait, null);
		dual_portrait = getLayoutInflater().inflate(R.layout.activity_double_portrail, null);
		dual_landscape = getLayoutInflater().inflate(R.layout.activity_double_landscape, null);
		setupLayout();
	}

	@Override
	public void onItemSelected(Item item, int position) {
		currentSelectedPosition = position;
		if (dualMode) {
			// Showing ItemDetailFragment on the right screen when the app is in spanned mode
			showFragment(ItemDetailFragment.newInstance(item), R.id.master_detail, false);
		} else {
			showFragment(ItemDetailFragment.newInstance(item), R.id.master_single, false);
			showBackOnActionBar(true);
		}
	}

	@Override
	public void onInit(Item item, int position) {
		if (dualMode) {
			// Showing ItemDetailFragment on the right screen when the app is in spanned mode
			showFragment(ItemDetailFragment.newInstance(item), R.id.master_detail, false);
		} else {
			if(fragmentToBeShownOnInit.equals(ItemDetailFragment.class.getName())) {
				showFragment(ItemDetailFragment.newInstance(item), R.id.master_single, false);
				showBackOnActionBar(true);
			}
		}
	}

	public void useSingleMode(int rotation) {
		Log.d(TAG,"useSingleScreenMode " + " position = " + currentSelectedPosition);
		// Setting layout for single portrait
		dualMode = false;
		setContentView(single);
		// If app is in landscape mode , detail is always shown on the top.
		if(rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
			if (currentSelectedPosition == -1) {
				currentSelectedPosition = 0;
			}
			fragmentToBeShownOnInit = ItemDetailFragment.class.getName();
		}
		if(currentSelectedPosition != -1) {
			showFragment(ItemsListFragment.newInstance(currentSelectedPosition), R.id.master_single, true);
		} else {
			showFragment(ItemsListFragment.newInstance(), R.id.master_single, true);
		}
	}

	public void useDualMode(int rotation) {
		Log.d(TAG,"useDualScreenMode " + " position = " + currentSelectedPosition);
		dualMode = true;
		showBackOnActionBar(false);
		if(currentSelectedPosition == -1) {
			currentSelectedPosition = 0;
		}
		switch (rotation) {
			case Surface.ROTATION_90:
			case Surface.ROTATION_270:
				// Setting layout for double landscape
				setContentView(dual_landscape);
				break;
			default:
				// Setting layout for double portrait
				setContentView(dual_portrait);
				break;
		}
		if(currentSelectedPosition != -1) {
			showFragment(ItemsListFragment.newInstance(currentSelectedPosition), R.id.master_dual, true);
		}
	}

	private void setupLayout() {
		final int rotation = ScreenHelper.getRotation(this);
		if(isDuo) {
			if (screenHelper.isDualMode()) {
				useDualMode(rotation);
			} else {
				useSingleMode(rotation);
			}
		} else {
			useSingleMode(rotation);
		}
	}

	@Override
	public void onConfigurationChanged(@NonNull Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setupLayout();
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

		if(!dualMode && !isInit) {
			fragmentTransaction.addToBackStack(fragment.getClass().getName());
		}
		fragmentTransaction.commit();
	}


	@Override
	public void onBackPressed() {
		if(getSupportFragmentManager().getBackStackEntryCount() == 0 || dualMode) {
			super.onBackPressed();
		} else{
			getSupportFragmentManager().popBackStack();
			getSupportFragmentManager().executePendingTransactions();
			if(!dualMode) {
				// Do not show back on the actionbar when current fragment is ItemsListFragment
				final Fragment showFragment = getSupportFragmentManager().findFragmentById(R.id.master_single);
				if(showFragment != null && showFragment instanceof ItemsListFragment) {
					showBackOnActionBar(false);
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

	private void showBackOnActionBar(boolean show) {
		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(show);
		actionbar.setHomeButtonEnabled(show);
	}
}