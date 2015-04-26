package com.codingchallenge.sendemailrobust;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements INetworkRequestListener {
	private enum EnumValidateInput {OK, FROM_MISSING, FROM_NOT_VALID_EMAIL, TO_MISSING, TO_NOT_VALID_EMAIL};
	
	private EditText mTxtFrom;
	private EditText mTxtTo;
	private EditText mTxtSubject;
	private EditText mTxtBody;
	private Button mBtnSend;

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
	}
	
	private OnClickListener btnSendClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			//validate input
			EnumValidateInput validateInput = validateInput();
			if (validateInput != EnumValidateInput.OK) {
				//input is not valid
				displayError(validateInput);
				return;
			}
			
			//send email
			//NetworkRequestTask sendEmailTask = new NetworkRequestTask(MainActivity.this);
			
			
			//sendEmailTask.execute(params)
		}
	};
	
	private EnumValidateInput validateInput() {
		String from = mTxtFrom.getText().toString();
		String to = mTxtTo.getText().toString();
		
		if (from.isEmpty()) {
			return EnumValidateInput.FROM_MISSING;
		}
		else if (!Utils.IsEmailAddress(from)) {
			return EnumValidateInput.FROM_NOT_VALID_EMAIL;
		}
		else if (to.isEmpty()) {
			return EnumValidateInput.TO_MISSING;
		}
		else if (!Utils.IsEmailAddress(to)) {
			return EnumValidateInput.TO_NOT_VALID_EMAIL;
		}
		
		return EnumValidateInput.OK;
	}
	
	private void displayError(EnumValidateInput validateInput) {
		String errorMsg = "";
		
		switch (validateInput) {
		case FROM_MISSING:
			errorMsg = "From field missing";
			break;
		case FROM_NOT_VALID_EMAIL:
			errorMsg = "From field is not an email address";
			break;
		case TO_MISSING:
			errorMsg = "To field missing";
			break;
		case TO_NOT_VALID_EMAIL:
			errorMsg = "To field is not an email address";
			break;
		}
		
		Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
	}
	
	private HashMap<String, String> prepareMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		return map;		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/***
	 * Received response from server after sending email
	 */
	@Override
	public void responseReceived(String response) {
		Logger logger = Logger.getLogger("");
		logger.log(Level.INFO, String.format("Received response: %s", response));
	}
}
