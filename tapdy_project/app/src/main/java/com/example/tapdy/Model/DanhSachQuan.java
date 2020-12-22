package com.example.tapdy.Model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class DanhSachQuan implements Comparable<DanhSachQuan>
{
    private Bitmap mHinhQuan;
    private String mTenQuan;
    private String mMaQuan;
    private String mMoTaQuan;
    private String mDiaChi;
    private double mCachBan;

    public DanhSachQuan(Bitmap mHinhQuan, String mTenQuan, String mMaQuan, String mMoTaQuan, String mDiaChi, double mCachBan)
    {
        this.mHinhQuan = mHinhQuan;
        this.mTenQuan = mTenQuan;
        this.mMaQuan = mMaQuan;
        this.mMoTaQuan = mMoTaQuan;
        this.mDiaChi = mDiaChi;
        this.mCachBan=mCachBan;
    }

    public double getmCachBan()
    {
        return mCachBan;
    }

    public void setmCachBan(double mCachBan)
    {
        this.mCachBan = mCachBan;
    }

    public Bitmap getmHinhQuan()
    {
        return mHinhQuan;
    }

    public void setmHinhQuan(Bitmap mHinhQuan)
    {
        this.mHinhQuan = mHinhQuan;
    }

    public String getmTenQuan()
    {
        return mTenQuan;
    }

    public void setmTenQuan(String mTenQuan)
    {
        this.mTenQuan = mTenQuan;
    }

    public String getmMaQuan()
    {
        return mMaQuan;
    }

    public void setmMaQuan(String mMaQuan)
    {
        this.mMaQuan = mMaQuan;
    }

    public String getmMoTaQuan()
    {
        return mMoTaQuan;
    }

    public void setmMoTaQuan(String mMoTaQuan)
    {
        this.mMoTaQuan = mMoTaQuan;
    }

    public String getmDiaChi()
    {
        return mDiaChi;
    }

    public void setmDiaChi(String mDiaChi)
    {
        this.mDiaChi = mDiaChi;
    }

    @Override
    public int compareTo(DanhSachQuan o)
    {
        if(this.getmCachBan()>o.getmCachBan())
        {
            return 1;
        }
        else if(this.getmCachBan()<o.getmCachBan())
        {
            return -1;
        }
        else return 0;
    }
}
