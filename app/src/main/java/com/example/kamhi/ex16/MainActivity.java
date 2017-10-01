package com.example.kamhi.ex16;

import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    TextView TVcounter;
    final int  MAX_VALUE = 1000;
    int counter;
    final int MAX_WORKERS = 1;
    final int UP = 1;
    final int DOWN = -1;
    final Object mutex = new Object();
    boolean go = false;
    final Object goLock = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private class Worker extends Thread{
        int maxValue;
        int direction;
        boolean synched;

        public Worker(int maxValue, int direction, boolean synched){
            this.maxValue = maxValue;
            this.direction = direction;
            this.synched = synched;
        }

        @Override
        public void run() {
            long time = 1;
            int i = 0;
            TVcounter = (TextView)findViewById(R.id.coloredTV);
                try {
                    synchronized (goLock) {
                        while (!go){
                            goLock.wait();
                        }
                    }
                    for (i = 0; i < this.maxValue; i++) {
                        if (synched) {
                            synchronized (mutex) {
                                updateCounter(direction);
                            }
                        } else {
                            updateCounter(direction);
                        }
                        Thread.sleep(time);
                    }
                    final int finali = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TVcounter.setText(Integer.toString(counter));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    private void updateCounter(int direction){
        int temp = counter;
        temp = temp + direction;
        counter = temp;
    }

    public void startWorkers(View view){
        synchronized (goLock) {
            counter = 0;
            go = false;

            for (int i = 0; i < MAX_WORKERS; i++) {
            //    new Worker(MAX_VALUE, i % 2 == 0 ? UP : DOWN, view.getId() == R.id.buttonSync).start();
                new Worker2().execute(MAX_VALUE, UP);
            }

            go = true;
            goLock.notifyAll();
        }
    }

    private class Worker2 extends AsyncTask<Integer, Integer, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... params) {
            long time = 100;
            int i = 0;
            TVcounter = (TextView)findViewById(R.id.coloredTV);
            try {
                synchronized (goLock) {
                    while (!go){
                        goLock.wait();
                    }
                }
                for (i = 0; i < params[0]; i++) {
                    Thread.sleep(time);
                    synchronized (mutex) {
                        updateCounter(params[1]);
                        }
                    publishProgress(counter);
                }
            } catch (Exception e) {
                return null;
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            TVcounter.setText(Integer.toString(values[0]));
        }

    }
}

