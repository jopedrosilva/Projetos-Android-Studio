package com.withconnection.musicplayer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private static final int REQUEST_PERMISSIONS = 12345;

    private static final int PERMISSIONS_COUNT = 1;


    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean arePermissionsDenied(){
        for (int i = 0; i < PERMISSIONS_COUNT; i++) {
            if (checkSelfPermission(PERMISSIONS[i]) != PackageManager.PERMISSION_GRANTED){
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantTesults){
        super.onRequestPermissionsResult(requestCode, permissions, grantTesults);
        if(arePermissionsDenied()){
            ((ActivityManager) (this.getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData();
            recreate();
        } else {
            onResume();
        }
    }

    private boolean isMusicPlayerInit;

    private List<String> musicFilesList;

    private void addMusicFilesFrom(String dirPath){
        final File musicDir = new File(dirPath);
        if(!musicDir.exists()){
            musicDir.mkdir();
            return;
        }
        final File[] files = musicDir.listFiles();
        for(File file: files){
            final String path = file.getAbsolutePath();
            if (path.endsWith(".mp3")){
                musicFilesList.add(path);
            }
        }
    }

    private void fillMusicList(){
        musicFilesList.clear();
        addMusicFilesFrom(String.valueOf(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MUSIC)));
        addMusicFilesFrom(String.valueOf(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS)));
    }

    private MediaPlayer mp;

    private int playMusicFile(String path){
        mp = new MediaPlayer();
        try {
            mp.setDataSource(path);
            mp.prepare();
            mp.start();
        } catch (Exception e){
            e.printStackTrace();
        }
        return mp.getDuration();
    }

    private int songPosition;

    private volatile boolean isSongPlaying;

    private int mPosition;

    private void playSong(){
        final String musicFilePath = musicFilesList.get(mPosition);
        final int songDuration = playMusicFile(musicFilePath)/1000;
        seekBar.setMax(songDuration);
        seekBar.setVisibility(View.VISIBLE);
        playbackControls.setVisibility(View.VISIBLE);
        songDurationTextView.setText(String.valueOf(songDuration/60) + ":"
                + String.valueOf(songDuration%60));
        isSongPlaying = true;
        new Thread(){
            public void run() {
                songPosition = 0;
                while (songPosition < songDuration){
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    if(isSongPlaying){
                        songPosition++;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                seekBar.setProgress(songPosition);
                                songPoritionTextView.setText(String.valueOf(songPosition/60) + ":"
                                        + String.valueOf((songPosition%60)/10) + ""
                                        + String.valueOf((songPosition%60)%10));
                            }
                        });
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mp.pause();
                        songPosition = 0;
                        mp.seekTo(songPosition);
                        songPoritionTextView.setText("0:00");
                        pauseButton.setText("play");
                        isSongPlaying = false;
                        seekBar.setProgress(songPosition);
                    }
                });
            }
        }.start();
    }

    private TextView songPoritionTextView;
    private TextView songDurationTextView;
    private SeekBar seekBar;
    private View playbackControls;
    private Button pauseButton;

    @Override
    protected void onResume(){
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && arePermissionsDenied()){
            requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS);
            return;
        }

        if (!isMusicPlayerInit){
            final ListView listView = findViewById(R.id.listView);
            final TextAdapter textAdapter = new TextAdapter();
            musicFilesList = new ArrayList<>();
            fillMusicList();
            textAdapter.setData(musicFilesList);
            listView.setAdapter(textAdapter);
            seekBar = findViewById(R.id.seekBar);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int songProgress;

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    songProgress = progress;
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    songPosition = songProgress;
                    mp.seekTo(songProgress);
                }
            });

            songPoritionTextView = findViewById(R.id.currentPosition);
            songDurationTextView = findViewById(R.id.songDuration);

            pauseButton = findViewById(R.id.pauseButton);

            playbackControls = findViewById(R.id.playBackButtons);

            pauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isSongPlaying) {
                        mp.pause();
                        pauseButton.setText("play");
                    } else {
                        if (songPosition==0){
                            playSong();
                        } else {
                            mp.start();
                        }
                        pauseButton.setText("pause");
                    }
                    isSongPlaying = !isSongPlaying;
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mPosition = position;
                    playSong();
                }
            });

            isMusicPlayerInit = true;
        }
    }

    class TextAdapter extends BaseAdapter{

        private List<String> data = new ArrayList<>();

        void setData(List<String> mData){
            data.clear();
            data.addAll(mData);
            notifyDataSetChanged();
        }

        @Override
        public int getCount(){
            return data.size();
        }

        @Override
        public String getItem(int position){
            return null;
        }

        @Override
        public long getItemId(int position){
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if (convertView == null){
                convertView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.item, parent, false);
                convertView.setTag(new ViewHolder((TextView) convertView.findViewById(R.id.myItem)));
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            final String item = data.get(position);
            holder.info.setText(item.substring(item.lastIndexOf('/')+1));
            return convertView;
        }

        class ViewHolder{
            TextView info;

            ViewHolder(TextView mInfo){
                info = mInfo;
            }
        }
    }

}