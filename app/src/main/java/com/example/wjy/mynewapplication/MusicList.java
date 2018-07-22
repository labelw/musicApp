package com.example.wjy.mynewapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import test.Music;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicList extends AppCompatActivity {

    private List<Music> MusicList =new ArrayList<Music>();
    private ListView list;
    private List<File> FileList =new ArrayList<File>();
    private static final int DB_BUILD_COMPLITE=1;

    SQLiteDatabase Musicdb;
    ProgressDialog waitDialog;
    String select;
    Thread thread;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar1);
        list=(ListView)findViewById(R.id.MusicList);
        Log.d("wjy", Build.VERSION.SDK_INT+"-------------------------------");
        Log.d("wjy",String.valueOf(Environment.getExternalStorageDirectory())+"/"+"-------------------------");
        if(Build.VERSION.SDK_INT>20)
        list.setNestedScrollingEnabled(true);
        imageView =(ImageView)findViewById(R.id.bgpic);
        Intent intent =getIntent();
        switch (intent.getStringExtra("IndexSelect"))
        {
            case "LocalMusic":
                select="MusicMessage";
                toolbar.setTitle("LocalMusic");
                break;
            case "RecentlyMusic":
                select="RecentlyMusic";
                imageView.setBackgroundResource(R.drawable.newpic);
                toolbar.setTitle("RecentlyMusic");
                break;
            case "LoveMusic":
                select="LoveMusic";
                toolbar.setTitle("LoveMusic");
                break;
            default:
                select="MusicMessage where name like '%"+intent.getStringExtra("IndexSelect")+"%' or singer like '%"+intent.getStringExtra("IndexSelect")+"%'";
                toolbar.setTitle("搜索结果");
                break;

        }
        setSupportActionBar(toolbar);

        thread =new Thread(localMusic);
        thread.start();
        waitDialog =new ProgressDialog(MusicList.this);
        waitDialog.setTitle("请稍后-------");
        waitDialog.setMessage("正在搜索本地曲目.....");
        waitDialog.setIndeterminate(true);
        waitDialog.setCancelable(false);
        waitDialog.setProgress(0);
        waitDialog.show();


        //Log.d("wjy",Environment.getExternalStorageDirectory()+"/KuwoMusic/");








    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (thread.isAlive())
        {

        }*/




    }

     Handler handler =new Handler()
    {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                switch (msg.what)
                {
                    case DB_BUILD_COMPLITE:
                        waitDialog.cancel();
                        Log.d("wjy","ok-------------------------------");
                        MusicAdapter musicAdapter =new MusicAdapter(MusicList.this,R.layout.music,MusicList);
                        list.setAdapter(musicAdapter);
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                Music RCmusic =MusicList.get(i);
                                MusicDatabase musicDatabase=new MusicDatabase(MusicList.this,"Music.db",null,1);//打开数据库的类
                                Musicdb=musicDatabase.getWritableDatabase();//返回一个可操作的数据库；
                                Musicdb.execSQL("insert into RecentlyMusic(name,singer,path) Values (?,?,?)",new String[]{RCmusic.getName(),RCmusic.getSinger(),RCmusic.getPath()});
                             /*Cursor cursor=Musicdb.rawQuery("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name",null);
                             while (cursor.moveToNext())
                             {
                                 String r=cursor.getString(0);
                                 Log.d("wjy", "onClick: --------------------------------------"+r);
                             }*/

                                Music[] music =new Music[MusicList.size()];
                                MusicList.toArray(music);
                                //Toast.makeText(MusicList.this, music.getName(), Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(com.example.wjy.mynewapplication.MusicList.this,Palyer.class);
                                Bundle bundle=new Bundle();
                                bundle.putSerializable("Music",music);//Serializable序列化的意思，将对象转换为一连串的字节来表示
                                intent.putExtras(bundle);
                                intent.putExtra("music","List");
                                intent.putExtra("position",i);
                                startActivity(intent);
                            }
                        });

                        break;
                    default:
                        break;

                }










        }

    };


    Runnable localMusic =new Runnable() {


        @Override
        public void run() {
            //Looper.prepare();
            MusicDatabase musicDatabase=new MusicDatabase(MusicList.this,"Music.db",null,1);//打开数据库的类
            Musicdb=musicDatabase.getWritableDatabase();//返回一个可操作的数据库；
            InitMusic();
            Message message =new Message();
            message.what=DB_BUILD_COMPLITE;
            handler.sendMessage(message);
            //Looper.loop();


        }
    };


    public class MusicAdapter extends ArrayAdapter<Music>
    {
        private int resourceid;

        public MusicAdapter(Context context, int resource, List<Music> objects) {
            super(context, resource, objects);
            resourceid=resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view=null;
            Music music=getItem(position);
            ViewHolde viewHolde;
            if(convertView==null)
            {
                view= LayoutInflater.from(getContext()).inflate(resourceid,null);
                viewHolde=new ViewHolde();
                viewHolde.name=(TextView)view.findViewById(R.id.name);
                viewHolde.path=(TextView)view.findViewById(R.id.path);
                view.setTag(viewHolde);
            }
            else
            {
                view=convertView;
                viewHolde=(ViewHolde)view.getTag();
            }
            try
            {
                viewHolde.name.setText(music.getName().replaceAll(".mp3",""));
                viewHolde.path.setText(music.getSinger());
            }
            catch (NullPointerException e)
            {

            }
            return view;

        }
        class ViewHolde {
            TextView name;
            TextView path;
        }
    }

    public static void  ListFile(File f, List<File> list, String regex)
    {

        if(f.length()>1024)
        {


                if(f.isDirectory())
                {
                    File[] a=f.listFiles();
                    for(File b:a)
                    {
                        ListFile(b, list,regex);
                    }
                }
                if(f.isFile()) {
                    if (regex == null) {
                        list.add(f);
                    } else {
                        if (f.getAbsolutePath().matches(regex))
                            list.add(f);
                    }

                }



        }



    }
    public void InitMusic()
    {
        //查询数据库实例化MusicList;
        //
        Cursor cursor=Musicdb.rawQuery("select * from "+select,null);
        Music music;
        while (cursor.moveToNext())
        {
            music=new Music();
            music.setName(cursor.getString(cursor.getColumnIndex("name")));
            music.setSinger(cursor.getString(cursor.getColumnIndex("singer")));
            music.setPath(cursor.getString(cursor.getColumnIndex("path")));
            MusicList.add(music);

        }


    }
    public class MusicDatabase extends SQLiteOpenHelper
    {


        private final String  CREATE_MUSICMESSAGE="Create Table MusicMessage ("
                +"id  integer primary key autoincrement,"
                +"name text,"
                +"singer text,"
                +"path text);";

        private final String  CREATE_RECNETLYMUSIC="Create Table RecentlyMusic ("
                +"id  integer primary key autoincrement,"
                +"name text,"
                +"singer text,"
                +"path text);";

        private final String  CREATE_LOVEMUSIC="Create Table LoveMusic ("
                +"id  integer primary key autoincrement,"
                +"name text,"
                +"singer text,"
                +"path text);";

        private Context mContext;

        public MusicDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            mContext=context;
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
                onCreate(sqLiteDatabase);

        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {


               String name=null,singer=null,MusicPath=null;
               sqLiteDatabase.execSQL(CREATE_MUSICMESSAGE);
               sqLiteDatabase.execSQL(CREATE_LOVEMUSIC);
               sqLiteDatabase.execSQL(CREATE_RECNETLYMUSIC);
               String regex=".*\\.mp3";//  "/storage/2EF1-C8AF/netease/"  String.valueOf(Environment.getExternalStorageDirectory())
               String r=String.valueOf(Environment.getExternalStorageDirectory());
               File path =new File(r);
               ListFile(path,FileList,regex);
               for(File f:FileList)
               {
                   try {
                       String[] Split=f.getName().split(" - ");
                       if(Split.length>=2)
                       {
                           singer=Split[0];
                           name=Split[1];
                           MusicPath=f.getPath();
                           sqLiteDatabase.execSQL("insert into MusicMessage(name,singer,path) Values (?,?,?)",new String[]{name,singer,MusicPath});
                       }
                       else
                           continue;
                   }
                   catch (ArrayIndexOutOfBoundsException e)
                   {
                       e.printStackTrace();
                   }
               }

               Toast.makeText(mContext, "MUSICMESSAGE Create succeeded", Toast.LENGTH_SHORT).show();
           }




    }



}
