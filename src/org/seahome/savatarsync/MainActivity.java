package org.seahome.savatarsync;

import greendroid.app.ActionBarActivity;
import greendroid.app.GDListActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ItemAdapter;
import greendroid.widget.NormalActionBarItem;
import greendroid.widget.QuickAction;
import greendroid.widget.QuickActionBar;
import greendroid.widget.QuickActionGrid;
import greendroid.widget.QuickActionWidget;
import greendroid.widget.QuickActionWidget.OnQuickActionClickListener;
import greendroid.widget.item.DescriptionItem;
import greendroid.widget.item.Item;
import greendroid.widget.item.SeparatorItem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.seahome.savatarsync.ext.ThumbnailItemWithId;
import org.seahome.savatarsync.utils.StringUtils;
import org.seahome.savatarsync.utils.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.AccountAPI;
import com.weibo.sdk.android.api.FriendshipsAPI;
import com.weibo.sdk.android.net.RequestListener;
import com.weibo.sdk.android.sso.SsoHandler;

public class MainActivity extends GDListActivity {
	
	public static final String TAG = "sPictureSync";
	private static final float TARGET_DISTANCERATE=0.6f;
	private static final int DIALOG_CONTACT_LIST = 0;
	private static final int DIALOG_SYNC_PROGRESS = 1;
	private static final int DIALOG_LOAD_PROGRESS = 2;
	private static final String[] CONTACTS_PROJECTION = new String[] {ContactsContract.RawContacts._ID, ContactsContract.RawContacts.DISPLAY_NAME_ALTERNATIVE };
	private ProgressDialog syncProgressDialog;
	private ProgressDialog loadProgressDialog;
	private int syncProgress=0;
	
	private Handler exitHandler;
	private boolean isExit;
	private boolean getInfoFlag=false;  
	
    private ArrayList<Friend> friends=new ArrayList<Friend>();
    private Cursor contactCursor;
    
    private int matchCount=0;
    private int unMatchCount=0;
    private int selectIndex=0;
	 
    private QuickActionWidget mBar;
	private QuickActionWidget mGrid;
	private ItemAdapter adapter;
	
	private Weibo sinaWeibo;
    private static final String SINA_CONSUMER_KEY = "532484824";
    private static final String SINA_REDIRECT_URL = "http://www.sina.com";
    private static Oauth2AccessToken sinaAccessToken;
    private static long sinaUID=0;
    SsoHandler mSsoHandler;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addActionBarItem(getActionBar().newActionBarItem(NormalActionBarItem.class).setDrawable(R.drawable.gd_action_bar_refresh).setContentDescription(R.string.action_sync), 0);
		addActionBarItem(getActionBar().newActionBarItem(NormalActionBarItem.class).setDrawable(R.drawable.gd_action_bar_share).setContentDescription(R.string.action_social), 1);		
		addActionBarItem(getActionBar().newActionBarItem(NormalActionBarItem.class).setDrawable(R.drawable.gd_action_bar_help).setContentDescription(R.string.action_help), 2);
		
		prepareQuickActionBar();
		prepareQuickActionGrid();
		
		List<Item> items = new ArrayList<Item>();
		
		items.add(new DescriptionItem(getApplicationContext().getString(R.string.msg_main)));
		
		adapter = new ItemAdapter(this, items);
		
        setListAdapter(adapter);
        
        contactCursor=managedQuery(ContactsContract.RawContacts.CONTENT_URI,CONTACTS_PROJECTION, null, null, ContactsContract.Contacts.DISPLAY_NAME);
        
        this.getListView().setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,	int arg2, long arg3) {				
				selectIndex=arg2;
				mBar.show(arg1);
				return false;
			}        	
        });
        
        exitHandler= new Handler() { 
        	@Override  
            public void handleMessage(Message msg) {  
                super.handleMessage(msg);  
                isExit = false;  
            }  
      
        };  
                
	}

	@Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {

        switch (item.getItemId()) {
        	case 0:
        		if (getInfoFlag){
	        		showDialog(DIALOG_SYNC_PROGRESS);
	        		syncProgressDialog.setMax(matchCount);
	        		new Thread()
	                {
	                    public void run()
	                    {
	                        try
	                        {
	                        	for (Friend friend:friends)
	                        	{
	                        		if (friend.isMatch()){
		                            	UpdateAvatar(friend.getContactId(),friend.getAvatarLargeUrl());
		                            	syncProgressDialog.setProgress(syncProgress++);
	                        		}
	                            }
	                            syncProgressDialog.dismiss();
	                            Utils.showToast(MainActivity.this, getApplicationContext().getString(R.string.msg_syncprocess_success));
	                        }
	                        catch (Exception e)
	                        {
	                        	syncProgressDialog.cancel();
	                        }
	                    }
	                }.start();     
        		}
        		break;
            case 1:
            	mGrid.show(item.getItemView());
                break;
            case 2:
            	Intent intent = new Intent(MainActivity.this, InfoTabActivity.class);
                intent.putExtra(ActionBarActivity.GD_ACTION_BAR_TITLE,getApplicationContext().getString(R.string.general_info_label));
                startActivity(intent);
            	//SinaAccessTokenKeeper.clear(MainActivity.this);
            	//Toast.makeText(this, "Clear", Toast.LENGTH_SHORT).show();
            	break;
            default:
                return super.onHandleActionBarItemClick(item, position);
        }

        return true;
    }
	
	private void prepareQuickActionGrid() {
        mGrid = new QuickActionGrid(this);
        mGrid.addQuickAction(new QuickAction(this, R.drawable.sina, R.string.action_sina));
        mGrid.addQuickAction(new QuickAction(this, R.drawable.qqweibo, R.string.action_qqweibo));
        mGrid.addQuickAction(new QuickAction(this, R.drawable.renren, R.string.action_renren));

        mGrid.setOnQuickActionClickListener(mActionListener);
    }
	
	private void prepareQuickActionBar() {
        mBar = new QuickActionBar(this);
        mBar.addQuickAction(new MyQuickAction(this, R.drawable.addlink, R.string.action_addlink));
        mBar.addQuickAction(new MyQuickAction(this, R.drawable.dellink, R.string.action_dellink));

        mBar.setOnQuickActionClickListener(mBarActionListener);
    }

    private OnQuickActionClickListener mActionListener = new OnQuickActionClickListener() {
        public void onQuickActionClicked(QuickActionWidget widget, int position) {
        	switch (position) {
        		case 0:
        			try{
        				GetSinaWeiboInformation();         				
        			}catch(Exception e){
        				Log.i(TAG, e.getMessage());
        			}
        			break;
        		case 1:
        			Utils.showToast(MainActivity.this, getApplicationContext().getString(R.string.msg_waiting));  
        			break;
        		case 2:
        			Utils.showToast(MainActivity.this, getApplicationContext().getString(R.string.msg_waiting));  
        			break;
        		default:
        	}
        }
    };
    
    private OnQuickActionClickListener mBarActionListener = new OnQuickActionClickListener() {
        public void onQuickActionClicked(QuickActionWidget widget, int position) {
        	switch (position) {
        		case 0:
        			ThumbnailItemWithId item=(ThumbnailItemWithId)adapter.getItem(selectIndex);
        	    	String uid=item.Id; 
        			for (Friend friend:friends){
        	    		if (friend.getId().equals(uid)){
        	    			if (friend.isMatch()){
        	    				Utils.showToast(MainActivity.this, R.string.msg_alreadymatch);
        	    				return;
        	    			}
        	    		}
        			}
        			showDialog(DIALOG_CONTACT_LIST);
        			break;
        		case 1:        			
        			toUnMatch(selectIndex);
        			break;
        		default:
        	}
        }
    };
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
    	if (keyCode == KeyEvent.KEYCODE_BACK) {  
            exit();  
            return false;  
        } else {  
        	return super.onKeyDown(keyCode, event);
        }
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	switch (id) {
    		case DIALOG_CONTACT_LIST:
    			return new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.dialog_contact_list_title).setCursor(contactCursor, new DialogInterface.OnClickListener() {
                	public void onClick(DialogInterface dialog, int which) {
                		contactCursor.moveToPosition(which);
                		int idColumn = contactCursor.getColumnIndex(ContactsContract.RawContacts._ID); 
            			int nameColumn = contactCursor.getColumnIndex(ContactsContract.RawContacts.DISPLAY_NAME_ALTERNATIVE);
            			toMatch(selectIndex,contactCursor.getString(idColumn),contactCursor.getString(nameColumn));
                	}
                }, ContactsContract.RawContacts.DISPLAY_NAME_ALTERNATIVE)
                .create();
    		case DIALOG_SYNC_PROGRESS:
    			syncProgressDialog = new ProgressDialog(MainActivity.this);
    			syncProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); 
    			syncProgressDialog.setMessage(getApplicationContext().getString(R.string.msg_syncprocess));
    			syncProgressDialog.setMax(100);
    			syncProgressDialog.setIndeterminate(false);
    			syncProgressDialog.setCancelable(false);
    			return syncProgressDialog;
    		case DIALOG_LOAD_PROGRESS:	
    			loadProgressDialog = new ProgressDialog(MainActivity.this);
    			loadProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    			loadProgressDialog.setMessage(getApplicationContext().getString(R.string.msg_loadprocess));
    			loadProgressDialog.setIndeterminate(true);
    			loadProgressDialog.setCancelable(false);
    			return loadProgressDialog;
    		
    	}
    	return null;
    }
    
    
    
    private void GetSinaWeiboInformation(){
    	if (CheckNetwork()){    	
	    	sinaWeibo = Weibo.getInstance(SINA_CONSUMER_KEY, SINA_REDIRECT_URL);
	    	sinaAccessToken = SinaAccessTokenKeeper.readAccessToken(MainActivity.this);
	    	if (sinaAccessToken.isSessionValid()) {
	    		Utils.showToast(MainActivity.this, R.string.msg_autoLogin_success);
	    		showDialog(DIALOG_LOAD_PROGRESS);
	    		AccountAPI AccountApi=new AccountAPI(sinaAccessToken);
	    		AccountApi.getUid(new SinaGetUIDListener());
	    	}
	    	else{	
		    	boolean hasSSO=false;
				try {
			        Class sso = Class.forName("com.weibo.sdk.android.sso.SsoHandler");
			        hasSSO=true;
			    } catch (ClassNotFoundException e) {
			        Log.i(TAG, "com.weibo.sdk.android.sso.SsoHandler not found");	
			    }
				if (hasSSO){
					mSsoHandler=new SsoHandler(MainActivity.this, sinaWeibo);
					mSsoHandler.authorize(new SinaAuthDialogListener());
				}else{
					sinaWeibo.authorize(MainActivity.this, new SinaAuthDialogListener());
				}
	    	}
    	}
    }
    
    private void addItmes(String id,String name,String screenName,String remark,String profileImageUrl,String avatarLargeUrl){
    	Friend friend=new Friend();
    	friend.setId(id);
		friend.setName(name);
		friend.setScreenName(screenName);
		friend.setRemark(remark==null?"":remark);
		friend.setProfileImageUrl(profileImageUrl);
		friend.setAvatarLargeUrl(avatarLargeUrl);
		friend.setMatch(false);	
		
		if (contactCursor.moveToFirst()){
			int idColumn = contactCursor.getColumnIndex(ContactsContract.RawContacts._ID); 
			int nameColumn = contactCursor.getColumnIndex(ContactsContract.RawContacts.DISPLAY_NAME_ALTERNATIVE);
			while (!contactCursor.isAfterLast()) { 				
				if (StringUtils.getDistanceRate(friend.getName(), contactCursor.getString(nameColumn))>=TARGET_DISTANCERATE ||
					StringUtils.getDistanceRate(friend.getScreenName(), contactCursor.getString(nameColumn))>=TARGET_DISTANCERATE ||
					StringUtils.getDistanceRate(friend.getRemark(), contactCursor.getString(nameColumn))>=TARGET_DISTANCERATE){
					friend.setMatch(true);
					friend.setContactId(contactCursor.getString(idColumn));
					friend.setContactName(contactCursor.getString(nameColumn));
					Log.i(TAG, friend.getContactId()+"  "+friend.getName()+"   " +friend.getAvatarLargeUrl());
					break;
				}
				contactCursor.moveToNext();
			}
		}
		
		friends.add(friend);
		
		if (friend.isMatch()){
			if (matchCount==0){
				adapter.insert(new SeparatorItem(getApplicationContext().getString(R.string.item_match)),0);				
			}
			adapter.insert(new ThumbnailItemWithId(friend.getId(), friend.getScreenName(), friend.getRemark()+" -> "+ friend.getContactName() ,R.drawable.defaultavatar,friend.getProfileImageUrl()),matchCount+1);
			matchCount++;
		}else{
			if (unMatchCount==0){
				adapter.add(new SeparatorItem(getApplicationContext().getString(R.string.item_unmatch)));
			}
			adapter.add(new ThumbnailItemWithId(friend.getId(), friend.getScreenName(), friend.getRemark() ,R.drawable.defaultavatar,friend.getProfileImageUrl()));
			unMatchCount++;
		}
    }
    
    private void toMatch(int position,String contactId,String contactName){
    	ThumbnailItemWithId item=(ThumbnailItemWithId)adapter.getItem(position);
    	String uid=item.Id;   
    	for (Friend friend:friends){
    		if (friend.getId().equals(uid)){
    			if (friend.isMatch()){
    				Utils.showToast(MainActivity.this, R.string.msg_alreadymatch);
    				return;
    			}
    			if (matchCount==0){
    				adapter.insert(new SeparatorItem(getApplicationContext().getString(R.string.item_match)),0);
    			}
    			friend.setMatch(true);
    			friend.setContactId(contactId);
    			friend.setContactName(contactName);
    			adapter.insert(new ThumbnailItemWithId(friend.getId(), friend.getScreenName(), friend.getRemark()+" -> "+ friend.getContactName() ,R.drawable.defaultavatar,friend.getProfileImageUrl()),matchCount+1);
    			matchCount++;
    			unMatchCount--;
    			adapter.remove(item);
    		}
    	}
    	MainActivity.this.runOnUiThread(new Runnable(){
			public void run(){
				adapter.notifyDataSetChanged();
			}
		});
    }
        
    private void toUnMatch(int position){
    	ThumbnailItemWithId item=(ThumbnailItemWithId)adapter.getItem(position);
    	String uid=item.Id;   
    	for (Friend friend:friends){
    		if (friend.getId().equals(uid)){
    			if (!friend.isMatch()){
    				Utils.showToast(MainActivity.this, R.string.msg_alreadyunmatch);
    				return;
    			}
    			if (unMatchCount==0){
    				adapter.insert(new SeparatorItem(getApplicationContext().getString(R.string.item_unmatch)),matchCount+1);
    			}
    			friend.setMatch(false);
    			friend.setContactId("");
    			friend.setContactName("");
    			adapter.insert(new ThumbnailItemWithId(friend.getId(), friend.getScreenName(), friend.getRemark() ,R.drawable.defaultavatar,friend.getProfileImageUrl()),matchCount+2);
    			matchCount--;
    			unMatchCount++;
    			adapter.remove(item);
    		}
    	}
    	MainActivity.this.runOnUiThread(new Runnable(){
			public void run(){
				adapter.notifyDataSetChanged();
			}
		});
    }
    
    private void UpdateAvatar(String contactId,String avatarUrl){
    	try{
	    	byte[] data=Utils.getImage(avatarUrl);
	        Bitmap bm=BitmapFactory.decodeByteArray(data, 0, data.length);
	        final ByteArrayOutputStream os = new ByteArrayOutputStream();
	        bm.compress(Bitmap.CompressFormat.PNG, 100, os);
	        byte[] avatar =os.toByteArray();     		    	
	        Utils.setContactPhoto5(getContentResolver(),avatar,Long.parseLong(contactId),true);
    	}catch(Exception e){
    		e.printStackTrace();
    		//Log.i(TAG, e.getMessage());
    	}
    }
    
    private boolean CheckNetwork(){
    	if (Utils.isConnectingToInternet(getApplicationContext())){
    		return true;
    	}else{
    		Utils.showToast(MainActivity.this,R.string.msg_disconnect);
    		return false;
    	}
    }
    
    public void exit(){  
        if (!isExit) {  
            isExit = true;  
            Utils.showToast(MainActivity.this, getApplicationContext().getString(R.string.msg_exit));  
            exitHandler.sendEmptyMessageDelayed(0, 2000);  
        } else { 
        	finish();   
            System.exit(0);  
        }  
    } 
      
    
    
    private static class MyQuickAction extends QuickAction {
        
        private static final ColorFilter BLACK_CF = new LightingColorFilter(Color.BLACK, Color.BLACK);

        public MyQuickAction(Context ctx, int drawableId, int titleId) {
            super(ctx, buildDrawable(ctx, drawableId), titleId);
        }
        
        private static Drawable buildDrawable(Context ctx, int drawableId) {
            Drawable d = ctx.getResources().getDrawable(drawableId);
            //d.setColorFilter(BLACK_CF);
            return d;
        }
        
    }
    
   
    
    class SinaAuthDialogListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
        	try{
	            String token = values.getString("access_token");
	            String expires_in = values.getString("expires_in");
	            sinaAccessToken = new Oauth2AccessToken(token, expires_in);
	            Log.i(TAG, "oncomplete");  
	            if (sinaAccessToken.isSessionValid()) {            	
	                SinaAccessTokenKeeper.keepAccessToken(MainActivity.this,sinaAccessToken);
	                Utils.showToast(MainActivity.this, R.string.msg_Login_success);
	            }
	            showDialog(DIALOG_LOAD_PROGRESS);
	            AccountAPI AccountApi=new AccountAPI(sinaAccessToken);
	    		AccountApi.getUid(new SinaGetUIDListener());
        	}catch(Exception e){
        		Log.e(TAG, e.getMessage());  
        		Utils.showToast(MainActivity.this,  e.getMessage());
        	}
        	
        }

        @Override
        public void onError(WeiboDialogError e) {  
        	Log.e(TAG, e.getMessage());  
        	Utils.showToast(MainActivity.this, getApplicationContext().getString(R.string.msg_Login_failed)+":"+e.getMessage());
        }

        @Override
        public void onCancel() {
        	Log.e(TAG, "Cancel");  
        	Utils.showToast(MainActivity.this, getApplicationContext().getString(R.string.msg_Login_cancel));
        }

        @Override
        public void onWeiboException(WeiboException e) {
        	Log.e(TAG, e.getMessage());  
        	Utils.showToast(MainActivity.this, getApplicationContext().getString(R.string.msg_Login_exception)+":"+e.getMessage());
        }
    }
    
    class SinaGetUIDListener implements RequestListener {

		@Override
		public void onComplete(String response) {
			try{
				JSONTokener jsonParser = new JSONTokener(response);
				JSONObject obj = (JSONObject) jsonParser.nextValue();
				sinaUID=obj.getLong("uid");
				Log.i(TAG, String.valueOf(sinaUID));
				
				friends.clear();
				adapter.clear();
				matchCount=0;
				unMatchCount=0;
				MainActivity.this.runOnUiThread(new Runnable(){
					public void run(){
						adapter.notifyDataSetChanged();
					}
				});
				
				FriendshipsAPI api=new FriendshipsAPI(sinaAccessToken);
	    		api.friends(sinaUID, 100, 0, false, new SinaGetFriendshipsListener());	    		
			}catch(Exception e){
				Log.e(TAG, e.getMessage());	
				Utils.showToast(MainActivity.this,  e.getMessage());
			}
			
		}

		@Override
		public void onError(WeiboException e) {
			Log.e(TAG, e.getMessage());	
			loadProgressDialog.dismiss();
			Utils.showToast(MainActivity.this, getApplicationContext().getString(R.string.msg_getuid_failed)+":"+e.getMessage());
			
		}

		@Override
		public void onIOException(IOException e) {			
			Log.e(TAG, e.getMessage());	
			loadProgressDialog.dismiss();
			Utils.showToast(MainActivity.this, getApplicationContext().getString(R.string.msg_getuid_exception)+":"+e.getMessage());
		}     	
    }
    
    class SinaGetFriendshipsListener implements RequestListener {

		@Override
		public void onComplete(String response) {
			try{
				//Log.i(TAG, response);
				JSONTokener jsonParser = new JSONTokener(response);
				int next=0;
				if (jsonParser.more()){
					JSONObject obj = (JSONObject) jsonParser.nextValue();
					JSONArray users=obj.getJSONArray("users");
					next=obj.getInt("next_cursor");
					for (int i=0; i<users.length(); i++){
						JSONObject userObj=users.getJSONObject(i);
						addItmes(userObj.getString("id"),userObj.getString("name"),userObj.getString("screen_name"),userObj.getString("remark"),userObj.getString("profile_image_url"),userObj.getString("avatar_large"));					
						
					}
				}
				MainActivity.this.runOnUiThread(new Runnable(){
					public void run(){
						adapter.notifyDataSetChanged();
					}
				});
				if (next!=0){
					FriendshipsAPI api=new FriendshipsAPI(sinaAccessToken);
		    		api.friends(sinaUID, 100, next, false, new SinaGetFriendshipsListener());	 
				}
				if (next==0){
					loadProgressDialog.dismiss();
				}
				getInfoFlag=true;
			}catch(Exception e){
				Log.e(TAG, e.getMessage());
				Utils.showToast(MainActivity.this,  e.getMessage());
			}
			
		}

		@Override
		public void onError(WeiboException e) {
			Log.e(TAG, e.getMessage());
			loadProgressDialog.dismiss();
			Utils.showToast(MainActivity.this, getApplicationContext().getString(R.string.msg_getfriend_failed)+":"+e.getMessage());	
		}

		@Override
		public void onIOException(IOException e) {
			loadProgressDialog.dismiss();
			Utils.showToast(MainActivity.this, getApplicationContext().getString(R.string.msg_getfriend_exception)+":"+e.getMessage());
		}     	
    }
    
    

}
