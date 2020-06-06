package com.tabhi.atmusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    ListView songsList;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        songsList=(ListView)findViewById(R.id.songsListView);
        runtimePermission();
    }

    public void runtimePermission()
    {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displaySongs();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    public ArrayList<File> findSongs(File file)
    {
        ArrayList<File> arrayList =new ArrayList<>();

        File[] file1 = file.listFiles();

        for(File singleFile:file1){
            if(singleFile.isDirectory())
            {
                arrayList.addAll(findSongs(singleFile));
            }
            else
            {
                if(singleFile.getName().endsWith(".mp3"))
                {
                    arrayList.add(singleFile);
                }
            }
        }
    return arrayList;
    }

    public void displaySongs()
    {
        final ArrayList<File> songs = findSongs(Environment.getExternalStorageDirectory());

        items = new String[songs.size()];

        for(int i=0;i<songs.size();i++) {
                items[i]=songs.get(i).getName().toString().replace(".mp3","");

        }
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(Home.this,android.R.layout.simple_list_item_1,items);
        songsList.setAdapter(arrayAdapter);



        songsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String songname=songsList.getItemAtPosition(position).toString();
                startActivity(new Intent(Home.this,Player.class)
                .putExtra("song",songs).putExtra("songname",songname).putExtra("posi",position));

            }
        });
    }
}