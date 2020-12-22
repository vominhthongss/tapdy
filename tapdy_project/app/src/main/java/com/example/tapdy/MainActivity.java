package com.example.tapdy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tapdy.Fragment.AccountFragment;
import com.example.tapdy.Fragment.HomeFragment;
import com.example.tapdy.Fragment.NotificationFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity
{
    private Toolbar toolbarMain;

    private SearchView searchMain;//bien toolbar
    private TextView tenBar;


    private BottomNavigationView bottomNavigationView;                                              //cac bien cua bottom Nav
    private HomeFragment homeFragment = new HomeFragment();
    private NotificationFragment notificationFragment = new NotificationFragment();
    private AccountFragment accountFragment = new AccountFragment();
    private FragmentManager fm = getSupportFragmentManager();
    private Fragment active = homeFragment;

    public static String U_C="";

    public static Bitmap avt = null;






    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        loadUserTuThietBi();
        khoiTaoToolbar();
        xuLyToolbar();
        khoiTaoBottomNav();







    }

    private void loadUserTuThietBi()
    {
        //load user cho U_C
        SharedPreferences s = getSharedPreferences("my_data",MODE_PRIVATE);
        U_C=s.getString("user","");
        if(U_C.equals(""))
        {
            //AccountFragment.avatar.setImageResource(R.drawable.account_icon);
        }
        else
        {
            new LayAvt().execute();
        }
    }
    private class LayAvt extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql = "SELECT * FROM nguoidung WHERE tennguoidung='"+U_C+"'";
            Statement stmt = null;
            ResultSet rs = null;




            try
            {
                stmt=getConnection().createStatement();
                rs=stmt.executeQuery(sql);
                while(rs.next())
                {

                    Blob b = rs.getBlob(6);
                    int blobLength = (int) b.length();
                    byte[] blobAsBytes = b.getBytes(1, blobLength);
                    avt = BitmapFactory.decodeByteArray(blobAsBytes, 0, blobAsBytes.length);





                }


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

            //setAvatar(avt);

            super.onPostExecute(aVoid);
        }
    }

    private void setAvatar(Bitmap avt)
    {
        accountFragment.avatar.setImageBitmap(avt);
    }








    private Connection getConnection()
    {
        return WelcomeActivity.CONNECTION;
    }

    private void xuLyToolbar()
    {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        View view = getLayoutInflater().inflate(R.layout.toolbar_main,null);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(view);




        tenBar = view.findViewById(R.id.tenBar);
        tenBar.setText("DANH SÁCH QUÁN ĂN");
        searchMain = view.findViewById(R.id.searchmain);
        searchMain.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
               searchMain.clearFocus();
               if(hasFocus)
               {
                   Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                   intent.putExtra("user",MainActivity.U_C);
                   startActivity(intent);
               }
            }
        });

    }



    private void khoiTaoToolbar()
    {
        toolbarMain = findViewById(R.id.toolbarmain);
        setSupportActionBar(toolbarMain);
    }


    private void khoiTaoBottomNav()
    {

        bottomNavigationView = findViewById(R.id.bottom_nav);
        fm.beginTransaction().add(R.id.frame,homeFragment,"1").commit();
        fm.beginTransaction().add(R.id.frame,notificationFragment,"2").hide(notificationFragment).commit();
        fm.beginTransaction().add(R.id.frame,accountFragment,"3").hide(accountFragment).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                int id = menuItem.getItemId();
                if(id==R.id.home)
                {

                    fm.beginTransaction().hide(active).show(homeFragment).commit();
                    active=homeFragment;
                    //
                    homeFragment.setUser(U_C);
                    //
                    searchMain.setVisibility(View.VISIBLE);
                    tenBar.setText("DANH SÁCH QUÁN ĂN");
                    //Toast.makeText(homeFragment.getContext(),"Home ="+homeFragment.getUser(),Toast.LENGTH_SHORT).show();
                    return true;
                }
                else if(id==R.id.notification)
                {

                    if(MainActivity.U_C.equals(""))
                    {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.detach(notificationFragment);
                        ft.attach(notificationFragment);
                        ft.commit();
                        fm.beginTransaction().hide(active).show(notificationFragment).commit();

                    }
                    else
                    {
                        //fm.beginTransaction().add(R.id.frame,notificationFragment2,"4").commit();
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.detach(notificationFragment);
                        ft.attach(notificationFragment);
                        ft.commit();
                        fm.beginTransaction().hide(active).show(notificationFragment).commit();

                    }
                    active=notificationFragment;
                    notificationFragment.setUser(U_C);



                    //


                    searchMain.setVisibility(View.INVISIBLE);
                    tenBar.setText("TIN TỨC THÔNG BÁO");


                    //Toast.makeText(notificationFragment.getContext(),"Noti ="+notificationFragment.getUser(),Toast.LENGTH_SHORT).show();
                    return true;
                }
                else if(id==R.id.account)
                {
                    fm.beginTransaction().hide(active).show(accountFragment).commit();
                    active=accountFragment;
                    //
                    accountFragment.setUSER(U_C);
                    if(avt==null)
                    {

                    }
                    else
                    {
                        if(accountFragment.getUSER().equals(""))
                        {

                        }
                        else
                        {
                            accountFragment.setAvatar(avt);
                        }

                    }

                    if(accountFragment.getUSER().equals(""))
                    {
                        accountFragment.tenNguoiDung.setText("Đăng nhập >");
                    }
                    else
                    {
                        //gọi hàm lấy tên người dùng
                        new LayHoTenNguoiDung().execute();
                        //accountFragment.tenNguoiDung.setText(accountFragment.getUSER());
                    }

                    //............
                    searchMain.setVisibility(View.INVISIBLE);
                    tenBar.setText("THÔNG TIN TÀI KHOẢN");
                  //  Toast.makeText(notificationFragment.getContext(),"Acc ="+accountFragment.getUSER(),Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });



    }
    private class LayHoTenNguoiDung extends AsyncTask<Void,Void,Void>
    {
        String hovaten="";

        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql = "SELECT hotennguoidung FROM nguoidung WHERE tennguoidung='"+accountFragment.getUSER()+"' ";
            Statement stmt = null;
            ResultSet rs= null;
            try
            {
                stmt=getConnection().createStatement();
                rs=stmt.executeQuery(sql);
                while (rs.next())
                {
                    hovaten=rs.getString(1);
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



            accountFragment.tenNguoiDung.setText(hovaten);
            super.onPostExecute(aVoid);
        }
    }


}
