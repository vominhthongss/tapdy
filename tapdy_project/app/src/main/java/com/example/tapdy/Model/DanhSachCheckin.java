package com.example.tapdy.Model;

import android.graphics.Bitmap;

public class DanhSachCheckin
{
    private Bitmap mAvt;
    private String mHoTenCheckin;
    private Bitmap mHinhCheckin;
    private String mNoiDungCheckin;
    private String mMaCheckin;
    private String mUserCheckin;


    public DanhSachCheckin(Bitmap mAvt, String mHoTenCheckin, Bitmap mHinhCheckin, String mNoiDungCheckin, String mMaCheckin, String mUserCheckin)
    {
        this.mAvt = mAvt;
        this.mHoTenCheckin = mHoTenCheckin;
        this.mHinhCheckin = mHinhCheckin;
        this.mNoiDungCheckin = mNoiDungCheckin;
        this.mMaCheckin = mMaCheckin;
        this.mUserCheckin=mUserCheckin;
    }

    public Bitmap getmAvt()
    {
        return mAvt;
    }

    public void setmAvt(Bitmap mAvt)
    {
        this.mAvt = mAvt;
    }

    public String getmHoTenCheckin()
    {
        return mHoTenCheckin;
    }

    public void setmHoTenCheckin(String mHoTenCheckin)
    {
        this.mHoTenCheckin = mHoTenCheckin;
    }

    public Bitmap getmHinhCheckin()
    {
        return mHinhCheckin;
    }

    public void setmHinhCheckin(Bitmap mHinhCheckin)
    {
        this.mHinhCheckin = mHinhCheckin;
    }

    public String getmNoiDungCheckin()
    {
        return mNoiDungCheckin;
    }

    public void setmNoiDungCheckin(String mNoiDungCheckin)
    {
        this.mNoiDungCheckin = mNoiDungCheckin;
    }

    public String getmMaCheckin()
    {
        return mMaCheckin;
    }

    public void setmMaCheckin(String mMaCheckin)
    {
        this.mMaCheckin = mMaCheckin;
    }

    public String getmUserCheckin()
    {
        return mUserCheckin;
    }

    public void setmUserCheckin(String mUserCheckin)
    {
        this.mUserCheckin = mUserCheckin;
    }
}
