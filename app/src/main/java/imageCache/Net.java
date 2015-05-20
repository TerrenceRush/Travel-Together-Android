package imageCache;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.ByteArrayBuffer;

import java.io.IOException;
import java.io.InputStream;

public class Net {

	private InputStream inputstream;
	private DefaultHttpClient httpClient;
	private boolean isStop = false;
	/**
	 * ��Դ����
	 * 
	 * @param context
	 *            �����Ķ���
	 * @param url
	 *            ����url��ַ
	 * @return
	 * @throws org.apache.http.client.ClientProtocolException
	 * @throws java.io.IOException
	 */
	public byte[] downloadResource(Context context, String url)
			throws ClientProtocolException, IOException {
		isStop = false;
		ByteArrayBuffer buffer = null;
		HttpGet hp = new HttpGet(url);
		httpClient = new DefaultHttpClient();
		String netType = isNetType(context);
		if (netType != null & netType.equals("cmwap")) {
			HttpHost proxy = new HttpHost("10.0.0.172", 80);
			httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,
					proxy);
		}
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
                5 * 1000);
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), 60 * 1000);
		HttpResponse response = httpClient.execute(hp);
		if (response.getStatusLine().getStatusCode() == 200) {
			inputstream = response.getEntity().getContent();
			if (inputstream != null) {
				@SuppressWarnings("unused")
				int i = (int) response.getEntity().getContentLength();
				buffer = new ByteArrayBuffer(1024);
				byte[] tmp = new byte[1024];
				int len;
				while (((len = inputstream.read(tmp)) != -1)
						&& (false == isStop)) {
					buffer.append(tmp, 0, len);
				}
			}
			cancel();
		}
		return buffer.toByteArray();
	}

	/**
	 * ǿ�ƹر�����
	 *
	 * @throws java.io.IOException
	 */
	public synchronized void cancel() throws IOException {
		if (null != httpClient) {
			isStop = true;
			httpClient.getConnectionManager().shutdown();
			httpClient = null;
		}
		if (inputstream != null) {
			inputstream.close();
		}
	}
	
	
	/**
	 * �жϽ��������
	 * 
	 * @return
	 */
	public static String isNetType(Context context) {
		String nettype = null;
		if (context == null) {
			return null;
		}
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobNetInfo = connectivityManager.getActiveNetworkInfo();
		if (mobNetInfo != null) {
			if (mobNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				nettype = mobNetInfo.getTypeName(); // ��ǰ����������WIFI
			} else {
				nettype = mobNetInfo.getExtraInfo();// ��ǰ����������cmnet/cmwap
			}
		}

		return nettype;
	}
	
}
