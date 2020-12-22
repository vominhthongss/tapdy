package com.example.tapdy;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.tapdy.MenuAcccount.HoSoActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ChinhSuaHoSoActivity extends AppCompatActivity
{
    private EditText hoTenChinhSua;
    private EditText ngaySinhChinhSua;
    private EditText sdtChinhSua;
    private ImageView hinhAnhChinhSua;
    private ImageButton imgTaiAnhChinhSua;
    private TextView userChinhSua;
    private Button capNhatChinhSua;
    private Button quayLaiChinhSua;
    private TextView thongBaoCS;
    private ProgressBar pr;

    private String user="";

    int SELECT_PHOTO = 1;
    private String realPath="";
    Uri uri;

    private Boolean nhanNutTaiAnh=false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinh_sua_ho_so);


        nhaThongTin();
        khoiTaoCacNut();
        xuLyUser();
        xyLyCacThongTinConLai();
        xuLyNutTaiHinh();
        xuLyNutCapNhat();
        xyLyQuayLai();


    }

    private void xyLyQuayLai()
    {
        quayLaiChinhSua.setOnClickListener(new View.OnClickListener()
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

    private void xuLyNutCapNhat()
    {
        capNhatChinhSua.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new UploadCapNhat().execute();
            }
        });

    }
    private class UploadCapNhat extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute()
        {
            pr.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @SuppressLint("WrongThread")
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql;
            if(realPath.equals(""))
            {
                sql = "UPDATE nguoidung SET hotennguoidung=?, ngaysinh=?,sodienthoainguoidung=? WHERE tennguoidung='"+getUser()+"' ";
            }
            else
            {
                sql = "UPDATE nguoidung SET hotennguoidung=?, ngaysinh=?,sodienthoainguoidung=?,avatar=? WHERE tennguoidung='"+getUser()+"' ";
            }


            try (PreparedStatement pstmt = getConnection().prepareStatement(sql))
            {
                if(realPath.equals(""))
                {
                    pstmt.setString(1,hoTenChinhSua.getText().toString());
                    pstmt.setString(2,daoNgaySinh(ngaySinhChinhSua.getText().toString()));
                    pstmt.setString(3,sdtChinhSua.getText().toString());
                    pstmt.executeUpdate();
                }

                else
                {
                    // read the file
                    File file = new File(realPath);
                    FileInputStream input = new FileInputStream(file);

                    // set parameters
                    pstmt.setString(1, hoTenChinhSua.getText().toString());
                    pstmt.setString(2, daoNgaySinh(ngaySinhChinhSua.getText().toString()));
                    pstmt.setString(3, sdtChinhSua.getText().toString());
                    pstmt.setBinaryStream(4, input);//avt

                    // store the resume file in database
                    Log.d("UPLOAD ", "Đọc file " + file.getAbsolutePath());
                    Log.d("UPLOAD ", "Đã lưu");
                    pstmt.executeUpdate();
                }




            } catch (SQLException | FileNotFoundException e)
            {
                Log.d("UPLOAD "," Loi"+e.getMessage());

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            pr.setVisibility(View.INVISIBLE);
            thongBaoCS.setText("Bạn đã cập nhật thành công tin hồ sơ !");

            //Toast.makeText(ChinhSuaHoSoActivity.this,"Đã cập nhật xong",Toast.LENGTH_SHORT).show();
            super.onPostExecute(aVoid);
        }
    }

    /////////// tai anh
    private void xuLyNutTaiHinh()
    {
        imgTaiAnhChinhSua.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(nhanNutTaiAnh==false)
                {
                    nhanNutTaiAnh=true;
                    if ((ContextCompat.checkSelfPermission(ChinhSuaHoSoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) != PackageManager.PERMISSION_GRANTED
                            || (ContextCompat.checkSelfPermission(ChinhSuaHoSoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) != PackageManager.PERMISSION_GRANTED)
                    {
                        makeRequest();
                    } else
                    {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, SELECT_PHOTO);
                    }
                }


            }
        });
    }
    protected void makeRequest() {
        ActivityCompat.requestPermissions(ChinhSuaHoSoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
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
                hinhAnhChinhSua.setImageBitmap(bitmap);
                nhanNutTaiAnh=false;
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
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void xyLyCacThongTinConLai()
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
            hoTenChinhSua.setText(hoten);
            ngaySinhChinhSua.setText(suaNgaySinh(ngaysinh));
            sdtChinhSua.setText(sdt);
            hinhAnhChinhSua.setImageBitmap(avatar);
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
    private String daoNgaySinh(String s)
    {
        int i = s.indexOf("-");
        int j = s.lastIndexOf("-");
        String ngay,thang,nam;
        ngay=s.substring(0,i);
        thang=s.substring(i+1,j);
        nam=s.substring(j+1,s.length());
        return nam+"-"+thang+"-"+ngay;
    }
    private Connection getConnection()
    {
        return WelcomeActivity.CONNECTION;
    }

    private void xuLyUser()
    {
        userChinhSua.setText(""+getUser());
    }

    private void nhaThongTin()
    {
        Intent intent = getIntent();
        setUser(intent.getStringExtra("user"));
    }

    private void khoiTaoCacNut()
    {
        hoTenChinhSua=findViewById(R.id.hoTenChinhSua);
        ngaySinhChinhSua =findViewById(R.id.ngaySinhChinhSua);
        sdtChinhSua = findViewById(R.id.sdtChinhSua);
        hinhAnhChinhSua = findViewById(R.id.hinhAnhChinhSua);
        imgTaiAnhChinhSua = findViewById(R.id.imgTaiAnhChinhSua);
        userChinhSua = findViewById(R.id.userChinhSua);
        capNhatChinhSua = findViewById(R.id.capNhatChinhSua);
        quayLaiChinhSua = findViewById(R.id.quayLaiChinhSua);
        thongBaoCS = findViewById(R.id.thongBaoCS);
        pr=findViewById(R.id.progressBarHoSo);
        pr.setVisibility(View.INVISIBLE);
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    @Override
    public void onBackPressed()
    {
        Intent resultIntent = new Intent();
        setResult(RESULT_OK,resultIntent);
        super.onBackPressed();
    }
}
