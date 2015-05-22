package imageCache;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.SoftReference;
import java.util.HashMap;

public class AsyncImageLoader {
	// 软引用
	private static HashMap<String, SoftReference<Drawable>> imageCache;
	static String SdDir;
	private Context context;

	public AsyncImageLoader(Context context) {
		if (imageCache == null)
			imageCache = new HashMap<String, SoftReference<Drawable>>();
		this.context = context;
		Files.mkdir(context);
	}

	/***
	 * 下载图片
	 * 
	 * @param imageUrl
	 *            图片地址
	 * @param imageCallback
	 *            回调接口
	 * @return
	 */
	@SuppressLint("HandlerLeak")
	public Drawable loadDrawable(final String imageUrl,
			final ImageCallback imageCallback) {
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = softReference.get();
			if (drawable != null)
				return drawable;
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
			}
		};
		// 开启线程下载图片
		new Thread() {
			@Override
			public void run() {
				Drawable drawable = null;
				try {
					drawable = loadImageFromUrl(imageUrl);
					if (drawable != null)
						// 将下载的图片保存至缓存中
						imageCache.put(imageUrl, new SoftReference<Drawable>(
								drawable));
					Message message = handler.obtainMessage(0, drawable);
					handler.sendMessage(message);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}.start();
		return null;
	}

	/***
	 * 根据URL下载图片（这里要进行判断，先去本地sd中查找，没有则根据URL下载，有则返回该drawable）
	 * 
	 * @param url
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Drawable loadImageFromUrl(String urlString) throws Exception {
		String fileName = urlString.substring(urlString.lastIndexOf("/") + 1)
				.trim();
		if (fileName.equals("*"))
			return null;

		boolean isExists = Files.compare(urlString);
		Bitmap bitmap;
		if (isExists == false) {
			Net net = new Net();
			byte[] data = net.downloadResource(context, urlString);
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			Files.saveImage(urlString, data);
		} else {
			byte[] data = Files.readImage(urlString);
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		}
		return new BitmapDrawable(bitmap);
	}

	// 回调接口
	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}

}
