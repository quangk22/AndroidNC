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

public class UpdateActivity extends AppCompatActivity {
    SQLiteDatabase database;
    String DATABASE_NAME = "QLNS.db";
    EditText txtTenNV, txtSDT;
    Button btnChonHinh, btnChupHinh, btnLuu, btnHuy;
    ImageView imgAnhSua;
    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;
    int maNV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        addConTronl();
        loadData();
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
                Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

    }
    private  void update(){
        String tenNV = txtTenNV.getText().toString();
        String SDT = txtSDT.getText().toString();
        byte[] anh = getByteArrayFromImageView(imgAnhSua);
        ContentValues contentValues = new ContentValues();
        contentValues.put("TenNV",tenNV);
        contentValues.put("SDT",SDT);
        contentValues.put("Anh",anh);
        database = Database.initDatabase(UpdateActivity.this ,DATABASE_NAME);
        database.update("NhanVien",contentValues,"manv = ? ", new String[] {maNV+""});
        Intent intent = new Intent(UpdateActivity.this,MainActivity.class);
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

    private void loadData() {
        Intent intent = getIntent();
         maNV = intent.getIntExtra("ID", -1);
        if (maNV != -1) {
            database = Database.initDatabase(UpdateActivity.this, DATABASE_NAME);
            Cursor cursor = database.rawQuery("SELECT * FROM NhanVien WHERE manv = ? ", new String[]{maNV + ""});
            cursor.moveToFirst();//con trỏ
            String tenNV = cursor.getString(1);
            String SDT = cursor.getString(2);
            byte[] anh = cursor.getBlob(3);
            if (anh != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(anh, 0, anh.length);
                imgAnhSua.setImageBitmap(bitmap);
            }
            txtTenNV.setText(tenNV);
            txtSDT.setText(SDT);

        }
    }

    private void addConTronl() {
        txtTenNV = (EditText) findViewById(R.id.editTextNhapTen);
        txtSDT = (EditText) findViewById(R.id.editTextSDT);
        btnChonHinh = (Button) findViewById(R.id.buttonChonHinh);
        btnChupHinh = (Button) findViewById(R.id.buttonChupHinh);
        btnLuu = (Button) findViewById(R.id.buttonLuu);
        btnHuy = (Button) findViewById(R.id.buttonQuyLai);
        imgAnhSua = (ImageView) findViewById(R.id.imageAvatar);

    }
}