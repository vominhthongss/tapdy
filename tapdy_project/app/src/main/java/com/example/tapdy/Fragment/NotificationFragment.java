package com.example.tapdy.Fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tapdy.Adapter.AdapterDanhSachThongBao;
import com.example.tapdy.ChiTietThongBaoActivity;
import com.example.tapdy.MainActivity;
import com.example.tapdy.Model.DanhSachMenu;
import com.example.tapdy.Model.ThongBaoLuu;
import com.example.tapdy.R;
import com.example.tapdy.WelcomeActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment
{

    private String user="";
    private String maThongBao="";
    private String ngayThongBao="";
    private String tenThongBao="";
    private String noidungThongBao="";

    private ImageButton nutXoaThongBao;
    private ListView lv_thongbao;
    private AdapterDanhSachThongBao arrayAdapterDanhSachThongBao;

    private ArrayList<ThongBaoLuu> arrayListThongBao;

    private ArrayList<ThongBaoLuu> tempList = new ArrayList<>();

    private String maThongBaoXoa="",tenNguoiDungXoa="";





    public NotificationFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        lv_thongbao=view.findViewById(R.id.lv_thongbao);
        new LayThongBao().execute();
        xuLyDanhSachThongBao(view);
        nutXoaThongBao=view.findViewById(R.id.nutXoaThongBao);
        xulyXoaThongBao();

        return view;
    }

    private void xulyXoaThongBao()
    {


        nutXoaThongBao.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new XoaThongBao().execute();


            }
        });

    }
    private class XoaThongBao extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {

            for (ThongBaoLuu t:tempList)
            {

                maThongBaoXoa=t.getmMaThongBao();
                tenNguoiDungXoa=MainActivity.U_C;

                String sql = "UPDATE quanlythongbao SET daxoa=1 WHERE tennguoidung='"+tenNguoiDungXoa+"' AND mathongbao='"+maThongBaoXoa+"' ";

                Statement stmt = null;
                try
                {
                    stmt=getConnection().createStatement();
                    stmt.execute(sql);
                }
                catch (Exception e)
                {

                }

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {

            arrayListThongBao.clear();
            arrayAdapterDanhSachThongBao.notifyDataSetChanged();

            super.onPostExecute(aVoid);
        }
    }

    private void xuLyDanhSachThongBao(View view)
    {
        lv_thongbao.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                TextView mathongbao = view.findViewById(R.id.maThongBao);
                TextView tenthongbao = view.findViewById(R.id.tenThongBao);
                TextView ngaythongbao = view.findViewById(R.id.ngayThongBao);
                TextView noidungthongbao = view.findViewById(R.id.noiDungThongBao);
                setMaThongBao(mathongbao.getText().toString());
                setTenThongBao(tenthongbao.getText().toString());
                setNgayThongBao(ngaythongbao.getText().toString());
                setNoidungThongBao(noidungthongbao.getText().toString());
                new DanhDauDaXem().execute();
                ImageView chamdo = view.findViewById(R.id.chamDo);
                chamdo.setImageResource(R.drawable.danhdau_thongbao);
                // chuyen sang 1 activity de xem chi tiet thong bao
                Intent intent = new Intent(getContext(), ChiTietThongBaoActivity.class);
                intent.putExtra("mathongbao",getMaThongBao());
                intent.putExtra("tenthongbao",getTenThongBao());
                intent.putExtra("ngaythongbao",getNgayThongBao());
                intent.putExtra("noidungthongbao",getNoidungThongBao());
                startActivity(intent);

            }
        });





    }
    private class DanhDauDaXem extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql = "UPDATE quanlythongbao SET daxem=1 WHERE mathongbao='"+getMaThongBao()+"' AND tennguoidung='"+getUser()+"' ";

            Statement stmt = null;
            try
            {
                stmt=getConnection().createStatement();
                stmt.execute(sql);

            }
            catch (Exception e)
            {
                Log.d("LOI",""+e);

            }

            return null;
        }


    }


    private class LayThongBao extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected void onPreExecute()
        {
            arrayListThongBao = new ArrayList<>();


            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            String sql = "SELECT * FROM thongbao,quanlythongbao WHERE thongbao.mathongbao=quanlythongbao.mathongbao AND tennguoidung='"+MainActivity.U_C+"'";
            Statement stmt = null;
            ResultSet rs = null;

            String mathongbao="";
            String tenthongbao="";
            String noidungthongbao="";
            String ngaythongbao="";
            int daxoa;
            int daxem;

            try
            {
                stmt=getConnection().createStatement();
                rs=stmt.executeQuery(sql);
                while (rs.next())
                {

                    mathongbao=rs.getString(1);
                    tenthongbao=rs.getString(2);
                    noidungthongbao=rs.getString(3);
                    ngaythongbao=rs.getString(4);
                    daxoa=rs.getInt(8);
                    daxem=rs.getInt(9);
                    if(daxoa==0)

                    {
                        arrayListThongBao.add(new ThongBaoLuu(mathongbao,tenthongbao,noidungthongbao,ngaythongbao,daxoa,daxem));
                    }
                }
                arrayAdapterDanhSachThongBao = new AdapterDanhSachThongBao(getContext(),R.layout.thongbao_row,arrayListThongBao);



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




            lv_thongbao.setAdapter(arrayAdapterDanhSachThongBao);
            tempList = new ArrayList<>();
            for(ThongBaoLuu t:arrayListThongBao)
            {
                tempList.add(new ThongBaoLuu(t.getmMaThongBao(),t.getmTenThongBao(),t.getmNoiDungThongBao(),t.getmNgayThongBao(),t.getmDaXoa(),t.getmDaXem()));

            }


            super.onPostExecute(aVoid);
        }
    }

    public Connection getConnection()
    {
        return WelcomeActivity.CONNECTION;
    }






    public void setUser (String s)
    {
        this.user=s;
    }
    public String getUser()
    {

        return this.user;
    }

    public String getMaThongBao()
    {
        return maThongBao;
    }

    public void setMaThongBao(String maThongBao)
    {
        this.maThongBao = maThongBao;
    }

    public String getNgayThongBao()
    {
        return ngayThongBao;
    }

    public void setNgayThongBao(String ngayThongBao)
    {
        this.ngayThongBao = ngayThongBao;
    }

    public String getTenThongBao()
    {
        return tenThongBao;
    }

    public void setTenThongBao(String tenThongBao)
    {
        this.tenThongBao = tenThongBao;
    }

    public String getNoidungThongBao()
    {
        return noidungThongBao;
    }

    public void setNoidungThongBao(String noidungThongBao)
    {
        this.noidungThongBao = noidungThongBao;
    }
}
