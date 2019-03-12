package com.zbyj.Yazhou.LeftCompanyProgram.Factory;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageCacheFactory
{
	private Context mContext;
	private LruCache<String, Bitmap> mLruCache;
	private Bitmap mBitmap;
	public ImageCacheFactory(){
		int maxMemory  = (int) (Runtime.getRuntime().maxMemory());//��ȡ������ʹ�õ���󻺴�ռ�
		int MemorySize = maxMemory / 8;//ʹ�ó������󻺴�ռ��8/1
		mLruCache = new LruCache<String, Bitmap>(MemorySize){
			@Override
			protected int sizeOf(String key, Bitmap value)
			{
				return value.getByteCount();
			}
		};
	}
	/**
	 * ����һ���ؼ��� ������ѯ�������Ƿ���ͼƬ
	 * @param key
	 * @return
	 */
	public Boolean isLocalLruchBitmap(String key){
		try
		{
			if(mLruCache.get(key) != null){
				//�����ļ�
				return true;
			}
			else{
				return false;
			}
		} catch (Exception e)
		{
			return false;
		}
	}
	/**
	 * ����ͼƬ
	 * @param key
	 * @param bitmap
	 */
	public void saveImage(String key,Bitmap bitmap){
		try {
			if(!isLocalLruchBitmap(key)){
				//�������ļ�
				try
				{
					mLruCache.put(key, bitmap);
				} catch (Exception e)
				{
					Log.i("capitalist", "File ProgramLocalLruch.java Error Line in :59");
				}
			}
		} catch (Exception e) {
			
		}
		
	}
	public Bitmap getImage(String key){
		try {
			if(mLruCache.get(key) != null){
				return mLruCache.get(key);
			}
			else{
				return null;
			}
		} catch (Exception e) {
			return null;
		}
		
	}
	public void ReleaseImageBitmap(ImageView image){
		if(image == null) return;
		Drawable drawable = image.getDrawable();
		if(drawable != null && drawable instanceof BitmapDrawable){
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			Bitmap bitmap = bitmapDrawable.getBitmap();
			if(bitmap != null && !bitmap.isRecycled()){
				bitmap.recycle();//ϵͳ����
			}
		}
	}

}
