package com.example.proyectoinventario;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.proyectoinventario.databinding.ActivityListviewBinding;

public class listviewActivity extends AppCompatActivity {

    ActivityListviewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.textView47.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                binding.textView47.setBackgroundColor(Color.RED);
                return false;
            }
        });
    }
}