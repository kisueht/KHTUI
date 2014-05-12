/**
 * 
 */
package com.kisueht.network.asynchttp.holder;

import org.apache.http.HttpEntity;

import com.kisueht.network.asynchttp.listener.AbstractAsyncHttpListener;

/**
 * output holder
 * @author kisueht
 *
 */
public class OutputHolder {
	
	private HttpEntity httpEntity;
	private Throwable exception;
	private AbstractAsyncHttpListener<? extends Object> mAbstractAsyncHttpListener;

	/**
	 * OutputHolder够着函数
	 * @param entity	发送请求获取的响应信息
	 * @param abstractAsyncHttpListener	回调函数,see:{@link AbstractAsyncHttpListener}
	 */
	public OutputHolder(HttpEntity entity, AbstractAsyncHttpListener<? extends Object> abstractAsyncHttpListener) {
		this.httpEntity = entity;
		this.mAbstractAsyncHttpListener = abstractAsyncHttpListener;
	}

	/**
	 * OutputHolder够着函数
	 * @param exception	发送请求时的异常
	 * @param abstractAsyncHttpListener	回调函数,see:{@link AbstractAsyncHttpListener}
	 */
	public OutputHolder(Throwable exception, AbstractAsyncHttpListener<? extends Object> abstractAsyncHttpListener) {
		this.exception = exception;
		this.mAbstractAsyncHttpListener = abstractAsyncHttpListener;
	}

	public HttpEntity getHttpEntity() {
		return httpEntity;
	}

	public Throwable getException() {
		return exception;
	}

	public AbstractAsyncHttpListener<? extends Object> getResponseCallback() {
		return this.mAbstractAsyncHttpListener;
	}

}
