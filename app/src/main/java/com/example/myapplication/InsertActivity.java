package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class InsertActivity extends AppCompatActivity {
    SQLiteDatabase database;
    String DATABASE_NAME = "QLNS.db";
    EditText txtTenNV, txtSDT, txtMaNV;
    Button btnChonHinh, btnChupHinh, btnLuu, btnHuy;
    ImageView imgAnhSua;
    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;
    int maNV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        addConTronl();
        addEvent();
    }
    private void addEvent() {
        btnChupHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        btnChonHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsertActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Insert();
            }
        });

    }
    private  void Insert(){
        String maNV = txtMaNV.getText().toString();
        String tenNV = txtTenNV.getText().toString();
        String SDT = txtSDT.getText().toString();
        byte[] anh = getByteArrayFromImageView(imgAnhSua);
        ContentValues contentValues = new ContentValues();
        contentValues.put("MaNV",maNV);
        contentValues.put("TenNV",tenNV);
        contentValues.put("SDT",SDT);
        contentValues.put("Anh",anh);
        database = Database.initDatabase(InsertActivity.this ,DATABASE_NAME);
        database.insert("NhanVien",null,contentValues);
        Intent intent = new Intent(InsertActivity.this,MainActivity.class);
        startActivity(intent);
    }
    private  void update(){
        String tenNV = txtTenNV.getText().toString();
        String SDT = txtSDT.getText().toString();
        byte[] anh = getByteArrayFromImageView(imgAnhSua);
        ContentValues contentValues = new ContentValues();
        contentValues.put("TenNV",tenNV);
        contentValues.put("SDT",SDT);
        contentValues.put("Anh",anh);
        database = Database.initDatabase(InsertActivity.this ,DATABASE_NAME);
        database.update("NhanVien",contentValues,"manv = ? ", new String[] {maNV+""});
        Intent intent = new Intent(InsertActivity.this,MainActivity.class);
        startActivity(intent);
    }
    public byte[] getByteArrayFromImageView(ImageView imgv){

        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);

    }

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOOSE_PHOTO) {

                try {
                    Uri imageUri = data.getData();
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imgAnhSua.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgAnhSua.setImageBitmap(bitmap);
            }
        }
    }
    private void addConTronl() {
        txtMaNV = (EditText) findViewById(R.id.editTextMaNVThem);
        txtTenNV = (EditText) findViewById(R.id.editTextNhapTenThem);
        txtSDT = (EditText) findViewById(R.id.editTextSDTThem);
        btnChonHinh = (Button) findViewById(R.id.buttonChonHinhThem);
        btnChupHinh = (Button) findViewById(R.id.buttonChupHinhThem);
        btnLuu = (Button) findViewById(R.id.buttonLuuThem);
        btnHuy = (Button) findViewById(R.id.buttonQuyLaiThem);
        imgAnhSua = (ImageView) findViewById(R.id.imageAvatarThem);

    }
}