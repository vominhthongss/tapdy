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

import com.example.tapdy.Model.DanhSachCheckin;
import com.example.tapdy.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AdapterDanhSachCheckin extends ArrayAdapter<DanhSachCheckin>
{
    private Context context;
    private int resource;
    private ArrayList<DanhSachCheckin> arrayList;
    public AdapterDanhSachCheckin(@NonNull Context context, int resource, @NonNull ArrayList<DanhSachCheckin> objects)
    {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrayList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        convertView = LayoutInflater.from(context).inflate(R.layout.checkin_row,parent,false);
        ImageView avtCheckin = convertView.findViewById(R.id.avtCheckin);
        TextView txtHoTenCheckin = convertView.findViewById(R.id.txtHoTenCheckin);
        ImageView avtAnhCheckin = convertView.findViewById(R.id.imgAnhCheckin);
        TextView txtNoiDungCheckin = convertView.findViewById(R.id.txtNoiDungCheckin);
        TextView txtMaCheckin = convertView.findViewById(R.id.txtMaCheckin);
        TextView txtUserCheckin = convertView.findViewById(R.id.txtUserCheckin);

        DanhSachCheckin danhSachCheckin = arrayList.get(position);

        avtCheckin.setImageBitmap(danhSachCheckin.getmAvt());
        txtHoTenCheckin.setText(danhSachCheckin.getmHoTenCheckin());
        avtAnhCheckin.setImageBitmap(danhSachCheckin.getmHinhCheckin());
        txtNoiDungCheckin.setText(danhSachCheckin.getmNoiDungCheckin());
        txtMaCheckin.setText(danhSachCheckin.getmMaCheckin());
        txtUserCheckin.setText(danhSachCheckin.getmUserCheckin());
        return convertView;
    }
}
