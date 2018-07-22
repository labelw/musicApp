package com.example.wjy.mynewapplication;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class Progress extends AppCompatActivity {


    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        progressBar =(ProgressBar)findViewById(R.id.progressBar2);
        progressBar.setMax(100);
        new Thread(runnable).start();





    }

    Runnable runnable =new Runnable(){
        @Override
        public void run() {

            for( int i=0;i<=100;i++)
            {
                try {
                    Message message =new Message();
                    Thread.sleep(1000);
                    message.arg1=i;
                    handler.sendMessage(message);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }


        }
    };
    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int i = msg.arg1;
            progressBar.setProgress(i);
        }
    };

}
