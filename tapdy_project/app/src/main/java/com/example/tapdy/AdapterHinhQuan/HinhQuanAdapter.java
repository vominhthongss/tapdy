package com.example.tapdy.AdapterHinhQuan;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.tapdy.R;

public class HinhQuanAdapter extends PagerAdapter
{
    private Context mContext;
    private Bitmap[] mHinhQuan;

    public HinhQuanAdapter(Context mContext, Bitmap [] mHinh, int n)
    {
        this.mContext = mContext;
        mHinhQuan = new Bitmap[n];
        for(int i=0;i<n;i++)
        {
            this.mHinhQuan[i]=mHinh[i];


        }
    }

    @Override
    public int getCount()
    {
        return mHinhQuan.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object)
    {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position)
    {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(mHinhQuan[position]);
        container.addView(imageView,0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object)
    {
        container.removeView((ImageView)object);
    }
}
