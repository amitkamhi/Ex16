package com.example.kamhi.ex16;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    TextView TVcounter;
    final int  MAX_VALUE = 1000;
    int counter;
    final int MAX_WORKERS = 2;
    final int UP = 1;
    final int DOWN = -1;
    final Object mutex = new Object();

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
            for(i = 0; i<this.maxValue; i++){
                try {
                    if (synched){
                        synchronized (mutex){
                            updateCounter(direction);
                        }
                    }
                    else{
                        updateCounter(direction);
                    }
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final int finali = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TVcounter.setText(Integer.toString(counter));
                    }
                });
            }
        }
    }

    private void updateCounter(int direction){
        int temp = counter;
        temp = temp + direction;
        counter = temp;
    }

    public void startWorkers(View view){
        counter = 0;
        for (int i = 0; i < MAX_WORKERS; i++){
            new Worker(MAX_VALUE, i%2==0?UP:DOWN, view.getId()==R.id.buttonSync).start();
        }
    }

}

