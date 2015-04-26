package com.codingchallenge.sendemailrobust;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/***
 * Displays a simple email client
 * Emails are sent using Send Robust Email API
 * @author sandscorpio
 *
 */
public class MainActivity extends ActionBarActivity implements INetworkRequestListener {
	private static final String DEBUG_TAG = "SendEmailRobust";
	private static final int API_SUCCESS = 200;
	
	private enum EnumValidateInput {OK, 
									FROM_MISSING, FROM_NOT_VALID_EMAIL, 
									TO_MISSING, TO_NOT_VALID_EMAIL}
	
	private final int RESULT_CONTACTS = Activity.RESULT_FIRST_USER+1;
	
	private EditText mTxtFrom;
	private EditText mTxtTo;
	private EditText mTxtSubject;
	private EditText mTxtBody;
	private Button mBtnSend;
	private Button mBtnSelectContact;
	
	private String mFrom;
	private String[] mTo;
	private String mSubject;
	private String mBody;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mTxtFrom = (EditText) findViewById (R.id.txtFrom);
		mTxtTo = (EditText) findViewById (R.id.txtTo);
		mTxtSubject = (EditText) findViewById (R.id.txtSubject);
		mTxtBody = (EditText) findViewById (R.id.txtBody);
		mBtnSend = (Button) findViewById (R.id.btnSend);
		mBtnSend.setOnClickListener(btnSendClicked);
		mBtnSelectContact = (Button) findViewById (R.id.btnSelectContact);
		mBtnSelectContact.setOnClickListener(btnSelectContactClicked);
		
		setDefaultUI();
	}
	
	/**
	 * Set FROM field to system email address associated with this user/device (if available)
	 */
	private void setDefaultUI() {
		//set FROM field to system email address associated with this user/device
		String defaultEmail = Utils.GetDefaultEmailAddress(this);
		if (defaultEmail != null) {
			mTxtFrom.setText(defaultEmail);
		}
	}
	
	/***
	 * Send email.
	 * All input fields must be valid.
	 * Email will be sent in a separate thread.
	 */
	private OnClickListener btnSendClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			//prevent additional clicks until this click is fully processed
			mBtnSend.setEnabled(false);
			
			if (!sendEmailClicked()) {
				//unable to send email due to input error
				mBtnSend.setEnabled(true);
			}
		}
	};
	
	/**
	 * Returns true if all input fields are valid and submits email request over network
	 * Returns false otherwise
	 * @return
	 */
	private boolean sendEmailClicked() {
		EnumValidateInput validateFrom = setFrom();
		if (validateFrom != EnumValidateInput.OK) {
			displayError(validateFrom);
			return false;
		}
		
		EnumValidateInput validateTo = setTo();
		if (validateTo != EnumValidateInput.OK) {
			displayError(validateTo);
			return false;
		}
		
		EnumValidateInput validateSubject = setSubject();
		if (validateSubject != EnumValidateInput.OK) {
			displayError(validateSubject);
			return false;
		}
		
		EnumValidateInput validateBody = setBody();
		if (validateBody != EnumValidateInput.OK) {
			displayError(validateBody);
			return false;
		}
		
		//package fields into maps
		HashMap<String, String> map1 = prepareMap(mFrom, mSubject, mBody);
		HashMap<String, String[]> map2 = prepareMap(mTo);
		
		//send email
		NetworkRequestTask sendEmailTask = new NetworkRequestTask(MainActivity.this);			
		sendEmailTask.execute(map1, map2);
		return true;
	}
	
	/***
	 * Set FROM
	 * Returns OK or error
	 * @return
	 */
	private EnumValidateInput setFrom() {
		String from = mTxtFrom.getText().toString().trim();

		if (from.isEmpty()) {
			return EnumValidateInput.FROM_MISSING;
		}
		else if (!Utils.IsEmailAddress(from)) {
			return EnumValidateInput.FROM_NOT_VALID_EMAIL;
		}
		
		mFrom = from;
		return EnumValidateInput.OK;
	}
	
	/***
	 * Set TO
	 * TO is a comma separated string consisting of 1 or more valid email addresses
	 * Returns OK or error
	 * @return
	 */
	private EnumValidateInput setTo() {
		String to = mTxtTo.getText().toString().trim();
		
		if (to.isEmpty()) {
			return EnumValidateInput.TO_MISSING;
		}
		else {
			//check if all TO fields are valid email addresses
			String[] toAddresses = to.split(",");
			for (String toAddress : toAddresses) {
				if (!Utils.IsEmailAddress(toAddress.trim())) {
					return EnumValidateInput.TO_NOT_VALID_EMAIL;
				}
			}
		}
		
		String[] toAddresses = to.split(",");
		for (String toAddress : toAddresses) {
			toAddress = toAddress.trim();
		}
		mTo = toAddresses;
		
		return EnumValidateInput.OK;
	}
	
	/***
	 * Set Subject (may be blank).
	 * Returns OK or error
	 * @return
	 */
	private EnumValidateInput setSubject() {
		String subject = mTxtSubject.getText().toString().trim();
		
		if (subject.isEmpty()) {
			//TODO: display a warning if user really wants empty subject, but it is not an error
		}
		
		mSubject = subject;		
		return EnumValidateInput.OK;
	}
	
	/***
	 * Set Body (may be blank).
	 * Returns OK or error
	 * @return
	 */
	private EnumValidateInput setBody() {
		String body = mTxtBody.getText().toString().trim();
		
		if (body.isEmpty()) {
			//TODO: display a warning if user really wants empty body, but it is not an error
		}
		
		mBody = body;		
		return EnumValidateInput.OK;
	}
	
	/***
	 * Helper method to display an error based on invalid input
	 * @param validateInput
	 */
	private void displayError(EnumValidateInput validateInput) {
		String errorMsg = "";
		
		switch (validateInput) {
		case FROM_MISSING:
			errorMsg = getString(R.string.errorFromMissing);
			break;
		case FROM_NOT_VALID_EMAIL:
			errorMsg = getString(R.string.errorFromNotEmail);
			break;
		case TO_MISSING:
			errorMsg = getString(R.string.errorToMissing);
			break;
		case TO_NOT_VALID_EMAIL:
			errorMsg = getString(R.string.errorToNotEmail);
			break;
		}
		
		Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
	}
	
	/***
	 * Helper method to put from, subject, and body into a map
	 * @param from
	 * @param subject
	 * @param body
	 * @return
	 */
	private HashMap<String, String> prepareMap(String from, String subject, String body) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("from", from);
		map.put("subject", subject);
		map.put("body", body);
		return map;		
	}
	
	/***
	 * Helper method to put "to" into a map
	 * @param from
	 * @param subject
	 * @param body
	 * @return
	 */
	private HashMap<String, String[]> prepareMap(String[] to) {
		HashMap<String, String[]> map = new HashMap<String, String[]>();
		map.put("to", to);
		return map;		
	}

	/***
	 * Received response from server after sending email
	 */
	@Override
	public void responseReceived(JSONObject response) {
		//user can again press send email button 
		mBtnSend.setEnabled(true);
		
		Logger logger = Logger.getLogger("");
		logger.log(Level.INFO, String.format("Received response: %s", response));
		
		String msg = response.optInt("status") == API_SUCCESS ? getString(R.string.sentEmail) : getString(R.string.sentEmailFailed);
		Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
	}
	
	/***
	 * Allow user to select a contact and import their email address
	 */
	private OnClickListener btnSelectContactClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, Email.CONTENT_URI);
		    contactPickerIntent.setType(ContactsContract.CommonDataKinds.Email.CONTENT_TYPE );
		    startActivityForResult(contactPickerIntent, RESULT_CONTACTS);
		}
	};
	
	/***
	 * Import selected contact's email address into TO field
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		switch (requestCode) {
		case RESULT_CONTACTS:
			if (resultCode != RESULT_OK) {
				break;
			}
			
			Cursor cursor = null;
			String email = null; 
			try {
				Uri result = data.getData();
				
				// get the contact id from the Uri
				String id = result.getLastPathSegment();
				
				cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
		                null,
		                ContactsContract.CommonDataKinds.Email._ID.concat("=?"),
		                new String[] { id },
		                null);
				
				int emailIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);

				// let's just get the first email
				if (cursor.moveToFirst()) {
					email = cursor.getString(emailIdx);
				} 
			} 
			catch (Exception e) {
				Log.e(DEBUG_TAG, "Failed to get email data", e);
			} 
			finally {
				if (cursor != null) {
					cursor.close();
				}
				
				//append selected email address
				if (email != null) {
					String to = mTxtTo.getText().toString();
					if (!to.isEmpty()) {
						to += ",";
					}
					to += email;
					mTxtTo.setText(to);
				}				
			}
			break;
		}
	}
}
