package com.example.tapdy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tapdy.Adapter.AdapterQuan;
import com.example.tapdy.Model.DanhSachQuan;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import java.sql.Connection;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

public class SearchActivity extends AppCompatActivity
{
    private Toolbar toolbarSearch;
    private SearchView searchSearch;
    private String user;

    private ListView lvSearchSearch;
    private ArrayList<DanhSachQuan> arrayListDanhSachQuan;
    private AdapterQuan adapterQuan;
    private Boolean coquyenlocaion=true;

    private double latitudeToi,longitudeToi,latitudeQuan,longitudeQuan;
    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        nhanThongTin();
        khoiTaoToolbar();
        xyLyToolbar();
        khoiTaoDanhSachQuan();
        if(coquyenlocaion==false)
        {
            guiYeuCauXinQuyenLocation();
        }
        xuLySearch();
    }

    private void khoiTaoDanhSachQuan()
    {
        lvSearchSearch=findViewById(R.id.lvSearch_Search);
        layDanhSach();

        lvSearchSearch.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                TextView maquan = view.findViewById(R.id.maQuan);
                TextView tenquan = view.findViewById(R.id.tenQuan);

                kiemTraCoLayQuyenLocation();
                if(coquyenlocaion)
                {

                    Intent intent = new Intent(SearchActivity.this, ChiTietQuanActivity.class);
                    intent.putExtra("maquan",getMaQuan(maquan));
                    intent.putExtra("tenquan",getTenQuan(tenquan));
                    intent.putExtra("user",getUser());
                    //Toast.makeText(getContext(),"latToi"+getLatitudeToi(),Toast.LENGTH_SHORT).show();
                    intent.putExtra("lat",latitudeToi);
                    intent.putExtra("long",longitudeToi);

                    startActivityForResult(intent,1);


                }
                if(coquyenlocaion==false)
                {
                    guiYeuCauXinQuyenLocation();
                    Toast.makeText(SearchActivity.this,"Bạn chưa cấp quyền vị trí",Toast.LENGTH_SHORT).show();

                }



            }
        });
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
                MainActivity.U_C=getUser();

            }
        }
    }
    private void nhanThongTin()
    {
        Intent intent = getIntent();
        setUser(intent.getStringExtra("user"));
    }

    public void setUser (String s)
    {
        this.user=s;
    }
    public String getUser()
    {
        return this.user;
    }
    private String getMaQuan(TextView textView)
    {
        return textView.getText().toString();
    }
    private String getTenQuan(TextView textView)
    {
        return textView.getText().toString();
    }

    private void xuLySearch()
    {

        searchSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                ArrayList<DanhSachQuan> ketqua = new ArrayList<>();
                for(DanhSachQuan x : arrayListDanhSachQuan)
                {

                    if(x.getmTenQuan().toLowerCase().contains(newText.toLowerCase())
                    || x.getmDiaChi().toLowerCase().contains(newText.toLowerCase()))
                    {
                        ketqua.add(x);

                    }
                }


                lvSearchSearch.setAdapter(new AdapterQuan(SearchActivity.this,R.layout.danhsachquan_row,ketqua));
                return false;
            }
        });
    }

    private void xyLyToolbar()
    {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.toolbar_search,null);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(view);
        searchSearch = view.findViewById(R.id.searchSearch);

    }

    private void khoiTaoToolbar()
    {

        toolbarSearch = findViewById(R.id.toolbarsearch);
        setSupportActionBar(toolbarSearch);



    }
    private void layDanhSach()
    {
        if(ActivityCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            coquyenlocaion=false;
        }
        else
        {

            coquyenlocaion=true;
            client = LocationServices.getFusedLocationProviderClient(this);
            client.getLastLocation().addOnSuccessListener(SearchActivity.this, new OnSuccessListener<Location>()
            {
                @Override
                public void onSuccess(Location location)
                {
                    if(location!=null)
                    {

                        latitudeToi=location.getLatitude();
                        longitudeToi=location.getLongitude();

                        new LayDanhSach().execute();

                    }

                }
            });

        }

    }
    private class LayDanhSach extends AsyncTask<Void,Void,Void>

    {


        @Override
        protected void onPreExecute()
        {

            arrayListDanhSachQuan = new ArrayList<>();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            String sql = "SELECT * FROM quanan ";

            Statement stmt =null;
            ResultSet rs = null;

            Bitmap hinhQuan=null;
            String tenQuan;
            String maQuan;
            String moTaQuan;
            String diaChiQuan;
            double cachBan=0f;

            //kiemTraCoLayQuyenLocation();
            try
            {
                stmt=getConnection().createStatement();
                rs=stmt.executeQuery(sql);

                while (rs.next())
                {
                    maQuan = rs.getString(1);
                    tenQuan = rs.getString(2);
                    moTaQuan = rs.getString(3);
                    diaChiQuan = rs.getString(4);
                    Blob b = rs.getBlob(6);
                    int blobLength = (int) b.length();
                    byte[] blobAsBytes = b.getBytes(1, blobLength);
                    hinhQuan = BitmapFactory.decodeByteArray(blobAsBytes, 0, blobAsBytes.length);
                    xuLyTachLatLng(rs.getString(8));
                    LatLng vtQuan = new LatLng(getLatitudeQuan(),getLongitudeQuan());
                    LatLng vtToi = new LatLng(getLatitudeToi(),getLongitudeToi());


                    //Log.d("latQuan = ",""+getLatitudeQuan());
                    //Log.d("longQuan = ",""+getLongitudeQuan());
                    double dt = getDistanceMeters(vtQuan,vtToi);
                    dt=dt/1000;





                    arrayListDanhSachQuan.add(new DanhSachQuan(hinhQuan,tenQuan,maQuan,moTaQuan,diaChiQuan,dt));
                    Log.d("ma quan = ",""+maQuan);

                }
                Collections.sort(arrayListDanhSachQuan);
                adapterQuan = new AdapterQuan(SearchActivity.this,R.layout.danhsachquan_row,arrayListDanhSachQuan);



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


            lvSearchSearch.setAdapter(adapterQuan);

            super.onPostExecute(aVoid);
        }
    }
    private void guiYeuCauXinQuyenLocation()
    {
        ActivityCompat.requestPermissions(SearchActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode==1)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(SearchActivity.this,"Da lay vi tri",Toast.LENGTH_SHORT).show();
                layDanhSach();
            }
            else Toast.makeText(SearchActivity.this,"Ko lay vi tri",Toast.LENGTH_SHORT).show();
        }
    }
    private void kiemTraCoLayQuyenLocation()
    {
        if(ActivityCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            coquyenlocaion=false;
        }
        else
        {

            coquyenlocaion=true;
            layViTriCuaToi();




        }
    }
    private void layViTriCuaToi()
    {


        client = LocationServices.getFusedLocationProviderClient(this);
        client.getLastLocation().addOnSuccessListener(SearchActivity.this, new OnSuccessListener<Location>()
        {
            @Override
            public void onSuccess(Location location)
            {
                if(location!=null)
                {

                    latitudeToi=location.getLatitude();
                    longitudeToi=location.getLongitude();

                    //new LayDanhSach().execute();




                }


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
    public double getDistanceMeters(LatLng pt1, LatLng pt2){
        double distance = 0d;
        try{
            double theta = pt1.longitude - pt2.longitude;
            double dist = Math.sin(Math.toRadians(pt1.latitude)) * Math.sin(Math.toRadians(pt2.latitude))
                    + Math.cos(Math.toRadians(pt1.latitude)) * Math.cos(Math.toRadians(pt2.latitude)) * Math.cos(Math.toRadians(theta));

            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            distance = dist * 60 * 1853.1596;
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return distance;
    }
    private Connection getConnection()
    {
        return WelcomeActivity.CONNECTION;
    }
    public double getLatitudeToi()
    {
        return latitudeToi;
    }

    public void setLatitudeToi(double latitudeToi)
    {
        this.latitudeToi = latitudeToi;
    }

    public double getLongitudeToi()
    {
        return longitudeToi;
    }

    public void setLongitudeToi(double longitudeToi)
    {
        this.longitudeToi = longitudeToi;
    }

    public double getLatitudeQuan()
    {
        return latitudeQuan;
    }

    public void setLatitudeQuan(double latitudeQuan)
    {
        this.latitudeQuan = latitudeQuan;
    }

    public double getLongitudeQuan()
    {
        return longitudeQuan;
    }

    public void setLongitudeQuan(double longitudeQuan)
    {
        this.longitudeQuan = longitudeQuan;
    }
}
