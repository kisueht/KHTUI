/**
 * 
 */
package com.kisueht.network.asynchttp.listener;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;

/**
 * @author kisueht
 *
 */
public class AsyncHttpStreamListener extends AbstractAsyncHttpListener<InputStream> {

	/* (non-Javadoc)
	 * @see com.cking.asynchttp.listener.AsyncHttpListenerInterface#onProgressData(org.apache.http.HttpEntity)
	 */
	@Override
	public final void onProcessData(HttpEntity response) {
		// TODO Auto-generated method stub
		try {
			this.onSuccess(response.getContent());
		} catch (IllegalStateException e) {
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
