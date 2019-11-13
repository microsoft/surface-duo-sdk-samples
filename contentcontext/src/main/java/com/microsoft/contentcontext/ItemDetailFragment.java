package com.microsoft.contentcontext;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

public class ItemDetailFragment extends Fragment {
	private Item item;
	private WebView mWebView;
	private double lat, lng;
	private static final String TAG = ItemDetailFragment.class.getName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		item = (Item) getArguments().getSerializable("item");
		lat = item.getLocation().x;
		lng = item.getLocation().y;
		Log.d(TAG,"item " + item.toString() + " lat " + lat + " lng " + lng);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_item_detail,
				container, false);
		mWebView = view.findViewById(R.id.webview);
		setupWebView();
		return view;
	}

	private void setupWebView() {
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WebViewClient());
		mWebView.addJavascriptInterface(ItemDetailFragment.this, "AndroidFunction");
		mWebView.loadUrl("file:///android_asset/googlemap.html");
	}

    public static ItemDetailFragment newInstance(Item item) {
    	ItemDetailFragment fragment = new ItemDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("item", item);
        fragment.setArguments(args);
        return fragment;
    }

	@JavascriptInterface
	public double getLat(){
		return lat;
	}

	@JavascriptInterface
	public double getLng(){
		return lng;
	}
}
