package com.example.kamil.aplikacja;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.common.io.Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.Scanner;

public class ListOfNoteActivity extends AppCompatActivity {
    private ListView listview;
    private String path = Environment.getExternalStorageDirectory().toString() + "/Aplikacja/";
    private String nazwaPliku = "";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_list_of_note);
        listview = (ListView)findViewById(R.id.listView);
        String[] values = wyswietlNotatki();
        listview.setAdapter(new ArrayAdapter<>(ListOfNoteActivity.this,
                android.R.layout.simple_list_item_1, values));

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                nazwaPliku = parent.getItemAtPosition(position).toString();
                Intent intent = new Intent(ListOfNoteActivity.this,NoteShowContent.class);
                intent.putExtra("nazwa",nazwaPliku);
                startActivity(intent);
            }
        });

       // System.out.println("tekst: "+tmp);
    }

    public String getNazwaPliku(){
        return this.nazwaPliku;
    }

    protected String[] wyswietlNotatki(){
        try{
            File file = new File(path);
            String[] fileName = file.list(new FilenameFilter() {
                @Override
                public boolean accept(File file, String name) {
                    return name.endsWith(".txt");
                }
            });

            return fileName;
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            return null;
        }
    }



}
