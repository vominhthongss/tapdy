package com.example.tapdy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.tapdy.Adapter.AdapterQuan;
import com.example.tapdy.ConnectMySQL.ConnectMySQL;
import com.example.tapdy.Fragment.HomeFragment;
import com.example.tapdy.Model.DanhSachQuan;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity
{
    public static Connection CONNECTION=null;
    private static int SPLASH_TIME_OUT=4000;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                new LayKetNoi().execute();
            }
        },SPLASH_TIME_OUT);
    }
    private class LayKetNoi extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids)
        {
            ConnectMySQL connectMySQL = new ConnectMySQL();
            connectMySQL.On();
            CONNECTION=connectMySQL.getConn();
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid)
        {
            Intent mainIntent = new Intent (WelcomeActivity.this,MainActivity.class);
            startActivity(mainIntent);
            finish();
            super.onPostExecute(aVoid);
        }
    }
}
