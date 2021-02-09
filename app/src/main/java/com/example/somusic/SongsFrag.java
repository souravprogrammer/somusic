package com.example.somusic;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.ImageDecoder;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.style.UpdateLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SongsFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongsFrag extends Fragment implements RecycleAdapter.OnItemListner {

    public static int pos ;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
 //   private View view ;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<SongData> D = new ArrayList<SongData>(); // mune doing

    public SongsFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SongsFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static SongsFrag newInstance(String param1, String param2) {
        SongsFrag fragment = new SongsFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.fragment_songs, container, false);


        D.add(new SongData("12", "zombie", "carabin night", "343"));
        D.add(new SongData("12", "zombie", "carabin night", "343"));

        /**
         *  Setting A recycle view here of song list with song name
         *
         * **/
        if (MainActivity.MusicFiles != null) {
            TextView txt = V.findViewById(R.id.Empty);
            txt.setVisibility(View.GONE);

            RecyclerView recyclerView = V.findViewById(R.id.SongRecycleView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            RecycleAdapter adapter = new RecycleAdapter(MainActivity.MusicFiles, this); //passing a list here for song
            recyclerView.setAdapter(adapter);



        }
        return V;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    /** This function will Iniciate the music in our application so when we click the music this
     *  function get called and prepare and Release the sng according to it...
     * **/
    public void OnItemClick(int position) {
        pos = position ;
        // AudioManager audioManager = (AudioManager) Context.getSystemService(Context.AUDIO_SERVICE);
        SongData currentsong = MainActivity.MusicFiles.get(position); // getting a song from the list
        long id = currentsong.getId();
        Uri contentUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

       int Result =   MainActivity.maudioManager.requestAudioFocus(MainActivity.afChangeListener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);

       if(Result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
           if (MainActivity.player == null) {
               MainActivity.player = new MediaPlayer();
               MainActivity.player.setAudioAttributes(new AudioAttributes.Builder()
                       .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                       .setUsage(AudioAttributes.USAGE_MEDIA)
                       .build()
               );
               try {
                   MainActivity.player.setDataSource(getContext(), contentUri);
                   MainActivity.player.prepare();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           } else  // end of If block
           {
               MainActivity.player.release();
               MainActivity.player = null;
               MainActivity.player = new MediaPlayer();
               MainActivity.player.setAudioAttributes(new AudioAttributes.Builder()
                       .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                       .setUsage(AudioAttributes.USAGE_MEDIA)
                       .build()
               );
               try {
                   MainActivity.player.setDataSource(getContext(), contentUri);
                   MainActivity.player.prepare();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }

           MainActivity.player.start();
       }
    } // function end bracket


}