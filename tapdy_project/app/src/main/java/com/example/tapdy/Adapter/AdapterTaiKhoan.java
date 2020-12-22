package com.example.tapdy.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tapdy.Model.DanhSachQuan;
import com.example.tapdy.Model.MenuTaiKhoan;
import com.example.tapdy.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterTaiKhoan extends ArrayAdapter<MenuTaiKhoan>
{
    private Context context;
    private int resource;
    private ArrayList<MenuTaiKhoan> arrayList;
    public AdapterTaiKhoan(@NonNull Context context, int resource, @NonNull ArrayList<MenuTaiKhoan> objects)
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
        convertView = LayoutInflater.from(context).inflate(R.layout.menutaikhoan_row,parent,false);
        ImageView hinhDongMenu = convertView.findViewById(R.id.hinhDongMenu);
        TextView tenDongMenu = convertView.findViewById(R.id.tenDongMenu);

        MenuTaiKhoan menuTaiKhoan = arrayList.get(position);
        hinhDongMenu.setImageResource(menuTaiKhoan.getmHinhDongMenu());
        tenDongMenu.setText(menuTaiKhoan.getmTenDongMenu());

        return convertView;
    }
}
