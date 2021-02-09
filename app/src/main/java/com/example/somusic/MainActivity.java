package com.example.somusic;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.animation.TimeAnimator;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.PlatformVpnProfile;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    public static boolean status = true;
    public static ArrayList<SongData> MusicFiles = new ArrayList<SongData>();
    public static ArrayList<SongData> Favsongs = new ArrayList<SongData>() ;
    private final int STORAGE_PERMISSION_CODE = 1;
    private final String Permission = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static MediaPlayer player = null;
    private Handler handler = new Handler();
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static int result;
    static AudioManager maudioManager;
    /**
     * Audio focous action Listener
     * act upon the player activity
     **/
     private static boolean stop = false ;
    static AudioManager.OnAudioFocusChangeListener afChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        if (player.isPlaying()){
                            player.pause();
                        }
                        // Permanent loss of audio focus
                        // Pause playback immediately
                        // Wait 30 seconds before stopping playback
                           new Handler().postDelayed(null,
                                   TimeUnit.SECONDS.toMillis(30));
                           if(stop){
                           player.stop();
                            player.release();
                           player = null ;}
                           stop = true ;
                        maudioManager.abandonAudioFocus(afChangeListener);

                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {

                        // Pause playback
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {


                        float volume = (float) (1 - (Math.log(100 - 90000) / Math.log(100)));
                        player.setVolume(volume, volume);
                        // Lower the volume, keep playing

                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {

                        // Your app has been granted audio focus again
                        // Raise volume to normal, restart playback if necessary
                        //player.setVolume(volume,volume);
                        if (player.isPlaying())
                            player.start();

                    }
                }
            };


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestStoragePermission();
        getAudioFocous();
        TabLayout tabLayout = findViewById(R.id.tabLayout); // Retrieving a tab layout object
        ViewPager viewPager = findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ReadFle();
//        SongData song = MusicFiles.get(10) ;
//        long id = song.getId();
//        Uri contentUri = ContentUris.withAppendedId(
//                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
//
//        player = new MediaPlayer();
//        player.setAudioAttributes(
//                new AudioAttributes.Builder()
//                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                        .setUsage(AudioAttributes.USAGE_MEDIA)
//                        .build()
//        );
//
//        try {
//            player.setDataSource(getApplicationContext(), contentUri);
//            player.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //       player.start();
// ...prepare and start...
        /*Lower currently layer song playing*/
//        Intent intent = new Intent(this,musicActivity.class);
//        startActivity(intent);

//
        TextView v = findViewById(R.id.Currently_playing_title);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), musicActivity.class);

                startActivity(intent);
            }
        });

        ImageView playbutton = findViewById(R.id.playPause);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(player!=null)
                {
                    if(player.isPlaying())
                    {
                        playbutton.setImageResource(R.drawable.baseline_pause_circle_filled_black_48);
                    }else
                    {
                        playbutton.setImageResource(R.drawable.baseline_play_circle_filled_black_48);
                    }
                }

//                if (status) {
//                    playbutton.setImageResource(R.drawable.baseline_pause_circle_filled_black_48);
//
//                } else {
//                    playbutton.setImageResource(R.drawable.baseline_play_circle_filled_black_48);
//
//                }
                if (RecycleAdapter.name != null && RecycleAdapter.clicked) {
                    LinearLayout linearLayout = findViewById(R.id.Playing_item);
                    ImageView button = findViewById(R.id.playPause);
                    linearLayout.setVisibility(View.VISIBLE);
                    TextView title = findViewById(R.id.Currently_playing_title);
                    title.setText(RecycleAdapter.name);
                    button.setImageResource(R.drawable.baseline_pause_circle_filled_black_48);
                    //title.setMaxLines(1);
                    RecycleAdapter.clicked = false;
                }
                handler.postDelayed(this, 1000);
            }
        });

        // Image button Play Pause
        playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (player.isPlaying()) {
                    playbutton.setImageResource(R.drawable.baseline_play_circle_filled_black_48);
                    player.pause();
                    status = false;

                } else {
                    playbutton.setImageResource(R.drawable.baseline_pause_circle_filled_black_48);
                    player.start();
                    status = true;
                }
            }
        });

//        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                playbutton.setImageResource(R.drawable.baseline_pause_circle_filled_black_48);
//            }
//        });
    }


    /* permission for storage*/
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
        // Toast.makeText(this,"loop out",Toast.LENGTH_LONG).show();
    }

    // Reading music files from a devide
    private void ReadFle() {
        //   MusicFiles = new ArrayList<SongData>() ;
        ContentResolver contentResolver = getContentResolver();
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null) {
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            // no media on the device
            MusicFiles = null; // list is empty
        } else {
            int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            do {
                SongData data = new SongData();
                long thisId = cursor.getLong(idColumn);
                String thisTitle = cursor.getString(titleColumn);
                data.setTitle(thisTitle); // Title of the song
                data.setId(thisId); // id of the song
                MusicFiles.add(data);
                // ...process entry...
            } while (cursor.moveToNext());
        }
    }

    private void ReadMusicFiles() {
//        // reading a DAta from a disk
//        // reading is complete
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST,};
        Cursor c = this.getContentResolver().query(uri, projection, null, null, null);

        if (c != null) {
            while (c.moveToNext()) {
                SongData audioModel = new SongData();
                String path = c.getString(0);
                String name = c.getString(1);
                String album = c.getString(2);
                String artist = c.getString(3);
                // Log.d("Path :" + path, " Artist :" + album);

                audioModel.setAlbum(album);
                audioModel.setTitle(name);
                audioModel.setPath(path);
                //   Log.d("Name :" + name, " Album :" + album);

                //

                MusicFiles.add(audioModel);
            }
            c.close();
        }
    }

    private void downPlayLayout() // lower player layout menu
    {
        if (player != null) {
            LinearLayout linearLayout = findViewById(R.id.Playing_item);
            linearLayout.setVisibility(View.VISIBLE);
            ImageView PlayPauseButton = findViewById(R.id.playPause);
            PlayPauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (player != null) {
                        if (player.isPlaying()) {
                            player.pause();
                            PlayPauseButton.setImageResource(R.drawable.baseline_pause_circle_filled_black_48);
                        } else {
                            player.start();
                            PlayPauseButton.setImageResource(R.drawable.baseline_play_circle_filled_black_48);
                        }
                    }
                }
            });
        }
    }
    /**
     * Getting audio Focous
     **/
    public void getAudioFocous() {
        maudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

    }
}
