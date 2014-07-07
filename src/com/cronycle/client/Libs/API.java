package com.cronycle.client.Libs;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.util.Log;

public class API {
	
	private static CronycleApiInterface sCronycleService;
	private static API instance;
	
	Twitter twitter;
	RequestToken requestToken;
//Please put the values of consumerKy and consumerSecret of your app 
	public final static String consumerKey = "6MMo4l9jicpOKGAOZubTWTgq4"; // "your key here";
	public final static String consumerSecret = "FiBsNbjAul4nVqo1mrkuIs0l2mKEEZHaEwEN6m1NHRSub1SXwG"; // "your secret key here";
	public final static String CALLBACKURL = "cronycle:///twitter";  //Callback URL that tells the WebView to load this activity when it finishes with twitter.com. (see manifest)

	public API() {
		twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(consumerKey, consumerSecret);
	}
	
	public static API Current() {
		if (instance == null) instance = new API();
		return instance;
	}
	
    public static CronycleApiInterface getCronycleApiClient() {
        if (sCronycleService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://api.cronycle.com")
                    .build();
            
            Log.i("API", "REST Service created");

            sCronycleService = restAdapter.create(CronycleApiInterface.class);
        }

        return sCronycleService;
    }
    
    /*
	 * - Creates object of Twitter and sets consumerKey and consumerSecret
	 * - Prepares the URL accordingly and opens the WebView for the user to provide sign-in details
	 * - When user finishes signing-in, WebView opens your activity back
	 */
	public String getOAuthLoginUrl() {
		try {
			requestToken = twitter.getOAuthRequestToken(CALLBACKURL);
			return requestToken.getAuthenticationURL();
		} catch (TwitterException ex) {
			Log.e("in API.getOAuthLoginUrl", ex.getMessage());
		}
		
		return null;
	}
	
	public AccessToken getAccessToken() {
		try {
			return twitter.getOAuthAccessToken(requestToken);
		} catch (TwitterException ex) {
			Log.e("in API.getAccessToken", ex.getMessage());
		}
		
		return null;
	}

    public interface CronycleApiInterface {
        @GET("/v3/user.json?auth_token=3e1bd989408c45d9")
        void getUser(@Query("limit") int limit, Callback<CronycleUserData> callback);
    }
	
}
