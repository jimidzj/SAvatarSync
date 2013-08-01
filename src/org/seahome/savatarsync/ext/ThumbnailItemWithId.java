package org.seahome.savatarsync.ext;

import greendroid.widget.item.ThumbnailItem;

public class ThumbnailItemWithId extends ThumbnailItem {
	public String Id;
	
	public ThumbnailItemWithId(String id,String text, String subtitle, int drawableId, String drawableURL) {
		super(text, subtitle, drawableId, drawableURL);
		this.Id=id;
	}
}
