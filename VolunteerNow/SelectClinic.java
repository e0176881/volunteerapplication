package com.KMDevelopers.volunteernow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class SelectClinic extends FragmentActivity {
	GoogleMap map ;
	MapView mapView;
	HttpPost httppost;
	StringBuffer buffer;
	TextView about;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;
	public int currentimageindex=0;
    Timer timer;
    TimerTask task;
    ImageView slidingimage;
    String cname, cdistance,caddress, clat, clng;
    String symb,desc,mc,mapname,clickedname;
    String title, oaddress, odays, ohoursmorning, ohoursafternoon, ohoursevening, ocontactno, ofaxnumber, date, time, address;
    Double lat,lng, noofhours;
   
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 //Remove title bar
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        super.onCreate(savedInstanceState);
StrictMode.ThreadPolicy policy = new StrictMode.
	    ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy); 
// Set View to register.xml
setContentView(R.layout.selectclinic);

     // 1. get passed intent 
        Intent intent = getIntent();
 

        // 4. get bundle from intent
        Bundle bundle = intent.getExtras();
 
        // 5. get status value from bundle
         cname = bundle.getString("clinicname");
         cdistance = bundle.getString("distance");
         caddress = bundle.getString("address");// this.setContentView(R.layout.selectclinic); 

         
       
        
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/verdana.ttf"); 
        TextView Pagename = (TextView)findViewById(R.id.pagename);
        Pagename.setTypeface(type);
        
        TextView tvClinicName = (TextView)findViewById(R.id.text1);
        tvClinicName.setTypeface(type);
        tvClinicName.setText(cname);
        
        TextView tvClinicAdd = (TextView)findViewById(R.id.text2);
        tvClinicAdd.setTypeface(type);
        tvClinicAdd.setText(caddress);
        
        TextView tvDistance = (TextView)findViewById(R.id.text3);
        tvDistance.setTypeface(type);
        tvDistance.setText(cdistance);
        
 	   
        String readgmap = readMaps();
		    try {
		   
		    	
		        JSONArray jsonArray = new JSONArray(readgmap);
		    //    Log.i(ParseJSON.class.getName(),
		      //      "Number of entries " + jsonArray.length());
		        for (int i = 0; i < jsonArray.length(); i++) {
		          JSONObject jsonObject = jsonArray.getJSONObject(i);
		          mapname =  jsonObject.getString("name");
		          oaddress =  jsonObject.getString("address");
		          lat=jsonObject.getDouble("lat");
		          lng=jsonObject.getDouble("lng");
		      
		       // which working u say?i google le like no have clear fragment leh nvm this one i can fix soon but json got prob
		          map = ((SupportMapFragment)  getSupportFragmentManager().findFragmentById(R.id.map))
		                  .getMap();
		          
		          ViewGroup.LayoutParams params = ((SupportMapFragment)  getSupportFragmentManager().findFragmentById(R.id.map)).getView().getLayoutParams();
		          params.height = 600;
		          ((SupportMapFragment)  getSupportFragmentManager().findFragmentById(R.id.map)).getView().setLayoutParams(params);
		          map.addMarker(new MarkerOptions()
		          .position(new LatLng(lat,lng))
		          .title(mapname)
		          .snippet( oaddress )
		          /*.snippet("<div>" +  oaddress + "</br>" + "Operating Days: " + "</br>" + odays + "</br>" + "Operating Hours" + "</br>" + "Morning: " + ohoursmorning + "</br>" + "Afternoon: " + ohoursafternoon + "</br>" + "Evening: " + ohoursevening + "</br>" + "Contact No: " + ocontactno + "</br>" + "Fax Number:" + ofaxnumber + "</div>")*/
		          
		          );
		          LatLng toCenter = new LatLng(1.424275, 103.838379);
		          map.setMyLocationEnabled(true);

		          map.moveCamera(CameraUpdateFactory.newLatLngZoom(toCenter, 12));
		          
		          

		        }
		      
		}catch (Exception e) {
	        e.printStackTrace();
	    }
		    
		    TextView tva = (TextView)findViewById(R.id.textViewa);
	        tva.setTypeface(type);
	        TextView tvd = (TextView)findViewById(R.id.textViewD);
	        tvd.setTypeface(type);
	        TextView tvb = (TextView)findViewById(R.id.textViewb);
	        tvb.setTypeface(type);
		    
	        TextView tvphone =  (TextView)findViewById(R.id.tbphone);
	        tvphone.setTypeface(type);
	        TextView tvpart =  (TextView)findViewById(R.id.tbparticipants);
	        tvpart.setTypeface(type);
	        
	        TextView tvoperating =  (TextView)findViewById(R.id.tboperatinghours);
	        tvoperating.setTypeface(type);
	        
		    String readgmapz = readMaps2(cname);
			 try {
		            JSONArray jsonArray = new JSONArray(readgmapz);

	   	            for (int i = 0; i < jsonArray.length(); i++) {
	   	            	 JSONObject jsonObject = jsonArray.getJSONObject(i);
	   	            	 String operatinghr = "";
	   	            	 date =  jsonObject.getString("date");
	   	            	 time =  jsonObject.getString("time");
	   	            	 noofhours =  jsonObject.getDouble("noofhours");
	   	            	 address =  jsonObject.getString("address");
	   	            	 title = jsonObject.getString("name");
	   	            	operatinghr += jsonObject.getString("date") + " - " + jsonObject.getString("time") + "\n" + jsonObject.getDouble("noofhours") + " Hours";
                  	 tvphone.setText(jsonObject.getString("contactno"));
	      	           tvoperating.setText(operatinghr);
	      	           tvoperating.setVisibility(1);
	      	           tvpart.setText(jsonObject.getString("participants") + "/" +jsonObject.getString("maxparticipants") );
	      	         LatLng toCenter = new LatLng(jsonObject.getDouble("lat"), jsonObject.getDouble("lng"));
	      	       ProgressBar progBar= (ProgressBar)findViewById(R.id.progBar1);
	      	    
	      	    int percent =    (int)(((double)jsonObject.getInt("participants") / (double)jsonObject.getInt("maxparticipants")) * 100 );
	      	     
	      	       progBar.setProgress(percent);
	      	       map.moveCamera(CameraUpdateFactory.newLatLngZoom(toCenter, 12));
	      	           
	   	            }
			 }
			 catch (Exception e) {
			        e.printStackTrace();
			    }
		    
			 
			 
			 
		    ImageButton bck = (ImageButton) findViewById(R.id.imageButton1);
		    bck.setOnClickListener(new View.OnClickListener() {
		   			
		   			public void onClick(View v) {
		   				// Switching to Register screen
		   				finish();
		   			}
		   		});
        
		    Button btnselect = (Button) findViewById(R.id.btnselect);
		    btnselect.setTypeface(type);
		    btnselect.setOnClickListener(new View.OnClickListener() {
	   			
	   			public void onClick(View v) {
	   				showAlert();
	   				// Switching to Register screen
	   				/*Intent launchmain= new Intent(getApplicationContext(),Book.class);
	            	launchmain.putExtra("clinicname",cname);
	                startActivity(launchmain);*/
	   			}
	   		});
		    Button btncal= (Button) findViewById(R.id.btnCAL);
		    btncal.setTypeface(type);
		    btncal.setOnClickListener(new View.OnClickListener() {
	   			
	   			public void onClick(View v) {
	   				
	   				SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	   				try {  
	   					Date date2 = format.parse(date + " " + time);
	   					
	   					Calendar cal = Calendar.getInstance();     
		   		        Intent intent = new Intent(Intent.ACTION_EDIT);
		   		        intent.setType("vnd.android.cursor.item/event");
		   		        long starttime = date2.getTime();
		   		        double addon = (1000 * 60 * 60 * noofhours);
		   		        intent.putExtra("beginTime", starttime);
		   		        intent.putExtra("endTime", starttime + addon);
		   		        intent.putExtra("allDay", false);
		   		        //intent.putExtra("rrule", "FREQ=YEARLY");
		   		     intent.putExtra("eventLocation", address);  
		   		     intent.putExtra("duration", "+P"+noofhours.toString()+"H");
		   		        intent.putExtra("title", title);
		   		        intent.putExtra("description", address);
		   		        startActivity(intent);
		   		        
	   				      
	   				    System.out.println(date2);  
	   				}catch (java.text.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	   				
	   			}
	   		});
		    
		    alreadyregisted();
		   
		    
    }
   
    public void showAlert(){
    	new AlertDialog.Builder(this)
        .setTitle("Are you Sure?")
        .setMessage("Are you sure you want to volunteer for this event?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {          
            	  //Yes
            	confirmdb();
            }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { 
            	//No
            }
        })
        .show();
    
	}
    public String readMaps() {
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
    public String readMaps2(String searchquery) {
    	
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httppost= new HttpPost("http://volunteernow.kmdotshop.com/appsettings/search.php"); // make sure the url is correct.
		
        try {
        	//add your data
			nameValuePairs = new ArrayList<NameValuePair>(1);
			// Getting latitude of the current location
			//EditText et = (EditText) findViewById(R.id.searchs);
			// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
			nameValuePairs.add(new BasicNameValuePair("search",searchquery)); 
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
            Log.e("ERROR", "Failed to load map");
          }
        } catch (ClientProtocolException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
        return builder.toString();
      }
    void confirmdb(){
  		try{			
  		   SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefs", MODE_APPEND);
  			httpclient=new DefaultHttpClient();
  			httppost= new HttpPost("http://volunteernow.kmdotshop.com/appsettings/volunteer.php"); 
  			nameValuePairs = new ArrayList<NameValuePair>(2);
  			nameValuePairs.add(new BasicNameValuePair("username",pref.getString("username", null).toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
  			
  			nameValuePairs.add(new BasicNameValuePair("clinicname",mapname.toString().trim()));  
  			
  			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
  			ResponseHandler<String> responseHandler = new BasicResponseHandler();
  			final String response = httpclient.execute(httppost, responseHandler);
  			System.out.println("Response : " + response); 
  		
  			
  			if(response !=""){
  				if(response.contains("Already Registered"))
  				{
  					Toast.makeText(SelectClinic.this,"You already registered previously.", Toast.LENGTH_LONG).show();
  					Button btnselect = (Button) findViewById(R.id.btnselect);
  					btnselect.setText("Already Registered");
  					btnselect.setBackgroundColor(R.drawable.buttonstyle2);
  				}else
  				{
  			
  				 runOnUiThread(new Runnable() {
				    public void run() {

				    	Toast.makeText(SelectClinic.this,"Registration Completed", Toast.LENGTH_LONG).show();
		  			finish();
				    	
				    }
				});
  				String rs = response;
  				}

  			}else{
  				
  			}
  			
  		}catch(Exception e){
  			dialog.dismiss();
  			System.out.println("Exception : " + e.getMessage());
  		}
  		
  	}
    void alreadyregisted(){
  		try{			
  		   SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefs", MODE_APPEND);
  			httpclient=new DefaultHttpClient();
  			httppost= new HttpPost("http://volunteernow.kmdotshop.com/appsettings/checkifregistered.php"); 
  			nameValuePairs = new ArrayList<NameValuePair>(2);
  			nameValuePairs.add(new BasicNameValuePair("username",pref.getString("username", null).toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
  			
  			nameValuePairs.add(new BasicNameValuePair("clinicname",mapname.toString().trim()));  
  			
  			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
  			ResponseHandler<String> responseHandler = new BasicResponseHandler();
  			final String response = httpclient.execute(httppost, responseHandler);
  			System.out.println("Response : " + response); 
  		
  			
  			if(response !=""){
  				if(response.contains("Already Registered"))
  				{
  					Toast.makeText(SelectClinic.this,"You already registered previously.", Toast.LENGTH_LONG).show();
  					Button btnselect = (Button) findViewById(R.id.btnselect);
  					btnselect.setText("Already Registered");
  					btnselect.setBackgroundColor(R.drawable.buttonstyle2);
  					btnselect.setEnabled(false); 
  				}
  				else
  				{
  					
  					 checkiffull();
  				}
  			
  				 

  			}else{
  				checkiffull();
  			}
  			
  		}catch(Exception e){
  			dialog.dismiss();
  			System.out.println("Exception : " + e.getMessage());
  		}
  		
  	}
    void checkiffull(){
  		try{			
  		   SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefs", MODE_APPEND);
  			httpclient=new DefaultHttpClient();
  			httppost= new HttpPost("http://volunteernow.kmdotshop.com/appsettings/checkiffull.php"); 
  			nameValuePairs = new ArrayList<NameValuePair>(1);
  			
  			nameValuePairs.add(new BasicNameValuePair("clinicname",mapname.toString().trim()));  
  			
  			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
  			ResponseHandler<String> responseHandler = new BasicResponseHandler();
  			final String response = httpclient.execute(httppost, responseHandler);
  			System.out.println("Response : " + response); 
  		
  			
  			if(response !=""){
  				if(response.contains("FULL"))
  				{
  					Toast.makeText(SelectClinic.this,"This event is full.", Toast.LENGTH_LONG).show();
  					Button btnselect = (Button) findViewById(R.id.btnselect);
  					btnselect.setText("Full");
  					btnselect.setBackgroundColor(R.drawable.buttonstyle2);
  					btnselect.setEnabled(false); 
  				}
  			

  			}else{
  				
  			}
  			
  		}catch(Exception e){
  			dialog.dismiss();
  			System.out.println("Exception : " + e.getMessage());
  		}
  		
  	}

  	}