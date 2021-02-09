package com.example.somusic;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.security.PublicKey;

public class musicActivity extends AppCompatActivity {
    private int maxduration ;
    private SeekBar bar ;
    private Handler handler = new Handler();
    public static String song_title = null ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        iniciatePlayer();
        cheakStatus();
        ImageView pause_play = findViewById(R.id.playPause_button);

        pause_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(MainActivity.player.isPlaying())
                {
                    MainActivity.player.pause();
                    pause_play.setImageResource(R.drawable.orange_play_64);
                    MainActivity.status = false ;
                }else
                {
                    MainActivity.player.start();
                    pause_play.setImageResource(R.drawable.orange_pause_64);
                    MainActivity.status = true ;
                }
            }
        });
        TextView currentime = findViewById(R.id.current_duration);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(MainActivity.player.isPlaying())
                {
                    pause_play.setImageResource(R.drawable.orange_pause_64);

                }else{  pause_play.setImageResource(R.drawable.orange_play_64); }
                bar.setProgress(MainActivity.player.getCurrentPosition());
                currentime.setText(milliSecondtoTimer(MainActivity.player.getCurrentPosition()));
                handler.postDelayed(this,1000) ;
            }
        });
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bar.setProgress(progress);
                if(fromUser)
                {
                    MainActivity.player.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ImageView likebutton = findViewById(R.id.like);
        likebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* just checking not working just demo */
              MainActivity.Favsongs.add( MainActivity.MusicFiles.get(SongsFrag.pos));

                // MainActivity.Favsongs.add();

                likebutton.setImageResource(R.drawable.baseline_favorite_black_48);
            }
        });
    }
    private void iniciatePlayer()
    {
        if(MainActivity.player!=null)
        {
            TextView maxtime  = findViewById(R.id.duration_max) ;
            TextView view  = findViewById(R.id.songTitile_activity) ;
            view.setText(song_title);
           this.maxduration= MainActivity.player.getDuration();
           bar = findViewById(R.id.seekBar);
           bar.setMax(maxduration);
           maxtime.setText(milliSecondtoTimer(MainActivity.player.getDuration()));

        }
    }

    public void cheakStatus()
    {
        ImageView pause_play = findViewById(R.id.playPause_button);

        if(MainActivity.status)
        {
            pause_play.setImageResource(R.drawable.orange_pause_64);
        } else
        {
            pause_play.setImageResource(R.drawable.orange_play_64);

        }
    }
   public String milliSecondtoTimer(long millisecond)
    {
        int hour =(int) (millisecond / (1000 * 60 *60) ) ;
        int min  = (int) (millisecond % (1000 *60 *60 )) / (1000*60) ;
        int sec  = (int) (millisecond % (1000 * 60 *60 ) % (1000*60)/1000 );
        String secondString ;
        String  timerString = "" ;

        if(hour > 0)
        {
            timerString =  + hour + ":" ;
        }
        if(sec < 10 )
        {
            secondString = "0" +sec ;
        }else
        {
            secondString = ""+sec ;
        }

        timerString = timerString + min + ":" +secondString ;

        return timerString ;
    }
}