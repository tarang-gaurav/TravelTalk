package com.example.ankit.traveltalk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePageActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // private Button gchat;
    ImageView imageView;
    Button b1,b2,b3,b4;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
      mAuth=FirebaseAuth.getInstance();

        imageView=(ImageView)findViewById(R.id.imageView);
        b1=(Button)findViewById(R.id.blog_btn);
        b2=(Button)findViewById(R.id.btn_nearby);
        b3=(Button)findViewById(R.id.btn_places);
        b4=(Button)findViewById(R.id.btn_contactus);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomePageActivity.this, BlogActivity.class);
                startActivity(i);
            }
            });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomePageActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomePageActivity.this,AboutActivity.class);
                startActivity(i);

            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomePageActivity.this,GuideActivity.class);
                startActivity(i);

            }
        });






    }

   public void onBackPressed()
   {
       Intent a = new Intent(Intent.ACTION_MAIN);
       a.addCategory(Intent.CATEGORY_HOME);
       a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       startActivity(a);

   }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user=mAuth.getCurrentUser();
        if(user==null)
        {
            SendtoStart();
        }
        }

        private void SendtoStart()
        {
            Intent i=new Intent(HomePageActivity.this,MainActivity.class);
            startActivity(i);

        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.menu_logout)
        {
            FirebaseAuth.getInstance().signOut();
            SendtoStart();
        }



        if(item.getItemId() == R.id.menu_settings)
        {
            Intent x=new Intent(HomePageActivity.this,SettingsActivity.class);
            startActivity(x);

        }


        return true;
    }


}

