package com.example.solemate.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.solemate.R;
import com.example.solemate.databinding.ActivityOrderBinding;
import com.example.solemate.konfigurasi;

import java.util.HashMap;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {

    private ActivityOrderBinding binding;
    String nama_sepatu, tanggal, metode, total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama_sepatu = String.valueOf(binding.namaSepatu.getText());
                tanggal = String.valueOf(binding.tanggal.getText());
                int selectedRadioButtonId = binding.metode.getCheckedRadioButtonId();
                metode = selectedRadioButtonId == R.id.radioButtonCash ? "Tunai" :
                         selectedRadioButtonId == R.id.radioButtonCreditCard ? "Kartu Kredit" : "";
                total = String.valueOf(binding.total.getText());

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = konfigurasi.createUrl;

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                binding.loading.setVisibility(View.GONE);
                                if(response.equals("success")) {
                                    Toast.makeText(getApplicationContext(), "Sepatu Berhasil Ditambahkan",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    binding.textError.setText(response);
                                    binding.textError.setVisibility(View.VISIBLE);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        binding.loading.setVisibility(View.GONE);
                        binding.textError.setText(error.getLocalizedMessage());
                        binding.textError.setVisibility(View.VISIBLE);
                    }
                }){
                    protected Map<String, String> getParams(){
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("nama_sepatu", nama_sepatu);
                        paramV.put("tanggal", tanggal);
                        paramV.put("metode", metode);
                        paramV.put("total", total);
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });
    }
}