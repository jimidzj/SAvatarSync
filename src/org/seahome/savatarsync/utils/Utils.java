package org.seahome.savatarsync.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.Toast;

public class Utils {
	public static byte[] getImage(String urlpath) throws Exception {
		URL url = new URL(urlpath);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(6 * 1000);
		// ±ð³¬¹ý10Ãë¡£
		if (conn.getResponseCode() == 200) {
			InputStream inputStream = conn.getInputStream();
			return readStream(inputStream);
		}
		return null;
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outstream.write(buffer, 0, len);
		}
		outstream.close();
		inStream.close();

		return outstream.toByteArray();
	}
	
	public static void setContactPhoto5(ContentResolver c, byte[] bytes,long personId, boolean Sync) {  
        ContentValues values = new ContentValues();  
        Uri u = Uri.parse("content://com.android.contacts/data");  
        int photoRow = -1;  
        String where ="raw_contact_id = " + personId + " AND mimetype ='vnd.android.cursor.item/photo'";  
        Cursor cursor = c.query(u, null, where, null, null);  
        int idIdx = cursor.getColumnIndexOrThrow("_id");  
        if (cursor.moveToFirst()) {  
            photoRow = cursor.getInt(idIdx);  
        }  
        cursor.close();  
        values.put("raw_contact_id", personId);  
        values.put("is_super_primary", 1);  
        values.put("data15", bytes);  
        values.put("mimetype","vnd.android.cursor.item/photo");  
        if (photoRow >= 0) {  
            c.update(u, values, " _id= " + photoRow, null);  
        } else {  
            c.insert(u, values);  
        }  
        if (! Sync){  
            u = Uri.withAppendedPath(Uri.parse("content://com.android.contacts/raw_contacts"), String.valueOf(personId));  
            values = new ContentValues();  
            values.put("dirty", 0);  
            c.update(u, values, null, null);  
        }  
    }
	
	public static boolean isConnectingToInternet(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null)
          {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null)
                  for (int i = 0; i < info.length; i++)
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      {
                          return true;
                      }
 
          }
          return false;
    }
	
	 public static void showToast(final Activity activity,final String content) {
	        activity.runOnUiThread(new Runnable() {
	        
	            @Override
	            public void run() {
	                Toast toast = Toast.makeText(activity, content, Toast.LENGTH_SHORT);
	                toast.show();
	                
	            }
	        });
	       
	    }
	 
	 public static void showToast(final Activity activity,final int resId) {
	        activity.runOnUiThread(new Runnable() {
	        
	            @Override
	            public void run() {
	                Toast toast = Toast.makeText(activity, activity.getApplicationContext().getString(resId), Toast.LENGTH_SHORT);
	                toast.show();
	                
	            }
	        });
	       
	    }
}
