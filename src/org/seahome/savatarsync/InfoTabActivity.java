package org.seahome.savatarsync;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import greendroid.app.ActionBarActivity;
import greendroid.app.GDTabActivity;

public class InfoTabActivity extends GDTabActivity{
	  @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        final String aboutText =  getString(R.string.about);
	        final Intent aboutIntent = new Intent(this, WebContentActivity.class);
	        aboutIntent.putExtra(ActionBarActivity.GD_ACTION_BAR_VISIBILITY, View.GONE);
	        aboutIntent.putExtra(WebContentActivity.EXTRA_CONTENT_URL, "file:///android_asset/about.html");
	        addTab(aboutText, aboutText, aboutIntent);

	        final String licenseText =  getString(R.string.help);
	        final Intent licenseIntent = new Intent(this, WebContentActivity.class);
	        licenseIntent.putExtra(ActionBarActivity.GD_ACTION_BAR_VISIBILITY, View.GONE);
	        licenseIntent.putExtra(WebContentActivity.EXTRA_CONTENT_URL, "file:///android_asset/help.html");
	        addTab(licenseText, licenseText, licenseIntent);
	    }
	    
	    @Override
	    public int createLayout() {
	        return R.layout.info;
	    }

}
