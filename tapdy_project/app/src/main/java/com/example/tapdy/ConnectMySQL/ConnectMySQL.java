package com.example.tapdy.ConnectMySQL;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectMySQL
{

    private Connection connection;
    public void On ()
    {
        connection =null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://192.168.43.98:3306/tapdy","root","");
            Log.d("THONG BAO ","Ket noi thanh cong ");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.d("LOI ",""+ex);
        }
    }
    public Connection getConn ()
    {
        return this.connection;
    }
}
