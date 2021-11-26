package com.example.ongktuandat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity2 extends AppCompatActivity implements  View.OnClickListener{
    public static MediaPlayer mp;
    private AudioManager mAudioManager;
    Button btnGoogleMap;
    TextView playerDuration,playerPosition;
    SeekBar seekBar;
    Handler handler = new Handler();
    ImageView btnStart, btnStop, btnDisplay;
    ImageView signOut;
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

        btnGoogleMap = findViewById(R.id.google_map);
        btnGoogleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity2.this,MainActivity3.class);
                startActivity(i);

            }
        });

        playerDuration =findViewById(R.id.playerDuaration);
        playerPosition =findViewById(R.id.playerPosition);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        btnStart =  findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
//        btnDisplay =(Button) findViewById(R.id.btnDisplay);
        circleImageView = findViewById(R.id.amee);

        mp = MediaPlayer.create(this,R.raw.dendakhongduong);
        playerDuration.setText(convertFormat(mp.getDuration()));
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mpl) {
                mp.start();
            }
        });

        seekBar = (SeekBar) findViewById(R.id.layoutChinh);
        seekBar.setMax(mp.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b){
                    mp.seekTo(i);
                    seekBar.setProgress(i);
                }
                playerPosition.setText(convertFormat(mp.getCurrentPosition()));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mp!= null){
                    try {
                        if(mp.isPlaying()){
                            Message message = new Message();

                            message.what = mp.getCurrentPosition();
                            handler.sendMessage(message);
                            Thread.sleep(1000);
                        }
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
//        btnDisplay.setOnClickListener(this);
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
                mp.pause();
                break;
//            case R.id.btnDisplay:
//                Intent iDisplay =
//                        new Intent(MainActivity2.this, MainActivity3.class);
//                startActivity(iDisplay);
//                break;

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

    private String convertFormat(int duration) {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }
    public class UpdateSeekBar implements Runnable {
        @Override
        public void run(){
            seekBar.setProgress(mp.getCurrentPosition());
            handler.postDelayed(this,100);
        }
    }
}