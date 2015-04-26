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

}
