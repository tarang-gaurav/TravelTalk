package com.example.ankit.traveltalk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    private TextView mProfileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String user_id=getIntent().getStringExtra("user_id");

        mProfileName=(TextView)findViewById(R.id.profile_displayName);
        mProfileName.setText(user_id);
    }
}
