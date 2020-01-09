package com.microsoft.device.display.samples.contentcontext;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.microsoft.device.display.samples.contentcontext.Fragment.BaseFragment;
import com.microsoft.device.display.samples.contentcontext.Fragment.DualLandscape;
import com.microsoft.device.display.samples.contentcontext.Fragment.DualPortrait;
import com.microsoft.device.display.samples.contentcontext.Fragment.SinglePortrait;
import com.microsoft.device.display.samples.utils.ScreenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements BaseFragment.OnItemSelectedListener {

	private ScreenHelper screenHelper;
	private boolean isDuo;
	private Map<String, BaseFragment> fragmentMap;
	private ArrayList<Item> items;
	private int currentSelectedPosition = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		screenHelper = new ScreenHelper();
		isDuo = screenHelper.initialize(this);
		items = Item.getItems();
		fragmentMap = new HashMap<>();

		SinglePortrait singlePortrait = SinglePortrait.newInstance(items);
		singlePortrait.registerOnItemSelectedListener(this);
		fragmentMap.put(SinglePortrait.class.getSimpleName(), singlePortrait);

		DualPortrait dualPortrait = DualPortrait.newInstance(items);
		dualPortrait.registerOnItemSelectedListener(this);
		fragmentMap.put(DualPortrait.class.getSimpleName(), dualPortrait);

		DualLandscape dualLandscape =  DualLandscape.newInstance(items);
		dualLandscape.registerOnItemSelectedListener(this);
		fragmentMap.put(DualLandscape.class.getSimpleName(), dualLandscape);

		setupLayout();
	}

	private void useSingleMode(int rotation) {
		showFragment(fragmentMap.get(SinglePortrait.class.getSimpleName()), R.id.activity_main);
	}

	private void useDualMode(int rotation) {
		switch (rotation) {
			case Surface.ROTATION_90:
			case Surface.ROTATION_270:
				// Setting layout for double landscape
				showFragment(fragmentMap.get(DualLandscape.class.getSimpleName()), R.id.activity_main);
				break;
			default:
				showFragment(fragmentMap.get(DualPortrait.class.getSimpleName()), R.id.activity_main);
				break;
		}
	}

	private void setupLayout() {
		int rotation = ScreenHelper.getRotation(this);
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

	private void showFragment(BaseFragment fragment, int id) {
		final FragmentManager fragmentManager = getSupportFragmentManager();
		final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (!fragment.isAdded()) {
			fragmentTransaction.add(id, fragment);
		}

		fragmentTransaction.show(fragment);

		if(currentSelectedPosition != -1) {
			fragment.setCurrentSelectedPosition(currentSelectedPosition);
		}

		Iterator entries = fragmentMap.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry thisEntry = (Map.Entry) entries.next();
			if(thisEntry.getValue() != fragment) {
				fragmentTransaction.hide((Fragment) thisEntry.getValue());
			}
		}
		fragmentTransaction.commit();
	}

	@Override
	public void onBackPressed() {
		setTitle(R.string.app_name);
		Iterator entries = fragmentMap.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry thisEntry = (Map.Entry) entries.next();
			BaseFragment fragment = (BaseFragment) thisEntry.getValue();
			if(fragment.isVisible()) {
				if(fragment.onBackPressed()) {
					this.finish();
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
	public void onItemSelected(int position) {
		currentSelectedPosition = position;
	}
}