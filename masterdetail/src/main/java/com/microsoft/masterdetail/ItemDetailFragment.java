package com.microsoft.masterdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ItemDetailFragment extends Fragment {
	private Item item;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		item = (Item) getArguments().getSerializable("item");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_item_detail,
				container, false);
		TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
		TextView tvBody = (TextView) view.findViewById(R.id.tvBody);
		tvTitle.setText(item.getTitle());
		tvBody.setText(item.getBody());

		return view;
	}

    public static ItemDetailFragment newInstance(Item item) {
    	ItemDetailFragment fragment = new ItemDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("item", item);
        fragment.setArguments(args);
        return fragment;
    }
}
