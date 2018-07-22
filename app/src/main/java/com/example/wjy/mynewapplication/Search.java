package com.example.wjy.mynewapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import test.Music;

public class Search extends AppCompatActivity {

    String searchString=null;
    Button searchButton;
    EditText searchEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchButton=(Button)findViewById(R.id.searchButton);
        searchEdit=(EditText)findViewById(R.id.searchEditText);
        Intent intent1 =getIntent();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchString=searchEdit.getText().toString().trim();

                if(intent1.getStringExtra("islocal").equals("local"))
                {
                    Intent intent =new Intent(Search.this,MusicList.class);
                    intent.putExtra("IndexSelect",searchString);
                    startActivity(intent);

                }
                else
                {
                    Intent intent =new Intent(Search.this,SearchRt.class);
                    intent.putExtra("Search",searchString);
                    startActivity(intent);
                }



            }
        });



    }
}
