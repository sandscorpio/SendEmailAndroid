package com.codingchallenge.sendemailrobust;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity {
	
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
			//send email
		}
	};

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
}
