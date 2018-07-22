package com.example.wjy.mynewapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Save extends AppCompatActivity {
    EditText textView ;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_save);
            textView =(EditText) findViewById(R.id.textView);

    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        SavaFile(textView.getText().toString());

    }
    public void SavaFile(String inputText)
    {
        FileOutputStream out=null;
        BufferedWriter writer =null;
        try {
            out =  openFileOutput("data", Context.MODE_PRIVATE);
            writer =new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        finally {
            if(writer != null)
            {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
