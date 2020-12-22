package com.example.tapdy.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tapdy.Model.ThongBaoLuu;
import com.example.tapdy.R;

import java.util.ArrayList;

public class AdapterDanhSachThongBao extends ArrayAdapter<ThongBaoLuu>
{
    private Context context;
    private int resource;
    private ArrayList<ThongBaoLuu> arrayList;
    public AdapterDanhSachThongBao(@NonNull Context context, int resource, @NonNull ArrayList<ThongBaoLuu> objects)
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
        convertView = LayoutInflater.from(context).inflate(R.layout.thongbao_row,parent,false);

        TextView maThongBao = convertView.findViewById(R.id.maThongBao);
        TextView tenThongBao = convertView.findViewById(R.id.tenThongBao);
        TextView noiDungThongBao = convertView.findViewById(R.id.noiDungThongBao);
        TextView ngayThongBao = convertView.findViewById(R.id.ngayThongBao);

        ThongBaoLuu t = arrayList.get(position);
        maThongBao.setText(t.getmMaThongBao());
        tenThongBao.setText(t.getmTenThongBao());
        noiDungThongBao.setText(t.getmNoiDungThongBao());
        ngayThongBao.setText(t.getmNgayThongBao());
        ImageView chamDo = convertView.findViewById(R.id.chamDo);

        if(t.getmDaXem()==0)
        {
            chamDo.setImageResource(R.drawable.chamdo);

        }




        return convertView;
    }
}
