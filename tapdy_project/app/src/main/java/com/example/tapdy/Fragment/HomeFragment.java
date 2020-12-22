package com.example.tapdy.Fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tapdy.Adapter.AdapterQuan;
import com.example.tapdy.ChiTietQuanActivity;
import com.example.tapdy.ConnectMySQL.ConnectMySQL;
import com.example.tapdy.MainActivity;
import com.example.tapdy.Model.DanhSachQuan;
import com.example.tapdy.R;
import com.example.tapdy.WelcomeActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;


import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment
{
    private static String TRUYVAN;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ProgressBar progressBar;
    private ListView ds;
    private ArrayList<DanhSachQuan> arrayListDanhSachQuan;
    private AdapterQuan adapterQuan;
    private Boolean flag=false;
    private Boolean coquyenlocaion=true;

    private String user="";

    private double latitudeToi,longitudeToi,latitudeQuan,longitudeQuan;
    private FusedLocationProviderClient client;

    private Spinner spinnerTinhThanh;
    ArrayList<String> dsTinhThanh;
    ArrayAdapter<String> adapterTinhThanh;









    public HomeFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        khoiTaoDanhSachQuan(view);
        xuLyChonTinhThanh();

        kiemTraCoLayQuyenLocation();
        if(coquyenlocaion==false)
        {
            guiYeuCauXinQuyenLocation();
        }
        return view;
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


    private void kiemTraCoLayQuyenLocation()
    {
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            coquyenlocaion=false;
        }
        else
        {

            coquyenlocaion=true;
            layViTriCuaToi();




        }
    }
    private void layDanhSach()
    {
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            coquyenlocaion=false;
        }
        else
        {

            coquyenlocaion=true;
            client = LocationServices.getFusedLocationProviderClient(getActivity());
            client.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>()
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

    private void layViTriCuaToi()
    {


        client = LocationServices.getFusedLocationProviderClient(getActivity());
        client.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>()
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
    private void guiYeuCauXinQuyenLocation()
    {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode==1)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getContext(),"Đã lấy vị trí",Toast.LENGTH_SHORT).show();
                layDanhSach();
            }
            else Toast.makeText(getContext(),"Không lấy vị trí",Toast.LENGTH_SHORT).show();
        }

      //  super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public double getLatitudeToi()
    {
        return latitudeToi;
    }

    public void setLatitudeToi(double latitude)
    {
        this.latitudeToi = latitude;
    }

    public double getLongitudeToi()
    {
        return longitudeToi;
    }

    public void setLongitudeToi(double longitude)
    {
        this.longitudeToi = longitude;
    }

    public Connection getConnection()
    {
        return WelcomeActivity.CONNECTION;
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

    public void setUser (String s)
    {
        this.user=s;
    }
    public String getUser()
    {
        return this.user;
    }


    private void xuLyChonTinhThanh()
    {


        new LayTinhThanh().execute();

        spinnerTinhThanh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String text = parent.getItemAtPosition(position).toString();
               // Toast.makeText(getContext(),"Xử lý chọn danh sách theo tỉnh thành "+text,Toast.LENGTH_SHORT).show();
                if(text.equals("Vị trí gần bạn"))
                {
                    TRUYVAN = "SELECT * FROM quanan ";
                    layDanhSach();
                }
                else
                {
                    TRUYVAN="SELECT * FROM quanan WHERE tinhthanh='"+text+"'";
                    layDanhSach();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }
    private class LayTinhThanh extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute()
        {
            dsTinhThanh = new ArrayList<>();
            dsTinhThanh.add("Vị trí gần bạn");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql = "SELECT * FROM quanan GROUP BY tinhthanh ORDER BY tinhthanh ASC";
            Statement stmt = null;
            ResultSet rs = null;
            try
            {
                stmt = getConnection().createStatement();
                rs=stmt.executeQuery(sql);
                while (rs.next())
                {
                    dsTinhThanh.add(rs.getString(9));
                }
                adapterTinhThanh = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,dsTinhThanh);
            }
            catch (Exception e)
            {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            spinnerTinhThanh.setAdapter(adapterTinhThanh);
            super.onPostExecute(aVoid);
        }
    }
    private void khoiTaoDanhSachQuan(View view)
    {
        spinnerTinhThanh = view.findViewById(R.id.spinnerTinhThanh);


        swipeRefreshLayout =view.findViewById(R.id.f5layout);



        ds = view.findViewById(R.id.danhSachQuan);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                while (flag==false)
                {
                    kiemTraCoLayQuyenLocation();
                    if(coquyenlocaion)
                    {
                        new LayDanhSach().execute();
                    }

                  //  Toast.makeText(getContext(),"lat home = "+getLatitudeToi(),Toast.LENGTH_SHORT).show();
                }


                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {

                        swipeRefreshLayout.setRefreshing(false);
                    }
                },400);
            }
        });
        ds.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                TextView maquan = view.findViewById(R.id.maQuan);
                TextView tenquan = view.findViewById(R.id.tenQuan);

                kiemTraCoLayQuyenLocation();
                if(coquyenlocaion)
                {

                    Intent intent = new Intent(getContext(), ChiTietQuanActivity.class);
                    intent.putExtra("maquan",getMaQuan(maquan));
                    intent.putExtra("tenquan",getTenQuan(tenquan));
                    setUser(MainActivity.U_C);
                    intent.putExtra("user",getUser());
                    //Toast.makeText(getContext(),"latToi"+getLatitudeToi(),Toast.LENGTH_SHORT).show();
                    intent.putExtra("lat",latitudeToi);
                    intent.putExtra("long",longitudeToi);

                    startActivityForResult(intent,1);


                }
                if(coquyenlocaion==false)
                {
                    guiYeuCauXinQuyenLocation();
                    Toast.makeText(getContext(),"Bạn chưa cấp quyền vị trí",Toast.LENGTH_SHORT).show();

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
    private String getMaQuan(TextView textView)
    {
        return textView.getText().toString();
    }
    private String getTenQuan(TextView textView)
    {
        return textView.getText().toString();
    }
    private void xuLyTachLatLng(String s)
    {
        int giua = s.indexOf(", ");
        int dai = s.length();

        setLatitudeQuan(Double.parseDouble(s.substring(0,giua)));
        setLongitudeQuan(Double.parseDouble(s.substring(giua+2,dai)));
    }




    private class LayDanhSach extends AsyncTask<Void,Void,Void>

    {


        @Override
        protected void onPreExecute()
        {
            flag=true;
            arrayListDanhSachQuan = new ArrayList<>();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            //TRUYVAN = "SELECT * FROM quanan ";

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
                rs=stmt.executeQuery(TRUYVAN);

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



                    Log.d("WHILE","1");

                    arrayListDanhSachQuan.add(new DanhSachQuan(hinhQuan,tenQuan,maQuan,moTaQuan,diaChiQuan,dt));
                    Log.d("ma quan = ",""+maQuan);

                }
                Collections.sort(arrayListDanhSachQuan);
                adapterQuan = new AdapterQuan(getContext(),R.layout.danhsachquan_row,arrayListDanhSachQuan);



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

            progressBar.setVisibility(View.INVISIBLE);
            ds.setAdapter(adapterQuan);
            flag=false;
            super.onPostExecute(aVoid);
        }
    }





}
