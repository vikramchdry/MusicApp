package com.example.musicplayerapp;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    ListView songsList;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songsList = (ListView)findViewById(R.id.songList);
        runtimePermission();

    }

    public void runtimePermission(){

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        display();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();

                    }
                })
                .check();

    }

   public ArrayList<File> findsongs(File file){

        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        for (File singleFile: files){
            if (singleFile.isDirectory() && !singleFile.isHidden()){
                arrayList.addAll(findsongs(singleFile));
            }
            else {
                if (singleFile.getName().endsWith(".mp3") ||
                singleFile.getName().endsWith(".wav")){
                    arrayList.add(singleFile);
                }
            }

        }
        return arrayList;
   }



   void display(){
        final ArrayList<File> mySongs = findsongs(Environment.getExternalStorageDirectory());
        items = new String[mySongs.size()];

        for (int i = 0; i<mySongs.size(); i++){
            items[i] = mySongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
        }
       ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        songsList.setAdapter(myAdapter);


        songsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String songName = songsList.getItemAtPosition(i).toString();
                startActivity(new Intent(getApplicationContext(),Player.class).putExtra("songs", mySongs).putExtra("songname",songName)
                        .putExtra("pos",i));
            }

        });


   }
    }
