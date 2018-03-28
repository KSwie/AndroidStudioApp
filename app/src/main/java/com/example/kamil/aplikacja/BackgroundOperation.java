package com.example.kamil.aplikacja;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

public class BackgroundOperation extends AsyncTask<Void, Integer, String> {

    Activity activity;
    ProgressBar progressBar;
    EditText whichPrime;

    public BackgroundOperation(Activity activity){
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = activity.findViewById(R.id.progressBarPrimeNumber);
        whichPrime = activity.findViewById(R.id.primeNumberEditText);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(Void... params) {
        return primeNumCalculate();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        progressBar.setProgress(values[0]);
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private boolean isPrime(int n){
        for(int i=2;i<n;i++)
            if(n%i == 0)
                return false;
        return true;
    }

    private String primeNumCalculate(){
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
        Snackbar.make(activity.getCurrentFocus(),result,Snackbar.LENGTH_INDEFINITE).show();
        return result;
    }
}
