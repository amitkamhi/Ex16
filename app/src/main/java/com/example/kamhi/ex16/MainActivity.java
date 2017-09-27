package com.example.kamhi.ex16;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    TextView counter;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Worker work = new Worker(100000);
        work.start();
    }

    private class Worker extends Thread{
        int maxValue;

        public Worker(int maxValue){
            this.maxValue = maxValue;
        }

        @Override
        public void run() {
            super.run();
            long time = 1;
            i = 0;
            counter = (TextView)findViewById(R.id.coloredTV);
            for(i = 0; i<=this.maxValue; i++){
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final int finali = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        counter.setText(Integer.toString(finali));
                    }
                });
            }
        }
    }
}

