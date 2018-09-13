package com.example.ankit.traveltalk;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    private TextInputLayout ustatus;
    private Button change;
    private DatabaseReference usdatabase;
    private FirebaseUser mcurrentuser;


    private ProgressDialog mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        FirebaseUser current_User= FirebaseAuth.getInstance().getCurrentUser();
        String current_uid=current_User.getUid();
        usdatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        String status_value=getIntent().getStringExtra("status_value");

        ustatus=(TextInputLayout)findViewById(R.id.txt_status);
        change=(Button)findViewById(R.id.btn_change);

        ustatus.getEditText().setText(status_value);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              mprogress=new ProgressDialog(StatusActivity.this);
                mprogress.setTitle("Saving Changes");
                mprogress.setMessage("Please Wait while we save changes");
                mprogress.setCanceledOnTouchOutside(false);
                mprogress.show();



                String status=ustatus.getEditText().getText().toString();


                usdatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            mprogress.dismiss();
                            Toast.makeText(StatusActivity.this, "Change Successful ", Toast.LENGTH_LONG).show();
                        }else
                        {
                            Toast.makeText(StatusActivity.this, "there was some error error ", Toast.LENGTH_LONG).show();
                        }
                    }
                });



            }
        });

    }


}
