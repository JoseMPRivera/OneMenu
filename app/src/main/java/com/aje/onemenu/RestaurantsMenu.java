package com.aje.onemenu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class RestaurantsMenu extends AppCompatActivity {

    private TextView in_n_out;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_menu);

        in_n_out = findViewById(R.id.in_n_out);
        in_n_out.setText("lifhgldfkhgoikln dflkgjnglkdf dfnglkdfjgkln");
    }
}