package com.notfound.makeamericafitagain;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements
        View.OnClickListener{

    //declaration
    TextInputEditText et_Name;
    Button btn_Apply;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference refRoot;
    DatabaseReference refUser;

    ProgressDialog dialog;

    User userObj;

    CircleBar cb_calorie;

    int progressStep = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //init
        et_Name = findViewById(R.id.et_Name);
        btn_Apply = findViewById(R.id.btn_Apply);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        refRoot = FirebaseDatabase.getInstance().getReference();
        refUser = refRoot.child(mUser.getUid());

        dialog = new ProgressDialog(this);

        cb_calorie = findViewById(R.id.cb_calorie);
        cb_calorie.setProgress(0);

        //attach listeners
        btn_Apply.setOnClickListener(this);


        //set progress bar
        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final int progress = (dataSnapshot.hasChild("calorie_today")) ? Integer.parseInt(dataSnapshot.child("calorie_today").getValue(String.class)) : 0;
                progressStep = 0;
                final Handler mUpdater = new Handler();
                Runnable mUpdateView = new Runnable() {
                    @Override
                    public void run() {
                        cb_calorie.setProgress(progressStep);
                        if(progressStep < progress){
                            progressStep++;
                        } else {
                            mUpdater.removeCallbacksAndMessages(null);
                        }
                        mUpdater.postDelayed(this, 10);
                    }
                };
                mUpdateView.run();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });

    }


    public void updateProfile() {
        //init
        final String name = et_Name.getText().toString();

        //field validation
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Please enter your name!", Toast.LENGTH_SHORT).show();
            return;
        } else {

            //lol another validation just to be safe
            if(name.equals("")){
                Toast.makeText(getApplicationContext(), "Please make sure all fields are entered!", Toast.LENGTH_SHORT).show();
                return;
            }

            //update profile info
            dialog.setMessage("Updating profile...");
            dialog.show();
            //init user object
            refUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userObj = new User(dataSnapshot.child("email").getValue(String.class), dataSnapshot.child("password").getValue(String.class));

                    //update user object
                    userObj.updateUserInfo(name);

                    //write user object to database
                    refUser.setValue(userObj);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Error occurred. Please contact support", Toast.LENGTH_SHORT).show();
                    return;
                }
            });

        }
    }


    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_Apply:

                break;
        }
    }
}
