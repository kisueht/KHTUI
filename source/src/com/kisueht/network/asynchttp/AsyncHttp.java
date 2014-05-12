/**
 * 建立请求连接类
 */
package com.kisueht.network.asynchttp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.kisueht.network.asynchttp.holder.InputHolder;
import com.kisueht.network.asynchttp.holder.OutputHolder;
import com.kisueht.network.asynchttp.listener.AbstractAsyncHttpListener;

/**
 * 建立请求连接类
 * <p>使用方法：<br>
 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp  AsyncHttp.{@link #post(String, String, String, AbstractAsyncHttpListener)};//发送POST请求<br>
 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp  AsyncHttp.{@link #get(String, String, AbstractAsyncHttpListener)};//发送GET请求<br>
 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp  AsyncHttp.{@link #cancelRequest(String)};//取消一个已经建立的请求<br>
 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp  AsyncHttp.{@link #shutdown()};//关闭链接，在程序结束时应当调用此方法，结束释放所有连接
 * @author kisueht
 * 
 */
public final class AsyncHttp {
	private static final String TAG = AsyncHttp.class.getSimpleName();

	public static int connection_timeout = 5 * 60 * 1000;
	public static int socket_timeout = 1 * 20 * 1000;
	
	private static boolean isTimeChange = false;

	private static DefaultHttpClient mHttpClient;
	
	private static ConcurrentHashMap<String, AsyncHttpSenderTask> tasks = new ConcurrentHashMap<String, AsyncHttpSenderTask>();
	
	
	private AsyncHttp() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 发送GET请求
	 * @param key	发送请求的唯一标记
	 * @param url	请求地址
	 * @param callback	回调方法
	 */
	public static void get(String key, String url, AbstractAsyncHttpListener<? extends Object> callback) {
		HttpGet httpGet = new HttpGet(url);
		
		sendRequest(key, httpGet, callback);
	}
	
	/**
	 * 发送POST请求
	 * @param key	发送请求的唯一标记
	 * @param url	请求地址
	 * @param content	请求发送的数据
	 * @param callback	回调方法
	 */
	public static void post(String key, String url, String content, AbstractAsyncHttpListener<? extends Object> callback) {
		if (TextUtils.isEmpty(key)) {
			if (callback!=null) {
				callback.onFailure(new Throwable("The request currentActivity is null!"));
			}
			return;
		}
		
		if (TextUtils.isEmpty(url)) {
			if (callback!=null) {
				callback.onFailure(new Throwable("The request url is null!"));
			}
			return;
		}
		
		if (TextUtils.isEmpty(content)) {
			if (callback!=null) {
				callback.onFailure(new Throwable("The request content is null!"));
			}
			return;
		}
		
		HttpPost httpPost = new HttpPost(url);
		
		try {
			httpPost.setEntity(new StringEntity(content, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "UnsupportedEncodingException>>>"+e.getMessage(), e);
			if (callback!=null) {
				callback.onFailure(e);
			}
			return;
		}
		
		sendRequest(key, httpPost, callback);
	}
	
	/**
	 * 发送请求
	 * @param key	发送请求的唯一标记
	 * @param request	请求的类型
	 * @param callback	回调函数
	 */
	public static void sendRequest(String key, HttpRequest request, AbstractAsyncHttpListener<? extends Object> callback) {
		sendRequest(key, request, callback, connection_timeout, socket_timeout);
	}
	
	/**
	 * 发送请求
	 * @param currentKey	发送请求的唯一标记
	 * @param request	请求的类型
	 * @param callback	回调函数
	 * @param timeoutConnection 连接超时时间,默认为5*60*1000秒
	 * @param timeoutSocket	请求超时时间,默认为{@link #socket_timeout}秒
	 */
	public static void sendRequest(String currentKey, HttpRequest request, AbstractAsyncHttpListener<? extends Object> abstractAsyncHttpListener, int timeoutConnection, int timeoutSocket){
		
		setConnection_timeout(timeoutConnection);
		setSocket_timeout(timeoutSocket);
		
		InputHolder inputHolder = new InputHolder(request, abstractAsyncHttpListener);
		AsyncHttpSenderTask asyncHttpSender = new AsyncHttpSenderTask(inputHolder);
		asyncHttpSender.execute();
		
		if (tasks.containsKey(currentKey)) {
			cancelRequest(currentKey);
		}
		tasks.put(currentKey, asyncHttpSender);
		
		StringBuilder sBuilder = new StringBuilder();
		for (String key : tasks.keySet()) {
			sBuilder.append(key+";");
		}
		Log.d(TAG, TAG+" include: "+sBuilder.toString());
	};
	
	/**
	 * 取消一个已经建立的请求
	 * @param currentKey	发起请求的唯一标记
	 */
	public static void cancelRequest(String currentKey) {
		if (tasks==null || tasks.size()==0) {
			return;
		}
		
		for (String key : tasks.keySet()) {
			if (currentKey.equals(key)) {
				AsyncTask<?, ?, ?> task = tasks.get(key);
				if (task.getStatus()!=null && task.getStatus()!=AsyncTask.Status.FINISHED) {
					task.cancel(true);
				}
				
				tasks.remove(key);Log.d(TAG, "cancelRequest "+key+" is "+key);
			}
		}
	}
	
	/**
	 * 重置所有链接
	 */
	public static synchronized void reset() {
		if (mHttpClient!=null) {
			mHttpClient.getConnectionManager().shutdown();
			mHttpClient = null;
		}
		
		for (String key : tasks.keySet()) {
			AsyncTask<?, ?, ?> task = tasks.get(key);
			if (task.getStatus()!=null && task.getStatus()!=AsyncTask.Status.FINISHED) {
				task.cancel(true);
			}
			
			tasks.remove(key);
		}
	}
	
	/**
	 * 关闭链接，在程序结束时应当调用此方法，结束释放所有连接
	 */
	public static void shutdown() {
		reset();
		tasks = null;
	}
	
	private static class AsyncHttpSenderTask extends AsyncTask<Void, Void, OutputHolder>{
		
		private InputHolder mInputHolder;

		public AsyncHttpSenderTask(InputHolder inputHolder) {
			super();
			// TODO Auto-generated constructor stub
			
			mInputHolder = inputHolder;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			mInputHolder.getAsyncHttpCallback().onStart();
		}

		@Override
		protected OutputHolder doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			HttpEntity entity = null;
			try {
				HttpUriRequest httpUriRequest = (HttpUriRequest) mInputHolder.getHttpRequest();
				HttpResponse response = AsyncHttp.getDefaultHttpClient().execute(httpUriRequest);
				StatusLine statusLine = response.getStatusLine();
				
				if (statusLine.getStatusCode()!=HttpStatus.SC_OK) {
					return new OutputHolder(new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase()), mInputHolder.getAsyncHttpCallback());
				}
				
				entity = response.getEntity();
				if (entity!=null) {
					entity = new BufferedHttpEntity(entity);
				}else {
					Log.e(TAG, "HttpEntity is null!");
					return new OutputHolder(new Throwable("返回结果为空！"), mInputHolder.getAsyncHttpCallback());
				}
				
				httpUriRequest.abort();//获取返回信息后，结束此次链接
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e(TAG, "ClientProtocolException>>>"+e.getMessage(), e);
				return new OutputHolder(e, mInputHolder.getAsyncHttpCallback());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e(TAG, "IOException>>>"+e.getMessage(), e);
				
				Throwable throwable = e;
				if (throwable.getMessage()==null) {
					if (throwable instanceof SocketTimeoutException) {
						throwable = new Throwable("请求服务器超时！");
					}
				}
				return new OutputHolder(throwable, mInputHolder.getAsyncHttpCallback());
			}
			
			return new OutputHolder(entity, mInputHolder.getAsyncHttpCallback());
		}

		@Override
		protected void onPostExecute(OutputHolder result) {
			// TODO Auto-generated method stub
			if (isCancelled()) {
				return;
			}
			
			AbstractAsyncHttpListener<? extends Object> listener = result.getResponseCallback();
			HttpEntity response = result.getHttpEntity();
			Throwable exception = result.getException();
			
			if (listener!=null) {
				if (response!=null) {
					listener.onProcessData(response);
				}else {
					listener.onFailure(exception);
				}
			}
			
			cancelRequestTask(this);
		}
		
	}
	
	private static void cancelRequestTask(AsyncHttpSenderTask asyncHttpSender) {
		if (tasks!=null && tasks.containsValue(asyncHttpSender)) {
			for (String key : tasks.keySet()) {
				if (asyncHttpSender.equals(tasks.get(key))) {
					boolean bl = tasks.remove(key, asyncHttpSender);Log.d(TAG, "cancelRequestTask "+key+" is "+bl);
					break;
				}
			}
		}
	}

	private static synchronized DefaultHttpClient getDefaultHttpClient() {
		if (mHttpClient == null) {
			BasicHttpParams params = new BasicHttpParams();

			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			final SSLSocketFactory sslSocketFactory = SSLSocketFactory
					.getSocketFactory();
			schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));

			// Set the timeout in milliseconds until a connection is established.
			HttpConnectionParams.setConnectionTimeout(params,
					connection_timeout);
			// Set the default socket timeout (SO_TIMEOUT) in milliseconds which is the timeout for waiting for data.
			HttpConnectionParams.setSoTimeout(params, socket_timeout);

			ClientConnectionManager cm = new ThreadSafeClientConnManager(
					params, schemeRegistry);
			mHttpClient = new DefaultHttpClient(cm, params);
		}else if(isTimeChange){
			HttpParams httpParams = mHttpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
								connection_timeout);// Set the timeout in milliseconds until a connection is established.
			HttpConnectionParams.setSoTimeout(httpParams, socket_timeout);// Set the default socket timeout (SO_TIMEOUT) in milliseconds which is the timeout for waiting for data.
		}
		
		return mHttpClient;
	}

	/**
	 * 设置链接超时时间
	 * @param connection_timeout
	 */
	public static void setConnection_timeout(int connection_timeout) {
		AsyncHttp.connection_timeout = connection_timeout;
		isTimeChange = true;
	}

	/**
	 * 设置请求超时时间
	 * @param socket_timeout
	 */
	public static void setSocket_timeout(int socket_timeout) {
		AsyncHttp.socket_timeout = socket_timeout;
		isTimeChange = true;
	}

}
