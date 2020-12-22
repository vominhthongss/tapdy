package com.example.tapdy;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DoiMatKhauActivity extends AppCompatActivity
{

    private EditText mkHienTai;
    private EditText mkMoi;
    private EditText mkMoi2;
    private Button doiMatKhau;
    private Button quayLaiDoiMatKhau;
    private ProgressBar pr;
    private TextView thongBaoMK;

    private String user="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mat_khau);
        nhanThongTin();
        khoiTaoCacNut();
        //xuLyNutDoiMatKhau();
        xuLyNutQuayLai();
        nutDoiMatKhau();
    }

    private void nutDoiMatKhau()
    {
        doiMatKhau.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
              //  Toast.makeText(DoiMatKhauActivity.this,mkHienTai.getText().toString()+" "+getUser(),Toast.LENGTH_SHORT).show();
                xuLyNutDoiMatKhau();
            }
        });
    }

    private void xuLyNutDoiMatKhau()
    {
        new LayMatKhau().execute();
        //1.lay user lay mk so sanh voi mkHienTai neu ko bang thi bao sai mk Hien Tai va` dung
        //2. mkMoi = mkMoi2 ? neu ko bang thi xoa 2 o duoi, bao loi~ keu nhap lai
    }
    private class LayMatKhau extends AsyncTask<Void,Void,Void>
    {
        String mk="";
        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql = "SELECT matkhau FROM nguoidung WHERE tennguoidung='"+getUser()+"' ";
            Statement stmt = null;
            ResultSet rs = null;


            try
            {
                stmt =getConnection().createStatement();
                rs=stmt.executeQuery(sql);
                while (rs.next())
                {
                    mk=rs.getString(1);
                }

            }
            catch (Exception e)
            {
                Log.d("LOI2",""+e);
            }
            return null;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        protected void onPostExecute(Void aVoid)
        {
            if(mk.equals(mkHienTai.getText().toString()))
            {
                //Toast.makeText(DoiMatKhauActivity.this,"dung mk",Toast.LENGTH_SHORT).show();
                if(mkMoi.getText().toString().equals(""))
                {
                 //   Toast.makeText(DoiMatKhauActivity.this,"Bạn chưa điền mật khẩu mới !",Toast.LENGTH_SHORT).show();
                    thongBaoMK.setText("Bạn chưa điền mật khẩu mới !");
                }
                else if(mkMoi.getText().toString().equals(mkMoi2.getText().toString()))
                {
                   // Toast.makeText(DoiMatKhauActivity.this,"Xu ly cap nhat mat khau",Toast.LENGTH_SHORT).show();
                    new CapNhatMatKhau().execute();
                }
                else
                {
                 //   Toast.makeText(DoiMatKhauActivity.this,"Xác nhận mật khẩu chưa chính xác ! Nhập lại !",Toast.LENGTH_SHORT).show();
                    thongBaoMK.setText("Xác nhận mật khẩu chưa chính xác ! Nhập lại !");

                    mkMoi.setText("");
                    mkMoi2.setText("");
                }
            }
            else
            {
                //Toast.makeText(DoiMatKhauActivity.this,"Mật khẩu hiện tại không đúng ! Nhập lại !",Toast.LENGTH_SHORT).show();
                thongBaoMK.setText("Mật khẩu hiện tại không đúng ! Nhập lại !");

                mkHienTai.setText("");
            }
            super.onPostExecute(aVoid);
        }
    }
    private class CapNhatMatKhau extends AsyncTask <Void,Void,Void>
    {

        @Override
        protected void onPreExecute()
        {
            pr.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql = "UPDATE nguoidung SET matkhau='"+mkMoi.getText().toString()+"' WHERE tennguoidung='"+getUser()+"' ";
            Statement stmt = null;
            try
            {
                stmt = getConnection().createStatement();
                stmt.execute(sql);
            }
            catch (Exception e)
            {

            }


            return null;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        protected void onPostExecute(Void aVoid)
        {
            pr.setVisibility(View.INVISIBLE);
            mkHienTai.setText("");
            mkMoi.setText("");
            mkMoi2.setText("");

            thongBaoMK.setText("Bạn đã cập nhật thành công mật khẩu !");
            super.onPostExecute(aVoid);
        }
    }

    private void xuLyNutQuayLai()
    {
        quayLaiDoiMatKhau.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent resultIntent = new Intent();
                setResult(RESULT_OK,resultIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed()
    {

        Intent resultIntent = new Intent();
        setResult(RESULT_OK,resultIntent);
        super.onBackPressed();
    }

    private void nhanThongTin()
    {
        Intent intent = getIntent();
        setUser(intent.getStringExtra("user"));
    }

    private void khoiTaoCacNut()
    {

        mkHienTai = findViewById(R.id.mkHienTai);
        mkMoi = findViewById(R.id.mkMoi);
        mkMoi2 = findViewById(R.id.mkMoi2);
        doiMatKhau = findViewById(R.id.doiMatKhau);
        quayLaiDoiMatKhau = findViewById(R.id.quayLaiDoiMatKhau);
        pr = findViewById(R.id.progressBarMK);
        pr.setVisibility(View.INVISIBLE);
        thongBaoMK = findViewById(R.id.thongBaoMK);



    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }
    private Connection getConnection()
    {
        return WelcomeActivity.CONNECTION;
    }
}
