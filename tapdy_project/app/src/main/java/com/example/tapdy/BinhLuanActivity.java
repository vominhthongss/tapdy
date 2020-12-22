package com.example.tapdy;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class BinhLuanActivity extends AppCompatActivity
{
    private Boolean f=false;
    private String user="";
    private String maQuan="";
    private String tenQuan="";
    private String diaChi="";
    private String soSao="";

    private ProgressBar progressBarBL;

    private TextView txtTenQuan;
    private TextView txtDiaChi;
    private EditText edtVietBinhLuan;
    private Button btnBinhLuan;
    private Button btnTroLai;
    private RatingBar ratingBar;
    private ImageButton chonHinhTaiLen;
    private ImageView hinhAnhTaiLen;
    int SELECT_PHOTO = 1;
    private String realPath="";
    Uri uri;

    String uuid = UUID.randomUUID().toString();
    String mabl= uuid.substring(0,8);
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binh_luan);
        nhanThongTin();
        khoiTaoCacNut();

    }

    @Override
    public void onBackPressed()
    {
        ChiTietQuanActivity.press=false;

        super.onBackPressed();
    }

    private void khoiTaoCacNut()
    {
        progressBarBL = findViewById(R.id.progressBarBL);
        progressBarBL.setVisibility(View.INVISIBLE);
        hinhAnhTaiLen = findViewById(R.id.hinhAnhTaiLenBinhLuan);
        chonHinhTaiLen = findViewById(R.id.chonHinhAnhTaiLenBinhLuan);
       // chonHinhTaiLen.setBackgroundResource(android.R.drawable.btn_default);
        ratingBar = findViewById(R.id.soSao);
        txtTenQuan = findViewById(R.id.tenQuanBinhLuan);
        txtDiaChi = findViewById(R.id.diaChiBinhLuan);
        edtVietBinhLuan = findViewById(R.id.vietBinhLuanBinhLuan);
        btnBinhLuan = findViewById(R.id.btnBinhLuanBinhLuan);
        btnTroLai = findViewById(R.id.btnTroLaiBinhLuan);

        txtTenQuan.setText(getTenQuan());
        txtDiaChi.setText(getDiaChi());


        btnTroLai.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChiTietQuanActivity.press=false;
                finish();
            }
        });
      //  btnBinhLuan.setBackgroundResource(android.R.drawable.btn_default);
      //  btnTroLai.setBackgroundResource(android.R.drawable.btn_default);
        xuLyNutBinhLuan();
        xuLyNutTaiHinh();


    }
    private String getMaBinhLuan()
    {
        return this.mabl;
    }
    private String getBinhLuan()
    {
        return edtVietBinhLuan.getText().toString();
    }

    private void xuLyNutBinhLuan()
    {

        btnBinhLuan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(f==false)
                {
                    if(String.valueOf(ratingBar.getRating()).equals("0") || getBinhLuan().equals("") )
                    {
                        Toast.makeText(BinhLuanActivity.this,"Bạn chưa ghi nội dung hoặc đánh giá !",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        f=true;

                        setSoSao(String.valueOf(ratingBar.getRating())); // gán số sao
                        //xu ly
                        //gữi dữ liệu lên bảng Bình Luận
                      //  Toast.makeText(BinhLuanActivity.this,"Lan2: "+realPath,Toast.LENGTH_LONG).show();
                        new UploadBinhLuan().execute();
                    }

                }




            }
        });
    }
    private  class UploadBinhLuan extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute()
        {
            progressBarBL.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Void doInBackground(Void... voids)
        {

            String sql = "INSERT INTO binhluan VALUE (?,?,?,?,?,?)";

            try (PreparedStatement pstmt = getConnection().prepareStatement(sql))
            {
                if(realPath.equals(""))
                {

                    // set parameters
                    pstmt.setString(1, getMaBinhLuan());
                    pstmt.setString(2,getMaQuan());
                    pstmt.setString(3,getUser());
                    pstmt.setString(4,getBinhLuan());
                    pstmt.setBinaryStream(5, null);
                    pstmt.setFloat(6,Float.parseFloat(getSoSao()));

                    pstmt.executeUpdate();

                }
                else
                {
                    // read the file

                    File file = new File(realPath);
                    FileInputStream input = new FileInputStream(file);

                    // set parameters
                    pstmt.setString(1, getMaBinhLuan());
                    pstmt.setString(2,getMaQuan());
                    pstmt.setString(3,getUser());
                    pstmt.setString(4,getBinhLuan());
                    pstmt.setBinaryStream(5, input);
                    pstmt.setFloat(6,Float.parseFloat(getSoSao()));

                    // store the resume file in database
                 //   Log.d("UPLOADBL ","Đọc file " + file.getAbsolutePath());
                    //Toast.makeText(BinhLuanActivity.this,""+file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
                   // Log.d("UPLOADBL ","Đã lưu");
                    pstmt.executeUpdate();

                }


            } catch (SQLException | FileNotFoundException e) {
                Log.d("UPLOADBL ",""+e.getMessage());

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            new UploadQuanLyBinhLuan().execute();

            super.onPostExecute(aVoid);
        }
    }
    private class UploadQuanLyBinhLuan extends AsyncTask <Void,Void,Void>
    {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Void doInBackground(Void... voids)
        {

            String sql = "INSERT INTO quanlybinhluan VALUE (?,?,?)";

            try (PreparedStatement pstmt = getConnection().prepareStatement(sql))
            {

                // set parameters
                pstmt.setString(1, getUser());
                pstmt.setString(2,getMaQuan());
                pstmt.setString(3,getMaBinhLuan());

                pstmt.executeUpdate();

            }
            catch (Exception e)
            {
                Log.d("UPLOAD ",""+e);

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            progressBarBL.setVisibility(View.INVISIBLE);
            ve();
            super.onPostExecute(aVoid);
        }
    }

    private void xuLyNutTaiHinh()
    {
        chonHinhTaiLen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if((ContextCompat.checkSelfPermission(BinhLuanActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))!= PackageManager.PERMISSION_GRANTED
                        || (ContextCompat.checkSelfPermission(BinhLuanActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) !=PackageManager.PERMISSION_GRANTED)
                {
                    makeRequest();
                }
                else
                {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent,SELECT_PHOTO);
                }


            }
        });
    }
    protected void makeRequest() {
        ActivityCompat.requestPermissions(BinhLuanActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SELECT_PHOTO && resultCode== RESULT_OK && data !=null && data.getData()!=null)
        {
            uri = data.getData();
            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                hinhAnhTaiLen.setImageBitmap(bitmap);

                realPath=getRealPathFromURI(uri);
                Log.d("=> PATH  ",""+realPath);
               // Toast.makeText(BinhLuanActivity.this,""+realPath,Toast.LENGTH_LONG).show();

            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    public String getRealPathFromURI (Uri contentUri)
    {
        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst())
        {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }
    private Connection getConnection ()
    {
        return WelcomeActivity.CONNECTION;
    }

    private void nhanThongTin()
    {
        Intent intent = getIntent();
        setUser(intent.getStringExtra("user"));
        setMaQuan(intent.getStringExtra("maquan"));
        setTenQuan(intent.getStringExtra("tenquan"));
        setDiaChi(intent.getStringExtra("diachi"));
    }

    private void ve ()
    {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("binhluanxong",true);

        setResult(RESULT_OK,resultIntent);

        ChiTietQuanActivity.press=false;

        finish();
    }

    public String getSoSao()
    {
        return soSao;
    }

    public void setSoSao(String soSao)
    {
        this.soSao = soSao;
    }

    public String getMaQuan()
    {
        return maQuan;
    }

    public void setMaQuan(String maQuan)
    {
        this.maQuan = maQuan;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getTenQuan()
    {
        return tenQuan;
    }

    public void setTenQuan(String tenQuan)
    {
        this.tenQuan = tenQuan;
    }

    public String getDiaChi()
    {
        return diaChi;
    }

    public void setDiaChi(String diaChi)
    {
        this.diaChi = diaChi;
    }
}
