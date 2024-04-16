package com.example.solemate.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.solemate.R;
import com.example.solemate.databinding.ActivityDetailBinding;
import com.example.solemate.databinding.ActivityDetailSepatuBinding;
import com.example.solemate.konfigurasi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailSepatuActivity extends AppCompatActivity {

    private ActivityDetailSepatuBinding binding;

    String id, nama_sepatu, tanggal, metode, total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailSepatuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = konfigurasi.deleteUrl + "?id=" + id;

                StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("DetailActivity", "Response received: " + response);
                                binding.loading.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Sepatu Berhasil Dihapus",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                                finish();
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
                        paramV.put("id", id);
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });

        getSepatu(id);

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama_sepatu = String.valueOf(binding.namaSepatu.getText());
                tanggal = String.valueOf(binding.tanggal.getText());
                int selectedRadioButtonId = binding.metode.getCheckedRadioButtonId();
                metode = selectedRadioButtonId == R.id.radioButtonCash ? "Tunai" :
                        selectedRadioButtonId == R.id.radioButtonCreditCard ? "Kartu Kredit" : "";
                total = String.valueOf(binding.total.getText());

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = konfigurasi.updateUrl;

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                binding.loading.setVisibility(View.GONE);
                                if(response.equals("success")) {
                                    Toast.makeText(getApplicationContext(), "Sepatu Berhasil DiUpdate",Toast.LENGTH_SHORT).show();
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
                        paramV.put("id", id);
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


    private void getSepatu(String  id) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = konfigurasi.tampilUrl + "?id=" + id;;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray resultArray = jsonResponse.getJSONArray("result");

                                JSONObject sepatuObject = resultArray.getJSONObject(0);
                                String namaSepatu = sepatuObject.getString("nama_sepatu");
                                String tanggal = sepatuObject.getString("tanggal");
                                String metode = sepatuObject.getString("metode");
                                String total = sepatuObject.getString("total");

                                binding.namaSepatu.setText(namaSepatu);
                                binding.tanggal.setText(tanggal);
                                binding.total.setText(total);
                                if (metode.equals("Tunai")) {
                                    binding.radioButtonCash.setChecked(true);
                                } else if (metode.equals("Kartu Kredit")) {
                                    binding.radioButtonCreditCard.setChecked(true);
                                }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }
}