package com.example.solemate.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.solemate.databinding.FragmentHomeBinding;
import com.example.solemate.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        sharedPreferences = requireActivity().getSharedPreferences("MyAppName", Context.MODE_PRIVATE);
        binding.textViewName.setText(sharedPreferences.getString("nama", ""));
        binding.textViewEmail.setText(sharedPreferences.getString("email", ""));
        binding.textViewAddress.setText(sharedPreferences.getString("alamat", ""));
        binding.textViewPhone.setText(sharedPreferences.getString("nomor_telepon", ""));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}