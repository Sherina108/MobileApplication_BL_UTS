package umn.ac.id.sherinachandra_uts;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Activity_Songs extends AppCompatActivity {
    Bundle songExtraData;
    ImageView prev, play, next;
    int position;
    SeekBar mSeekBarTime;
    static MediaPlayer mMediaPlayer;
    TextView songName;
    ArrayList<File> musicList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__songs);
        prev = findViewById(R.id.prev);
        play = findViewById(R.id.play);
        next = findViewById(R.id.next);
        mSeekBarTime = findViewById(R.id.seekBarTime);
        songName = findViewById(R.id.songTitle);

        if(mMediaPlayer != null){
            mMediaPlayer.stop();
        }

        Intent intent = getIntent();
        songExtraData = intent.getExtras();

        musicList = (ArrayList)songExtraData.getParcelableArrayList("songList");
        position = songExtraData.getInt("position", 0);

        initializeMusicPlayer(position);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position < musicList.size() -1){
                    position++;
                }
                else {
                    position = 0;
                }
                initializeMusicPlayer(position);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position<=0){
                    position = musicList.size();
                }
                else {
                    position++;
                }

                initializeMusicPlayer(position);
             }
        });
    }

    private void initializeMusicPlayer(int position) {
        if(mMediaPlayer!=null && mMediaPlayer.isPlaying()){
            mMediaPlayer.reset();
        }

        String name = musicList.get(position).getName();
        songName.setText(name);

        Uri uri = Uri.parse(musicList.get(position).toString());

        mMediaPlayer = MediaPlayer.create(this, uri);

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mSeekBarTime.setMax(mMediaPlayer.getDuration());
                play.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                mMediaPlayer.start();
            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                play.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
            }
        });

        mSeekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mSeekBarTime.setProgress(progress);
                    mMediaPlayer.seekTo(progress);
                }
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
                while (mMediaPlayer != null){
                    try {
                        if(mMediaPlayer.isPlaying()){
                            Message message = new Message();
                            message.what = mMediaPlayer.getCurrentPosition();
                            handler.sendMessage(message);
                            Thread.sleep(1000);
                        }
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @SuppressLint("Handler Leak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mSeekBarTime.setProgress(msg.what);
        }
    };

    private void play(){
        if (mMediaPlayer!=null && mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
            play.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
        }
        else {
            mMediaPlayer.start();
            play.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
        }
    }
}