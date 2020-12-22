package com.example.tapdy;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class CheckinActivity extends AppCompatActivity
{
    private Boolean f=false;
    private String user="";
    private String maQuan="";
    private String tenQuan="";
    private String diaChi="";

    private ProgressBar progressBarCK;
    private TextView txtTenQuan;
    private TextView txtDiaChi;
    private EditText edtVietBinhLuan;
    private Button btnCheckin;
    private Button btnTroLai;

    private ImageButton chonHinhTaiLen;
    private ImageView hinhAnhTaiLen;
    int SELECT_PHOTO = 1;
    private String realPath="";
    Uri uri;
//    int permission_read= ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
//    int permission_write= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
String uuid = UUID.randomUUID().toString();
    String mabl= uuid.substring(0,8);
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        nhanThongTin();
        khoiTaoCacNut();

    }

    private void khoiTaoCacNut()
    {
        progressBarCK = findViewById(R.id.progressBarCK);
        progressBarCK.setVisibility(View.INVISIBLE);
        hinhAnhTaiLen = findViewById(R.id.hinhAnhTaiLenCheckin);
        chonHinhTaiLen = findViewById(R.id.chonHinhAnhTaiLenCheckin);
     //   chonHinhTaiLen.setBackgroundResource(android.R.drawable.btn_default);
        txtTenQuan = findViewById(R.id.tenQuanCheckin);
        txtDiaChi = findViewById(R.id.diaChiCheckin);
        edtVietBinhLuan = findViewById(R.id.vietBinhLuanCheckin);
        btnCheckin = findViewById(R.id.btnCheckinCheckin);
        btnTroLai = findViewById(R.id.btnTroLaiCheckin);


        txtTenQuan.setText(getTenQuan());
        txtDiaChi.setText(getDiaChi());

        //btnCheckin.setBackgroundResource(android.R.drawable.btn_default);
      //  btnTroLai.setBackgroundResource(android.R.drawable.btn_default);
        btnTroLai.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChiTietQuanActivity.press=false;
                finish();
            }
        });
        xuLyNutCheckin();
        xuLyNutTaiHinh();

    }

    @Override
    public void onBackPressed()
    {
        ChiTietQuanActivity.press=false;
        super.onBackPressed();
    }

    private String getMaCheckin()
    {
        return this.mabl;
    }
    private String getNoiDungCheckin()
    {
        return edtVietBinhLuan.getText().toString();
    }
    private void xuLyNutCheckin()
    {
        btnCheckin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(f==false)
                {
                    if(getNoiDungCheckin().equals(""))
                    {
                        Toast.makeText(CheckinActivity.this,"Bạn chưa ghi nội dung ! !",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        f=true;
                        //xu ly
                        //gữi dữ liệu lên bảng Checkin
                        new UploadCheckin().execute();
                    }

                }


            }
        });
    }
    private class UploadCheckin extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute()
        {
            progressBarCK.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql="INSERT INTO checkin VALUE (?,?,?,?,?)";

            try (PreparedStatement pstmt = getConnection().prepareStatement(sql))
            {

                if(realPath.equals(""))
                {
                    pstmt.setString(1, getMaCheckin());
                    pstmt.setString(2,getNoiDungCheckin());
                    pstmt.setBinaryStream(3,null);

                    pstmt.setString(4,getUser());
                    pstmt.setString(5,getMaQuan());

                    pstmt.executeUpdate();

                }
                else
                {

                    File file = new File(realPath);

                    FileInputStream input = new FileInputStream(file);
                    // set parameters

                    pstmt.setString(1, getMaCheckin());
                    pstmt.setString(2,getNoiDungCheckin());

                    pstmt.setBinaryStream(3, input);
                    pstmt.setString(4,getUser());
                    pstmt.setString(5,getMaQuan());
                        // store the resume file in database
                   // Log.d("UPLOAD ","Đọc file " + file.getAbsolutePath());
                //    Log.d("UPLOAD ","Đã lưu");
                //    Log.d("UPLOAD ", ""+pstmt.executeUpdate());
                    pstmt.executeUpdate();



                    //




                }


            } catch (SQLException | FileNotFoundException e) {
                Log.d("UPLOAD ",""+e.getMessage());

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            progressBarCK.setVisibility(View.INVISIBLE);
            ve();
            super.onPostExecute(aVoid);
        }
    }

    ///////////////////////////
    private void xuLyNutTaiHinh()
    {
        chonHinhTaiLen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if((ContextCompat.checkSelfPermission(CheckinActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))!= PackageManager.PERMISSION_GRANTED || (ContextCompat.checkSelfPermission(CheckinActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))!=PackageManager.PERMISSION_GRANTED)
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
        ActivityCompat.requestPermissions(CheckinActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
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

            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private Connection getConnection ()
    {
        return WelcomeActivity.CONNECTION;
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
    /////////////////////////////////////////////


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
        resultIntent.putExtra("checkinxong",true);

        setResult(RESULT_OK,resultIntent);
        ChiTietQuanActivity.press=false;

        finish();
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
