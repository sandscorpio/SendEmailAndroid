package com.codingchallenge.sendemailrobust;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Patterns;

/**
 * Utility class
 */
public class Utils {
	
	/***
	 * Returns true if email is a valid email address
	 * @param email
	 * @return
	 */
	public static boolean IsEmailAddress(String email) {
		java.util.regex.Pattern pattern = Patterns.EMAIL_ADDRESS;  
		Matcher matcher = pattern.matcher(email);  
		return matcher.matches();
	}
	
	/***
	 * Returns default system addresses associated with this device
	 * @param context
	 * @return
	 */
	public static String GetDefaultEmailAddress(Context context) {
		Pattern emailPattern = Patterns.EMAIL_ADDRESS;
		Account[] accounts = AccountManager.get(context).getAccounts();
		for (Account account : accounts) {
		    if (IsEmailAddress(account.name)) {
		        return account.name;
		    }
		}
		
		return null;
	}

}
