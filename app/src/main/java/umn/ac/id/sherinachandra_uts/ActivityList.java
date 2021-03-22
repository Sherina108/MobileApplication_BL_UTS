package umn.ac.id.sherinachandra_uts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class ActivityList extends AppCompatActivity {
    ListView allMusicList;
    ArrayAdapter<String> musicArrayAdapter;
    String songs[];
    ArrayList<File> musics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        popUpMenu();
        allMusicList = findViewById(R.id.listView);

        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                musics = findMusicFiles(Environment.getExternalStorageDirectory());
                songs = new String[musics.size()];
                for(int i = 0; i < musics.size(); i++){
                    songs[i] = musics.get(i).getName();
                }

                musicArrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, R.id.listView, songs);

                allMusicList.setAdapter(musicArrayAdapter);

                allMusicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        startActivity(new Intent(ActivityList.this, Activity_Songs.class)
                                .putExtra("SongList", musics)
                                .putExtra("position", position));
                    }
                });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.btnProfil2:
                Intent intentProfil = new Intent(ActivityList.this, Activity_Profile.class);
                if (intentProfil.resolveActivity(getPackageManager()) != null) {
                    startActivity(intentProfil);
                }
                return true;
            case R.id.btnLogout:
                Intent intentLogout = new Intent(ActivityList.this, Activity_Login.class);
                startActivityForResult(intentLogout, 1);
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void popUpMenu() {
        new AlertDialog.Builder(this).setTitle("Selamat Datang").setMessage("Sherina Chandra - 00000027163")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int click) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    private ArrayList<File> findMusicFiles(File file){
        ArrayList<File> musicfileobject = new ArrayList<>();
        File[] files = file.listFiles();

        for(File currentFiles: files){
            if(currentFiles.isDirectory() && !currentFiles.isHidden()){
                musicfileobject.addAll(findMusicFiles(currentFiles));
            }else {
                if(currentFiles.getName().endsWith(".mp3")){
                    musicfileobject.add(currentFiles);
                }
            }
        }
        return musicfileobject;
    }
}