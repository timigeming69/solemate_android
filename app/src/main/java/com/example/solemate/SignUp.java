package com.example.solemate;

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
import com.example.solemate.databinding.ActivitySignUpBinding;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private ActivitySignUpBinding binding;

    String nama, alamat, no_telepon, email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.textError.setVisibility(View.GONE);
                binding.loading.setVisibility(View.VISIBLE);
                nama = String.valueOf(binding.nama.getText());
                email = String.valueOf(binding.email.getText());
                alamat = String.valueOf(binding.alamat.getText());
                no_telepon = String.valueOf(binding.noTelepon.getText());
                password = String.valueOf(binding.password.getText());
                email = String.valueOf(binding.email.getText());

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url ="https://10.4.13.117/SolaMate/register.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                binding.loading.setVisibility(View.GONE);
                                if(response.equals("success")) {
                                    Toast.makeText(getApplicationContext(), "Registrasi Berhasil",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Login.class);
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
                    protected Map<String, String>getParams(){
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("nama", nama);
                        paramV.put("alamat", alamat);
                        paramV.put("no_telepon", no_telepon);
                        paramV.put("email", email);
                        paramV.put("password", password);
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });
    }
}