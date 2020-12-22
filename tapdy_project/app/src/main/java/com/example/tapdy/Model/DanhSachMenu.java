package com.example.tapdy.Model;

import android.graphics.Bitmap;

public class DanhSachMenu
{
    private Bitmap mHinhMonAn;
    private String mTenMonAn;
    private String mGiaMonAn;

    public DanhSachMenu(Bitmap mHinhMonAn, String mTenMonAn, String mGiaMonAn)
    {
        this.mHinhMonAn = mHinhMonAn;
        this.mTenMonAn = mTenMonAn;
        this.mGiaMonAn = mGiaMonAn;
    }

    public Bitmap getmHinhMonAn()
    {
        return mHinhMonAn;
    }

    public void setmHinhMonAn(Bitmap mHinhMonAn)
    {
        this.mHinhMonAn = mHinhMonAn;
    }

    public String getmTenMonAn()
    {
        return mTenMonAn;
    }

    public void setmTenMonAn(String mTenMonAn)
    {
        this.mTenMonAn = mTenMonAn;
    }

    public String getmGiaMonAn()
    {
        return mGiaMonAn;
    }

    public void setmGiaMonAn(String mGiaMonAn)
    {
        this.mGiaMonAn = mGiaMonAn;
    }
}
