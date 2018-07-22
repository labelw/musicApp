package com.example.wjy.mynewapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SqlLite extends AppCompatActivity {

    private MyDatabaseHelper myhelp;
    private Button YiKu;
    private EditText insert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql_lite);
        YiKu=(Button)findViewById(R.id.YiKu);
        insert=(EditText)findViewById(R.id.insert);
        myhelp=new MyDatabaseHelper(this,"BookStore.db",null,1);
        final SQLiteDatabase  db= myhelp.getWritableDatabase();
        //db.execSQL("insert into Book (name,author,pages,price) Values(?,?,?,?)",new String[] {"","Dan brown","454","16.96"} );
        YiKu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Cursor c=db.rawQuery("select name from book where pages=?",new String[]{"454"});
                String r=null;
                while (c.moveToNext())
                {
                    r=c.getString(c.getColumnIndex("name"));
                }

                Toast.makeText(SqlLite.this, r, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public class MyDatabaseHelper extends SQLiteOpenHelper
    {
        public static final String CREATE_BOOK ="create table book ("
                +"id integer primary key autoincrement,"
                +"author text ,"
                +"price real,"
                +"pages integer,"
                +"name text)";
        private Context mContext;
        public  MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version)
        {
            super(context,name,factory,version);
            mContext=context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_BOOK);
            Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();


        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }

    }


}

