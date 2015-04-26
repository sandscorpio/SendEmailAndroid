package com.codingchallenge.sendemailrobust;

import org.json.JSONObject;

/***
 * Interface for objects that wish to listen to NetworkRequestTask messages
 * @author sandscorpio
 *
 */
public interface INetworkRequestListener {
	/***
	 * NetworkRequestTask received a response (or error)
	 * @param response
	 */
	void responseReceived(JSONObject response);
}
