package com.example.solemate.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import com.example.solemate.databinding.ActivityOrderBinding;
import com.example.solemate.konfigurasi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    private ActivityDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        listSepatu();
        binding.listItem.setOnItemClickListener(this);
    }

    private void listSepatu() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = konfigurasi.tampilUrl;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("DetailActivity", "Response received: " + response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray resultArray = jsonResponse.getJSONArray("result");

                            for (int i = 0; i < resultArray.length(); i++) {
                                JSONObject sepatuObject = resultArray.getJSONObject(i);
                                String id = sepatuObject.getString("id");
                                String namaSepatu = sepatuObject.getString("nama_sepatu");
                                HashMap<String,String> sepatu = new HashMap<>();
                                sepatu.put("id", id);
                                sepatu.put("nama_sepatu", namaSepatu);
                                list.add(sepatu);
                            }
                            ListAdapter adapter = new SimpleAdapter(
                                    DetailActivity.this, list, R.layout.list_item,
                                    new String[]{"id","nama_sepatu"},
                                    new int[]{R.id.id, R.id.nama});
                            binding.listItem.setAdapter(adapter);

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, DetailSepatuActivity.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String orderId = map.get("id").toString();
        intent.putExtra("id",orderId);
        startActivity(intent);
    }
}