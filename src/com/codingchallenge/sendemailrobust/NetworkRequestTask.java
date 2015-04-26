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
public class NetworkRequestTask extends AsyncTask<Map, String, JSONObject> {
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
    protected JSONObject doInBackground(Map... maps) {    	
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
			JSONObject returnJson = new JSONObject();
			try {
				returnJson.put("status", -1);
				returnJson.put("error", "Error converting json to addresses");
			} catch (JSONException e) {
			}
			return returnJson; 
		}
   	
        //passes the results to a string builder/entity
        StringEntity se;
		try {
			se = new StringEntity(json.toString());
		} catch (UnsupportedEncodingException e) {
			JSONObject returnJson = new JSONObject();
			try {
				returnJson.put("status", -2);
				returnJson.put("error", "Error converting json to stringentity");
			} catch (JSONException e1) {
			}
			return returnJson; 
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
			JSONObject returnJson = new JSONObject(returnValue);
			return returnJson;
		} catch (ClientProtocolException e) {
			JSONObject returnJson = new JSONObject();
			try {
				returnJson.put("status", -3);
				returnJson.put("error", e.getMessage());
			} catch (JSONException e1) {
			}
			return returnJson;
		} catch (IOException e) {
			JSONObject returnJson = new JSONObject();
			try {
				returnJson.put("status", -4);
				returnJson.put("error", e.getMessage());
			} catch (JSONException e1) {
			}
			return returnJson;
		} catch (JSONException e) {
			JSONObject returnJson = new JSONObject();
			try {
				returnJson.put("status", -5);
				returnJson.put("error", "Unable to jsonify response");
			} catch (JSONException e2) {				
			}
			return returnJson;
		}
    }

    /***
     * Notify any registered listener of response
     */
    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);
 
        if (_requestListener != null) {
        	_requestListener.responseReceived(result);
        }
    }
}
