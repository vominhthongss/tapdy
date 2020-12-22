package com.example.tapdy.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tapdy.Model.DanhSachQuan;
import com.example.tapdy.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AdapterQuan extends ArrayAdapter<DanhSachQuan>
{
    private Context context;
    private int resource;
    private ArrayList<DanhSachQuan> arrayList;
    public AdapterQuan(@NonNull Context context, int resource, @NonNull ArrayList<DanhSachQuan> objects)
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
        convertView = LayoutInflater.from(context).inflate(R.layout.danhsachquan_row,parent,false);

        ImageView hinhQuan= convertView.findViewById(R.id.hinhQuan);
        TextView tenQuan = convertView.findViewById(R.id.tenQuan);
        TextView maQuan = convertView.findViewById(R.id.maQuan);
        TextView moTaQuan = convertView.findViewById(R.id.moTaQuan);
        TextView diaChiQuan = convertView.findViewById(R.id.diaChiQuan);
        TextView cachBan = convertView.findViewById(R.id.cachBan);

        DanhSachQuan danhSachQuan = arrayList.get(position);

        hinhQuan.setImageBitmap(danhSachQuan.getmHinhQuan());
        tenQuan.setText(danhSachQuan.getmTenQuan());
        maQuan.setText(danhSachQuan.getmMaQuan());
        moTaQuan.setText(danhSachQuan.getmMoTaQuan());
        diaChiQuan.setText(danhSachQuan.getmDiaChi());

        String soMet;
        DecimalFormat f = new DecimalFormat("0.00");
        soMet=f.format(danhSachQuan.getmCachBan());
        cachBan.setText("Đang cách bạn "+soMet+" Km");



        return convertView;
    }
}
