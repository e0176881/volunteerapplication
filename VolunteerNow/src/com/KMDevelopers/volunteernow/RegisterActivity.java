package com.KMDevelopers.volunteernow;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class RegisterActivity extends Activity {
	static final int DATE_DIALOG_ID = 999;
	Button b;
	EditText pass,email, fullname,nric,mobilenumber;
	TextView tv;
	HttpPost httppost;
	StringBuffer buffer;
	SQLiteDatabase db;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;
	  public int currentimageindex=0;
	    Timer timer;
	    TimerTask task;
	    ImageView slidingimage;
	    
	   
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

       //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.register); 
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.register);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/verdana.ttf"); 
        TextView Pagename = (TextView)findViewById(R.id.pagename);
        Pagename.setTypeface(type);
        TextView lbu = (TextView)findViewById(R.id.lbusername);
        TextView lbp = (TextView)findViewById(R.id.lbpassword);
        
        //TextView lbe = (TextView)findViewById(R.id.lbemail);
        lbu.setTypeface(type);
        lbp.setTypeface(type);

        //lbe.setTypeface(type);
        //slidingimage = (ImageView)findViewById(R.id.ImageView3_Left);
        //slidingimage.setImageResource(R.drawable.staticimage);
        
        /*
        final Handler mHandler = new Handler();
        
        // Create runnable for posting
        final Runnable mUpdateResults = new Runnable() {
            public void run() {
 
                AnimateandSlideShow();
 
            }
        };
 
        int delay = 3; // delay for 1 sec.
 
        int period = 6000; // repeat every 4 sec.
 
        Timer timer = new Timer();
 
        timer.scheduleAtFixedRate(new TimerTask() {
 
        public void run() {
 
             mHandler.post(mUpdateResults);
 
        }
 
        }, delay, period);*/
        

        b = (Button)findViewById(R.id.btnRegister); 
        b.setTypeface(type);
        email = (EditText)findViewById(R.id.reg_email);
        pass= (EditText)findViewById(R.id.reg_password);
        tv = (TextView)findViewById(R.id.TextViewaaa);
        nric = (EditText)findViewById(R.id.reg_ICNumber);
        fullname = (EditText)findViewById(R.id.reg_fullName);
        mobilenumber = (EditText)findViewById(R.id.reg_mobilenumber);
        
        
        pass.setTypeface(type);
        tv.setTypeface(type);
        email.setTypeface(type);
        nric.setTypeface(type);
        fullname.setTypeface(type);
        mobilenumber.setTypeface(type);
        
        b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String upw = pass.getText().toString();
				String uemail = email.getText().toString();
				 if( uemail.length() == 0)
				{
					showAlert4();
				}
				else if(upw.length() == 0)
				{
					
					showAlert3();
				}
				
				else {
				dialog = ProgressDialog.show(RegisterActivity.this, "", 
                        "Registering...", true);
				 new Thread(new Runnable() {
					    public void run() {
					    	Register();					      
					    }
					  }).start();				
					}
			}
		});
        //Button frgtpw = (Button) findViewById(R.id.link_to_register2);
        //frgtpw.setTypeface(type);
        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);
        loginScreen.setTypeface(type);
     // Listening to Login Screen link
        /*frgtpw.setOnClickListener(new View.OnClickListener() {
        
            public void onClick(View arg0) {
                                // Closing registration screen
                // Switching to Login Screen/closing register screen
            	Intent i = new Intent(getApplicationContext(), ForgetPassword.class);
				startActivity(i);
            }
        });
        */
        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {
        
            public void onClick(View arg0) {
                                // Closing registration screen
                // Switching to Login Screen/closing register screen
                finish();
            }
        });
        ImageButton bck = (ImageButton) findViewById(R.id.imageButton1);
        bck.setOnClickListener(new View.OnClickListener() {
       			
       			public void onClick(View v) {
       				// Switching to Register screen
       				finish();
       			}
       		});

           
    }
   
    void Register(){
    	db=openOrCreateDatabase("VolunteerNow", Context.MODE_PRIVATE, null);
    	 try {
  			 db.execSQL("DROP TABLE IF EXISTS " + "account");
  		 		db.execSQL("CREATE TABLE IF NOT EXISTS account(fullname VARCHAR,contactno VARCHAR,email VARCHAR,nric VARCHAR);");
  	            	 db.execSQL("INSERT INTO account VALUES('"+fullname.getText().toString().trim()+"','"+mobilenumber.getText().toString().trim()+"','"+email.getText().toString().trim()+
  	         				   "','"+nric.getText().toString().trim()+"');");
  	           
  		 
  	}catch (Exception e) {
        e.printStackTrace();
    } 
  		try{			
  			
  			httpclient=new DefaultHttpClient();
  			httppost= new HttpPost("http://volunteernow.kmdotshop.com/appsettings/register.php"); // make sure the url is correct.
  			nameValuePairs = new ArrayList<NameValuePair>(5); // $Edittext_value = $_POST['Edittext_value'];
  			nameValuePairs.add(new BasicNameValuePair("password",pass.getText().toString().trim())); 
  			nameValuePairs.add(new BasicNameValuePair("Email",email.getText().toString().trim())); 
  			nameValuePairs.add(new BasicNameValuePair("nric",nric.getText().toString().trim()));
  			nameValuePairs.add(new BasicNameValuePair("fullname",fullname.getText().toString().trim())); 
  			nameValuePairs.add(new BasicNameValuePair("mobilenumber",mobilenumber.getText().toString().trim())); 
  			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
  			
  			ResponseHandler<String> responseHandler = new BasicResponseHandler();
  			final String response = httpclient.execute(httppost, responseHandler);
  			System.out.println("Response : " + response); 
  			runOnUiThread(new Runnable() {
  			    public void run() {
  			   // 	tv.setText("Response from PHP : " + response);
  					dialog.dismiss();
  			    }
  			});
  			
  			
  			if(response.contains("success")){
  				runOnUiThread(new Runnable() {
  				    public void run() {
  				    	Toast.makeText(RegisterActivity.this,"Registration Success", Toast.LENGTH_SHORT).show();
  				    
  				    }
  				});
  				
  				SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefs", MODE_APPEND); // 0 - for private mode
  				Editor editor = pref.edit();
  				editor.putString("email", email.getText().toString().trim()); // Storing string
  				editor.commit();
  				startActivity(new Intent(RegisterActivity.this, VolunteerNowHome.class));
  			
  			}else{
  				showAlert();				
  			}
  			
  		}catch(Exception e){
  			dialog.dismiss();
  			System.out.println("Exception : " + e.getMessage());
  		}
  	}
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.exit_title:
        	this.finish();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        return true;

        default:
        return super.onOptionsItemSelected(item);
        }
    }
    
  	public void showAlert(){
  		RegisterActivity.this.runOnUiThread(new Runnable() {
  		    public void run() {
  		    	AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
  		    	builder.setTitle("Error");
  		    	builder.setMessage("Username is already used")  
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
  	public void showAlert2(){
  		RegisterActivity.this.runOnUiThread(new Runnable() {
  		    public void run() {
  		    	AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
  		    	builder.setTitle("Error");
  		    	builder.setMessage("Please enter username (HandPhone Number)!")  
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
  	public void showAlert3(){
  		RegisterActivity.this.runOnUiThread(new Runnable() {
  		    public void run() {
  		    	AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
  		    	builder.setTitle("Error");
  		    	builder.setMessage("Please enter Password!")  
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
  	public void showAlert4(){
  		RegisterActivity.this.runOnUiThread(new Runnable() {
  		    public void run() {
  		    	AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
  		    	builder.setTitle("Error");
  		    	builder.setMessage("Please enter your Email Address!")  
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
}