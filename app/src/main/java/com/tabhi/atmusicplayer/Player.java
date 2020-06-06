package com.tabhi.atmusicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileObserver;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity {

    Button pause,forword,back;
    SeekBar songBar;
    TextView songName;

    MediaPlayer mediaPlayer;
    Thread upseekbar;
    int position;
    ArrayList<File> Songs;
    String Snm;


    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     *
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        pause=(Button)findViewById(R.id.btnPause);
        forword=(Button)findViewById(R.id.btnforword);
        back=(Button)findViewById(R.id.btnBack);

        songBar=(SeekBar)findViewById(R.id.seekBar);

        songName=(TextView)findViewById(R.id.songName);

        getSupportActionBar().setTitle("AT Music Player");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        upseekbar = new Thread()
        {
            @Override
            public void run() {
                int totalDuration=mediaPlayer.getDuration();
                int currentPosition=0;

                while(currentPosition<totalDuration)
                {
                    try {
                        sleep(500);
                        currentPosition=mediaPlayer.getCurrentPosition();
                        songBar.setProgress(currentPosition);
                    }
                    catch (InterruptedException error)
                    {
                        error.printStackTrace();
                    }
                }
            }
        };

        if(mediaPlayer!=null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle bundle =i.getExtras();

        Songs = (ArrayList) bundle.getParcelableArrayList("song");

        Snm=Songs.get(position).getName().toString();
        String sn=i.getStringExtra("songname");

        songName.setText(sn);
        songName.setSelected(true);

        position=bundle.getInt("posi",0);

        Uri u=Uri.parse(Songs.get(position).toString());

        mediaPlayer=MediaPlayer.create(Player.this,u);

        mediaPlayer.start();
        songBar.setMax(mediaPlayer.getDuration());
        upseekbar.start();

        songBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songBar.setMax(mediaPlayer.getDuration());

                if(mediaPlayer.isPlaying())
                {
                    pause.setBackgroundResource(R.drawable.play);
                    mediaPlayer.pause();
                }
                else {
                    pause.setBackgroundResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });

        forword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();

                position=((position+1)%Songs.size());

                Uri u=Uri.parse(Songs.get(position).toString());
                mediaPlayer=MediaPlayer.create(Player.this,u);

                Snm=Songs.get(position).getName().toString();
                songName.setText(Snm);

                mediaPlayer.start();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.stop();
                mediaPlayer.release();

                position=((position-1)<0)?(Songs.size()-1):(position-1);

                Uri u=Uri.parse(Songs.get(position).toString());
                mediaPlayer=MediaPlayer.create(Player.this,u);

                Snm=Songs.get(position).getName().toString();
                songName.setText(Snm);

                mediaPlayer.start();

            }
        });
    }
}