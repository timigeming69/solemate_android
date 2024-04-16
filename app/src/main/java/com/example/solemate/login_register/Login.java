package com.example.solemate.login_register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.solemate.konfigurasi;
import com.example.solemate.ui.HomeActivity;
import com.example.solemate.databinding.ActivityLoginBinding;
import com.example.solemate.splash.SplashScreen2;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private ActivityLoginBinding binding;
    String nama, email, nomor_telepon, alamat, password, apiKey;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences = getSharedPreferences("MyAppName", MODE_PRIVATE);

        if(sharedPreferences.getString("logged", "false").equals("true")){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }
        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.textError.setVisibility(View.GONE);
                binding.loading.setVisibility(View.VISIBLE);
                password = String.valueOf(binding.password.getText());
                email = String.valueOf(binding.email.getText());

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = konfigurasi.loginUrl;

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("LoginActivity", "Response received: " + response);
                                binding.loading.setVisibility(View.GONE);
                                try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    String message = jsonObject.getString("message");
                                    if(status.equals("success")){
                                        Toast.makeText(getApplicationContext(), "Login Berhasil",Toast.LENGTH_SHORT).show();
                                        nama = jsonObject.getString("nama");
                                        email = jsonObject.getString("email");
                                        nomor_telepon = jsonObject.getString("nomor_telepon");
                                        alamat = jsonObject.getString("alamat");
                                        email = jsonObject.getString("email");
                                        apiKey = jsonObject.getString("apiKey");
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("logged", "true");
                                        editor.putString("nama", nama);
                                        editor.putString("alamat", alamat);
                                        editor.putString("nomor_telepon", nomor_telepon);
                                        editor.putString("email", email);
                                        editor.putString("apiKey", apiKey);
                                        editor.apply();
                                        Intent intent = new Intent(getApplicationContext(), SplashScreen2.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Login Gagal",Toast.LENGTH_SHORT).show();
                                        binding.textError.setText(message);
                                        binding.textError.setVisibility(View.VISIBLE);
                                    }
                                } catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LoginActivity", "Error: " + error.getMessage());
                        binding.loading.setVisibility(View.GONE);
                        binding.textError.setText(error.getLocalizedMessage());
                        binding.textError.setVisibility(View.VISIBLE);
                    }
                }){
                    protected Map<String, String>getParams(){
                        Map<String, String> paramV = new HashMap<>();
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