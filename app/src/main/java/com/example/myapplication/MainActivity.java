package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String DATABASE_NAME = "QLNS.db";
    SQLiteDatabase database;
    ListView listDSNV;
    Button btnThem;
    ArrayList<NhanVien> list;
    AdapterNhanVien adapterNV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnThem = (Button) findViewById(R.id.buttonThem);
        addEvent();
        listDSNV = (ListView) findViewById(R.id.listviewDSNV);
        list = new ArrayList<>();
        adapterNV = new AdapterNhanVien(MainActivity.this ,list);
        listDSNV.setAdapter(adapterNV);
        database = Database.initDatabase(MainActivity.this,DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM NhanVien",null );
        for (int i = 0; i < cursor.getCount() ; i++) {
            cursor.moveToPosition(i);
            int manv = cursor.getInt(0);
            String tennv = cursor.getString(1);
            String sdt = cursor.getString(2);
            byte[] anh = cursor.getBlob(3);
            list.add(new NhanVien(manv,tennv,sdt,anh));
        }
        adapterNV.notifyDataSetChanged();
    }

    private void addEvent() {
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,InsertActivity.class);
                startActivity(intent);
            }
        });
    }
}