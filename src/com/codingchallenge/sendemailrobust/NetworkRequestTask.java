package com.codingchallenge.sendemailrobust;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

/***
 * NetworkRequestTask submits an email request to our backend API.
 * It is done in a separate thread so that the GUI thread is not blocked.
 * @author sandscorpio
 *
 */
public class NetworkRequestTask extends AsyncTask<Map, String, String> {
	private static final String EMAIL_ENDPOINT = "";
	
	private INetworkRequestListener _requestListener;
	
	public NetworkRequestTask(INetworkRequestListener requestListener) {
		super();
		_requestListener = requestListener;
	}
	
	@Override
	protected void onPreExecute() {
	}

	/***
	 * Submit email request asynchronously
	 * 2 maps are expected to be passed: 
	 *  first contains from, subject, body
	 *  second containts to addresses
	 * A HTTP Post request is done to submit the request in JSON
	 * Any registered listener is notified when response is received
	 */
    @Override
    protected String doInBackground(Map... maps) {    	
        //parse from, title, body from first map to json
        JSONObject json = new JSONObject(maps[0]);
        
        //parse to addresses from second map to json
        JSONArray jsonTo = new JSONArray();
        String[] toAddresses = (String[]) maps[1].get("to");
        for (String toAddress : toAddresses) {
        	jsonTo.put(toAddress);
        }
        try {
			json.put("to", jsonTo);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "Error converting json to addresses"; 
		}
   	
        //passes the results to a string builder/entity
        StringEntity se;
		try {
			se = new StringEntity(json.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error converting json to stringentity"; 
		}
		
    	//instantiates httpclient to make request
        DefaultHttpClient httpclient = new DefaultHttpClient();

        //url with the post data
        HttpPost httpost = new HttpPost(EMAIL_ENDPOINT);

        //sets the post request as the resulting string
        httpost.setEntity(se);
        
        //sets a request header so the page receiving the request will know what to do with it
        httpost.setHeader("Content-type", "application/json");

        //Handles what is returned from the page 
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try {
			String returnValue = httpclient.execute(httpost, responseHandler);
			return returnValue;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Unable to make request due to " + e.getMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Unable to make request due to " + e.getMessage();
		}
    }

    /***
     * Notify any registered listener of response
     */
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
 
        if (_requestListener != null) {
        	_requestListener.responseReceived(result);
        }
    }
}
