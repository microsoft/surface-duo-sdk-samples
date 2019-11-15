package com.microsoft.device.display.samples.masterdetail;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ItemsListFragment extends Fragment implements ListView.OnItemClickListener {
	private ArrayAdapter<Item> adapterItems;
	private ListView lvItems;
	private static final String TAG = "ItemsListFragment";

	private OnItemSelectedListener listener;

	public interface OnItemSelectedListener {
		void onItemSelected(Item i, int position);
		void onInit(Item i, int position);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnItemSelectedListener) {
			listener = (OnItemSelectedListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement ItemsListFragment.OnItemSelectedListener");
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ArrayList<Item> items = Item.getItems();
		adapterItems = new ArrayAdapter<Item>(getActivity(),
				android.R.layout.simple_list_item_activated_1, items);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_items_list, container, false);
		lvItems = (ListView) view.findViewById(R.id.lvItems);
		lvItems.setAdapter(adapterItems);
		lvItems.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lvItems.setOnItemClickListener(this);
		Bundle arguements = getArguments();
		if(arguements != null) {
			int position = arguements.getInt(MainActivity.POSITION_KEY);
			Log.d(TAG,"position " + position);
			lvItems.setItemChecked(position, true);
			listener.onInit(adapterItems.getItem(position), position);
			setArguments(null);
		}
		
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View item, int position, long rowId) {
		Item i = adapterItems.getItem(position);
		listener.onItemSelected(i, position);
	}

	public static ItemsListFragment newInstance() {
    	ItemsListFragment fragment = new ItemsListFragment();
        return fragment;
	}
	
	public static ItemsListFragment newInstance(int position) {
		ItemsListFragment fragment = new ItemsListFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(MainActivity.POSITION_KEY, position);
		fragment.setArguments(bundle);
        return fragment;
    }

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.d(TAG,"onDestroyView");
	}
}
