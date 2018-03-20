package com.example.kamil.aplikacja;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.charset.Charset;

public class NotatkiActivity extends AppCompatActivity {

    private TextView notatki;
    private String path = Environment.getExternalStorageDirectory().toString() + "/Aplikacja/";
    String przerywnik = "\n\n\n/////------/////-----/////\n\n\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notatki);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        notatki = findViewById(R.id.notatkiView);
        notatki.setText(getAllContent());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private String getAllContent(){
        try{
            File file = new File(path);
            String[] path1 = file.list(new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    return s.endsWith(".txt");
                }
            });

            StringBuilder sb = new StringBuilder();
            for (int i=0;i<path1.length;i++){
                sb.append(Files.toString(new File(path+path1[i]), Charsets.UTF_8));
                sb.append(przerywnik);
            }
            return sb.toString();
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            return "";
        }


    }

}
