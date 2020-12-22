package com.example.tapdy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tapdy.Adapter.AdapterDanhSachCheckin;
import com.example.tapdy.Model.DanhSachCheckin;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class XemCheckinActivity extends AppCompatActivity
{
    private TextView _tennguoidung,_macheckin;
    private String _TenNguoiDung="",_MaCheckin="";
    private String maQuan="";
    private Toolbar toolbarCheckin;
    private ImageButton back;
    private ListView lvXemCheckin;
    private ArrayList<DanhSachCheckin> arrayListDanhSachChekin;
    private AdapterDanhSachCheckin adapterDanhSachCheckin;
    private ProgressBar progressBar3;

    private String user="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_checkin);
        nhanThongTin();
        khoiTaoToolbar();
        khoiTaoDanhSachCheckin();



    }

    private void nhanThongTin()
    {
        Intent intent = getIntent();
        setMaQuan(intent.getStringExtra("maquan"));
        setUser(intent.getStringExtra("user"));
    }

    private void khoiTaoDanhSachCheckin()
    {
        lvXemCheckin = findViewById(R.id.lvXemCheckin);
        progressBar3 = findViewById(R.id.progressBar3);

        new LayCheckin().execute();
        xuLySuaXoaCheckin();
    }

    private void xuLySuaXoaCheckin()
    {

        lvXemCheckin.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                unregisterForContextMenu(lvXemCheckin);
                _tennguoidung = view.findViewById(R.id.txtUserCheckin);
                _TenNguoiDung=_tennguoidung.getText().toString();
                _macheckin = view.findViewById(R.id.txtMaCheckin);
                _MaCheckin = _macheckin.getText().toString();

                if(getUser().equals(_TenNguoiDung))
                {
                    registerForContextMenu(lvXemCheckin);
                }
                return false;
            }
        });

    }

    private void khoiTaoToolbar()
    {
        toolbarCheckin = findViewById(R.id.toolbarCheckin);
        setSupportActionBar(toolbarCheckin);
        View view = getLayoutInflater().inflate(R.layout.toolbar_checkin,null);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        back = view.findViewById(R.id.toolbarBackCheckin);
        back.setOnClickListener(new View.OnClickListener()
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

    @Override
    public void onBackPressed()
    {
        Intent resultIntent = new Intent();
        setResult(RESULT_OK,resultIntent);
        finish();
        super.onBackPressed();
    }

    private class LayCheckin extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute()
        {
            arrayListDanhSachChekin = new ArrayList<>();
            progressBar3.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql = "SELECT avatar,hotennguoidung,hinhcheckin,noidungcheckin,macheckin,nguoidung.tennguoidung " +
                    "FROM checkin,nguoidung WHERE checkin.tennguoidung=nguoidung.tennguoidung AND maquan ='"+getMaQuan()+"' ";
            Statement stmt = null;
            ResultSet rs = null;
            Bitmap avt=null;
            String hoten="";
            Bitmap hinhcheckin=null;
            String noidungcheckin="";
            String macheckin="";
            String user="";
            try
            {
                stmt = getConnection().createStatement();
                rs=stmt.executeQuery(sql);

                while (rs.next())
                {
                    // 1
                    Blob b = rs.getBlob(1);
                    int blobLength = (int) b.length();
                    byte[] blobAsBytes = b.getBytes(1, blobLength);
                    avt = BitmapFactory.decodeByteArray(blobAsBytes, 0, blobAsBytes.length);
                    //2
                    hoten=rs.getString(2);
                    //3

                    Blob c = rs.getBlob(3);
                    if(c==null)
                    {
                        hinhcheckin =null;
                    }
                    else
                    {
                        int blobLength2 = (int) c.length();
                        byte[] blobAsBytes2 = c.getBytes(1, blobLength2);
                        hinhcheckin = BitmapFactory.decodeByteArray(blobAsBytes2, 0, blobAsBytes2.length);
                    }

                    //4
                    noidungcheckin=rs.getString(4);
                    //5
                    macheckin=rs.getString(5);
                    //6
                    user=rs.getString(6);

                    arrayListDanhSachChekin.add(new DanhSachCheckin(avt,hoten,hinhcheckin,noidungcheckin,macheckin,user));
                }
                adapterDanhSachCheckin = new AdapterDanhSachCheckin(XemCheckinActivity.this,R.layout.checkin_row,arrayListDanhSachChekin);


            }
            catch (Exception e)
            {

            }
            
            
            

            


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            lvXemCheckin.setAdapter(adapterDanhSachCheckin);
            progressBar3.setVisibility(View.INVISIBLE);
            progressBar3.getLayoutParams().height=0;
            super.onPostExecute(aVoid);
        }

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dscheckin_context_menu,menu);

    }

    //tạo context menu
    //xử lý context menu

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.xoa_checkin_context_menu:

                xoaCheckin();
                break;
//            case R.id.sua_checkin_context_menu:
////
////                suaCheckin();
//                break;
        }
        return super.onContextItemSelected(item);
    }

//    private void suaCheckin()
//    {
//    }

    private void xoaCheckin()
    {
        new XoaCheckin().execute();
    }
    private class XoaCheckin extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql = "DELETE FROM checkin WHERE macheckin = '"+_MaCheckin+"' ";
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
            new LayCheckin().execute();
            super.onPostExecute(aVoid);
        }
    }

    private void setMaQuan(String maquan)
    {
        this.maQuan = maquan;
    }
    private String getMaQuan()
    {
        return maQuan;
    }
    private Connection getConnection()
    {
        return WelcomeActivity.CONNECTION;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }
}
