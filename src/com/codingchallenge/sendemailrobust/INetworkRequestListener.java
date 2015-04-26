package com.codingchallenge.sendemailrobust;

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
	void responseReceived(String response);
}
