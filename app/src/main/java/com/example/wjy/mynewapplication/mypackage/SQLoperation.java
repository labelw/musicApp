package com.example.wjy.mynewapplication.mypackage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJY on 2017/6/17.
 */

public class SQLoperation {

    List<File> FileList=new ArrayList<File>();
    String  ListPath;
    MusicDatabase musicDatabase;

    /*
    如果触发onCreate就要传入ListPath
     */
    public SQLoperation(String listPath,Context context,int Version) {
        ListPath = listPath;
       musicDatabase=new MusicDatabase(context,"Music.db",null,Version);
    }
    public SQLiteDatabase GetDatabase()
    {
        return musicDatabase.getWritableDatabase();
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
            sqLiteDatabase.execSQL("Drop table if exists MusicMessage");
            onCreate(sqLiteDatabase);

        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String name=null,singer=null,MusicPath=null;
            sqLiteDatabase.execSQL(CREATE_MUSICMESSAGE);
            sqLiteDatabase.execSQL(CREATE_LOVEMUSIC);
            sqLiteDatabase.execSQL(CREATE_RECNETLYMUSIC);
            String regex=".*\\.mp3";//  "/storage/2EF1-C8AF/netease/"  String.valueOf(Environment.getExternalStorageDirectory())
            File path =new File(ListPath);
            ListFile(path,FileList,regex);
            for(File f:FileList)
            {
                try {
                    String[] Split=f.getName().split(" - ");
                    if(Split.length==2)
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

    public static void  ListFile(File f, List<File> list, String regex)
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
                list.add(f);
            }
            else
            {
                if(f.getAbsolutePath().matches(regex))
                    list.add(f);
            }

        }

    }
}
