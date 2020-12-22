package com.example.tapdy.Model;

import android.graphics.Bitmap;
import android.widget.TextView;

import java.util.PriorityQueue;

public class DanhSachBinhLuan
{
    private Bitmap mAvt;
    private String mHoTenBinhLuan;
    private Float mSoSaoDanhGia;
    private Bitmap mHinhBinhLuan;
    private String mNoiDungBinhLuan;
    private String mMaBinhLuan;
    private String mUserBinhLuan;

    public DanhSachBinhLuan(Bitmap mAvt, String mHoTenBinhLuan, Float mSoSaoDanhGia, Bitmap mHinhBinhLuan, String mNoiDungBinhLuan, String mMaBinhLuan,String mUserBinhLuan)
    {
        this.mAvt = mAvt;
        this.mHoTenBinhLuan = mHoTenBinhLuan;
        this.mSoSaoDanhGia = mSoSaoDanhGia;
        this.mHinhBinhLuan = mHinhBinhLuan;
        this.mNoiDungBinhLuan = mNoiDungBinhLuan;
        this.mMaBinhLuan=mMaBinhLuan;
        this.mUserBinhLuan=mUserBinhLuan;
    }

    public Bitmap getmAvt()
    {
        return mAvt;
    }

    public void setmAvt(Bitmap mAvt)
    {
        this.mAvt = mAvt;
    }

    public String getmHoTenBinhLuan()
    {
        return mHoTenBinhLuan;
    }

    public void setmHoTenBinhLuan(String mHoTenBinhLuan)
    {
        this.mHoTenBinhLuan = mHoTenBinhLuan;
    }

    public Float getmSoSaoDanhGia()
    {
        return mSoSaoDanhGia;
    }

    public void setmSoSaoDanhGia(Float mSoSaoDanhGia)
    {
        this.mSoSaoDanhGia = mSoSaoDanhGia;
    }

    public Bitmap getmHinhBinhLuan()
    {
        return mHinhBinhLuan;
    }

    public void setmHinhBinhLuan(Bitmap mHinhBinhLuan)
    {
        this.mHinhBinhLuan = mHinhBinhLuan;
    }

    public String getmNoiDungBinhLuan()
    {
        return mNoiDungBinhLuan;
    }

    public void setmNoiDungBinhLuan(String mNoiDungBinhLuan)
    {
        this.mNoiDungBinhLuan = mNoiDungBinhLuan;
    }

    public String getmMaBinhLuan()
    {
        return mMaBinhLuan;
    }

    public void setmMaBinhLuan(String mMaBinhLuan)
    {
        this.mMaBinhLuan = mMaBinhLuan;
    }

    public String getmUser()
    {
        return mUserBinhLuan;
    }

    public void setmUser(String mUser)
    {
        this.mUserBinhLuan = mUser;
    }
}

