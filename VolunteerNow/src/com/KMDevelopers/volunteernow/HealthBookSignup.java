package com.KMDevelopers.volunteernow;



import java.util.ArrayList;
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

import com.google.android.gms.maps.SupportMapFragment;



import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class HealthBookSignup extends Fragment {
	
    View view;
	  @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
		  
		  
		  view = inflater.inflate(R.layout.healthbooksignup, container, false);
		   Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/verdana.ttf"); 
	        /* TextView Pagename = (TextView)view.findViewById(R.id.pagename) */
	        TextView lb1 = (TextView)view.findViewById(R.id.lbusername);
	        lb1.setTypeface(type);
	        TextView lb2 = (TextView)view.findViewById(R.id.regg);
	        lb2.setTypeface(type);
	        
	       
	        //Pagename.setTypeface(type);
	        TextView loginScreen = (TextView) view.findViewById(R.id.link_to_login);
	        loginScreen.setTypeface(type);
	     
	        loginScreen.setOnClickListener(new View.OnClickListener() {
	        
	            public void onClick(View arg0) {
	                                // Closing registration screen
	                // Switching to Login Screen/closing register screen
	            	startActivity(new Intent(getActivity(), LoginActivity.class));
	               getActivity().finish();
	            }
	        });

	        TextView signup = (TextView) view.findViewById(R.id.btnBook);
	        signup.setTypeface(type);
	     
	        signup.setOnClickListener(new View.OnClickListener() {
	        
	            public void onClick(View arg0) {
	                                // Closing registration screen
	                // Switching to Login Screen/closing register screen
	            	startActivity(new Intent(getActivity(), RegisterActivity.class));
	               getActivity().finish();
	            }
	        });
	       
	    
		  
		  
	        return view;
	    
}

	  
	
}
