package com.inomma.kandu.server;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.inomma.SharedPreferencesHelper;

public abstract class Request<T extends Response>
{
	protected ResponseHandler<T> handler;
	private Map<String, Object> params;
	private Context context;
	private String successMessage;
	private ProgressDialog pd;

	public Request(ResponseHandler<T> handler,Context context)
	{
		this.handler = handler;
		params = new HashMap<String, Object>();
		this.context = context;
	}

	protected abstract T handleResponse(String response) throws JSONException;

	protected Map<String, Object> getParams()
	{
		return params;
	}

	public final Response handle(String response)
	{
		try
		{
			T r = response == null ? null : handleResponse(response);

			if (pd != null)
			{
				pd.dismiss();
				pd = null;
			}
			if (successMessage != null)
				Toast.makeText(context, r.isOk() ? successMessage : r.getErrorMessage(), Toast.LENGTH_SHORT).show();
			
			if (handler != null)
				handler.handleResponse(r);

			return r;
		} catch (JSONException e)
		{
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public void putParam(String name, Object value)
	{
		params.put(name, value);
	}

	public abstract String getAction();

	public void execute(Context context, String loadingMessage)
	{
		this.context = context;
		pd = ProgressDialog.show(context, "", loadingMessage);
		execute();
	}

	public void execute(Context context, String loadingMessage, String successMessage)
	{
		this.context = context;
		pd = ProgressDialog.show(context, "", loadingMessage);
		this.successMessage = successMessage;
		execute();
	}

	public void execute()
	{
		String cachedData = getCachedData();
		if(getCachedData()!=null && !isNetworkAvailable()){
			handle(cachedData);
			return;
		}
		MainSender.instance.execute1(this);
	}

	public RequestMethod getMethod()
	{
		return RequestMethod.GET;
	}
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	public Context getContext(){
		return this.context;
	}
	
	public String getCachedData(){
		return SharedPreferencesHelper.getStringData(getAction(),null);

	}
	
	public void saveCachedData(String data){
		SharedPreferencesHelper.putStringData(getAction(), data);
	}

}
