package com.example.tapdy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ChiTietThongBaoActivity extends AppCompatActivity
{

    private TextView maTB;
    private TextView tenTB;
    private TextView ngayTB;
    private TextView noidungTB;
    private Button troLaiTB;
    private ImageView hinhTB;
    private Bitmap hinhthongbao=null;

    private String maThongBao="";
    private String tenThongBao="";
    private String ngayThongBao="";
    private String noiDungThongBao="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_thong_bao);
        maTB = findViewById(R.id.maTB);
        tenTB = findViewById(R.id.tenTB);
        ngayTB = findViewById(R.id.ngayTB);
        noidungTB = findViewById(R.id.noidungTB);
        troLaiTB = findViewById(R.id.troLaiTB);
        hinhTB = findViewById(R.id.hinhTB);
        //troLaiTB.setBackgroundResource(android.R.drawable.btn_default);
        nhanThongTin();
        xuLyTaiHinhThongBao();

        //
        // xu ly cac thong tin khac
        xuLyTaiThongTinThongBao();

        //
        xuLyNutTroLai();

    }

    private void xuLyTaiHinhThongBao()
    {
        new LayHinhThongBao().execute();
    }
    private class LayHinhThongBao extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql = "SELECT hinhthongbao FROM thongbao WHERE mathongbao='"+getMaThongBao()+"'";

            Statement stmt = null;
            ResultSet rs = null;
            Bitmap hinh=null;
            try
            {
                stmt =getConnection().createStatement();
                rs=stmt.executeQuery(sql);
                while(rs.next())
                {
                    Blob b = rs.getBlob(1);
                    int blobLength = (int) b.length();
                    byte[] blobAsBytes = b.getBytes(1, blobLength);
                    hinh = BitmapFactory.decodeByteArray(blobAsBytes, 0, blobAsBytes.length);
                    setHinhthongbao(hinh);

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
            hinhTB.setImageBitmap(getHinhthongbao());
            super.onPostExecute(aVoid);
        }
    }

    private void xuLyTaiThongTinThongBao()
    {
        maTB.setText(getMaThongBao());
        tenTB.setText(getTenThongBao());
        ngayTB.setText(getNgayThongBao());
        noidungTB.setText(getNoiDungThongBao());

    }
    private Connection getConnection()
    {
        return WelcomeActivity.CONNECTION;
    }

    private void nhanThongTin()
    {
        Intent intent = getIntent();
        setMaThongBao(intent.getStringExtra("mathongbao"));
        setTenThongBao(intent.getStringExtra("tenthongbao"));
        setNgayThongBao(intent.getStringExtra("ngaythongbao"));
        setNoiDungThongBao(intent.getStringExtra("noidungthongbao"));

    }

    private void xuLyNutTroLai()
    {
        troLaiTB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    public String getMaThongBao()
    {
        return maThongBao;
    }

    public void setMaThongBao(String maThongBao)
    {
        this.maThongBao = maThongBao;
    }

    public String getTenThongBao()
    {
        return tenThongBao;
    }

    public void setTenThongBao(String tenThongBao)
    {
        this.tenThongBao = tenThongBao;
    }

    public String getNgayThongBao()
    {
        return ngayThongBao;
    }

    public void setNgayThongBao(String ngayThongBao)
    {
        this.ngayThongBao = ngayThongBao;
    }

    public String getNoiDungThongBao()
    {
        return noiDungThongBao;
    }

    public void setNoiDungThongBao(String noiDungThongBao)
    {
        this.noiDungThongBao = noiDungThongBao;
    }

    public Bitmap getHinhthongbao()
    {
        return hinhthongbao;
    }

    public void setHinhthongbao(Bitmap hinhthongbao)
    {
        this.hinhthongbao = hinhthongbao;
    }
}
