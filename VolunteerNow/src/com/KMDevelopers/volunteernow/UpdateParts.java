package com.KMDevelopers.volunteernow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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




import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateParts extends Activity {
	EditText pass,email, fullname,nric,mobilenumber;
	Button b;
	TextView tv;
	HttpPost httppost;
	StringBuffer buffer;
	String fn,nricla,emailla,contactnola,un;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;
	 SQLiteDatabase db;
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	 //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

       //set content view AFTER ABOVE sequence (to avoid crash)
       
        super.onCreate(savedInstanceState);
    
        setContentView(R.layout.updateparts);
        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/verdana.ttf"); 
        TextView Pagename = (TextView)findViewById(R.id.pagename);
        Pagename.setTypeface(type);
        TextView lbu = (TextView)findViewById(R.id.lbusername);
       
        
       
        lbu.setTypeface(type);
      

     

        b = (Button)findViewById(R.id.btnRegister); 
        b.setTypeface(type);
        tv = (TextView)findViewById(R.id.TextViewaaa);
        email = (EditText)findViewById(R.id.reg_email);
        nric = (EditText)findViewById(R.id.reg_ICNumber);
        fullname = (EditText)findViewById(R.id.reg_fullName);
        mobilenumber = (EditText)findViewById(R.id.reg_mobilenumber);
      
       
        tv.setTypeface(type);
        email.setTypeface(type);
        email.setEnabled(false);
        nric.setTypeface(type);
        fullname.setTypeface(type);
        mobilenumber.setTypeface(type);
        
        
        
        String getParticulars = getParticulars();
		 try {
	            JSONArray jsonArray = new JSONArray(getParticulars);
	            for (int i = 0; i < jsonArray.length(); i++) {
	            	 JSONObject jsonObject = jsonArray.getJSONObject(i);
			          fn =  jsonObject.getString("fullname");
			          contactnola =  jsonObject.getString("contactno");
			          emailla=jsonObject.getString("email");
			          nricla=jsonObject.getString("nric");
	            }
	            fullname.setText(fn.replace("null", ""));
	            email.setText(emailla);
	            nric.setText(nricla.replace("null", ""));
	            mobilenumber.setText(contactnola.replace("null", ""));
	            

		    

	   	  
	}catch (Exception e) {
      e.printStackTrace();
  }
        
        b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String fullnamez = fullname.getText().toString();
				final String contactno = mobilenumber.getText().toString();
				final String nricz = nric.getText().toString();
				final String uemail = email.getText().toString();
				if(fullnamez.length() == 0)
				{
					showAlertfullname();
					
				}
				else if(contactno.length() == 0)
				{
					
					showAlerthp();
				}
				else if( nricz.length() == 0)
				{
					showAlertnric();
				}
				else if( uemail.length() == 0)
				{
					showAlertemail();
				}
				else {
				dialog = ProgressDialog.show(UpdateParts.this, "", 
                        "Updating...", true);
				 new Thread(new Runnable() {
					    public void run() {
		
					    	Update(uemail,nricz,contactno,fullnamez);					      
					    }
					  }).start();				
					}
			}
		});
       
        
        ImageButton bck = (ImageButton) findViewById(R.id.imageButton1);
        bck.setOnClickListener(new View.OnClickListener() {
       			
       			public void onClick(View v) {
       				Toast.makeText(UpdateParts.this,"Please update your particulars", Toast.LENGTH_SHORT).show();
       				
       			}
       		});

           
    }
	@Override
	public void onBackPressed()
	{
		Toast.makeText(UpdateParts.this,"Please update your particulars", Toast.LENGTH_SHORT).show();
	   //thats it
	}
	  public String getParticulars() {
	    	
	        StringBuilder builder = new StringBuilder();
	        HttpClient client = new DefaultHttpClient();
	        HttpPost httppost= new HttpPost("http://volunteernow.kmdotshop.com/appsettings/getparticulars.php"); // make sure the url is correct.
			// got return smth leh this
	        
	        try {
	        	SharedPreferences pref = this.getApplicationContext().getSharedPreferences("MyPrefs", 0);
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
	  public void showAlerthp(){
	  		UpdateParts.this.runOnUiThread(new Runnable() {
	  		    public void run() {
	  		    	AlertDialog.Builder builder = new AlertDialog.Builder(UpdateParts.this);
	  		    	builder.setTitle("Error");
	  		    	builder.setMessage("Please enter HandPhone Number!")  
	  		    	       .setCancelable(false)
	  		    	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	  		    	           public void onClick(DialogInterface dialog, int id) {
	  		    	           }
	  		    	       });		    	       
	  		    	AlertDialog alert = builder.create();
	  		    	alert.show();		    	
	  		    }
	  		});
	  	}
	  public void showAlertnric(){
	  		UpdateParts.this.runOnUiThread(new Runnable() {
	  		    public void run() {
	  		    	AlertDialog.Builder builder = new AlertDialog.Builder(UpdateParts.this);
	  		    	builder.setTitle("Error");
	  		    	builder.setMessage("Please enter NRIC!")  
	  		    	       .setCancelable(false)
	  		    	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	  		    	           public void onClick(DialogInterface dialog, int id) {
	  		    	           }
	  		    	       });		    	       
	  		    	AlertDialog alert = builder.create();
	  		    	alert.show();		    	
	  		    }
	  		});
	  	}
	  public void showAlertemail(){
	  		UpdateParts.this.runOnUiThread(new Runnable() {
	  		    public void run() {
	  		    	AlertDialog.Builder builder = new AlertDialog.Builder(UpdateParts.this);
	  		    	builder.setTitle("Error");
	  		    	builder.setMessage("Please enter email!")  
	  		    	       .setCancelable(false)
	  		    	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	  		    	           public void onClick(DialogInterface dialog, int id) {
	  		    	           }
	  		    	       });		    	       
	  		    	AlertDialog alert = builder.create();
	  		    	alert.show();		    	
	  		    }
	  		});
	  	}
	  public void showAlertfullname(){
	  		UpdateParts.this.runOnUiThread(new Runnable() {
	  		    public void run() {
	  		    	AlertDialog.Builder builder = new AlertDialog.Builder(UpdateParts.this);
	  		    	builder.setTitle("Error");
	  		    	builder.setMessage("Please enter fullname!")  
	  		    	       .setCancelable(false)
	  		    	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	  		    	           public void onClick(DialogInterface dialog, int id) {
	  		    	           }
	  		    	       });		    	       
	  		    	AlertDialog alert = builder.create();
	  		    	alert.show();		    	
	  		    }
	  		});
	  	}
	  void Update(String em,String nric,String mobileno,String fn){
		  try {
			 	db=openOrCreateDatabase("VolunteerNow", Context.MODE_PRIVATE, null);
	   			 db.execSQL("DROP TABLE IF EXISTS " + "account");
	   		 		db.execSQL("CREATE TABLE IF NOT EXISTS account(fullname VARCHAR,contactno VARCHAR,email VARCHAR,nric VARCHAR);");
	   	         
	   	            	 db.execSQL("INSERT INTO account VALUES('"+fn+"','"+mobileno+"','"+em+
	   	         				   "','"+nric+"');");
	   	            
	   		 
	   	}catch (Exception e) {
	         e.printStackTrace();
	     }
	  		try{			
	  			SharedPreferences pref = this.getApplicationContext().getSharedPreferences("MyPrefs", 0);
	  			httpclient=new DefaultHttpClient();
	  			httppost= new HttpPost("http://volunteernow.kmdotshop.com/appsettings/update.php"); // make sure the url is correct.
	  			nameValuePairs = new ArrayList<NameValuePair>(4);
	  			
	  			nameValuePairs.add(new BasicNameValuePair("nric",nric.toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
	  			nameValuePairs.add(new BasicNameValuePair("fullname",fn.toString().trim())); 
	  			nameValuePairs.add(new BasicNameValuePair("mobileno",mobileno.toString().trim())); 
	  			nameValuePairs.add(new BasicNameValuePair("email",em.toString().trim())); 
	  			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	  			
	  			ResponseHandler<String> responseHandler = new BasicResponseHandler();
	  			final String response = httpclient.execute(httppost, responseHandler);
	  			System.out.println("Response : " + response); 
	  			
	  			
	  			if(response.contains("success")){
	  				
	  				runOnUiThread(new Runnable() {
	  				    public void run() {
	  				    	
	  				    


	  				    	Toast.makeText(UpdateParts.this,"Particulars Updated!", Toast.LENGTH_SHORT).show();
	  				    	startActivity(new Intent(UpdateParts.this, VolunteerNowHome.class));
	  				    }
	  				});
	  			
	  		
	  			}else{
	  				showAlert();				
	  			}
	  			
	  		}catch(Exception e){
	  			dialog.dismiss();
	  			System.out.println("Exception : " + e.getMessage());
	  		}
	  	}
	  	public void showAlert(){
	  		UpdateParts.this.runOnUiThread(new Runnable() {
	  		    public void run() {
	  		    	AlertDialog.Builder builder = new AlertDialog.Builder(UpdateParts.this);
	  		    	builder.setTitle("Error");
	  		    	builder.setMessage("Update Error")  
	  		    	       .setCancelable(false)
	  		    	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	  		    	           public void onClick(DialogInterface dialog, int id) {
	  		    	           }
	  		    	       });		    	       
	  		    	AlertDialog alert = builder.create();
	  		    	alert.show();		    	
	  		    }
	  		});
	  	}
	  	 @Override
		    protected void onResume() {
		    
	  		super.onResume();
		       
		      
		       
		       
		       String gg = null,ggg = null,gggg = null,ggggg = null;
		       String getParticulars = getParticulars();
				 try {
			            JSONArray jsonArray = new JSONArray(getParticulars);
			            for (int i = 0; i < jsonArray.length(); i++) {
			            	 JSONObject jsonObject = jsonArray.getJSONObject(i);
			            	  
					        gg= jsonObject.getString("fullname");
					         ggg=  jsonObject.getString("contactno");
					        gggg=  jsonObject.getString("email");
					        ggggg= jsonObject.getString("nric");// lol
					        //LOL srs? YUP ccb rly sia
					        // ok wait got 1 mre i etst again wait// zz now it doesnt run
			            }
			            //wait 
			            //check if fields empty // ok. now is all fields not empty le. i  debug ow test
			          if(gg != "null" && ggg !="null" && gggg!="null" && ggggg!="null" ) //&& gg != "" && ggg !="" && gggg!="" && ggggg!="" 
			          {
			        	  startActivity(new Intent(UpdateParts.this, VolunteerNowHome.class)); 
			          }
			            

			          

			   	  
			}catch (Exception e) {
		      e.printStackTrace();
		  } 
		    }
}
