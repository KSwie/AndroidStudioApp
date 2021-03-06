package com.example.kamil.aplikacja;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class NoteActivity extends AppCompatActivity{

    EditText et;
    private String path = Environment.getExternalStorageDirectory().toString() + "/Aplikacja";
    private final int memoryAccess = 5;
    private EditText fileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("aktywnosc","onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        et = findViewById(R.id.editText);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(NoteActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(NoteActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, memoryAccess);
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case memoryAccess:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
                else{
                    Toast.makeText(getApplicationContext(),"Musi zostać wyrażona zgoda na dostęp do pamięci!",Toast.LENGTH_LONG).show();//wyswietlenie dymku z informacja
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_save){
            final AlertDialog alertDialog = new AlertDialog.Builder(NoteActivity.this).create();
            alertDialog.setMessage("Podaj nazwe pliku:");
            LayoutInflater inflater = NoteActivity.this.getLayoutInflater();
            alertDialog.setView(inflater.inflate(R.layout.filename_dialog_window, null));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Zapisz",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            fileName = alertDialog.findViewById(R.id.fileNameDialogText);
                            createDir();
                            createFile(fileName.getText().toString());
                            finish();
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onRestart() {
        Log.d("aktywnosc","onStart");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.d("aktywnosc","onResume");
        super.onResume();
    }

    @Override
    protected void onStart() {
        Log.d("aktywnosc","onStart");
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        Log.d("aktywnosc","onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.d("aktywnosc","onPause");
        et.getText().toString(); //po obroceniu ekranu wpisany tekst nie jest usuwany
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("aktywnosc","onStop");
        super.onStop();
    }

    protected void createDir(){
        File folder = new File(path);
        if(!folder.exists()){
            try{
                folder.mkdir();
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();//wyswietlenie dymku z informacja
            }
        }
    }


    protected void createFile(String nazwa){
        File file = new File(path+"/"+nazwa+".txt");
        FileOutputStream fos;
        OutputStreamWriter outWriter;

        try{
            fos = new FileOutputStream(file);
            outWriter = new OutputStreamWriter(fos);
            outWriter.append(et.getText());
            outWriter.close();
            fos.close();

            Toast.makeText(getApplicationContext(),"Zapisano nowy plik!",Toast.LENGTH_LONG).show();//wyswietlenie dymku z informacja
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();//wyswietlenie dymku z informacja
        }
    }
}
