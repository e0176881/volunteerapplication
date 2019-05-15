package com.KMDevelopers.volunteernow;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;


public class Application extends android.app.Application {

  public Application() {
  }

  @Override
  public void onCreate() {
    super.onCreate();

	// Initialize the Parse SDK.
    Parse.initialize(this, "IkrEhuW6qBe49lIrl2XBbCBukQGmIkgYbm8mSJa1", "yp2dkFbnZzQm8aJPdoqIwaWtwDmXdIQPesqpMBEq"); 

	// Specify an Activity to handle all pushes by default.
	PushService.setDefaultPushCallback(this, VolunteerNowHome.class);
	
	ParseInstallation.getCurrentInstallation().saveInBackground(); PushService.subscribe(this, "Broadcast", VolunteerNowHome.class);
  }
}