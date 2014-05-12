/**
 * 结果回调的抽象类，可以继承此类进行扩展
 */
package com.kisueht.network.asynchttp.listener;


/**
 * 结果回调的抽象类，可以继承此类进行扩展
 * @author kisueht
 *
 */
public abstract class AbstractAsyncHttpListener<T extends Object> implements
		AsyncHttpListenerInterface {

	/**
	 * 
	 */
	public AbstractAsyncHttpListener() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.cking.asynchttp.listener.AsyncHttpListenerInterface#onStart()
	 */
	@Override
	public void onStart() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.cking.asynchttp.listener.AsyncHttpListenerInterface#onFailure(java.lang.Throwable)
	 */
	@Override
	public void onFailure(Throwable e) {
		// TODO Auto-generated method stub

	}

	/**
	 * 成功返回结果数据
	 * @param t
	 */
	public void onSuccess(T t) {
		// TODO Auto-generated method stub
		
	}

}
