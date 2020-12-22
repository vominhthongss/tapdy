package com.example.tapdy.MenuAcccount;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tapdy.ChinhSuaHoSoActivity;
import com.example.tapdy.DoiMatKhauActivity;
import com.example.tapdy.Fragment.AccountFragment;
import com.example.tapdy.MainActivity;
import com.example.tapdy.R;
import com.example.tapdy.WelcomeActivity;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class HoSoActivity extends AppCompatActivity
{
    private TextView userHoSo;
    private TextView hoTenHoSo;
    private TextView ngaySinhHoSo;
    private TextView sdtHoSo;
    private ImageView hinhAnhHoSo;
    private ImageButton imgChinhSuaHoSo;
    private ImageButton imgDoiMatKhau;
    private Button quayLaiHoSo;

    private Boolean nhanNutChinhSua=false;
    private Boolean nhanNutDoiMk=false;


    private String user="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ho_so);
        nhanThongTin();
        khoiTaoCacNut();
        xuLyUser();
        xuLyCacThongTinConLai();
        nutChinhSUaHoSo();
        nutDoiMatKhau();
        xuLyNutQuayLai();

    }

    private void xuLyNutQuayLai()
    {
        quayLaiHoSo.setOnClickListener(new View.OnClickListener()
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

    private void nutDoiMatKhau()
    {
        imgDoiMatKhau.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(nhanNutDoiMk==false)
                {
                    nhanNutDoiMk=true;
                    xuLyNutDoiMatKhau();
                }

            }
        });
    }

    private void nutChinhSUaHoSo()
    {
        imgChinhSuaHoSo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(nhanNutChinhSua==false)
                {
                    nhanNutChinhSua=true;

                    xuLyNutChinhSuaHoSo();


                }

            }
        });
    }

    private void xuLyNutDoiMatKhau()
    {
        //chuyen sang activity moi
        Intent intent = new Intent(HoSoActivity.this, DoiMatKhauActivity.class);
        intent.putExtra("user",getUser());
        startActivityForResult(intent,2);
    }

    private void xuLyNutChinhSuaHoSo()
    {
        //chuyen sang activity moi
        Intent intent = new Intent(HoSoActivity.this, ChinhSuaHoSoActivity.class);
        intent.putExtra("user",getUser());
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            if(resultCode==RESULT_OK)
            {
                //Toast.makeText(HoSoActivity.this,"Xu ly tai lai thong tin",Toast.LENGTH_SHORT).show();
                nhanNutChinhSua=false;
                xuLyCacThongTinConLai();
            }
        }
        if(requestCode==2)
        {
            if(resultCode==RESULT_OK)
            {
              //  Toast.makeText(HoSoActivity.this,"Xu ly",Toast.LENGTH_SHORT).show();
                nhanNutDoiMk=false;
            }
        }

    }

    private void xuLyCacThongTinConLai()
    {
        new LayThongTin().execute();
    }
    private class LayThongTin extends AsyncTask<Void,Void,Void>
    {
        String hoten;
        String ngaysinh;
        String sdt;
        Bitmap avatar=null;

        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql = "SELECT * FROM nguoidung WHERE tennguoidung='"+getUser()+"'";
            Statement stmt = null;
            ResultSet rs = null;


            try
            {
                stmt =getConnection().createStatement();
                rs=stmt.executeQuery(sql);
                while(rs.next())
                {
                    hoten=rs.getString(3);
                    ngaysinh=rs.getString(4);
                    sdt=rs.getString(5);
                    Blob b = rs.getBlob(6);
                    int blobLength = (int) b.length();
                    byte[] blobAsBytes = b.getBytes(1, blobLength);
                    avatar = BitmapFactory.decodeByteArray(blobAsBytes, 0, blobAsBytes.length);
                }
            }
            catch (Exception e)
            {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            hoTenHoSo.setText(hoten);
            ngaySinhHoSo.setText(suaNgaySinh(ngaysinh));
            sdtHoSo.setText(sdt);
            hinhAnhHoSo.setImageBitmap(avatar);
            AccountFragment.avatar.setImageBitmap(avatar);
            MainActivity.avt=avatar;
            super.onPostExecute(aVoid);
        }
    }
    private String suaNgaySinh(String s)
    {
        int i = s.indexOf("-");
        int j = s.lastIndexOf("-");
        String ngay,thang,nam;
        nam=s.substring(0,i);
        thang=s.substring(i+1,j);
        ngay=s.substring(j+1,s.length());
        return ngay+"-"+thang+"-"+nam;
    }

    private void xuLyUser()
    {
        userHoSo.setText(""+getUser());
    }

    private void khoiTaoCacNut()
    {
        userHoSo = findViewById(R.id.userHoSo);
        hoTenHoSo = findViewById(R.id.hoTenHoSo);
        ngaySinhHoSo = findViewById(R.id.ngaySinhHoSo);
        sdtHoSo = findViewById(R.id.sdtHoSo);
        hinhAnhHoSo = findViewById(R.id.hinhAnhHoSo);
        imgChinhSuaHoSo = findViewById(R.id.imgChinhSuaHoSo);
        imgDoiMatKhau = findViewById(R.id.imgDoiMatKhau);
        quayLaiHoSo = findViewById(R.id.quayLaiHoSo);
    }

    private void nhanThongTin()
    {
        Intent intent= getIntent();
        setUser(intent.getStringExtra("user"));

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

    @Override
    public void onBackPressed()
    {
        Intent resultIntent = new Intent();
        setResult(RESULT_OK,resultIntent);
        super.onBackPressed();
    }
}
