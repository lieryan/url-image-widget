package com.lieryan.urlimagewidget;

import java.io.InputStream;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.RemoteViews;

public class URLImageAppWidgetProvider extends AppWidgetProvider {
	public static String TAG = "URLImageWidget";
	
	public static class Size_1_1 extends URLImageAppWidgetProvider {}
	public static class Size_1_2 extends URLImageAppWidgetProvider {}
	public static class Size_1_4 extends URLImageAppWidgetProvider {}
	public static class Size_2_2 extends URLImageAppWidgetProvider {}
	public static class Size_4_4 extends URLImageAppWidgetProvider {}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
			
		SharedPreferences urls = context.getSharedPreferences("urls.conf", Context.MODE_PRIVATE);
		for (int id : appWidgetIds) {
			String url = urls.getString("url_" + id, "");
			update(context, appWidgetManager, id, url);
		}	
	}
    
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
    	super.onDeleted(context, appWidgetIds);
    	
    	SharedPreferences urls = context.getSharedPreferences("urls.conf", Context.MODE_PRIVATE);
    	SharedPreferences.Editor urls_editor = urls.edit();
		for (int id : appWidgetIds) {
			urls_editor.remove("url_" + id);
		}
		
		urls_editor.commit();
    }

	public static void update(final Context context, final AppWidgetManager appWidgetManager, final int id, final String url) {
		new Thread() {
			public void run() {
				Bitmap img = getBitmapFromUrl(context, url);
				RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);
				views.setImageViewBitmap(R.id.img, img);
				appWidgetManager.updateAppWidget(id, views);	
			}
		}.start();
	}
	
    private static Bitmap getBitmapFromUrl(Context context, final String url) {
		try {
			Log.d(TAG, "Downloading image from  url: " + url);
			return BitmapFactory.decodeStream((InputStream) new java.net.URL(url).getContent());
		} catch (Exception e) {
			Log.d(TAG, "Image cannot be loaded, using default image: " + e);
			return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
		}	
    }
}
