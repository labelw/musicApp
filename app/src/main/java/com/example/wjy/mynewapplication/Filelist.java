package com.example.wjy.mynewapplication;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.regex.*;
import java.util.List;
import java.io.*;
import java.util.ArrayList;;

public class Filelist extends AppCompatActivity {
    String regex=".*\\.mp3";
    List<String> list =new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filelist);
        File path =new File(String.valueOf(Environment.getExternalStorageDirectory()));
        ListFile(path,list,null);
        for(String r:list)
            Log.d("wjy", "onCreate: -----------------------------------"+r);
        Log.d("wjy", "onCreate: -----------------------------------共有文件："+list.size());

    }
    public static void  ListFile(File f,List<String> list,String regex)
    {

        if(f.isDirectory())
        {
            File[] a=f.listFiles();
            for(File b:a)
            {
                ListFile(b, list,regex);
            }
        }
        if(f.isFile())
        {
            if(regex==null)
            {
                list.add(f.getAbsolutePath());
            }
            else
            {
                if(f.getAbsolutePath().matches(regex))
                    list.add(f.getAbsolutePath());
            }

        }

    }
}
