package com.inomma.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class HttpHelper
{
	private Header header;
	private String url;
	protected HashMap<String, String> params;

	public HttpHelper(String url)
	{
		this.url = url;
		this.params = new HashMap<String, String>();
	}

	public void setHeader(String name, String value)
	{
		header = new Header(name, value);
	}

	public void addParams(HashMap<String, String> params)
	{
		this.params.putAll(params);
	}

	public void setParams(HashMap<String, String> params){
		this.params = params;
	}
	public void httpPostAsync(final JSONObject body, final JsonResponseHandler handler)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				if (handler == null)
					return;
				handler.onSuccess(httpPost(body));
			}
		}).start();
	}
	
	public void httpPostAsync(final List<NameValuePair> postParameters, final JsonResponseHandler handler)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				if (handler == null)
					return;
				handler.onSuccess(httpPost(postParameters));
			}
		}).start();
	}
	
	protected JSONObject httpPost(List<NameValuePair> postParameters)
	{
		if (postParameters == null)
			postParameters = new ArrayList<NameValuePair>();
		try
		{
			return httpPost(new UrlEncodedFormEntity(postParameters));
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	protected JSONObject httpPost(JSONObject body)
	{
		if (body == null)
			body = new JSONObject();
		try
		{
			return httpPost(new StringEntity(body.toString()));
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	private JSONObject httpPost(StringEntity se)
	{
		url = addUrlParams(url, params);

		url = url.replace(" ", "%20");
		HttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object
		HttpPost httpPost = new HttpPost(url);
		if (header != null)
			httpPost.addHeader(header.name, header.value);
		else
			httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
		
		try
		{
			//se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			httpPost.setEntity(se);
			Log.i("Requesting post", url);
			Log.i("Post body", EntityUtils.toString(se));
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			// If the response does not enclose an entity, there is no need
			// to worry about connection release
			String result = EntityUtils.toString(entity);
			Log.i("Response", "result: " + result);
			if (entity != null)
			{
				// now you have the string representation of the HTML request
				return new JSONObject(result);
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public void httpGetObjectAsync(final JsonObjectResponseHandler handler)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				JSONObject result = httpGetObject();
				if (handler != null)
					handler.onSuccess(result);
			}
		}).start();
	}

	public JSONObject httpGetObject()
	{
		String result = httpGet();
		try
		{
			return result == null ? null : new JSONObject(result);
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public void httpGetArrayAsync(final JsonArrayResponseHandler handler)
	{
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				JSONArray result = httpGetArray();
				if (handler != null)
					handler.onSuccess(result);
			}
		}).start();
	}

	public JSONArray httpGetArray()
	{
		String result = httpGet();
		try
		{
			return result == null ? null : new JSONArray(result);
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public void httpGetAsync(final JsonResponseHandler handler)
	{
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				if (handler == null)
					return;
				String result = httpGet().trim();
				try
				{
					if (result.startsWith("["))
						handler.onSuccess(new JSONArray(result));
					else if (result.startsWith("{"))
						handler.onSuccess(new JSONObject(result));
				} catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}

	protected String httpGet()
	{
		url = addUrlParams(url, params);
		url = url.replace(" ", "%20");
		HttpClient httpclient = new DefaultHttpClient();

		// Prepare a request object
		HttpGet httpGet = new HttpGet(url);
		if (header != null)
			httpGet.setHeader(header.name, header.value);
		else
			httpGet.addHeader(HTTP.CONTENT_TYPE, "application/json");

		// Execute the request
		HttpResponse response;
		try
		{
			Log.i("Requesting get", url);
			response = httpclient.execute(httpGet);

			// Get hold of the response entity
			HttpEntity entity = response.getEntity();
			// If the response does not enclose an entity, there is no need
			// to worry about connection release
			String result = EntityUtils.toString(entity);
			Log.i("Response", result);
			if (entity != null)
			{
				// now you have the string representation of the HTML request
				return result;
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public String httpDelete()
	{
		url = addUrlParams(url, params);
		url = url.replace(" ", "%20");
		HttpClient httpclient = new DefaultHttpClient();

		// Prepare a request object
		HttpDelete httpDelete = new HttpDelete(url);
		if (header != null)
			httpDelete.setHeader(header.name, header.value);
		else
			httpDelete.addHeader(HTTP.CONTENT_TYPE, "application/json");

		// Execute the request
		HttpResponse response;
		try
		{
			Log.i("Requesting delete", url);
			response = httpclient.execute(httpDelete);

			// Get hold of the response entity
			HttpEntity entity = response.getEntity();
			// If the response does not enclose an entity, there is no need
			// to worry about connection release
			String result = EntityUtils.toString(entity);
			Log.i("Response", result);
			if (entity != null)
			{
				// now you have the string representation of the HTML request
				return result;
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public void httpDeleteAsync(final JsonResponseHandler handler)
	{
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				if (handler == null)
					return;
				String result = httpDelete().trim();
				try
				{
					if (result.startsWith("["))
						handler.onSuccess(new JSONArray(result));
					else if (result.startsWith("{"))
						handler.onSuccess(new JSONObject(result));
				} catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private String addUrlParams(String url, HashMap<String, String> params)
	{
		if (params != null && params.size() > 0)
		{
			StringBuilder urlBuilder = new StringBuilder(url + "?");
			for (String param : params.keySet())
				urlBuilder.append(param + "=" + params.get(param) + "&");

			urlBuilder.deleteCharAt(urlBuilder.length() - 1);
			url = urlBuilder.toString();
		}
		return url;
	}

	public interface JsonObjectResponseHandler
	{
		public void onSuccess(JSONObject result);
	}

	public interface JsonArrayResponseHandler
	{
		public void onSuccess(JSONArray result);
	}

	public interface JsonResponseHandler
	{
		public void onSuccess(JSONObject response);

		public void onSuccess(JSONArray response);
	}

	private class Header
	{
		private String name;
		private String value;

		public Header(String name, String value)
		{
			this.name = name;
			this.value = value;
		}
	}
}