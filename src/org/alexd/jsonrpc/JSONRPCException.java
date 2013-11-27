package org.alexd.jsonrpc;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents an error during JSON-RPC method call. Various reasons can make a
 * JSON-RPC call fail (network not available, non existing method, error during
 * the remote execution ...) You can use the inherited method getCause() to see
 * which Exception has caused a JSONRPCException to be thrown
 * 
 * @author Alexandre
 * 
 */
public class JSONRPCException extends Exception {

	private static final long serialVersionUID = 4657697652848090922L;

	private String name = "";
	private int errorCode = 0;
	private String message = "";
	
	public JSONRPCException(String message, Throwable ex) {
		super(ex);
		
		this.message = message;
	}

	public JSONRPCException(String name, int errorCode, String message) {
		this.name = name;
		this.errorCode = errorCode;
		this.message = message;
	}

	public JSONRPCException(JSONObject errorObject) {
		super();

		try {
			this.name = errorObject.getString("name");
			this.errorCode = errorObject.getInt("code");
			this.message = errorObject.getString("message");
		} catch (JSONException e) {
		}
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getName() {
		return name;
	}

	public String getMessage() {
		return message;
	}

}
