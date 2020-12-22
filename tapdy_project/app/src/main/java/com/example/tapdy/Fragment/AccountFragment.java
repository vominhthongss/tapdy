package com.example.tapdy.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

import com.example.tapdy.Adapter.AdapterTaiKhoan;
import com.example.tapdy.LoginActivity;
import com.example.tapdy.MainActivity;
import com.example.tapdy.MenuAcccount.BaoLoiActivity;
import com.example.tapdy.MenuAcccount.ChinhSachQuyDinhActivity;
import com.example.tapdy.MenuAcccount.GopYActivity;
import com.example.tapdy.MenuAcccount.HoSoActivity;
import com.example.tapdy.Model.MenuTaiKhoan;
import com.example.tapdy.R;
import com.example.tapdy.WelcomeActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment
{


    private ListView menuTaiKhoan;
    private LinearLayout dangNhap;
    public TextView tenNguoiDung;
    public static ImageView avatar;
    private String USER="";
    public static Boolean nhan=false;




    public AccountFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        khoiTaoMenuTaiKhoan(view);
        xuLyDangNhap(view);

        return view;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1)
        {
            if(resultCode==RESULT_OK)
            {
                setUSER(data.getStringExtra("user"));
                //gọi hàm lấy tên người dùng
                new LayHoTenNguoiDung().execute();

            }
        }
        if(requestCode==2)
        {
            if(resultCode==RESULT_OK)
            {
              //  Toast.makeText(getContext(),"Xu ly lay HO VA TEN",Toast.LENGTH_SHORT).show();
                new LayHoTenNguoiDung().execute();
            }
        }
    }
    private class LayHoTenNguoiDung extends AsyncTask<Void,Void,Void>
    {
        String hovaten="";

        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql = "SELECT hotennguoidung FROM nguoidung WHERE tennguoidung='"+getUSER()+"' ";
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



            tenNguoiDung.setText(hovaten);
            super.onPostExecute(aVoid);
        }
    }


    public Connection getConnection()
    {
        return WelcomeActivity.CONNECTION;
    }



    private void dangXuat()
    {
        MainActivity.U_C="";
        setUSER(MainActivity.U_C);

        tenNguoiDung.setText("Đăng nhập >");
        AccountFragment.avatar.setImageResource(R.drawable.account_icon);
        xoaUserTrongThietBi();

    }
    public void setUSER (String s)
    {
        this.USER=s;
    }
    public String getUSER()
    {
        return USER;
    }

    private void xuLyDangNhap(View view)
    {
        avatar = view.findViewById(R.id.avartar);
        tenNguoiDung = view.findViewById(R.id.tenNguoiDung);
        dangNhap = view.findViewById(R.id.dangNhap);
        dangNhap.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(nhan==false)
                {
                    nhan=true;

                    if (tenNguoiDung.getText().toString().equals("Đăng nhập >"))
                    {
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivityForResult(intent, 1);
                    } else
                    {
                    //    Toast.makeText(getContext(), "Ban dang dang nhap", Toast.LENGTH_SHORT).show();
                        nhan=false;
                    }
                }

            }
        });
    }


    private void khoiTaoMenuTaiKhoan(View view)
    {
        menuTaiKhoan = view.findViewById(R.id.menuTaiKhoan);
        ArrayList<MenuTaiKhoan> arrayList = new ArrayList<>();
        arrayList.add(new MenuTaiKhoan(R.drawable.ho_so,"Hồ sơ"));
        arrayList.add(new MenuTaiKhoan(R.drawable.gop_y,"Góp ý"));
        arrayList.add(new MenuTaiKhoan(R.drawable.bao_loi,"Báo lỗi"));
        arrayList.add(new MenuTaiKhoan(R.drawable.chinh_sach,"Chính sách quy định"));

        arrayList.add(new MenuTaiKhoan(R.drawable.dang_xuat,"Đăng xuất"));
        AdapterTaiKhoan adapterTaiKhoan = new AdapterTaiKhoan(getContext(),R.layout.menutaikhoan_row,arrayList);
        menuTaiKhoan.setAdapter(adapterTaiKhoan);

        menuTaiKhoan.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                TextView dx = view.findViewById(R.id.tenDongMenu);
                if(dx.getText().toString().equals("Đăng xuất"))
                {
                    if(getUSER().equals(""))
                    {
                        Toast.makeText(getContext(),"Bạn chưa đăng nhập nên không thể đăng xuất !",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getContext(),"Đã đăng xuất",Toast.LENGTH_SHORT).show();
                        dangXuat();
                    }
                }
                if(dx.getText().toString().equals("Hồ sơ"))
                {
                    if(getUSER().equals(""))
                    {
                        Toast.makeText(getContext(),"Bạn chưa đăng nhập !",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //chuyen activity
                        Intent intent = new Intent(getContext(), HoSoActivity.class);
                        intent.putExtra("user",getUSER());
                        startActivityForResult(intent,2);
                    }
                }
                if(dx.getText().toString().equals("Góp ý"))
                {
                    // chuyen activity
                    Intent intent = new Intent(getContext(), GopYActivity.class);

                    startActivity(intent);
                }
                if(dx.getText().toString().equals("Báo lỗi"))
                {
                    //chuyen activity
                    Intent intent = new Intent(getContext(), BaoLoiActivity.class);

                    startActivity(intent);
                }

                if(dx.getText().toString().equals("Chính sách quy định"))
                {
                    //chuyen activity
                    Intent intent = new Intent(getContext(), ChinhSachQuyDinhActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
    public void xoaUserTrongThietBi ()
    {

        SharedPreferences s = this.getActivity().getSharedPreferences("my_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        editor.putString("user","");
        editor.commit();
    }
    public void setAvatar(Bitmap b)
    {
        avatar.setImageBitmap(b);
    }

}
