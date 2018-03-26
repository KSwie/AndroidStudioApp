package com.example.kamil.aplikacja;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.util.Scanner;

public class NoteShowContent extends AppCompatActivity {

    private String path = Environment.getExternalStorageDirectory().toString() + "/Aplikacja/";
    TextView textView;
    ListOfNoteActivity listOfNoteActivity = new ListOfNoteActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_show_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String fileName = intent.getStringExtra("nazwa");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView = findViewById(R.id.noteShowTextView);


        System.out.println(fileName);
        textView.setText(showContent(fileName));
    }


    private String showContent(String nazwa){

        String zawartosc = "";

        try{
            FileInputStream fis = new FileInputStream(path+nazwa);
            Scanner scanner = new Scanner(fis);

            while(scanner.hasNext()){
                zawartosc += scanner.nextLine();
            }

            fis.close();
            scanner.close();

            return zawartosc;
        }
        catch (Exception e){
            Toast.makeText(NoteShowContent.this,e.toString(),Toast.LENGTH_LONG).show();
            return null;
        }
    }
}
