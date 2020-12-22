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
import androidx.core.content.ContextCompat;

import com.example.tapdy.Model.DanhSachMenu;
import com.example.tapdy.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterDanhSachMenu extends ArrayAdapter<DanhSachMenu>
{
    private Context context;
    private int resource;
    private ArrayList<DanhSachMenu> arrayList;
    public AdapterDanhSachMenu(@NonNull Context context, int resource, @NonNull ArrayList<DanhSachMenu> objects)
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
        convertView = LayoutInflater.from(context).inflate(R.layout.menu_row_item,parent,false);
        ImageView imgHinhMonAn = convertView.findViewById(R.id.hinhMonAn);
        TextView txtTenMonAn = convertView.findViewById(R.id.tenMonAn);
        TextView txtGiaMonAn = convertView.findViewById(R.id.giaMonAn);

        DanhSachMenu danhSachMenu = arrayList.get(position);

        imgHinhMonAn.setImageBitmap(danhSachMenu.getmHinhMonAn());
        txtTenMonAn.setText(danhSachMenu.getmTenMonAn());
        txtGiaMonAn.setText(danhSachMenu.getmGiaMonAn());

        return convertView;
    }
}
