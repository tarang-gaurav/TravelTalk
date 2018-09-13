package com.example.ankit.traveltalk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    TextInputLayout uemail,upass;
    Button submit;
    TextView regi,fgpass,uexit;
    private FirebaseAuth mAuth;
    private ProgressDialog mprogress;

    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    mAuth=FirebaseAuth.getInstance();





        uemail= (TextInputLayout)findViewById(R.id.email);
        upass= (TextInputLayout)findViewById(R.id.password);
        submit=(Button)findViewById(R.id.btn_login);
        regi=(TextView)findViewById(R.id.txt_create);
        uexit=(TextView)findViewById(R.id.txtexit);
        fgpass=(TextView)findViewById(R.id.txt_forgot);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startsignin();




            }
        });

        fgpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,PasswordActivity.class);
                startActivity(i);

            }
        });

        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null)

                {
                    Intent i=new Intent(MainActivity.this,HomePageActivity.class);
                    startActivity(i);

                }
            }
        };

        uexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


         regi.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 mprogress=new ProgressDialog(MainActivity.this);

                 mprogress.setMessage("Loading");
                 mprogress.setCanceledOnTouchOutside(false);
                 mprogress.show();
                 Intent i=new Intent(MainActivity.this,RegistrationActivity.class);
                 startActivity(i);
                 finish();

             }
         });

    }
    private void startsignin()
    {
        String d_email=uemail.getEditText().getText().toString();
        String d_password=upass.getEditText().getText().toString();

        if(TextUtils.isEmpty(d_email) || TextUtils.isEmpty(d_password)) {

            Toast.makeText(MainActivity.this, "Enter all details", Toast.LENGTH_SHORT).show();
        }
        else
        {

            mAuth.signInWithEmailAndPassword(d_email,d_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful())
                    {

                        Toast.makeText(MainActivity.this, "Login problem", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}