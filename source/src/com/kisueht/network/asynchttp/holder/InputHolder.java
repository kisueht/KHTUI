/**
 * 
 */
package com.kisueht.network.asynchttp.holder;

import org.apache.http.HttpRequest;

import com.kisueht.network.asynchttp.listener.AbstractAsyncHttpListener;

/**
 * Input Holder
 * @author kisueht
 * @param <T>
 *
 */
public class InputHolder {
	
	private HttpRequest httpRequest;
	private AbstractAsyncHttpListener<? extends Object> mAbstractAsyncHttpListener;
	
	/**
	 * InputHolder构造函数
	 * @param request	发送的请求
	 * @param abstractAsyncHttpListener	回调函数,see:{@link AbstractAsyncHttpListener}
	 */
	public InputHolder(HttpRequest request, AbstractAsyncHttpListener<? extends Object> abstractAsyncHttpListener) {
		this.httpRequest = request;
		this.mAbstractAsyncHttpListener = abstractAsyncHttpListener;
	}

	public HttpRequest getHttpRequest() {
		return this.httpRequest;
	}

	public AbstractAsyncHttpListener<? extends Object> getAsyncHttpCallback() {
		return this.mAbstractAsyncHttpListener;
	}
	
}
