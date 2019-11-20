package com.microsoft.device.display.samples.contentcontext;

import android.graphics.PointF;

import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable {
	private static final long serialVersionUID = 8383901821872620925L;
	private String title;
	private PointF location;


	public Item(String title, PointF l) {
		this.title = title;
		this.location = l;
	}

	public String getBody() {
		return title;
	}

	public PointF getLocation() {
		return location;
	}

	public static ArrayList<Item> getItems() {
		ArrayList<Item> items = new ArrayList<Item>();
		items.add(new Item("Helsinki", new PointF(60.164320f,24.912592f)));
		items.add(new Item("Bellevue", new PointF(47.610378f,-122.200676f)));
		items.add(new Item("Palo Alto", new PointF(37.444184f, -122.161059f)));
		items.add(new Item("Taipei", new PointF(25.082329f, 121.569124f)));
		items.add(new Item("Iasi", new PointF(47.155487f,27.587743f)));
		return items;
	}

	@Override
	public String toString() {
		return getBody();
	}

}
