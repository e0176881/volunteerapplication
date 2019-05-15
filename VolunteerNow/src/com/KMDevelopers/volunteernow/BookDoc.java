package com.KMDevelopers.volunteernow;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import android.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class BookDoc extends Fragment {
	GoogleMap map ;
	ListView l;
	MapView mapView;
	String oaddress, odays, ohoursmorning, ohoursafternoon, ohoursevening, ocontactno, ofaxnumber, odistance;
	String symb,desc,mc,mapname,clickedname, clat, clong;
	List<NameValuePair> nameValuePairs;
	SQLiteDatabase db;
	Double lat,lng;
	
	SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String pUserName = "username"; 
    public static final String pPassword = "password"; 
	
	View view;
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
		 if (view != null) {
		        ViewGroup parent = (ViewGroup) view.getParent();
		        if (parent != null) {
		            parent.removeView(view);
		        }
		    }
		    try {
		        view = inflater.inflate(R.layout.activity_main, container, false);
		    } catch (InflateException e) {

		    }
		 StrictMode.ThreadPolicy policy = new StrictMode.
                 ThreadPolicy.Builder().permitAll().build();
                 StrictMode.setThreadPolicy(policy);

	    Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/verdana.ttf"); 
	    TextView lbl = (TextView) view.findViewById(R.id.textViewa);
	    
	    LinearLayout ltop = (LinearLayout)view.findViewById(R.id.top);
	    TextView tba = (TextView) view.findViewById(R.id.tba);
	    tba.setTypeface(type);
	    TextView tbb = (TextView) view.findViewById(R.id.tbb);
	    tbb.setTypeface(type);
	    TextView tbc = (TextView) view.findViewById(R.id.tbc);
	    tbc.setTypeface(type);
	    
	    
	   
	  /*  
	    if(checkbooking().contains("have")){
	    	ltop.setVisibility(View.VISIBLE);
	    	
	    }
	    */
	    
	    ltop.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				//Intent i = new Intent(getActivity().getApplicationContext(), ViewAllActivity.class);
				//startActivity(i);
				
				// 1. create an intent pass class name or intnet action name 
		        //Intent intent = new Intent("com.skytech.ez_dr.ViewAllActivity");
		        //Intent intent = new Intent(getActivity().getBaseContext(), MyBooking.class);
		        
		 
		        // 5. start the activity
		        //startActivity(intent);
			}
				 
		});
	    
	    
	    String readgmapz = readMaps2();
		 try {
	            JSONArray jsonArray = new JSONArray(readgmapz);
	            l=(ListView) view.findViewById(R.id.listView1);
	            SimpleAdapter adapter = new SimpleAdapter(
	            		getActivity(),
	            		list,
	            		R.layout.custom_row_view,
	            		new String[] {"lbname","lbaddress","lbdistance", "lbdate"},
	            		new int[] {R.id.text1,R.id.text2, R.id.text3,  R.id.text4}){
	                @Override
	            public View getView(int pos, View convertView, ViewGroup parent){
	                View v = convertView;
	                if(v== null){
	                    LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                    v=vi.inflate(R.layout.custom_row_view, null);
	                }
	                Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/verdana.ttf"); 
	                TextView tva = (TextView)v.findViewById(R.id.text1);
	                tva.setText(list.get(pos).get("lbname"));
	                TextView tv = (TextView)v.findViewById(R.id.text2);
	                tv.setText(list.get(pos).get("lbaddress"));
	                tv.setTypeface(type);
	                TextView tvs = (TextView)v.findViewById(R.id.text3);
	                tvs.setText(list.get(pos).get("lbdistance"));
	                tvs.setTypeface(type);
	                TextView tvs2 = (TextView)v.findViewById(R.id.text4);
	                tvs2.setText(list.get(pos).get("lbdate"));
	                tvs2.setTypeface(type);
	                return v;
	            }
	            };
	           
              l.setAdapter(null);
              list.clear(); 
   	            for (int i = 0; i < jsonArray.length(); i++) {
   	            	


   	            	 JSONObject jsonObject = jsonArray.getJSONObject(i);
      	              mapname =  jsonObject.getString("name");

      	              oaddress =  jsonObject.getString("address");
      	           odistance  =  jsonObject.getString("distance");
      	            HashMap<String,String> temp = new HashMap<String,String>();
   			    	temp.put("lbname",mapname);
   			    	temp.put("lbaddress", oaddress);
   			    	temp.put("lbdistance", odistance);
   			    	temp.put("lbdate",jsonObject.getString("date") + " - " + jsonObject.getString("time"));
   			    	list.add(temp);
      	           
   	            }
   	          //gg is it time to pas u back ? :P the action bar menu also there le?tat one easy de u comeh ere
 		   	  //ArrayAdapter<String> adapterzz=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,list);
 		   	  l.setAdapter(adapter);
 		  
 		     l.setOnItemClickListener(new OnItemClickListener()

 		       {    

 		        @Override

 		          public void onItemClick(AdapterView<?> a, View v,int position, long id) 

 		          {
 		        	TextView textview1 = (TextView) v.findViewById(R.id.text1);
 		        	TextView textview2 = (TextView) v.findViewById(R.id.text2);
 		        	TextView textview3 = (TextView) v.findViewById(R.id.text3);
 		               Toast.makeText(v.getContext(), textview1.getText(), Toast.LENGTH_SHORT).show();
 		            Intent intent = new Intent(getActivity().getBaseContext(), SelectClinic.class);
 				        // 2. put key/value data
 				        //intent.putExtra("message", "Hello From MainActivity");
 				 
 				        // 3. or you can add data to a bundle
 				        Bundle extras = new Bundle();
 				        extras.putString("clinicname", textview1.getText().toString());
 				        extras.putString("distance", textview3.getText().toString());
 				       extras.putString("address", textview2.getText().toString());
 				        //extras.putString("lat", clat);
 				        //extras.putString("lng", clong);
 				        
 				        
 				        // 4. add bundle to intent
 				        intent.putExtras(extras);
 				        getActivity().finish();
 				        // 5. start the activity
 				        startActivity(intent);
 		           }

 		     });

 		    

 		    

	   	  
	}catch (Exception e) {
       e.printStackTrace();
   }
		 String interestz = getInterest();
		 lbl.setText("Based on Your Interest : " + interestz  );
		 lbl.setTypeface(type);
		 db=getActivity().openOrCreateDatabase("VolunteerNow", Context.MODE_PRIVATE, null);
		 Cursor c=db.rawQuery("SELECT * FROM markers", null);
		 while(c.moveToNext())
 		{
 		
 		
         
		      
		       // which working u say?i google le like no have clear fragment leh nvm this one i can fix soon but json got prob
		          map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		  	    ViewGroup.LayoutParams params = ((MapFragment)  getFragmentManager().findFragmentById(R.id.map)).getView().getLayoutParams();
		          params.height = 600;
		          ((MapFragment)  getFragmentManager().findFragmentById(R.id.map)).getView().setLayoutParams(params);
		          map.addMarker(new MarkerOptions()
		          .position(new LatLng(Double.parseDouble(c.getString(3)),Double.parseDouble(c.getString(4))))
		          .title(c.getString(1))
		          .snippet(c.getString(2))
		          
		          );
		          LatLng toCenter = new LatLng(1.424275, 103.838379);


		          map.moveCamera(CameraUpdateFactory.newLatLngZoom(toCenter, 10));
		          map.setMyLocationEnabled(true);
		          map.setOnMapClickListener(new OnMapClickListener() {

		              @Override
		              public void onMapClick(LatLng point) {
		                  Log.d("Map","Map clicked");
		                  //Intent intent = new Intent(getActivity().getBaseContext(), MapFullView.class);
		                  //startActivity(intent);
		              }
		          });
 		}
		    TextView viewall = (TextView) view.findViewById(R.id.textViewff);
		    
		    viewall.setTypeface(type);
			viewall.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {

			        Intent intent = new Intent(getActivity().getBaseContext(), ViewAllActivity.class);
			   
			        Bundle extras = new Bundle();
			        extras.putString("lat", clat);
			        extras.putString("lng", clong);

			        intent.putExtras(extras);
			 
			        startActivity(intent);
			        
				}
					 
			});
       
return view;
		}
	 
	 static final ArrayList<HashMap<String,String>> list = 
		    	new ArrayList<HashMap<String,String>>(); 
	 public String getInterest(){
       	 String interest = "";
       	sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
       	try{			
       		
       		
	        HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost= new HttpPost("http://volunteernow.kmdotshop.com/appsettings/getInterest.php"); // make sure the url is correct.
   			//add your data
   			nameValuePairs = new ArrayList<NameValuePair>(1);
   			// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
   			nameValuePairs.add(new BasicNameValuePair("username",sharedpreferences.getString("email", null)));  // $Edittext_value = $_POST['Edittext_value'];
   			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
   			//response=httpclient.execute(httppost);
   			ResponseHandler<String> responseHandler = new BasicResponseHandler();
   			final String response = httpclient.execute(httppost, responseHandler);
   			System.out.println("Response : " + response); 
   			
   			
   			interest = response;
   			
   		}catch(Exception e){
   			
   			System.out.println("Exception : " + e.getMessage());
   		}
   		return interest;
       
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
	 
	  
	 
	  public String readMaps2() {
	    	
	        StringBuilder builder = new StringBuilder();
	        HttpClient client = new DefaultHttpClient();
	        HttpPost httppost= new HttpPost("http://volunteernow.kmdotshop.com/appsettings/getnearest.php"); // make sure the url is correct.
	        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
	        try {
	        	LatLng latl= getCurrentLocation(this.getActivity());
				nameValuePairs = new ArrayList<NameValuePair>(3);
				// Getting latitude of the current location
		      
				// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
				nameValuePairs.add(new BasicNameValuePair("currentlat",String.valueOf(latl.latitude)));  // $Edittext_value = $_POST['Edittext_value'];
				nameValuePairs.add(new BasicNameValuePair("currentlng",String.valueOf(latl.longitude)));
				nameValuePairs.add(new BasicNameValuePair("username",sharedpreferences.getString("email", null)));
				clat = String.valueOf(latl.latitude);
				clong = String.valueOf(latl.longitude);
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
	        public LatLng getCurrentLocation(Context context)
	        {
	            try
	            {
	                LocationManager locMgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	                Criteria criteria = new Criteria();
	                String locProvider = locMgr.getBestProvider(criteria, false);
	                Location location = locMgr.getLastKnownLocation(locProvider);
	 
	                // getting GPS status
	                boolean isGPSEnabled = locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
	                // getting network status
	                boolean isNWEnabled = locMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	 
	                if (!isGPSEnabled && !isNWEnabled)
	                {
	                    // no network provider is enabled
	                    return null;
	                }
	                else
	                {
	                    // First get location from Network Provider
	                    if (isNWEnabled)
	                        if (locMgr != null)
	                            location = locMgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	 
	                    // if GPS Enabled get lat/long using GPS Services
	                    if (isGPSEnabled)
	                        if (location == null)
	                            if (locMgr != null)
	                                location = locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	                }
	 
	                return new LatLng(location.getLatitude(), location.getLongitude());
	            }
	            catch (NullPointerException ne)
	            {
	                Log.e("Current Location", "Current Lat Lng is Null");
	                return new LatLng(0, 0);
	            }
	            catch (Exception e)
	            {
	                e.printStackTrace();
	                return new LatLng(0, 0);
	            }
	        }
	        public String checkbooking(){
	       	 String fulllname = "";
	       	sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
	       	try{			
	       		
	       		
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost= new HttpPost("http://volunteernow.kmdotshop.com/appsettings/getbooking.php"); // make sure the url is correct.
	   			//add your data
	   			nameValuePairs = new ArrayList<NameValuePair>(1);
	   			// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
	   			nameValuePairs.add(new BasicNameValuePair("username",sharedpreferences.getString("email", null)));  // $Edittext_value = $_POST['Edittext_value'];
	   			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	   			//response=httpclient.execute(httppost);
	   			ResponseHandler<String> responseHandler = new BasicResponseHandler();
	   			final String response = httpclient.execute(httppost, responseHandler);
	   			System.out.println("Response : " + response); 
	   			
	   			
	   			fulllname = response;
	   			
	   		}catch(Exception e){
	   			
	   			System.out.println("Exception : " + e.getMessage());
	   		}
	   		return fulllname;
	       
	       }
	       
}
