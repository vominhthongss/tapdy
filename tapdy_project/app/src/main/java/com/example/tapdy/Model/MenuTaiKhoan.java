package com.example.tapdy.Model;

public class MenuTaiKhoan
{
    private int mHinhDongMenu;
    private String mTenDongMenu;

    public MenuTaiKhoan(int mHinhDongMenu, String mTenDongMenu)
    {
        this.mHinhDongMenu = mHinhDongMenu;
        this.mTenDongMenu = mTenDongMenu;
    }

    public int getmHinhDongMenu()
    {
        return mHinhDongMenu;
    }

    public void setmHinhDongMenu(int mHinhDongMenu)
    {
        this.mHinhDongMenu = mHinhDongMenu;
    }

    public String getmTenDongMenu()
    {
        return mTenDongMenu;
    }

    public void setmTenDongMenu(String mTenDongMenu)
    {
        this.mTenDongMenu = mTenDongMenu;
    }
}
