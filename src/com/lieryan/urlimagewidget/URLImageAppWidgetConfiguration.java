package com.lieryan.urlimagewidget;

import com.lieryan.urlimagewidget.R;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class URLImageAppWidgetConfiguration extends Activity {
	private int id;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.configuration);
	    setResult(RESULT_CANCELED);
	    
	    Intent intent = getIntent();
	    Bundle extras = intent.getExtras();
	    if (extras != null) {
	        id = extras.getInt(
	                AppWidgetManager.EXTRA_APPWIDGET_ID, 
	                AppWidgetManager.INVALID_APPWIDGET_ID);
	    }
	    
	    if (id == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
	    
	}
	
	public void addWidget(View v) {
		SharedPreferences urls = getSharedPreferences("urls.conf", Context.MODE_PRIVATE);
		SharedPreferences.Editor urls_editor = urls.edit();
		
		String url = ((TextView) findViewById(R.id.url)).getText().toString();
		if (!url.startsWith("http://")) url = "http://" + url;
		urls_editor.putString("url_" + id, url);
		urls_editor.commit();
		
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
		URLImageAppWidgetProvider.update(this, appWidgetManager, id, url);
						
        setResult(RESULT_OK, 
        		  new Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
        );
        finish();
	}
}
