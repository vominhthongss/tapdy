package com.example.tapdy.MenuAcccount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.tapdy.R;
import com.example.tapdy.WelcomeActivity;

import java.sql.Connection;
import java.sql.Statement;

public class BaoLoiActivity extends AppCompatActivity
{

    private EditText noiDungBaoLoi;
    private Button guiBaoLoi;
    private ProgressBar prBaoLoi;
    private Button quayLaiBaoLoi;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bao_loi);

        khoiTaoCacNut();
        nutGuiBaoLoi();
    }

    private void nutGuiBaoLoi()
    {
        guiBaoLoi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                xuLyGuiBaoLoi();
            }
        });
    }

    private void xuLyGuiBaoLoi()
    {
        new GuiThongTin().execute();
    }
    private class GuiThongTin extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected void onPreExecute()
        {
            prBaoLoi.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql = "INSERT INTO phanhoi(tenphanhoi,noidungphanhoi) VALUES('BAOLOI','"+noiDungBaoLoi.getText().toString()+"') ";
            Statement stmt = null;
            try
            {
                stmt = getConnection().createStatement();
                stmt.execute(sql);
            }
            catch (Exception e)
            {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            prBaoLoi.setVisibility(View.INVISIBLE);
            finish();
            super.onPostExecute(aVoid);
        }
    }

    private void khoiTaoCacNut()
    {
        noiDungBaoLoi = findViewById(R.id.noiDungBaoLoi);
        guiBaoLoi = findViewById(R.id.guiBaoLoi);
        prBaoLoi = findViewById(R.id.progressBarBaoLoi);
        prBaoLoi.setVisibility(View.INVISIBLE);
        quayLaiBaoLoi = findViewById(R.id.quayLaiBaoLoi);
        quayLaiBaoLoi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }


    private Connection getConnection()
    {
        return WelcomeActivity.CONNECTION;
    }
}
