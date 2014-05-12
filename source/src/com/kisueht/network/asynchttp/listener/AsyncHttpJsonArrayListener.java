/**
 * 
 */
package com.kisueht.network.asynchttp.listener;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * @author kisueht
 *
 */
public class AsyncHttpJsonArrayListener extends AbstractAsyncHttpListener<JSONArray> {

	/* (non-Javadoc)
	 * @see com.cking.asynchttp.listener.AbstractAsyncHttpListener#onProgressData(org.apache.http.HttpEntity)
	 */
	@Override
	public final void onProcessData(HttpEntity response) {
		// TODO Auto-generated method stub
		try {
			this.onSuccess(new JSONArray(EntityUtils.toString(response, HTTP.UTF_8)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.onFailure(e);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.onFailure(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.onFailure(e);
		}
	}

}
