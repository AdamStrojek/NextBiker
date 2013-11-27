package org.alexd.jsonrpc;

import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public abstract class JSONRPCClient {

	/**
	 * Create a JSONRPCClient from a given uri 
	 * @param uri The URI of the JSON-RPC service
	 * @return a JSONRPCClient instance acting as a proxy for the web service
	 */
	public static JSONRPCClient create(String uri)
	{
		return new JSONRPCHttpClient(uri);
	}
	
	protected abstract JSONObject doJSONRequest(JSONObject request) throws JSONRPCException;
	
	private static Random rnd = new Random();
	
	protected JSONObject doRequest(String method, Map<String,?> params) throws JSONRPCException
	{
		//Copy method arguments in a json object
		JSONObject jsonParams = new JSONObject(params);
		
		//Create the json request object
		JSONObject jsonRequest = new JSONObject();
		try 
		{
			//id hard-coded at 1 for now
			jsonRequest.put("id", rnd.nextInt());
			jsonRequest.put("method", method);
			jsonRequest.put("params", jsonParams);
		}
		catch (JSONException e1)
		{
			throw new JSONRPCException("Invalid JSON request", e1);
		}
		return doJSONRequest(jsonRequest);
	}
	
	protected int soTimeout = 0, connectionTimeout = 0;
	
	/**
	 * Get the socket operation timeout in milliseconds
	 */
	public int getSoTimeout()
	{
		return soTimeout;
	}

	/**
	 * Set the socket operation timeout
	 * @param soTimeout timeout in milliseconds
	 */
	public void setSoTimeout(int soTimeout)
	{
		this.soTimeout = soTimeout;
	}

	/**
	 * Get the connection timeout in milliseconds
	 */
	public int getConnectionTimeout()
	{
		return connectionTimeout;
	}

	/**
	 * Set the connection timeout
	 * @param connectionTimeout timeout in milliseconds
	 */
	public void setConnectionTimeout(int connectionTimeout)
	{
		this.connectionTimeout = connectionTimeout;
	}
	
	/**
	 * Perform a remote JSON-RPC method call
	 * @param method The name of the method to invoke
	 * @param params Arguments of the method
	 * @return The result of the RPC as a JSONObject
	 * @throws JSONRPCException if an error is encountered during JSON-RPC method call
	 */
	public JSONObject call(String method, Map<String,?> params) throws JSONRPCException
	{
		try 
		{
			JSONObject result = doRequest(method, params); 
			
			if( !result.get("error").equals(null)) {
				throw new JSONRPCException(result.getJSONObject("error"));
			}
			
			return result.getJSONObject("result");
		} 
		catch (JSONException e)
		{
			throw new JSONRPCException("Cannot convert result to JSONObject", e);
		}
	}

	/**
	 * Perform a remote JSON-RPC method call
	 * @param method The name of the method to invoke
	 * @param params Arguments of the method
	 * @return The result of the RPC
	 * @throws JSONRPCException if an error is encountered during JSON-RPC method call
	 */
	/*public Object call(String method, Map<String,?> params) throws JSONRPCException
	{
		try 
		{
			return doRequest(method, params).get("result");
		} 
		catch (JSONException e)
		{
			throw new JSONRPCException("Cannot convert result", e);
		}
	}*/
	
	/**
	 * Perform a remote JSON-RPC method call
	 * @param method The name of the method to invoke
	 * @param params Arguments of the method
	 * @return The result of the RPC as a String
	 * @throws JSONRPCException if an error is encountered during JSON-RPC method call
	 */
	public String callString(String method, Map<String,?> params) throws JSONRPCException
	{
		try 
		{
			return doRequest(method, params).getString("result");
		} 
		catch (JSONException e)
		{
			throw new JSONRPCException("Cannot convert result to String", e);
		}
	}
	
	/**
	 * Perform a remote JSON-RPC method call
	 * @param method The name of the method to invoke
	 * @param params Arguments of the method
	 * @return The result of the RPC as an int
	 * @throws JSONRPCException if an error is encountered during JSON-RPC method call
	 */
	public int callInt(String method, Map<String,?> params) throws JSONRPCException
	{
		try 
		{
			return doRequest(method, params).getInt("result");
		} 
		catch (JSONException e)
		{
			throw new JSONRPCException("Cannot convert result to int", e);
		}
	}
	
	/**
	 * Perform a remote JSON-RPC method call
	 * @param method The name of the method to invoke
	 * @param params Arguments of the method
	 * @return The result of the RPC as a long
	 * @throws JSONRPCException if an error is encountered during JSON-RPC method call
	 */
	public long callLong(String method, Map<String,?> params) throws JSONRPCException
	{
		try 
		{
			return doRequest(method, params).getLong("result");
		} 
		catch (JSONException e)
		{
			throw new JSONRPCException("Cannot convert result to long", e);
		}
	}
	
	/**
	 * Perform a remote JSON-RPC method call
	 * @param method The name of the method to invoke
	 * @param params Arguments of the method
	 * @return The result of the RPC as a boolean
	 * @throws JSONRPCException if an error is encountered during JSON-RPC method call
	 */
	public boolean callBoolean(String method, Map<String,?> params) throws JSONRPCException
	{
		try 
		{
			return doRequest(method, params).getBoolean("result");
		} 
		catch (JSONException e)
		{
			throw new JSONRPCException("Cannot convert result to boolean", e);
		}
	}
	
	/**
	 * Perform a remote JSON-RPC method call
	 * @param method The name of the method to invoke
	 * @param params Arguments of the method
	 * @return The result of the RPC as a double
	 * @throws JSONRPCException if an error is encountered during JSON-RPC method call
	 */
	public double callDouble(String method, Map<String,?> params) throws JSONRPCException
	{
		try 
		{
			return doRequest(method, params).getDouble("result");
		} 
		catch (JSONException e)
		{
			throw new JSONRPCException("Cannot convert result to double", e);
		}
	}
	
	/**
	 * Perform a remote JSON-RPC method call
	 * @param method The name of the method to invoke
	 * @param params Arguments of the method
	 * @return The result of the RPC as a JSONArray
	 * @throws JSONRPCException if an error is encountered during JSON-RPC method call
	 */
	public JSONArray callJSONArray(String method, Map<String,?> params) throws JSONRPCException
	{
		try 
		{
			return doRequest(method, params).getJSONArray("result");
		} 
		catch (JSONException e)
		{
			throw new JSONRPCException("Cannot convert result to JSONArray", e);
		}
	}
}
