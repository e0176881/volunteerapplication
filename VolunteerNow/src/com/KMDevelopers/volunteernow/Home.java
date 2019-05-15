package com.KMDevelopers.volunteernow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;
import android.provider.Settings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class Home extends FragmentActivity implements LocationListener {
	private LoginButton loginBtn;
	private UiLifecycleHelper uiHelper;
	 // DB Class to perform DB related operations
 
    // Progress Dialog Object
    ProgressDialog prgDialog;
    HashMap<String, String> queryValues;
	// flag for Internet connection status
    Boolean isInternetPresent = false;
     
    // Connection detector class
    ConnectionDetector cd;
    SQLiteDatabase db;
	SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String pUserName = "username"; 
    public static final String pPassword = "password"; 
    boolean enabled;
    boolean gg ;
    LocationManager lm;
    HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;
	private static final List<String> PERMISSIONS = Arrays.asList("email");
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

       //set content view AFTER ABOVE sequence (to avoid crash)
    
       
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(this, statusCallback);
		uiHelper.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        
        
        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
        Toast.makeText(getBaseContext(), "This app require internet connection & gps to work", Toast.LENGTH_LONG).show();
        
        cd = new ConnectionDetector(getApplicationContext());
        
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE); 
       
        	  lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, this);
          
      
        
         enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER); 
       
         loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
         
 		loginBtn.setUserInfoChangedCallback(new UserInfoChangedCallback() {
 			@Override
 			public void onUserInfoFetched(GraphUser user) {
 				
 			
 				if (user != null) {
 					
 					//Register(user.asMap().get("email").toString(),user.getName(),user.getId());
 					SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefs", MODE_APPEND); // 0 - for private mode
	  				Editor editor = pref.edit();
	  				editor.putString("email", user.getProperty("email").toString().trim());
	  				editor.commit();
	    		       if (sharedpreferences.getString("email", null) != null && Session.getActiveSession().isOpened())
	    		       {
	    		    	   Register(user.getProperty("email").toString(),user.getName(),user.getId());
	    		    	    String getParticulars = getParticulars();
	    		   		 try {
	    		   			 db.execSQL("DROP TABLE IF EXISTS " + "account");
	    		   		 		db.execSQL("CREATE TABLE IF NOT EXISTS account(fullname VARCHAR,contactno VARCHAR,email VARCHAR,nric VARCHAR);");
	    		   	            JSONArray jsonArray = new JSONArray(getParticulars);
	    		   	            for (int i = 0; i < jsonArray.length(); i++) {
	    		   	            	 JSONObject jsonObject = jsonArray.getJSONObject(i);
	    		   	            	 db.execSQL("INSERT INTO account VALUES('"+jsonObject.getString("fullname")+"','"+jsonObject.getString("contactno")+"','"+jsonObject.getString("email")+
	    		   	         				   "','"+jsonObject.getString("nric")+"');");
	    		   	            }
	    		   		 
	    		   	}catch (Exception e) {
	    		         e.printStackTrace();
	    		     }
	    		    	   startActivity(new Intent(Home.this, UpdateParts.class));
	    		     //     finish();
	    		       
	    		       }
 					// startActivity(new Intent(Home.this, VolunteerNowHome.class));
 				} else {
 				
 				//	 startActivity(new Intent(Home.this, VolunteerNowHome.class));
 					 
 				}
 				
 			}
 		});
 		loginBtn.setReadPermissions(Arrays.asList(
 				        "email", "user_birthday"));
        // Check if enabled and if not send user to the GPS settings
      
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/verdana.ttf"); 
        TextView tv1 = (TextView)findViewById(R.id.tv1);
        tv1.setTypeface(type);
        TextView tv2 = (TextView)findViewById(R.id.tv2);
        tv2.setTypeface(type);
        TextView tv3 = (TextView)findViewById(R.id.tv3);
        tv3.setTypeface(type);
        
        Button nbtnBook = (Button)findViewById(R.id.btnLogin);
        nbtnBook.setTypeface(type);
        nbtnBook.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// Switching to Register screen
				// get Internet status
               
  				  if (!enabled) {
					  showAlert();
			          
			        }
  				  else
  				  {
				Intent i = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(i);
  				  }
                

			}
		});
        
        
        TextView nbtnLogin = (TextView)findViewById(R.id.btnRegister);
        nbtnLogin.setTypeface(type);
        nbtnLogin.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// Switching to Register screen
				  
				 
				  if (!enabled) {
					  showAlert();
			          
			        }
  				  else
  				  {
				Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
				startActivity(i);
				finish();
  				  }
			}
		});
        db=openOrCreateDatabase("VolunteerNow", Context.MODE_PRIVATE, null);
		
        String readLocations = getEventsLocations();
	    try {
	    	db.execSQL("DROP TABLE IF EXISTS " + "markers");
	 		db.execSQL("CREATE TABLE IF NOT EXISTS markers(id INTEGER,name VARCHAR,address VARCHAR,lat VARCHAR,lng VARCHAR);");
	        JSONArray jsonArray = new JSONArray(readLocations);
	        for (int i = 0; i < jsonArray.length(); i++) {
	          JSONObject jsonObject = jsonArray.getJSONObject(i);
	          db.execSQL("INSERT INTO markers VALUES('"+jsonObject.getString("id")+"','"+jsonObject.getString("address")+"','"+jsonObject.getString("address")+
   				   "','"+jsonObject.getDouble("lat")+"','"+jsonObject.getString("lng")+"');");
	        }
	    		}catch (Exception e) {
	    	        e.printStackTrace();
	    	    }
	    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefs", MODE_APPEND);
	    if(pref.getString("email", null)!=null && pref.getString("email", null)!="null")
	    {
	    
	
	    }
    }
    
   
    
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
 
        // Setting Dialog Title
        alertDialog.setTitle(title);
 
        // Setting Dialog Message
        alertDialog.setMessage(message);
         
        // Setting alert dialog icon
        //alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
 
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
 
        // Showing Alert Message
        alertDialog.show();
    }
    
    public void showAlert(){
    	new AlertDialog.Builder(this)
        .setTitle("GPS Off")
        .setMessage("Do you want to switch on the gps?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {          
            	  Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		            startActivity(intent);
            }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { 
            	showAlert();
            }
        })
        .show();
    
	}
    

    	  @Override
    	    public void onProviderEnabled(String provider) {
    	         
    	        /******** Called when User on Gps  *********/
    	         
    	        Toast.makeText(getBaseContext(), "Gps turned on ", Toast.LENGTH_LONG).show();
    	        Intent i = new Intent(getApplicationContext(),Home.class);
    	          startActivity(i);
    	        
    	    }
    		@Override
    		public void onLocationChanged(Location location) {
    			// TODO Auto-generated method stub
    			
    		}
    		@Override
    		public void onStatusChanged(String provider, int status, Bundle extras) {
    			// TODO Auto-generated method stub
    			
    		}
    		@Override
    		public void onProviderDisabled(String provider) {
    			 
    			  isInternetPresent = cd.isConnectingToInternet();
    			  
                  // check for Internet status
                 
                      // Internet Connection is Present
                      // make HTTP requests
 
  					  showAlert();
  			          

                  

    		}
    
    		private Session.StatusCallback statusCallback = new Session.StatusCallback() {
    			@Override
    			public void call(Session session, SessionState state,
    					Exception exception) {
    				if (state.isOpened()) {
    					//buttonsEnabled(true);
    				//	 sharedpreferences=getSharedPreferences(MyPREFERENCES, 
    	    		  //  		   MODE_APPEND);
    	    		     
    					//Register(/*user.asMap().get("gender").toString(),*/sharedpreferences.getString("email", null),sharedpreferences.getString("namela", null),sharedpreferences.getString("username", null));
    			//		startActivity(new Intent(Home.this, UpdateParts.class));
    					Log.d("FacebookSampleActivity", "Facebook session opened");
    					
    				} else if (state.isClosed()) {
    					//buttonsEnabled(false);
    					Log.d("FacebookSampleActivity", "Facebook session closed");
    				}
    				
    			}
    		};
    		 @Override
    		    protected void onResume() {
    			 super.onResume();
    			 Session session=Session.getActiveSession();
    		      
  		       uiHelper.onResume();
    		       sharedpreferences=getSharedPreferences(MyPREFERENCES, 
    		    		   MODE_APPEND);
    		  
    		      
    		    //   Register(/*user.asMap().get("gender").toString(),*/sharedpreferences.getString("email", null),sharedpreferences.getString("namela", null),sharedpreferences.getString("username", null));
    		     
    		       
    		       if (!enabled) {
    					  showAlert();
    			          
    			        }
    		       if (Session.getActiveSession().isOpened() != true && sharedpreferences.getString("email", null) != null)
    		       {
    		    	    String getParticulars = getParticulars();
    		   		 try {
    		   			 db.execSQL("DROP TABLE IF EXISTS " + "account");
    		   		 		db.execSQL("CREATE TABLE IF NOT EXISTS account(fullname VARCHAR,contactno VARCHAR,email VARCHAR,nric VARCHAR);");
    		   	            JSONArray jsonArray = new JSONArray(getParticulars);
    		   	            for (int i = 0; i < jsonArray.length(); i++) {
    		   	            	 JSONObject jsonObject = jsonArray.getJSONObject(i);
    		   	            	 db.execSQL("INSERT INTO account VALUES('"+jsonObject.getString("fullname")+"','"+jsonObject.getString("contactno")+"','"+jsonObject.getString("email")+
    		   	         				   "','"+jsonObject.getString("nric")+"');");
    		   	            }
    		   		 
    		   	}catch (Exception e) {
    		         e.printStackTrace();
    		     }
    		    	  // Register(/*user.asMap().get("gender").toString(),*/sharedpreferences.getString("email", null),sharedpreferences.getString("namela", null),sharedpreferences.getString("username", null));
    		    	   startActivity(new Intent(Home.this, VolunteerNowHome.class));
    		     //     finish();
    		       
    		       }
    		       
    		     

    		       
    		    }
    	/*	@Override
    		public void onResume() {
    			super.onResume();
    			uiHelper.onResume();
    			buttonsEnabled(Session.getActiveSession().isOpened());
    		}*/

    		@Override
    		public void onPause() {
    			super.onPause();
    			uiHelper.onPause();
    		}

    		@Override
    		public void onDestroy() {
    			super.onDestroy();
    			uiHelper.onDestroy();
    		}

    		@Override
    		public void onActivityResult(int requestCode, int resultCode, Intent data) {
    			super.onActivityResult(requestCode, resultCode, data);
    			uiHelper.onActivityResult(requestCode, resultCode, data);
    		}

    		@Override
    		public void onSaveInstanceState(Bundle savedState) {
    			super.onSaveInstanceState(savedState);
    			uiHelper.onSaveInstanceState(savedState);
    		}
    		void Register(String email, String name, String id){
    	  		try{			
    	  			 
    	  			httpclient=new DefaultHttpClient();
    	  			httppost= new HttpPost("http://volunteernow.kmdotshop.com/appsettings/registerfb.php"); // make sure the url is correct.
    	  			nameValuePairs = new ArrayList<NameValuePair>(3);
    	  			nameValuePairs.add(new BasicNameValuePair("email",email.toString().trim()));
    	  			nameValuePairs.add(new BasicNameValuePair("name",name.toString().trim()));// $Edittext_value = $_POST['Edittext_value'];
    	  			nameValuePairs.add(new BasicNameValuePair("id",id.toString().trim())); 		 
    	  			
    	  			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	  			
    	  			ResponseHandler<String> responseHandler = new BasicResponseHandler();
    	  			final String response = httpclient.execute(httppost, responseHandler);
    	  			System.out.println("Response : " + response); 
    	  			runOnUiThread(new Runnable() {
    	  			    public void run() {
    	  			    //	tv.setText("Response from PHP : " + response);
    	  				//	dialog.dismiss();
    	  			    }
    	  			});
    	  			
    	  			
    	  			if(response.contains("success")){
    	  				runOnUiThread(new Runnable() {
    	  				    public void run() {
    	  				   // 	Toast.makeText(Home.this,"Registration Success", Toast.LENGTH_SHORT).show();
    	  				    
    	  				    }
    	  				});
    	  				
    	  				SharedPreferences pref = this.getApplicationContext().getSharedPreferences("MyPrefs", MODE_APPEND); // 0 - for private mode
    	  				Editor editor = pref.edit();
    	  				editor.putString("email", email.toString().trim()); // Storing string
    	  				editor.commit();
    	  				editor.apply();
    	  				//startActivity(new Intent(RegisterActivity.this, RHistory.class));
    	  			
    	  			}else{
    	  				//showAlert();				
    	  			}
    	  			
    	  		}catch(Exception e){
    	  			dialog.dismiss();
    	  			System.out.println("Exception : " + e.getMessage());
    	  		}
    	  	}
    		boolean got = false;
    		boolean checkexist(String fkla){
    			
    			try{			
    				
    				httpclient=new DefaultHttpClient();
    				httppost= new HttpPost("http://volunteernow.kmdotshop.com/appsettings/exist.php"); // make sure the url is correct.
    				//add your data
    				nameValuePairs = new ArrayList<NameValuePair>(1);
    				// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
    				nameValuePairs.add(new BasicNameValuePair("email",fkla.toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
    				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    				//response=httpclient.execute(httppost);
    				ResponseHandler<String> responseHandler = new BasicResponseHandler();
    				final String response = httpclient.execute(httppost, responseHandler);
    				System.out.println("Response : " + response); 
    				
    				
    				if(response.contains("success")){
    					got= true;
    				
    									
    				}
    				
    			}catch(Exception e){
    				dialog.dismiss();
    				System.out.println("Exception : " + e.getMessage());
    			}
    			return got;
    		}
    		 public String getEventsLocations() {
    		        StringBuilder builder = new StringBuilder();
    		        HttpClient client = new DefaultHttpClient();
    		        HttpGet httpGet = new HttpGet("http://volunteernow.kmdotshop.com/appsettings/clinics.php");
    		        try {
    		          HttpResponse response = client.execute(httpGet);
    		          StatusLine statusLine = response.getStatusLine();
    		          int statusCode = statusLine.getStatusCode();
    		          if (statusCode == 200) {
    		            HttpEntity entity = response.getEntity();
    		            InputStream content = entity.getContent();
    		            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
    		            String line;
    		            while ((line = reader.readLine()) != null) {
    		              builder.append(line);
    		            }
    		          } else {
    		            Log.e("ERROR", "Failed to load map");
    		          }
    		        } catch (ClientProtocolException e) {
    		          e.printStackTrace();
    		        } catch (IOException e) {
    		          e.printStackTrace();
    		        }
    		        return builder.toString();
    		      }
    		 public String getParticulars() {
		    	SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefs", 0);
		    	
		    	
		    	
    		        StringBuilder builder = new StringBuilder();
    		        HttpClient client = new DefaultHttpClient();
    		        HttpPost httppost= new HttpPost("http://volunteernow.kmdotshop.com/appsettings/getparticulars.php"); // make sure the url is correct.
    				
    		        try {
    		        	
    					nameValuePairs = new ArrayList<NameValuePair>(1);
    					nameValuePairs.add(new BasicNameValuePair("email",pref.getString("email", null).toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
    					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    					HttpResponse response= client.execute(httppost);
    		          StatusLine statusLine = response.getStatusLine();
    		          int statusCode = statusLine.getStatusCode();
    		          if (statusCode == 200) {
    		            HttpEntity entity = response.getEntity();
    		            InputStream content = entity.getContent();
    		            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
    		            String line;
    		            while ((line = reader.readLine()) != null) {
    		              builder.append(line);
    		            }
    		          } else {
    		            Log.e("ERROR", "Failed to load particulars");
    		          }
    		        } catch (ClientProtocolException e) {
    		          e.printStackTrace();
    		        } catch (IOException e) {
    		          e.printStackTrace();
    		        }
    		        return builder.toString();
		    	  }
				
}