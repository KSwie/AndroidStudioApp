package com.example.kamil.aplikacja;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class PrimeNumberActivity extends AppCompatActivity {

    EditText whichPrime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prime_number);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        whichPrime = findViewById(R.id.primeNumberEditText);
    }

    private boolean isPrime(int n){
        for(int i=2;i<n;i++)
            if(n%i == 0)
                return false;
        return true;
    }

    public void primeNumberClick(View view) {
        int which = Integer.parseInt(whichPrime.getText().toString());
        int m = 0;
        int candidate = 1;

        while(m<=which){
            if(isPrime(candidate)){
                m++;
                candidate++;
            }
            else{
                candidate++;
            }

        }

        candidate--;
        String result = which + " -> " + candidate;
        whichPrime.setText(result);
    }
}
