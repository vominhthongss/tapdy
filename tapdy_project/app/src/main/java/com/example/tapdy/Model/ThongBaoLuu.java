package com.example.tapdy.Model;

public class ThongBaoLuu
{
    private String mMaThongBao;
    private String mTenThongBao;
    private String mNoiDungThongBao;
    private String mNgayThongBao;
    private int mDaXoa;
    private int mDaXem;

    public ThongBaoLuu(String mMaThongBao, String mTenThongBao, String mNoiDungThongBao, String mNgayThongBao, int mDaXoa, int mDaXem)
    {
        this.mMaThongBao = mMaThongBao;
        this.mTenThongBao = mTenThongBao;
        this.mNoiDungThongBao = mNoiDungThongBao;
        this.mNgayThongBao = mNgayThongBao;
        this.mDaXoa = mDaXoa;
        this.mDaXem = mDaXem;
    }

    public String getmMaThongBao()
    {
        return mMaThongBao;
    }

    public void setmMaThongBao(String mMaThongBao)
    {
        this.mMaThongBao = mMaThongBao;
    }

    public String getmTenThongBao()
    {
        return mTenThongBao;
    }

    public void setmTenThongBao(String mTenThongBao)
    {
        this.mTenThongBao = mTenThongBao;
    }

    public String getmNoiDungThongBao()
    {
        return mNoiDungThongBao;
    }

    public void setmNoiDungThongBao(String mNoiDungThongBao)
    {
        this.mNoiDungThongBao = mNoiDungThongBao;
    }

    public String getmNgayThongBao()
    {
        return mNgayThongBao;
    }

    public void setmNgayThongBao(String mNgayThongBao)
    {
        this.mNgayThongBao = mNgayThongBao;
    }

    public int getmDaXoa()
    {
        return mDaXoa;
    }

    public void setmDaXoa(int mDaXoa)
    {
        this.mDaXoa = mDaXoa;
    }

    public int getmDaXem()
    {
        return mDaXem;
    }

    public void setmDaXem(int mDaXem)
    {
        this.mDaXem = mDaXem;
    }
}
