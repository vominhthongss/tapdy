package com.example.tapdy.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tapdy.Model.DanhSachBinhLuan;
import com.example.tapdy.Model.DanhSachQuan;
import com.example.tapdy.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AdapterDanhSachBinhLuan extends ArrayAdapter<DanhSachBinhLuan>
{
    private Context context;
    private int resource;
    private ArrayList<DanhSachBinhLuan> arrayList;
    public AdapterDanhSachBinhLuan(@NonNull Context context, int resource, @NonNull ArrayList<DanhSachBinhLuan> objects)
    {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.arrayList=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        convertView = LayoutInflater.from(context).inflate(R.layout.binh_luan_row,parent,false);

        ImageView avtBinhLuan = convertView.findViewById(R.id.avtBinhLuan);
        TextView txtHoTenBinhLuan = convertView.findViewById(R.id.txtHoTenBinhLuan);
        RatingBar soSaoDanhGia = convertView.findViewById(R.id.saoDanhGia);
        ImageView imgHinhBinhLuan = convertView.findViewById(R.id.imgAnhBinhLuan);
        TextView txtNoiDungBinhLuan = convertView.findViewById(R.id.txtNoiDungBinhLuan);
        TextView txtMaBinhLuan = convertView.findViewById(R.id.txtMaBinhLuan);
        TextView txtUserBinhLuan = convertView.findViewById(R.id.txtUserBinhLuan);

        DanhSachBinhLuan danhSachBinhLuan = arrayList.get(position);

        avtBinhLuan.setImageBitmap(danhSachBinhLuan.getmAvt());
        txtHoTenBinhLuan.setText(danhSachBinhLuan.getmHoTenBinhLuan());
        soSaoDanhGia.setRating(danhSachBinhLuan.getmSoSaoDanhGia());
        imgHinhBinhLuan.setImageBitmap(danhSachBinhLuan.getmHinhBinhLuan());
        txtNoiDungBinhLuan.setText(danhSachBinhLuan.getmNoiDungBinhLuan());
        txtMaBinhLuan.setText(danhSachBinhLuan.getmMaBinhLuan());
        txtUserBinhLuan.setText(danhSachBinhLuan.getmUser());

        return convertView;
    }
}
