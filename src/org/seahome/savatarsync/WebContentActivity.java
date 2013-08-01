package org.seahome.savatarsync;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;

import greendroid.app.GDActivity;

public class WebContentActivity extends GDActivity{
	public static final String EXTRA_CONTENT_URL = "org.seahome.savatarsync.CONTENT_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String contentUrl = getIntent().getStringExtra(EXTRA_CONTENT_URL);
        if (!TextUtils.isEmpty(contentUrl)) {
            setActionBarContentView(R.layout.web_view);
            final WebView webView = (WebView) findViewById(R.id.web_view);
                webView.loadUrl(contentUrl);

        }
    }
}
