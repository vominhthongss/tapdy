package com.example.tapdy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tapdy.ConnectMySQL.ConnectMySQL;
import com.example.tapdy.Fragment.AccountFragment;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class LoginActivity extends AppCompatActivity
{
    private Connection connection=null;
    private Button btnTroLai;
    private Button btnDangNhap;
    private EditText taiKhoan;
    private EditText matKhau;
    private ProgressBar progressBarLogin;
    private String TK,MK;
    private Boolean kt=false;
    private Bitmap avt=null;
    private TextView thongBaoLG;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBarLogin = findViewById(R.id.progressBarLogin);
        progressBarLogin.setVisibility(View.INVISIBLE);
        thongBaoLG = findViewById(R.id.thongBaoLG);

        btnDangNhap = findViewById(R.id.btnDangNhap);
        //btnDangNhap.setBackgroundResource(android.R.drawable.btn_default);
        btnTroLai = findViewById(R.id.troLai);
        //btnTroLai.setBackgroundResource(android.R.drawable.btn_default);
        taiKhoan = findViewById(R.id.taiKhoan);
        matKhau = findViewById(R.id.matKhau);


        btnDangNhap.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TK=taiKhoan.getText().toString();
                MK=matKhau.getText().toString();
                new KiemTraNguoiDung().execute();


            }
        });
        btnTroLai.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
                ChiTietQuanActivity.press=false;
                AccountFragment.nhan=false;
            }
        });
    }

    public Connection getConnection()
    {
        return WelcomeActivity.CONNECTION;
    }
    //ham luu user vao share
    public  void luuUserVaoThietBi (String str)
    {

        SharedPreferences s = getSharedPreferences("my_data",MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        editor.putString("user",str);
        editor.commit();
    }
    private void ve ()
    {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("user",taiKhoan.getText().toString());

        setResult(RESULT_OK,resultIntent);
        MainActivity.U_C=taiKhoan.getText().toString();
        /// luu USER vao Shared references
        luuUserVaoThietBi(taiKhoan.getText().toString());

        AccountFragment.nhan=false;
        finish();
    }

    @Override
    public void onBackPressed()
    {
        ChiTietQuanActivity.press=false;
        AccountFragment.nhan=false;
        super.onBackPressed();
    }

    private void setAvatar(Bitmap avt)
    {
        AccountFragment.avatar.setImageBitmap(avt);
        MainActivity.avt=avt;
    }

    private class KiemTraNguoiDung extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute()
        {
            progressBarLogin.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql = "SELECT * FROM nguoidung";
            Statement stmt = null;
            ResultSet rs = null;


            String tk,mk;

            try
            {
                stmt=getConnection().createStatement();
                rs=stmt.executeQuery(sql);
                while(rs.next())
                {

                    tk = rs.getString(1);
                    mk = rs.getString(2);
                    Log.d("THONG BAO","tk="+tk+" mk="+mk);
                    Log.d("THONG BAO","TK="+TK+" MK="+MK);



                    if(TK.equals(tk) && MK.equals(mk))
                    {
                        kt=true;
                        Blob b = rs.getBlob(6);
                        int blobLength = (int) b.length();
                        byte[] blobAsBytes = b.getBytes(1, blobLength);

                        avt = BitmapFactory.decodeByteArray(blobAsBytes, 0, blobAsBytes.length);



                        break;
                    }



                }
               // getConnection().close();

            }
            catch (Exception e)
            {
                Log.d("LOI",""+e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            progressBarLogin.setVisibility(View.INVISIBLE);
            if(kt)
            {
              //  Log.d("THONG BAO","Chinh xac");
                setAvatar(avt);
                ve();


            }
            else
            {
              //  Log.d("THONG BAO","KO Chinh xac");
                thongBaoLG.setText("Sai tài khoản hoặc mật khẩu !");

            }
            super.onPostExecute(aVoid);
        }
    }
}
