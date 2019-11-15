package com.microsoft.device.display.samples.contentcontext;

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

import com.microsoft.device.display.samples.contentcontext.eos.DualScreenDetectionListener;
import com.microsoft.device.display.samples.contentcontext.eos.DualScreenHelper;


public class MainActivity extends AppCompatActivity implements ItemsListFragment.OnItemSelectedListener, DualScreenDetectionListener {

	private static final String TAG = "MainActivity";
	public static final String FRAGMENT_KEY = "fragment";
	public static final String POSITION_KEY = "position";

	private boolean isTwoPane = false;
	private DualScreenHelper mDualScreenHelper;
	private int currentSelectedPosition;
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
		boolean isDuo = mDualScreenHelper.initialize(this, getWindow().getDecorView().getRootView());
		if(!isDuo) {
			useSingleScreenMode(Surface.ROTATION_0);
		} else {
			mDualScreenHelper.addListener(this);
		}

	}

	@Override
	public void onItemSelected(Item item, int position) {
		currentSelectedPosition = position;
		if (isTwoPane) {
			showFragment(ItemDetailFragment.newInstance(item), R.id.detail, false);
		} else {
			showFragment(ItemDetailFragment.newInstance(item), R.id.master_single, false);
		}
	}

	@Override
	public void onInit(Item item, int position) {
		if (isTwoPane) {
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
		setContentView(R.layout.activity_items_single_portrait);
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
		if(currentSelectedPosition == -1) {
			currentSelectedPosition = 0;
		}
		switch (rotation) {
			case Surface.ROTATION_90:
			case Surface.ROTATION_270:
				setContentView(R.layout.activity_double_landscape);
				break;
			default:
				setContentView(R.layout.activity_double_portrail);
				break;
		}
		if(currentSelectedPosition != -1) {
			showFragment(ItemsListFragment.newInstance(currentSelectedPosition), R.id.master_dual, true);
		} else {
			showFragment(ItemsListFragment.newInstance(), R.id.master_dual, true);
		}
		isTwoPane = true;
		// go from span to single
		currentFragment = ItemDetailFragment.class.getName();
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