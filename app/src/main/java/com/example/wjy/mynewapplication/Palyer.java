package com.example.wjy.mynewapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import com.example.wjy.mynewapplication.mypackage.*;

import test.Music;
import java.io.*;
import java.net.*;
import java.io.File;
import java.io.IOException;

public class Palyer extends AppCompatActivity implements View.OnClickListener {

    private Button paly, pause, reset,lovebt;
    private SeekBar progressBar;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private static final int READ_EXTERNAL_STORAGE_CODE = 1;
    private int length = 0;
    private boolean IsDwon = false;
    private Music music;
    private ImageView singerpic;
    Thread thread;
    boolean isStop = true;
    boolean isnetwork = false;
    private Music[] musics;
    int position;
    File musicpath;
    ProgressDialog waitDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        singerpic=(ImageView)findViewById(R.id.singerpic);
        singerpic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
               if(!isnetwork)
               {
                   Music music =musics[position];
                   SQLoperation sqLoperation =new SQLoperation(null,Palyer.this,1);
                   SQLiteDatabase database=sqLoperation.GetDatabase();
                   database.execSQL("insert into LoveMusic(name,singer,path) Values (?,?,?)",new String[]{music.getName(),music.getSinger(),music.getPath()});
                   Toast.makeText(Palyer.this, "收藏成功", Toast.LENGTH_SHORT).show();
                   return true;
               }
                else
                   return false;
            }
        });
        progressBar = (SeekBar) findViewById(R.id.seekbar);
        paly = (Button) findViewById(R.id.paly);
        pause = (Button) findViewById(R.id.pause);
        reset = (Button) findViewById(R.id.reset);
        paly.setOnClickListener(this);
        pause.setOnClickListener(this);
        reset.setOnClickListener(this);
        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


                if (mediaPlayer.isPlaying()) mediaPlayer.seekTo(seekBar.getProgress() * 1000);


            }
        });

        //new Thread(networkTask).start();
        waitDialog =new ProgressDialog(Palyer.this);
        waitDialog.setTitle("请稍后-------");
        waitDialog.setMessage("等待中.....");
        waitDialog.setIndeterminate(true);
        waitDialog.setCancelable(false);
        Intent intent = getIntent();
        thread = new Thread(move);
        position = intent.getIntExtra("position", 0);
        musics = (Music[])intent.getSerializableExtra("Music");
        music = musics[position];
        /*try
        {
            File f1 =new File("/storage/2EF1-C8AF/MusicList/music/");
            if(!f1.exists())f1.mkdirs();
            File f2 =new File("/storage/2EF1-C8AF/singerpic/pic/");
            if(!f2.exists())f2.mkdirs();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/
        //Log.d("wjy", "onCreate: ----------------------------------------------------------------------------"+intent.getStringExtra("music") );
        if (intent.getStringExtra("music").equals("Index") || intent.getStringExtra("music").equals("Search")) {

            // Log.d("wjy", "onCreate: ----------------------------------------------------------------------------"+music.getPath());
            isnetwork = true;
            new Thread(getPic).start();
            waitDialog.show();

        } else {
            isnetwork = false;
            initMediaPlayer();
            if (!mediaPlayer.isPlaying()) {
                try {
                    mediaPlayer.start();
                    if (mediaPlayer != null) progressBar.setMax(mediaPlayer.getDuration() / 1000);
                    //////
                    thread.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
        setTitle(music.getName().replaceAll(".mp3", ""));
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                isStop=false;
                mediaPlayer.reset();
                if(position<musics.length)position=position+1;
                music=musics[position];
                if(isnetwork)
                {
                    waitDialog.show();
                    new Thread(networkTask).start();
                }
                else
                {
                    initMediaPlayer();
                    if (!mediaPlayer.isPlaying())
                    {
                        try
                        {
                            mediaPlayer.start();
                            if(mediaPlayer!=null)progressBar.setMax(mediaPlayer.getDuration()/1000);
                            //////
                            isStop=true;
                            new Thread(move).start();

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }

                setTitle(music.getName().replaceAll(".mp3",""));

            }
        });





    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            /*Bundle data = msg.getData();//数据对象
            String val = data.getString("value");
            Log.i("mylog", "请求结果为-->" + val);
            // TODO*/
            // UI界面的更新等相关操作
            int position = msg.arg1;
            progressBar.setProgress(position);


        }
    };
    Handler handler2 = new Handler()
    {
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
            /*Bundle data = msg.getData();//数据对象
            String val = data.getString("value");
            Log.i("mylog", "请求结果为-->" + val);
            // TODO*/
        // UI界面的更新等相关操
        Bundle bundle =msg.getData();
        String singeradd=bundle.getString("picaddress");
        File f=new File(singeradd);
        if(f.length()>0)
        {
            singerpic.setBackgroundResource(0);
            singerpic.setImageDrawable(Drawable.createFromPath(singeradd));
        }
        else
        {
            f.delete();
            singerpic.setBackgroundResource(0);
            singerpic.setImageResource(R.drawable.cd);
        }




    }
};

    Handler handler3 = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            /*Bundle data = msg.getData();//数据对象
            String val = data.getString("value");
            Log.i("mylog", "请求结果为-->" + val);
            // TODO*/
            // UI界面的更新等相关操
          if(msg.arg1==1)
          {
              waitDialog.cancel();
              music.setPath(musicpath.getPath());
              try
              {   SQLoperation sqLoperation =new SQLoperation(null,Palyer.this,1);
                  SQLiteDatabase database=sqLoperation.GetDatabase();
                  database.execSQL("insert into MusicMessage(name,singer,path) Values (?,?,?)",new String[]{music.getName(),music.getSinger(),music.getPath()});
                  database.execSQL("insert into RecentlyMusic(name,singer,path) Values (?,?,?)",new String[]{music.getName(),music.getSinger(),music.getPath()});
              }
              catch (Exception e)
              {
                  e.printStackTrace();
              }

          }







        }
    };

    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            try {

                Socket s =new Socket(GETIp.getip(),10010);
                //s.connect(s.getLocalSocketAddress());
                byte[] b =new byte[1024];
                OutputStream outputStream =s.getOutputStream();
                outputStream.write("Music".getBytes());
                InputStream inputStream =s.getInputStream();
                inputStream.read(b);
                if(new String(b).trim().equals("address"))
                {
                    outputStream=s.getOutputStream();
                    outputStream.write(music.getPath().getBytes());
                }

                    Log.d("wjy", "run: "+Environment.getExternalStorageDirectory()+"/MusicList/"+music.getSinger().trim()+" - "+music.getName().trim());
                    musicpath =new File(Environment.getExternalStorageDirectory()+"/"+music.getSinger().trim()+" - "+music.getName().trim());
                //File f =new File("/storage/sdcard0/"+music.getSinger().trim()+" - "+music.getName().trim());
                    OutputStream out =new FileOutputStream(musicpath);
                    inputStream=s.getInputStream();
                    int len=0;
                    while ((len=inputStream.read(b))!=-1)
                    {
                        out.write(b,0,len);
                        out.flush();
                    }
                    out.flush();
                    out.close();


                s.close();


            } catch (IOException e) {
                e.printStackTrace();
            }


            initMediaPlayer();
            if (!mediaPlayer.isPlaying())//播放
            {
                try
                {
                    mediaPlayer.start();
                    if(mediaPlayer!=null)progressBar.setMax(mediaPlayer.getDuration()/1000);
                    //////
                    isStop=true;
                   new Thread(move).start();

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }


            Message message =new Message();
            message.arg1=1;
            handler3.sendMessage(message);




            //music.setPath("/storage/2EF1-C8AF/"+music.getSinger().trim()+" - "+music.getName().trim());







           /* Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", "请求结果");
            msg.setData(data);
            handler.sendMessage(msg);*/
        }
    };
    /*
    获取图片
     */
    Runnable getPic =new Runnable() {

        File f;
        @Override
        public void run() {
            try {
                Socket s =new Socket(GETIp.getip(),10010);
                //s.connect(s.getLocalSocketAddress());
                byte[] b =new byte[1024];
                OutputStream outputStream =s.getOutputStream();
                outputStream.write("singerpic".getBytes());
                InputStream inputStream =s.getInputStream();
                inputStream.read(b);
                if(new String(b).trim().equals("address"))
                {
                    outputStream=s.getOutputStream();
                    outputStream.write(("D:\\垃圾文件夹\\"+music.getSinger()+".jpg").getBytes());
                }
                 //"/storage/2EF1-C8AF/singerpic/"

                 f =new File(Environment.getExternalStorageDirectory()+"/"+music.getSinger()+".jpg");

                //File f =new File("/storage/sdcard0/"+music.getSinger().trim()+" - "+music.getName().trim());
                OutputStream out =new FileOutputStream(f);
                inputStream=s.getInputStream();
                int len=0;
                while ((len=inputStream.read(b))!=-1)
                {
                    out.write(b,0,len);
                    out.flush();
                }
                out.flush();
                out.close();
                s.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
            Thread network = new Thread(networkTask);
            network.start();
            Message message =new Message();
            Bundle bundle =new Bundle();
            bundle.putString("picaddress",f.getAbsolutePath());
            message.setData(bundle);
            handler2.sendMessage(message);
        }
    };

    private void initMediaPlayer()
    {
        try
        {
            //File file =new File(Environment.getExternalStorageDirectory(),"wjy1.mp3");


            //Log.d("wjy",musicpath.getAbsolutePath());
            //Thread.sleep(5000);
            File f =new File(music.getPath());
            if(!f.exists())
            {
                mediaPlayer.setDataSource(musicpath.getAbsolutePath());
            }
            //mediaPlayer.setDataSource(music.getPath());
            else
            {
                mediaPlayer.setDataSource(music.getPath());

            }
            mediaPlayer.prepare();





        }
        catch(Exception e)
        {

            e.printStackTrace();
        }

    }
    Runnable move =new Runnable() {
        @Override
        public void run() {


            while (isStop)//mediaPlayer.getDuration()-mediaPlayer.getCurrentPosition()>500
            {
                int position=mediaPlayer.getCurrentPosition()/1000;
                /*try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                Message message =new Message();
                message.arg1=position;
                handler.sendMessage(message);
            }
        }
    };
    @Override
    public void onClick(View v)
    {
        File f;
        SQLoperation sqLoperation =new SQLoperation(null,Palyer.this,1);
        SQLiteDatabase database =sqLoperation.GetDatabase();
        switch (v.getId())
        {
            case R.id.paly:
                isStop=false;
                mediaPlayer.reset();
                position=position-1;
                music=musics[position];
                f =new File(music.getPath());
                if(!f.exists())
                {

                    new Thread(getPic).start();
                    //new Thread(networkTask).start();
                    waitDialog.show();

                }
                else
                {
                    initMediaPlayer();
                    if (!mediaPlayer.isPlaying())
                    {
                        try
                        {
                            mediaPlayer.start();
                            if(mediaPlayer!=null)progressBar.setMax(mediaPlayer.getDuration()/1000);
                            //////
                            isStop=true;
                            new Thread(move).start();

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        database.execSQL("insert into RecentlyMusic(name,singer,path) Values (?,?,?)",new String[]{music.getName(),music.getSinger(),music.getPath()});
                    }

                }

                setTitle(music.getName().replaceAll(".mp3",""));


                break;
            case R.id.pause:
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    pause.setBackgroundResource(R.drawable.play);
                }
                else {
                    mediaPlayer.start();
                    pause.setBackgroundResource(R.drawable.pause);

                }

                break;
            case R.id.reset:
                isStop=false;
                mediaPlayer.reset();
                if(position<musics.length)position=position+1;
                music=musics[position];
                f=new File(music.getPath());
                if(!f.exists())
                {

                    new Thread(getPic).start();
                    //new Thread(networkTask).start();
                    waitDialog.show();
                }
                else
                {
                    initMediaPlayer();
                    if (!mediaPlayer.isPlaying())
                    {
                        try
                        {
                            mediaPlayer.start();
                            if(mediaPlayer!=null)progressBar.setMax(mediaPlayer.getDuration()/1000);
                            //////
                            isStop=true;
                            new Thread(move).start();

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        database.execSQL("insert into RecentlyMusic(name,singer,path) Values (?,?,?)",new String[]{music.getName(),music.getSinger(),music.getPath()});

                    }

                }

                setTitle(music.getName().replaceAll(".mp3",""));



                break;
            default:
                break;
        }


    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mediaPlayer!=null)
        {
            //handler.removeCallbacks(move);
            isStop=false;
            thread.interrupt();
            mediaPlayer.pause();
            mediaPlayer.stop();
            mediaPlayer.release();
        }
       /* File f =new File("storage/emulated/0/wjy1.ape");
        f.delete();
        Log.d("wjy", "onDestroy:Is Delete ");*/
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permission,int[] grantResults)
    {
        if (requestCode==READ_EXTERNAL_STORAGE_CODE)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                initMediaPlayer();
            }
            else
            {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
            return;

        }
        super.onRequestPermissionsResult(requestCode,permission,grantResults);
    }



}
