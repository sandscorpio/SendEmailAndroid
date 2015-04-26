package com.codingchallenge.sendemailrobust;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Patterns;

/**
 * Utility class
 */
public class Utils {
	
	public static boolean IsEmailAddress(String email) {
		java.util.regex.Pattern pattern = Patterns.EMAIL_ADDRESS;  
		Matcher matcher = pattern.matcher(email);  
		return matcher.matches();
	}
	
	/**
	 * Helper method converts Map params to JSONObject
	 * Code from https://stackoverflow.com/questions/6218143/how-to-send-post-request-in-json-using-httpclient
	 * @param params
	 * @return
	 * @throws JSONException
	 */
	private static JSONObject getJsonObjectFromMap(Map params) throws JSONException {

	    //all the passed parameters from the post request
	    //iterator used to loop through all the parameters
	    //passed in the post request
	    Iterator iter = params.entrySet().iterator();

	    //Stores JSON
	    JSONObject holder = new JSONObject();

	    //using the earlier example your first entry would get email
	    //and the inner while would get the value which would be 'foo@bar.com' 
	    //{ fan: { email : 'foo@bar.com' } }

	    //While there is another entry
	    while (iter.hasNext()) 
	    {
	        //gets an entry in the params
	        Map.Entry pairs = (Map.Entry)iter.next();

	        //creates a key for Map
	        String key = (String)pairs.getKey();

	        //Create a new map
	        Map m = (Map)pairs.getValue();   

	        //object for storing Json
	        JSONObject data = new JSONObject();

	        //gets the value
	        Iterator iter2 = m.entrySet().iterator();
	        while (iter2.hasNext()) 
	        {
	            Map.Entry pairs2 = (Map.Entry)iter2.next();
	            data.put((String)pairs2.getKey(), (String)pairs2.getValue());
	        }

	        //puts email and 'foo@bar.com'  together in map
	        holder.put(key, data);
	    }
	    
	    return holder;
	}
}
