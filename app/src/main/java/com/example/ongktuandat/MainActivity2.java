package com.example.ongktuandat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity2 extends AppCompatActivity {
    ImageButton btnAdd ,btnSub,btnMulti,btnDiv;
    EditText edtA, edtB;
    TextView kq;
    Button signOut;
    ImageView imgAvatar;
    TextView textView_UserName;
    GoogleSignInClient mGoogleSignInClient;
    private ServiceConnection serviceConnection;
    private boolean isConnected;
    private MyService myService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initView();
        connectService();

        textView_UserName = findViewById(R.id.textView_AccGoogle);
        imgAvatar = findViewById(R.id.imgAvatar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signOut = findViewById(R.id.signOut);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    // ...
                    case R.id.signOut:
                        signOut();
                        break;
                    // ...
                }
            }
        });
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            Uri personPhoto = acct.getPhotoUrl();

            textView_UserName.setText(personName);

            Glide.with(this).load(String.valueOf(personPhoto)).into(imgAvatar);
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity2.this, "Signed Out Successfull", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }

    private void connectService() {

        Intent intent = new Intent(this, MyService.class);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MyService.MyBinder myBinder = (MyService.MyBinder) service;

                myService = myBinder.getService();
                isConnected = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isConnected = false;
                myService = null;
            }
        };
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }
    void initView()
    {
        btnAdd =(ImageButton) findViewById(R.id.btnAdd);
        btnSub =(ImageButton) findViewById(R.id.btnSub);
        btnMulti =(ImageButton) findViewById(R.id.btnMulti);
        btnDiv=(ImageButton) findViewById(R.id.btnDiv);
        edtA = (EditText) findViewById(R.id.edtA);
        edtB = (EditText) findViewById(R.id.edtB);
        kq = (TextView)findViewById(R.id.textViewKq) ;



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected){
                    return;
                }
                kq.setText("");
                int result = myService.add(
                        Integer.parseInt(edtA.getText().toString()),
                        Integer.parseInt(edtB.getText().toString()));

                kq.setText(kq.getText().toString() +result);
                Toast.makeText(myService, "Result:" + result, Toast.LENGTH_SHORT).show();


            }

        });



        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected){
                    return;
                }
                kq.setText("");
                int result = myService.sub(
                        Integer.parseInt(edtA.getText().toString()),
                        Integer.parseInt(edtB.getText().toString()));

                kq.setText(kq.getText().toString() +result);
                Toast.makeText(myService, "Result:" + result, Toast.LENGTH_SHORT).show();

            }
        });


        btnMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected){
                    return;
                }
                kq.setText("");
                int result = myService.multi(
                        Integer.parseInt(edtA.getText().toString()),
                        Integer.parseInt(edtB.getText().toString()));

                kq.setText(kq.getText().toString() +result);
                Toast.makeText(myService, "Result:" + result, Toast.LENGTH_SHORT).show();

            }
        });


        btnDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected){
                    return;
                }
                kq.setText("");
                int result = myService.div(
                        Integer.parseInt(edtA.getText().toString()),
                        Integer.parseInt(edtB.getText().toString()));

                kq.setText(kq.getText().toString() +result);
                Toast.makeText(myService, "Result:" + result, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }
}