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
import java.sql.ResultSet;
import java.sql.Statement;

public class GopYActivity extends AppCompatActivity
{

    private EditText noiDungGopY;
    private Button guiGopY;
    private ProgressBar prGopY;
    private Button quayLaiGopY;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gop_y);

        khoiTaoCacNut();
        nutGuiGopY();
    }

    private void nutGuiGopY()
    {
        guiGopY.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                xuLyGuiGopY();
            }
        });
    }

    private void xuLyGuiGopY()
    {
        new GuiThongTin().execute();
    }
    private class GuiThongTin extends AsyncTask <Void,Void,Void>
    {
        @Override
        protected void onPreExecute()
        {
            prGopY.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            String sql = "INSERT INTO phanhoi(tenphanhoi,noidungphanhoi) VALUES('GOPY','"+noiDungGopY.getText().toString()+"') ";
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
            prGopY.setVisibility(View.INVISIBLE);
            finish();
            super.onPostExecute(aVoid);
        }
    }

    private void khoiTaoCacNut()
    {
        noiDungGopY = findViewById(R.id.noiDungGopY);
        guiGopY = findViewById(R.id.guiGopY);
        prGopY = findViewById(R.id.progressBarGopY);
        prGopY.setVisibility(View.INVISIBLE);
        quayLaiGopY = findViewById(R.id.quayLaiGopY);
        quayLaiGopY.setOnClickListener(new View.OnClickListener()
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
