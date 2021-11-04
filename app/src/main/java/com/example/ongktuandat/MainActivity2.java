package com.example.ongktuandat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity2 extends AppCompatActivity implements  View.OnClickListener{
    MediaPlayer mp;
    SeekBar seekBar;
    Handler handler = new Handler();
    Button btnStart, btnStop, btnDisplay;
    Button signOut;
    GoogleSignInClient mGoogleSignInClient;
    private CircleImageView circleImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
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

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnDisplay =(Button) findViewById(R.id.btnDisplay);
        circleImageView = findViewById(R.id.amee);

        mp = MediaPlayer.create(this,R.raw.buonvuongmauao_nguyenhung);

        seekBar = (SeekBar) findViewById(R.id.layoutChinh);
        seekBar.setMax(mp.getDuration());




        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnDisplay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnStart:
                Intent iStartService =
                        new Intent(MainActivity2.this, ServiceMyClass.class);
                startService(iStartService);
                
                starAnimation();

                mp.start();

                UpdateSeekBar updateSeekBar = new UpdateSeekBar();
                handler.post(updateSeekBar);

                break;
            case R.id.btnStop:
                Intent iStopService =
                        new Intent(MainActivity2.this, ServiceMyClass.class);
                stopService(iStopService);
                stopAnimation();
                mp.stop();
                break;
            case R.id.btnDisplay:
                Intent iDisplay =
                        new Intent(MainActivity2.this, MainActivity3.class);
                startActivity(iDisplay);
                break;

        }
    }

    private void stopAnimation() {
        circleImageView.animate().cancel();
    }

    private void starAnimation() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                circleImageView.animate().rotationBy(360).withEndAction(this).setDuration(10000)
                        .setInterpolator(new LinearInterpolator()).start();
            }
        };
        circleImageView.animate().rotationBy(360).withEndAction(runnable).setDuration(10000)
                .setInterpolator(new LinearInterpolator()).start();

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


    public class UpdateSeekBar implements Runnable {
        @Override
        public void run(){
            seekBar.setProgress(mp.getCurrentPosition());
            handler.postDelayed(this,100);
        }
    }
}