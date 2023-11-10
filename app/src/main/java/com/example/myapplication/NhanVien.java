package com.example.myapplication;

public class NhanVien {
    int MaNV;
    String TenNV;
    String SDT;
    byte[] Anh;

    public NhanVien(int maNV, String tenNV, String SDT, byte[] anh) {
        MaNV = maNV;
        TenNV = tenNV;
        this.SDT = SDT;
        Anh = anh;
    }
}
