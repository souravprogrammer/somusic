package com.example.somusic;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.Holder> {

    ArrayList<SongData> list ;
   public static Boolean clicked = true ;
    public static String name = null ;
    public static int position = -1 ;
   private OnItemListner onItemListner ;
   public  TextView prevSongTitle = null ;

    public RecycleAdapter(ArrayList <SongData> list,OnItemListner onItemListner)
    {
        this.list = list ;
        this.onItemListner = onItemListner ;

    }
    @NonNull
    @Override

    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleitem,
                parent,
                false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        SongData data = list.get(position);

        holder.title.setText(data.getTitle());
     //   holder.album.setText(data.getAlbum());
    }
    ///////////////////////////////////////////////////////////
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
       public TextView title,album , unique ;
        public Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.SongTitle);
            album = itemView.findViewById(R.id.songAlbum);
            ImageView img =  itemView.findViewById(R.id.nodeImage);
           // img.setBackgroundColor();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clicked = true ;
            TextView SongTitle = v.findViewById(R.id.SongTitle);

            SongTitle.setTypeface(null, Typeface.BOLD);

            /*for the Music_activity */

            onItemListner.OnItemClick(getAdapterPosition());
            /*setting a song title toa music second activity*/
            musicActivity.song_title = (String)SongTitle.getText() ;

            if(MainActivity.player != null )
            { name =(String)SongTitle.getText() ; }


            if(prevSongTitle!=null)
            {
                String p = (String) prevSongTitle.getText();
               String c = (String) SongTitle.getText();
                if(!p.equals(c))
                {
                    prevSongTitle.setTypeface(null,Typeface.NORMAL);
                    prevSongTitle = SongTitle ;

                }
            }
            if(prevSongTitle == null)
                prevSongTitle = SongTitle ;


        }
    }
    //////////////////////////////////////////////////////
    public  interface OnItemListner
    {
        void OnItemClick(int position);
    }
}
