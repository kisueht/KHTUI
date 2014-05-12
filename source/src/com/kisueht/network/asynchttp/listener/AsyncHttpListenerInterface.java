/**
 * 
 */
package com.kisueht.network.asynchttp.listener;

import org.apache.http.HttpEntity;


/**
 * 发送http请求的回调函数
 * <p>
 *  Main method:<br>
 *      {@link #onStart()}<br>
 *      {@link #onProcessData(HttpEntity)}<br>
 *      {@link #onFailure(Throwable)}<br>
 *     
 * @author kisueht
 * 
 */
public interface AsyncHttpListenerInterface {
	
	/**
	 * 发起请求之前
	 */
	public void onStart();
	
	/**
	 * 根据泛型参数类型处理http请求返回的数据
	 */
	public void onProcessData(HttpEntity response);
	
	/**
	 * 出现异常错误
	 * @param e
	 */
	public void onFailure(Throwable e);
	
}
