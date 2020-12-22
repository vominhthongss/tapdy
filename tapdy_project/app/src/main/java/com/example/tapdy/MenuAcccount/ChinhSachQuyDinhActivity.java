package com.example.tapdy.MenuAcccount;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tapdy.R;

public class ChinhSachQuyDinhActivity extends AppCompatActivity
{

    private Button quayLaiChinhSach;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinh_sach_quy_dinh);
        quayLaiChinhSach = findViewById(R.id.quayLaiChinhSach);
        quayLaiChinhSach.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }
}
