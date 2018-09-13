package com.example.ankit.traveltalk;

import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    private TextInputLayout dname;
    private  TextInputLayout email;
    private TextInputLayout password;
    Button submit;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mdatabase;
    private ProgressDialog mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        dname= (TextInputLayout)findViewById(R.id.lay_display);
        email= (TextInputLayout)findViewById(R.id.lay_email);
        password= (TextInputLayout)findViewById(R.id.lay_password);
        submit=(Button)findViewById(R.id.btn_submit);

        mAuth=FirebaseAuth.getInstance();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mprogress=new ProgressDialog(RegistrationActivity.this);
                mprogress.setMessage("Creating Account");
                mprogress.setCanceledOnTouchOutside(false);
                mprogress.show();
                String d_name=dname.getEditText().getText().toString();
                String d_email=email.getEditText().getText().toString();
                String d_password=password.getEditText().getText().toString();



                register_user(d_name,d_email,d_password);


            }
        });





    }

    private void register_user(final String d_name, final String d_email, String d_password) {

        if (TextUtils.isEmpty(d_email) || TextUtils.isEmpty(d_password) || TextUtils.isEmpty(d_name)) {

            Toast.makeText(RegistrationActivity.this, "Enter all details", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(d_email, d_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        mprogress.dismiss();
                        FirebaseUser current_User=FirebaseAuth.getInstance().getCurrentUser();
                        String uid=current_User.getUid();

                        mdatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                        HashMap<String,String> usermap=new HashMap< >();
                        usermap.put("name",d_name);
                        usermap.put("status","Hey I am using TravelTalk App");
                      //  usermap.put("image","default");
                       // usermap.put("thumbnail_image","default");

                        mdatabase.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful())
                                {
                                    mprogress.dismiss();
                                    Toast.makeText(RegistrationActivity.this, "Account created Successfully", Toast.LENGTH_SHORT).show();
                                    Intent x = new Intent(RegistrationActivity.this, MainActivity.class);
                                    startActivity(x);
                                   // finish();

                                }
                            }
                        });





                    } else {
                        Toast.makeText(RegistrationActivity.this, "You got error", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }





}

