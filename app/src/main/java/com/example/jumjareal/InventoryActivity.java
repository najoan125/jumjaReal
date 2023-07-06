package com.example.jumjareal;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashSet;

public class InventoryActivity extends AppCompatActivity {

    public static HashSet<Integer> inventory = new HashSet<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
    }
}