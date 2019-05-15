package com.KMDevelopers.volunteernow;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;

public class HealthBook extends Fragment {
	ListView l,l2,l3;
	String oaddress, odays, ohoursmorning, ohoursafternoon, ohoursevening, ocontactno, ofaxnumber, odistance;
	String symb,desc,mc,mapname,clickedname, clat, clong;
	String fullname,contactno,email,nric;
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
		  
		  
		  view = inflater.inflate(R.layout.yourvolunteer, container, false);
		   Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/verdana.ttf"); 
		   TextView tb1 = (TextView)view.findViewById(R.id.textViewa);
		   TextView tb2 = (TextView)view.findViewById(R.id.textViewa1);
		   TextView tb3 = (TextView)view.findViewById(R.id.textViewb1);
		   TextView clocked = (TextView)view.findViewById(R.id.textViewff);
		   TextView fn = (TextView)view.findViewById(R.id.textViewgg);
		   TextView cn = (TextView)view.findViewById(R.id.textViewcontact);
		   TextView em = (TextView)view.findViewById(R.id.textViewemail);
		   TextView nc = (TextView)view.findViewById(R.id.textViewnric);
		   
		   tb1.setTypeface(type);
		   tb2.setTypeface(type);
		   tb3.setTypeface(type);
		   clocked.setTypeface(type);
		   fn.setTypeface(type);
		   String noofhourss = readMaps3();
		   clocked.setText(noofhourss);
		   SharedPreferences pref = this.getActivity().getApplicationContext().getSharedPreferences("MyPrefs", 0);
		   db=getActivity().openOrCreateDatabase("VolunteerNow", Context.MODE_PRIVATE, null);
			 Cursor c=db.rawQuery("SELECT * FROM account WHERE email='"+pref.getString("email", null)+"'", null);
			 while(c.moveToNext())
	 		{
				  fullname =  c.getString(0);
		          contactno =  c.getString(1);
		          email=c.getString(2);
		          nric=c.getString(3);
	 		}
		   
		 
	 		    fn.setText("Name : " + fullname);
	 		   nc.setText("NRIC : " + nric);
	 		    em.setText("Email : " + email);
	 		   cn.setText("Mobile No : " + contactno);

	 		    

	
		   
		   String readgmapz = readMaps2();
			 try {
		            JSONArray jsonArray = new JSONArray(readgmapz);
		            l2=(ListView) view.findViewById(R.id.listView2);
		            SimpleAdapter adapter = new SimpleAdapter(
		            		getActivity(),
		            		list2,
		            		R.layout.custom_row_view2,
		            		new String[] {"lbname","lbhours", "lbaddress"},
		            		new int[] {R.id.text1,R.id.text2, R.id.text3}){
		                @Override
		            public View getView(int pos, View convertView, ViewGroup parent){
		                View v = convertView;
		                if(v== null){
		                    LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		                    v=vi.inflate(R.layout.custom_row_view2, null);
		                }
		                Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/verdana.ttf"); 
		                TextView tva = (TextView)v.findViewById(R.id.text1);
		                tva.setText(list2.get(pos).get("lbname"));
		                TextView tv = (TextView)v.findViewById(R.id.text2);
		                tv.setText(list2.get(pos).get("lbhours"));
		                tv.setTypeface(type);
		                TextView tv2 = (TextView)v.findViewById(R.id.text3);
		                tv2.setText(list2.get(pos).get("lbaddress"));
		                tv2.setTypeface(type);
		                
		                return v;
		            }
		            };
		           
	              l2.setAdapter(null);
	              list2.clear(); 
	   	            for (int i = 0; i < jsonArray.length(); i++) {
	   	            	


	   	            	 JSONObject jsonObject = jsonArray.getJSONObject(i);
	      	              mapname =  jsonObject.getString("name");

	      	              oaddress =  jsonObject.getString("address");
	      	           
	      	            HashMap<String,String> temp = new HashMap<String,String>();
	   			    	temp.put("lbname",mapname);
	   			    	temp.put("lbhours", jsonObject.getString("noofhours") + " Hours");
	   			    	temp.put("lbaddress",oaddress);
	   			    	list2.add(temp);
	      	           
	   	            }
	   	          //gg is it time to pas u back ? :P the action bar menu also there le?tat one easy de u comeh ere
	 		   	  //ArrayAdapter<String> adapterzz=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,list);
	 		   	  l2.setAdapter(adapter);
	 		  
	 		     l2.setOnItemClickListener(new OnItemClickListener()

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
	 				        
	 				       extras.putString("noofhours", textview2.getText().toString());
	 				      extras.putString("address", textview3.getText().toString());
	 				        //extras.putString("lat", clat);
	 				        //extras.putString("lng", clong);
	 				        
	 				        
	 				        // 4. add bundle to intentx	
	 				        intent.putExtras(extras);
	 				        getActivity().finish();
	 				        // 5. start the activity
	 				        startActivity(intent);
	 		           }

	 		     });

	 		    

	 		    

		   	  
		}catch (Exception e) {
	       e.printStackTrace();
	   }
			
			 String readgmap = readMaps1();
			 try {
		            JSONArray jsonArray = new JSONArray(readgmap);
		            l=(ListView) view.findViewById(R.id.listView1);
		            SimpleAdapter adapter = new SimpleAdapter(
		            		getActivity(),
		            		list,
		            		R.layout.custom_row_view2,
		            		new String[] {"lbname","lbhours", "lbaddress"},
		            		new int[] {R.id.text1,R.id.text2, R.id.text3}){
		                @Override
		            public View getView(int pos, View convertView, ViewGroup parent){
		                View v = convertView;
		                if(v== null){
		                    LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		                    v=vi.inflate(R.layout.custom_row_view2, null);
		                }
		                Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/verdana.ttf"); 
		                TextView tva = (TextView)v.findViewById(R.id.text1);
		                tva.setText(list.get(pos).get("lbname"));
		                TextView tv = (TextView)v.findViewById(R.id.text2);
		                tv.setText(list.get(pos).get("lbhours"));
		                tv.setTypeface(type);
		                TextView tv2 = (TextView)v.findViewById(R.id.text3);
		                tv2.setText(list.get(pos).get("lbaddress"));
		                tv2.setTypeface(type);
		                
		                return v;
		            }
		            };
		           
	              l.setAdapter(null);
	              list.clear(); 
	   	            for (int i = 0; i < jsonArray.length(); i++) {
	   	            	


	   	            	 JSONObject jsonObject = jsonArray.getJSONObject(i);
	      	              mapname =  jsonObject.getString("name");

	      	              oaddress =  jsonObject.getString("address");
	      	           
	      	            HashMap<String,String> temp = new HashMap<String,String>();
	   			    	temp.put("lbname",mapname);
	   			    	temp.put("lbhours", jsonObject.getString("noofhours") + " Hours");
	   			    	temp.put("lbaddress",oaddress);
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
	 				        
	 				       extras.putString("noofhours", textview2.getText().toString());
	 				      extras.putString("address", textview3.getText().toString());
	 				        //extras.putString("lat", clat);
	 				        //extras.putString("lng", clong);
	 				        
	 				        
	 				        // 4. add bundle to intentx	
	 				        intent.putExtras(extras);
	 				        getActivity().finish();
	 				        // 5. start the activity
	 				        startActivity(intent);
	 		           }

	 		     });

	 		    

	 		    

		   	  
		}catch (Exception e) {
	       e.printStackTrace();
	   }
		   
		   return view;
	  }
	  static final ArrayList<HashMap<String,String>> list = 
		    	new ArrayList<HashMap<String,String>>(); 
	  static final ArrayList<HashMap<String,String>> list2 = 
		    	new ArrayList<HashMap<String,String>>(); 
	  static final ArrayList<HashMap<String,String>> list3 = 
		    	new ArrayList<HashMap<String,String>>(); 
	  public String readMaps3() {
	    	
	        StringBuilder builder = new StringBuilder();
	        HttpClient client = new DefaultHttpClient();
	        HttpPost httppost= new HttpPost("http://volunteernow.kmdotshop.com/appsettings/volunteerhours.php"); // make sure the url is correct.
			
	        try {
	        	SharedPreferences pref = this.getActivity().getApplicationContext().getSharedPreferences("MyPrefs", 0);
				nameValuePairs = new ArrayList<NameValuePair>(1);
				// Getting latitude of the current location
		      String username = pref.getString("email", null).toString().trim();
				// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
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
	    	
	        StringBuilder builder = new StringBuilder();
	        HttpClient client = new DefaultHttpClient();
	        HttpPost httppost= new HttpPost("http://volunteernow.kmdotshop.com/appsettings/getparticulars.php"); // make sure the url is correct.
			
	        try {
	        	SharedPreferences pref = this.getActivity().getApplicationContext().getSharedPreferences("MyPrefs", 0);
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
	  public String readMaps1() {
	    	
	        StringBuilder builder = new StringBuilder();
	        HttpClient client = new DefaultHttpClient();
	        HttpPost httppost= new HttpPost("http://volunteernow.kmdotshop.com/appsettings/getcurrentregistered.php"); // make sure the url is correct.
			
	        try {
	        	SharedPreferences pref = this.getActivity().getApplicationContext().getSharedPreferences("MyPrefs", 0);
				nameValuePairs = new ArrayList<NameValuePair>(1);
				// Getting latitude of the current location
		      String username = pref.getString("email", null).toString().trim();
				// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
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
	        HttpPost httppost= new HttpPost("http://volunteernow.kmdotshop.com/appsettings/getpreviousvolunteer.php"); // make sure the url is correct.
			
	        try {
	        	SharedPreferences pref = this.getActivity().getApplicationContext().getSharedPreferences("MyPrefs", 0);
				nameValuePairs = new ArrayList<NameValuePair>(1);
				// Getting latitude of the current location
		      String username = pref.getString("email", null).toString().trim();
				// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
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
	            Log.e("ERROR", "Failed to load map");
	          }
	        } catch (ClientProtocolException e) {
	          e.printStackTrace();
	        } catch (IOException e) {
	          e.printStackTrace();
	        }
	        return builder.toString();
	      }
}
	        



	  
	    
	

