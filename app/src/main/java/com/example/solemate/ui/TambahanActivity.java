package com.example.solemate.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.solemate.R;
import com.example.solemate.databinding.ActivityDetailBinding;
import com.example.solemate.databinding.ActivityTambahanBinding;

public class TambahanActivity extends AppCompatActivity {

    private ActivityTambahanBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambahan);
        binding = ActivityTambahanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}