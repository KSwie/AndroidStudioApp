package com.example.kamil.aplikacja;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TestActivity.this, NoteActivity.class);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void listaNotatek(View view) {
        Intent intent = new Intent(TestActivity.this,ListOfNoteActivity.class);

        startActivity(intent);
    }

    public void countdownClick(View view) {
        Intent intent =  new Intent(TestActivity.this,CountdownActivity.class);
        startActivity(intent);
    }


    public void primeNumber(View view) {
        Intent intent =  new Intent(TestActivity.this,PrimeNumberActivity.class);
        startActivity(intent);
    }

    public void levelActivityClick(View view) {
        Intent intent =  new Intent(TestActivity.this,LevelActivity.class);
        startActivity(intent);
    }

    public void drawActivityClick(View view) {
        Intent intent =  new Intent(TestActivity.this,DrawActivity.class);
        startActivity(intent);
    }

    public void takePhotoClick(View view) {
        Intent intent =  new Intent(TestActivity.this,CameraActivity.class);
        startActivity(intent);
    }
}
