package com.example.tapdy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.tapdy.Adapter.AdapterDanhSachBinhLuan;
import com.example.tapdy.Adapter.AdapterDanhSachMenu;
import com.example.tapdy.AdapterHinhQuan.HinhQuanAdapter;
import com.example.tapdy.Dialog.CustomDialog;
import com.example.tapdy.Model.DanhSachBinhLuan;

import com.example.tapdy.Model.DanhSachMenu;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;

import java.security.Permission;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ChiTietQuanActivity extends AppCompatActivity implements OnMapReadyCallback
{
    private TextView _tennguoidung, _mabinhluan;
    private String _TenNguoiDung = "", _MaBinhLuan = "";

    public static Boolean press=false;


    //AppCompatActivity
    private Toolbar toolbarChiTietQuan;
    private String maQuan = "";
    private String tenQuan = "";
    private Bitmap hinhQuan = null;
    private String diaChi = "";
    private String soDienThoai = "";
    private String gioHoatDong = "";
    private String user = "";

    private String soBinhLuan = "";
    private String soCheckin = "";
    private String soLuuLai = "";
    private String soHinhAnh = "";
    private int soHinhBinhLuan, soHinhCheckin;

    private Boolean checkin = false;
    private Boolean binhluan = false;
    private Boolean luulai = false;

    private Boolean daluulai = false;
    private Boolean dabinhluan = false;
    private TextView cachXa;

    //số điểm
    private TextView soDiem;

//    private ImageView hinhChiTietQuan;
//    private ViewFlipper hinhChiTietQuan;

    private ViewPager hinhChiTietQuan;
    private LinearLayout dotPanel;
    private int dotCount;
    private ImageView [] dots;
    private Bitmap [] dsHinhQuan = new Bitmap[100];
    private int soDsHinhQuan;

    private TextView diaChiChiTiet;
    private TextView sdtChiTiet;
    private TextView gioHoatDongChiTiet;
    private ImageButton btnLienHe;
    private ImageButton btnLuuLai;
    private ImageButton btnChiaSe;
    private ImageButton btnCheckin;
    private ImageButton btnBinhLuan;
    private TextView txtSoBinhLuan;
    private TextView txtSoCheckin;
    private TextView txtSoLuuLai;
    private TextView txtSoHinhAnh;

    private ListView lvDanhSachBinhLuan;
    private ArrayList<DanhSachBinhLuan> arrayListDanhSachBinhLuan;
    private AdapterDanhSachBinhLuan adapterDanhSachBinhLuan;

    private ListView lvDanhSachMenu;
    private ArrayList<DanhSachMenu> arrayListDanhSachMenu;
    private AdapterDanhSachMenu adapterDanhSachMenu;
    private LinearLayout khungMN;

    private GoogleMap map;
    private Double distance;
    private LinearLayout khungDS;

    private double latitudeToi,longitudeToi,lattitudeQuan,longitudeQuan;


    private String cachxa;
    private FusedLocationProviderClient client;






    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_quan);

        nhanThongTin();
        layViTriCuaToi();


        khoiTaoHinhQuan();
        khoiTaoDiaChivaSoDienThoai();
        khoiTaoGioHoatDong();
        khoiTaoCacNut();
        kiemTraCoLuuLai();
        khoiTaoToolbar();
        demSoBinhLuan();
        demHinhAnh();
        demSoCheckin();
        demLuuLai();

        new LayCacHinhCuaQuan().execute();
        new LaySoDiem().execute();

        new LayDanhSachMenu().execute();
        new LayThongTinQuan().execute();


        new KiemTraCoLuuLaiChua().execute();
        new KiemTraCoBinhLuan().execute();
        new LayBinhLuan().execute();





    }
    private class LaySoDiem extends AsyncTask<Void,Void,Void>
    {

        int i=0;
        float diem=0f;
        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql = "SELECT sao FROM binhluan WHERE maquan='"+getMaQuan()+"' ";
            Statement stmt = null;
            ResultSet rs = null;
            try
            {
                stmt = getConnection().createStatement();
                rs=stmt.executeQuery(sql);
                while(rs.next())
                {
                    i++;
                    diem+=rs.getFloat(1);

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
            if(diem==0 && i==0)
            {
                soDiem.setText("Chưa đánh giá");
            }
            else
            {


                diem /= i;


                DecimalFormat f = new DecimalFormat("0.00");
                soDiem.setText("" + f.format(diem));
            }
            super.onPostExecute(aVoid);
        }
    }
    private class LayDanhSachMenu extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute()
        {
            Log.d("LOI","1");
            arrayListDanhSachMenu = new ArrayList<>();
            Log.d("LOI","2");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            Log.d("LOI","3");
            String sql =  "SELECT hinhmonan,tenmonan,giamonan FROM menuquanan WHERE maquan='"+getMaQuan()+"' ORDER BY tenmonan ASC";
            Log.d("LOI","4");
            Statement stmt = null;
            ResultSet rs = null;

            Bitmap hinhmonan=null;
            String tenmonan="";
            String giamonan="";
            try
            {
                stmt=getConnection().createStatement();
                rs=stmt.executeQuery(sql);
                while(rs.next())
                {
                    // 1
                    Blob b = rs.getBlob(1);
                    int blobLength = (int) b.length();
                    byte[] blobAsBytes = b.getBytes(1, blobLength);
                    hinhmonan = BitmapFactory.decodeByteArray(blobAsBytes, 0, blobAsBytes.length);
                    //2
                    tenmonan=rs.getString(2);
                    //3
                    giamonan=rs.getString(3);

                    Log.d("LOI","5");
                    arrayListDanhSachMenu.add(new DanhSachMenu(hinhmonan,tenmonan,giamonan));
                    Log.d("LOI","6");

                }
                adapterDanhSachMenu = new AdapterDanhSachMenu(ChiTietQuanActivity.this,R.layout.menu_row_item,arrayListDanhSachMenu);

            }
            catch (Exception e)
            {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            lvDanhSachMenu.setAdapter(adapterDanhSachMenu);
            khungMN.getLayoutParams().height=getLVHeight(lvDanhSachMenu);
            super.onPostExecute(aVoid);


        }
    }
    private void layViTriCuaToi()
    {

        client = LocationServices.getFusedLocationProviderClient(this);
        client.getLastLocation().addOnSuccessListener(ChiTietQuanActivity.this, new OnSuccessListener<Location>()
        {
            @Override
            public void onSuccess(Location location)
            {
                if(location!=null)
                {
                    latitudeToi=location.getLatitude();
                    longitudeToi=location.getLongitude();



                }


            }
        });




    }




    public double getLatitudeToi()
    {
        return latitudeToi;
    }

    public void setLatitudeToi(double latitude)
    {
        this.latitudeToi = latitude;
    }

    public double getLattitudeQuan()
    {
        return lattitudeQuan;
    }

    public void setLatitudeQuan(double lattitudeQuan)
    {
        this.lattitudeQuan = lattitudeQuan;
    }

    public double getLongitudeToi()
    {
        return longitudeToi;
    }

    public void setLongitudeToi(double longitude)
    {
        this.longitudeToi = longitude;
    }

    public double getLongitudeQuan()
    {
        return longitudeQuan;
    }

    public void setLongitudeQuan(double longitudeQuan)
    {
        this.longitudeQuan = longitudeQuan;
    }

    private void khoiTaoGoogleMap ()
    {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.banDoQuan);
        mapFragment.getMapAsync(ChiTietQuanActivity.this);

    }
    private void demHinhAnh()
    {
        new DemSoHinhAnh().execute();
    }
    private void demLuuLai()
    {
        new DemLuuLai().execute();
    }
    private void demSoCheckin()
    {
        new DemCheckin().execute();
    }
    private void demSoBinhLuan()
    {
        new DemBinhLuan().execute();
    }
    private void kiemTraCoLuuLai()
    {
        new KiemTraCoLuuLaiChua().execute();
    }

    @Override
    public void onBackPressed()
    {

      //  Toast.makeText(ChiTietQuanActivity.this,"back",Toast.LENGTH_SHORT).show();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("user",getUser());
        setResult(RESULT_OK,resultIntent);
        super.onBackPressed();

    }
    private void xuLyBinhLuan()
    {
        if(binhluan)
        {


            binhluan=false;

            if(dabinhluan==false)
            {
                Intent intent = new Intent(ChiTietQuanActivity.this, BinhLuanActivity.class);
                intent.putExtra("user", getUser());
                intent.putExtra("tenquan", getTenQuan());
                intent.putExtra("maquan", getMaQuan());
                intent.putExtra("diachi", getDiaChi());
                startActivityForResult(intent, 3);
            }
            else
            {
                Toast.makeText(ChiTietQuanActivity.this,"Đã bình luận rồi",Toast.LENGTH_SHORT).show();

            }



        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1)
        {
            if(resultCode==RESULT_OK)
            {

                setUser(data.getStringExtra("user"));
                kiemTraCoLuuLai();



                if(checkin)
                {


                        checkin=false;
                        Intent intent = new Intent(ChiTietQuanActivity.this,CheckinActivity.class);
                        intent.putExtra("user",getUser());
                        intent.putExtra("tenquan",getTenQuan());
                        intent.putExtra("maquan",getMaQuan());
                        intent.putExtra("diachi",getDiaChi());
                        startActivityForResult(intent,2);


                }
                new KiemTraCoBinhLuan().execute();
                press=false;



            }
        }
        if(requestCode==2)
        {
            if(resultCode==RESULT_OK)
            {
                Boolean checkinxong;
                checkinxong=data.getBooleanExtra("checkinxong",false);

                if(checkinxong)
                {
                   // Toast.makeText(ChiTietQuanActivity.this,"Cập nhật số checkin",Toast.LENGTH_SHORT).show();
                    demSoCheckin();
                    demHinhAnh();
                }

            }


        }
        if(requestCode==3)
        {
            if(resultCode==RESULT_OK)
            {
                Boolean binhluanxong;
                binhluanxong=data.getBooleanExtra("binhluanxong",false);
                if(binhluanxong)
                {
                 //   Toast.makeText(ChiTietQuanActivity.this,"Cập nhật số Bình luận",Toast.LENGTH_SHORT).show();
                    new LayBinhLuan().execute();
                    new LaySoDiem().execute();
                    demSoBinhLuan();
                    demHinhAnh();
                    dabinhluan=true;

                }
            }

        }
        if(requestCode==4)
        {
            if(resultCode==RESULT_OK)
            {
                demSoCheckin();
                demHinhAnh();
            }
        }
    }

    public int getSoDsHinhQuan()
    {
        return soDsHinhQuan;
    }

    public void setSoDsHinhQuan(int soDsHinhQuan)
    {
        this.soDsHinhQuan = soDsHinhQuan;
    }

    private void khoiTaoGioHoatDong()
    {
        gioHoatDongChiTiet = findViewById(R.id.gioHoatDongChiTiet);
    }

    private void khoiTaoDiaChivaSoDienThoai()
    {
        diaChiChiTiet = findViewById(R.id.diaChiChiTiet);
        sdtChiTiet = findViewById(R.id.sdtChiTiet);
    }
    private class LayCacHinhCuaQuan extends AsyncTask<Void,Void,Void>
    {



        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql = "SELECT hinh FROM hinhquan WHERE maquan = '"+getMaQuan()+"' ORDER BY stt ASC";
            Statement stmt = null;
            ResultSet rs = null;
            Bitmap hinh=null;
            int i=0;

            try
            {
                stmt=getConnection().createStatement();
                rs=stmt.executeQuery(sql);
                while(rs.next())
                {
                    Blob b = rs.getBlob(1);
                    int blobLength = (int) b.length();
                    byte[] blobAsBytes = b.getBytes(1, blobLength);
                    hinh = BitmapFactory.decodeByteArray(blobAsBytes, 0, blobAsBytes.length);
                    dsHinhQuan[i]=hinh;

                    i++;
                }
                setSoDsHinhQuan(i);





            }
            catch (Exception e)
            {
                Log.d("LOI","Khong lay duoc hinh quan");
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(Void aVoid)
        {
            hienHinhQuan();
            super.onPostExecute(aVoid);
        }
    }

    private void khoiTaoHinhQuan()
    {

        hinhChiTietQuan = findViewById(R.id.hinhChiTietQuan);
        dotPanel = findViewById(R.id.dotPanel);


    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void hienHinhQuan ()
    {

        HinhQuanAdapter adapter = new HinhQuanAdapter(this, dsHinhQuan,soDsHinhQuan);
        hinhChiTietQuan.setAdapter(adapter);
        dotCount = adapter.getCount(); //đếm số chấm
        dots = new ImageView[dotCount]; // tạo mảng chấm

        for(int i=0;i<dotCount;i++)
        {
            dots[i]=new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.unactive_dot_24dp));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8,0,8,0);
            dotPanel.addView(dots[i],params);

        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.active_dot_24dp));
        xuLySlideHinhAnhQuan();



    }
    private void xuLySlideHinhAnhQuan()
    {
        hinhChiTietQuan.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                for (int i=0;i<dotCount;i++)
                {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.unactive_dot_24dp));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.active_dot_24dp));

            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
    }


    private void khoiTaoCacNut()
    {
        soDiem = findViewById(R.id.soDiem);
        khungMN = findViewById(R.id.khungMN);
        lvDanhSachMenu = findViewById(R.id.danhSachMenu);
        cachXa = findViewById(R.id.cachXa);
        khungDS = findViewById(R.id.khungDS);
        lvDanhSachBinhLuan=findViewById(R.id.danhSachBinhLuan);
        xuLySuaXoaBinhLuan();
        txtSoHinhAnh = findViewById(R.id.soHinhAnh);
        txtSoLuuLai = findViewById(R.id.soLuuLai);
        txtSoCheckin = findViewById(R.id.soCheckin);
        txtSoBinhLuan = findViewById(R.id.soBinhLuan);
        btnBinhLuan = findViewById(R.id.btnBinhLuan);
        //btnBinhLuan.setBackgroundResource(android.R.drawable.btn_default);
        btnBinhLuan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(press==false)
                {
                    press = true;

                    if (getUser().equals(""))
                    {
                        binhluan = true;
                        Intent intent = new Intent(ChiTietQuanActivity.this, LoginActivity.class);
                        startActivityForResult(intent, 1);


                    }
                    else
                    {
                        if (dabinhluan == false)
                        {
                            Intent intent = new Intent(ChiTietQuanActivity.this, BinhLuanActivity.class);

                            intent.putExtra("user", getUser());
                            intent.putExtra("maquan", getMaQuan());
                            intent.putExtra("tenquan", getTenQuan());
                            intent.putExtra("diachi", getDiaChi());
                            startActivityForResult(intent, 3);
                        } else
                        {
                            Toast.makeText(ChiTietQuanActivity.this, "Đã bình luận rồi", Toast.LENGTH_SHORT).show();
                            press=false;
                        }

                    }

                }
            }
        });
        btnCheckin = findViewById(R.id.btnCheckin);
    //    btnCheckin.setBackgroundResource(android.R.drawable.btn_default);
        btnCheckin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(press==false)
                {
                    press=true;

                    if (getUser().equals(""))
                    {
                        checkin = true;
                        Intent intent = new Intent(ChiTietQuanActivity.this, LoginActivity.class);
                        startActivityForResult(intent, 1);
                    } else
                    {
                        Intent intent = new Intent(ChiTietQuanActivity.this, CheckinActivity.class);

                        intent.putExtra("user", getUser());
                        intent.putExtra("maquan", getMaQuan());
                        intent.putExtra("tenquan", getTenQuan());
                        intent.putExtra("diachi", getDiaChi());
                        startActivityForResult(intent, 2);
                    }
                }
            }
        });
        btnLuuLai = findViewById(R.id.btnLuuLai);
     //   btnLuuLai.setBackgroundResource(android.R.drawable.btn_default);
        btnLuuLai.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v)
            {
                if(getUser().equals(""))
                {
                    Intent intent = new Intent(ChiTietQuanActivity.this,LoginActivity.class);
                    startActivityForResult(intent,1);
                }
                else
                {
                    if(daluulai)
                    {
                        new XoaLuuLai().execute();
                        btnLuuLai.setImageResource(R.drawable.luu_lai);
                    }
                    else
                    {
                        new ThongBaoLuuLai().execute();
                    }





                }

            }
        });
        btnLienHe = findViewById(R.id.lienHe);
    //    btnLienHe.setBackgroundResource(android.R.drawable.btn_default);
        btnLienHe.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri uri = Uri.parse("tel:"+getSoDienThoai());
                intent.setData(uri);
                if(ActivityCompat.checkSelfPermission(ChiTietQuanActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(ChiTietQuanActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);

                }
                if(ActivityCompat.checkSelfPermission(ChiTietQuanActivity.this, Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED)
                {
                    startActivity(intent);
                }

//                Intent intent = new Intent(Intent.ACTION_CALL);
//                Uri uri = Uri.parse("tel:"+getSoDienThoai());
//                intent.setData(uri);
//                startActivity(intent);

            }
        });
        btnChiaSe=findViewById(R.id.btnChiaSe);
    //    btnChiaSe.setBackgroundResource(android.R.drawable.btn_default);
        btnChiaSe.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = ""+getTenQuan();
                String shareSub = "Hãy cùng đến với "+getTenQuan()+" để thưởng thức những món ăn ngon nào !!!";
                intent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                intent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(intent,"Chia sẻ qua"));

            }
        });
        xuLyHienCacCheckin();




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode==1)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri uri = Uri.parse("tel:"+getSoDienThoai());
                intent.setData(uri);
                if(ActivityCompat.checkSelfPermission(ChiTietQuanActivity.this, Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED)
                {
                    startActivity(intent);
                }

            }
        }
    }

    private void xuLySuaXoaBinhLuan()
    {

        lvDanhSachBinhLuan.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                unregisterForContextMenu(lvDanhSachBinhLuan);
                _tennguoidung = view.findViewById(R.id.txtUserBinhLuan);
                _TenNguoiDung=_tennguoidung.getText().toString();//lấy user
                _mabinhluan = view.findViewById(R.id.txtMaBinhLuan);
                _MaBinhLuan=_mabinhluan.getText().toString();//lấy mã bình luận
                if(getUser().equals(_TenNguoiDung))
                {
                    registerForContextMenu(lvDanhSachBinhLuan);
                }



                return false;
            }
        });



    }

    private void xuLyHienCacCheckin()
    {
        txtSoCheckin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ChiTietQuanActivity.this,XemCheckinActivity.class);
                intent.putExtra("maquan",getMaQuan());
                intent.putExtra("user",getUser());
                startActivityForResult(intent,4);
            }
        });
    }

    private class KiemTraCoBinhLuan extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql = "SELECT maquan,tennguoidung FROM binhluan WHERE maquan='"+getMaQuan()+"' ";
            Statement stmt = null;
            ResultSet rs = null;
            try
            {
                stmt=getConnection().createStatement();
                rs=stmt.executeQuery(sql);
                while(rs.next())
                {
                    if(rs.getString(1).equals(getMaQuan()) && rs.getString(2).equals(getUser()))
                    {
                        dabinhluan=true;
                    }

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
            xuLyBinhLuan();
            super.onPostExecute(aVoid);
        }
    }
    private class LayBinhLuan extends  AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute()
        {
            arrayListDanhSachBinhLuan = new ArrayList<>();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql =  "SELECT avatar,hotennguoidung,sao,hinhbinhluan,binhluan,mabinhluan,nguoidung.tennguoidung FROM binhluan,nguoidung WHERE maquan='"+getMaQuan()+"' AND binhluan.tennguoidung=nguoidung.tennguoidung";

            Statement stmt = null;
            ResultSet rs = null;

            Bitmap avt=null;
            String hoten="";
            Float sosao=0f;
            Bitmap hinhbinhluan=null;
            String noidungbinhluan="";
            String mabinhluan="";
            String user="";
            try
            {
                stmt=getConnection().createStatement();
                rs=stmt.executeQuery(sql);
                while(rs.next())
                {
                    // 1
                    Blob b = rs.getBlob(1);
                    int blobLength = (int) b.length();
                    byte[] blobAsBytes = b.getBytes(1, blobLength);
                    avt = BitmapFactory.decodeByteArray(blobAsBytes, 0, blobAsBytes.length);
                    //2
                    hoten=rs.getString(2);
                    //3
                    sosao=rs.getFloat(3);
                    //4
                    Blob c = rs.getBlob(4);
                    if(c==null)
                    {
                        hinhbinhluan =null;
                    }
                    else
                    {
                        int blobLength2 = (int) c.length();
                        byte[] blobAsBytes2 = c.getBytes(1, blobLength2);
                        hinhbinhluan = BitmapFactory.decodeByteArray(blobAsBytes2, 0, blobAsBytes2.length);
                    }

                    //5
                    noidungbinhluan=rs.getString(5);
                    //6
                    mabinhluan=rs.getString(6);
                    //7
                    user=rs.getString(7);

                    arrayListDanhSachBinhLuan.add(new DanhSachBinhLuan(avt,hoten,sosao,hinhbinhluan,noidungbinhluan,mabinhluan,user));

                }
                adapterDanhSachBinhLuan = new AdapterDanhSachBinhLuan(ChiTietQuanActivity.this,R.layout.binh_luan_row,arrayListDanhSachBinhLuan);

            }
            catch (Exception e)
            {

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            lvDanhSachBinhLuan.setAdapter(adapterDanhSachBinhLuan);
            khungDS.getLayoutParams().height=getLVHeight(lvDanhSachBinhLuan);
            super.onPostExecute(aVoid);
        }
    }
    public static int getLVHeight(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        int height = 0;
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            View view = adapter.getView(i, null, listView);
            view.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            height += view.getMeasuredHeight();
        }
        height += listView.getDividerHeight() * (count - 1);
        return height;
    }

    private class DemSoHinhAnh extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute()
        {
            new DemSoHinhAnhBinhLuan().execute();
            new DemSoHinhAnhCheckin().execute();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            soHinhBinhLuan+=soHinhCheckin;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            setSoHinhAnh(""+soHinhBinhLuan);
            txtSoHinhAnh.setText(getSoHinhAnh());
            super.onPostExecute(aVoid);
        }
    }
    private class DemSoHinhAnhBinhLuan extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql = "SELECT COUNT(maquan) FROM binhluan WHERE maquan = '"+getMaQuan()+"' AND hinhbinhluan IS NOT NULL";
            Statement stmt = null;
            ResultSet rs = null;
            try
            {
                stmt = getConnection().createStatement();
                rs = stmt.executeQuery(sql);
                while(rs.next())
                {
                    soHinhBinhLuan=rs.getInt(1);
                }

            }
            catch (Exception e)
            {

            }
            return null;
        }
    }
    private class DemSoHinhAnhCheckin extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql = "SELECT COUNT(maquan) FROM checkin WHERE maquan = '"+getMaQuan()+"' AND hinhcheckin IS NOT NULL";
            Statement stmt = null;
            ResultSet rs = null;
            try
            {
                stmt = getConnection().createStatement();
                rs = stmt.executeQuery(sql);
                while(rs.next())
                {
                    soHinhCheckin=rs.getInt(1);
                }

            }
            catch (Exception e)
            {

            }
            return null;
        }
    }
    private class DemLuuLai extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql = "SELECT COUNT(maquan) FROM luulai WHERE maquan='"+getMaQuan()+"'";
            Statement stmt = null;
            ResultSet rs = null;
            try
            {
                stmt = getConnection().createStatement();
                rs = stmt.executeQuery(sql);
                while(rs.next())
                {
                    //gán số bình luận
                    setSoLuuLai(rs.getString(1));
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
            txtSoLuuLai.setText(getSoLuuLai());
            super.onPostExecute(aVoid);
        }
    }
    private class DemCheckin extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql = "SELECT COUNT(maquan) FROM checkin WHERE maquan='"+getMaQuan()+"'";
            Statement stmt = null;
            ResultSet rs = null;
            try
            {
                stmt = getConnection().createStatement();
                rs = stmt.executeQuery(sql);
                while(rs.next())
                {
                    //gán số bình luận
                    setSoCheckin(rs.getString(1));
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
            //set text cho binh luan
            txtSoCheckin.setText(getSoCheckin());
            super.onPostExecute(aVoid);
        }
    }
    private class DemBinhLuan extends AsyncTask <Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql = "SELECT COUNT(maquan) FROM quanlybinhluan WHERE maquan='"+getMaQuan()+"'";
            Statement stmt = null;
            ResultSet rs = null;
            try
            {
                stmt = getConnection().createStatement();
                rs = stmt.executeQuery(sql);
                while(rs.next())
                {
                    //gán số bình luận
                    setSoBinhLuan(rs.getString(1));
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
            //set text cho binh luan
            txtSoBinhLuan.setText(getSoBinhLuan());
            super.onPostExecute(aVoid);
        }
    }
    private class XoaLuuLai extends AsyncTask<Void,Void,Void>
    {


        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql = "DELETE FROM luulai WHERE maquan='"+getMaQuan()+"' AND tennguoidung='"+getUser()+"' ";
            Statement stmt = null;
           // ResultSet rs = null;
            try
            {
                stmt = getConnection().createStatement();
                stmt.execute(sql);
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
            daluulai=false;

          //  Toast.makeText(ChiTietQuanActivity.this,"Xoa luu lai",Toast.LENGTH_SHORT).show();
            demLuuLai();
            super.onPostExecute(aVoid);
        }
    }
    private class ThongBaoLuuLai extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute()
        {
            CustomDialog thongbao = new CustomDialog(ChiTietQuanActivity.this);
            thongbao.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            while (CustomDialog.Yes_LuuLai==false)
            {



            }
            if(CustomDialog.Yes_LuuLai)
            {
                //thuc hien luu
                luulai = true;


            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            if(luulai)
            {
                new ThucHienLuuLai().execute();
            }

            super.onPostExecute(aVoid);
        }
    }

    private class ThucHienLuuLai extends AsyncTask <Void,Void,Void>
    {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql = "INSERT INTO luulai VALUE (?,?) ";

            try(PreparedStatement pstmt = getConnection().prepareStatement(sql))
            {
                pstmt.setString(1,getMaQuan());
                pstmt.setString(2,getUser());
                pstmt.executeUpdate();

            }
            catch (Exception e)
            {
                Log.d("LOI",""+e);
            }
            return null;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        protected void onPostExecute(Void aVoid)
        {
            CustomDialog.Yes_LuuLai=false;
            luulai=false;
        //    Toast.makeText(ChiTietQuanActivity.this,"Đã lưu lại",Toast.LENGTH_SHORT).show();
            btnLuuLai.setImageResource(R.drawable.dasave);
            daluulai=true;
            demLuuLai();

            super.onPostExecute(aVoid);
        }
    }

    public String getSoHinhAnh()
    {
        return soHinhAnh;
    }

    public void setSoHinhAnh(String soHinhAnh)
    {
        this.soHinhAnh = soHinhAnh;
    }

    private void nhanThongTin()
    {
        Intent intent = getIntent();
        setMaQuan(intent.getStringExtra("maquan"));
        setTenQuan(intent.getStringExtra("tenquan"));
        setUser(intent.getStringExtra("user"));
        setLatitudeToi(intent.getDoubleExtra("lat",0.000000000));
        setLongitudeToi(intent.getDoubleExtra("long",0.000000000));
    }
    private class KiemTraCoLuuLaiChua extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            String user="",maquan="";

            String sql = "SELECT * FROM luulai WHERE maquan = '"+getMaQuan()+"'";
            Statement stmt = null;
            ResultSet rs = null;
            try
            {
                stmt=getConnection().createStatement();
                rs=stmt.executeQuery(sql);

                while(rs.next())
                {
                    maquan = rs.getString(1);
                    user = rs.getString(2);
                    if(getUser().equals(user) && getMaQuan().equals(maquan))
                    {
                        daluulai=true;
                    }
                }
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
            if(daluulai)
            {
                btnLuuLai.setImageResource(R.drawable.dasave);
            }
            super.onPostExecute(aVoid);
        }
    }

    public String getSoCheckin()
    {
        return soCheckin;
    }

    public void setSoCheckin(String soCheckin)
    {
        this.soCheckin = soCheckin;
    }

    public String getSoBinhLuan()
    {
        return soBinhLuan;
    }

    public void setSoBinhLuan(String soBinhLuan)
    {
        this.soBinhLuan = soBinhLuan;
    }

    public Connection getConnection()
    {
        return WelcomeActivity.CONNECTION;
    }

    public String getGioHoatDong()
    {
        return gioHoatDong;
    }

    public void setGioHoatDong(String gioHoatDong)
    {
        this.gioHoatDong = gioHoatDong;
    }



    public String getDiaChi()
    {
        return diaChi;
    }

    public void setDiaChi(String diaChi)
    {
        this.diaChi = diaChi;
    }

    public String getSoDienThoai()
    {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai)
    {
        this.soDienThoai = soDienThoai;
    }

    public Bitmap getHinhQuan()
    {
        return hinhQuan;
    }

    public void setHinhQuan(Bitmap hinhQuan)
    {
        this.hinhQuan = hinhQuan;
    }

    public void setMaQuan(String maQuan)
    {
        this.maQuan = maQuan;
    }

    public void setTenQuan(String tenQuan)
    {
        this.tenQuan = tenQuan;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getSoLuuLai()
    {
        return soLuuLai;
    }

    public void setSoLuuLai(String soLuuLai)
    {
        this.soLuuLai = soLuuLai;
    }

    private String getUser()
    {
        return this.user;
    }
    private String getMaQuan()
    {
        return this.maQuan;
    }
    private String getTenQuan()
    {
        return this.tenQuan;
    }

    private void khoiTaoToolbar()
    {
        toolbarChiTietQuan = findViewById(R.id.toolbarchitietquan);
        setSupportActionBar(toolbarChiTietQuan);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        View view = getLayoutInflater().inflate(R.layout.toolbar_chi_tiet_quan,null);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(view);

        ImageButton toolbarBack = view.findViewById(R.id.toolbarBack);
        TextView toolbarTenQuan = view.findViewById(R.id.toolbarTenQuan);
        toolbarTenQuan.setText(getTenQuan().toUpperCase());

        toolbarBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
              //  Toast.makeText(ChiTietQuanActivity.this,"toolbar back",Toast.LENGTH_SHORT).show();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("user",getUser());
                setResult(RESULT_OK,resultIntent);
                finish();
            }
        });
    }
    private void xuLyTachLatLng(String s)
    {
        int giua = s.indexOf(", ");
        int dai = s.length();

        setLatitudeQuan(Double.parseDouble(s.substring(0,giua)));
        setLongitudeQuan(Double.parseDouble(s.substring(giua+2,dai)));
    }

    private class LayThongTinQuan extends AsyncTask <Void,Void,Void>
    {


        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql = "SELECT * FROM quanan WHERE maquan ='"+getMaQuan()+"'  ";
            Statement stmt = null;
            ResultSet rs = null;
            try
            {
                stmt=getConnection().createStatement();
                rs = stmt.executeQuery(sql);
                while (rs.next())
                {
                    setDiaChi(rs.getString(4));
                    setSoDienThoai(rs.getString(5));
//                    Blob b = rs.getBlob(6);
//                    int blobLength = (int) b.length();
//                    byte[] blobAsBytes = b.getBytes(1, blobLength);
//                    setHinhQuan(BitmapFactory.decodeByteArray(blobAsBytes, 0, blobAsBytes.length));
                    setGioHoatDong(rs.getString(7));
                    xuLyTachLatLng(rs.getString(8));

              //      Toast.makeText(ChiTietQuanActivity.this,"lat="+getLattitudeQuan()+" long="+getLongitudeQuan(),Toast.LENGTH_SHORT).show();

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
            //hinhChiTietQuan.setImageBitmap(getHinhQuan());
            diaChiChiTiet.setText(getDiaChi());
            sdtChiTiet.setText(getSoDienThoai());
            gioHoatDongChiTiet.setText(getGioHoatDong());

            khoiTaoGoogleMap();
            super.onPostExecute(aVoid);
        }
    }

    public void onMapReady(GoogleMap googleMap)
    {
        map = googleMap;
        LatLng viTriQuan =  new LatLng(getLattitudeQuan(), getLongitudeQuan());
        LatLng viTriCuaToi = new LatLng(getLatitudeToi(), getLongitudeToi());
        map.addMarker(new MarkerOptions().position(viTriQuan).title(getTenQuan()));
        map.moveCamera(CameraUpdateFactory.newLatLng(viTriQuan));
        map.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
        distance= SphericalUtil.computeDistanceBetween(viTriQuan,viTriCuaToi);
        double sokm=distance/1000;
        DecimalFormat f = new DecimalFormat("0.00");
        cachxa=f.format(sokm);

        cachXa.setText("Cách bạn "+cachxa+" km");

        //Toast.makeText(ChiTietQuanActivity.this,"+ "+getDistanceMeters(viTriCuaToi,viTriQuan)/1000,Toast.LENGTH_SHORT).show();








    }
//    public double getDistanceMeters(LatLng pt1, LatLng pt2){
//        double distance = 0d;
//        try{
//            double theta = pt1.longitude - pt2.longitude;
//            double dist = Math.sin(Math.toRadians(pt1.latitude)) * Math.sin(Math.toRadians(pt2.latitude))
//                    + Math.cos(Math.toRadians(pt1.latitude)) * Math.cos(Math.toRadians(pt2.latitude)) * Math.cos(Math.toRadians(theta));
//
//            dist = Math.acos(dist);
//            dist = Math.toDegrees(dist);
//            distance = dist * 60 * 1853.1596;
//        }
//        catch (Exception ex){
//            System.out.println(ex.getMessage());
//        }
//        return distance;
//    }


    // location
    //tạo context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dsbinhluan_context_menu,menu);

    }

    //tạo context menu
    //xử lý context menu

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.xoa_binhluan_context_menu:
                xoaBinhLuan();

                break;
//            case R.id.sua_binhluan_context_menu:
//                suaBinhLuan();
//                break;
        }
        return super.onContextItemSelected(item);
    }

//    private void suaBinhLuan()
//    {
//    }

    private void xoaBinhLuan()
    {
        new XoaBinhLuan().execute();
    }

    //xử lý context menu

    private class XoaBinhLuan extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql = "DELETE FROM binhluan WHERE mabinhluan = '"+_MaBinhLuan+"' ";
            Statement stmt =null;

            try
            {
                stmt = getConnection().createStatement();
                stmt.execute(sql);
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
            new LayBinhLuan().execute();
            new DemBinhLuan().execute();
            new DemSoHinhAnh().execute();
            new LaySoDiem().execute();
            dabinhluan=false;
            super.onPostExecute(aVoid);
        }
    }

}
