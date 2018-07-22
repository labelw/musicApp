package com.example.wjy.mynewapplication;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Network extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        new Thread (Network).start();
    }
    Runnable Network =new Runnable() {
        @Override
        public void run() {
            try {
                Log.d("mylog",Environment.getExternalStorageDirectory()+"wjy1.mp3"+"--------------------------------------------------");
                InetAddress IP = InetAddress.getByName("WJY-PC");
                Log.d("WJY",IP.getHostAddress()+IP.getHostName()+"--------------------------------------------------");

                Socket s =new Socket("172.24.75.2",10010);
                byte[] b ="test connect".getBytes();
                OutputStream out =s.getOutputStream();
                out.write(b);
                s.shutdownOutput();
                InputStream in =s.getInputStream();
                File f =new File("/storage/emulated/0/wjy1.mp3");
                OutputStream out1 =new FileOutputStream(f);
                byte[] b1 =new byte[1024];
                int len =0;
                while((len=in.read(b1))!=-1)
                {
                    out1.write(b1,0,len);

                }

                out1.close();
                in.close();
                out.close();
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    };
}
