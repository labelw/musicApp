package com.example.wjy.mynewapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.wjy.mynewapplication.mypackage.*;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import test.Music;

public class SearchRt extends AppCompatActivity {

    Music[] musics=null;
    String searchString;
    private ListView networkMusicListView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        Intent intent=getIntent();
        searchString=intent.getStringExtra("Search");
        networkMusicListView =(ListView)findViewById(R.id.MusicList);
        Thread thread=new Thread(networkMusic);
        thread.start();
        if(Build.VERSION.SDK_INT>20)
        networkMusicListView.setNestedScrollingEnabled(true);
        toolbar=(Toolbar)findViewById(R.id.toolbar1);
        toolbar.setTitle("'"+searchString+"'"+"搜索结果");
        setSupportActionBar(toolbar);



    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    Runnable networkMusic =new Runnable() {
        @Override
        public void run() {
            //List<Music> networkmusicList=null;


            try {

                Socket socket =new Socket(GETIp.getip(),10010);
                OutputStream outputStream=socket.getOutputStream();
                byte[] tag="NetworkMusicList".getBytes();
                outputStream.write(tag);
                InputStream inputStream=socket.getInputStream();
                byte[] b =new byte[1024];
                inputStream.read(b);
                if(new String(b).trim().equals("search"))
                {
                    searchString= "select * From MusicPath where MusicName like '%"+searchString+"%' or MusicSinger like '%"+searchString+"%'";
                    outputStream=socket.getOutputStream();
                    outputStream.write(searchString.getBytes());

                }
                ObjectInputStream obin=new ObjectInputStream(socket.getInputStream());
                musics=(Music[]) obin.readObject();
                /*networkmusicList =new ArrayList<Music>();
                networkmusicList= Arrays.asList(music);*/
                socket.close();


            } catch (Exception e) {
                e.printStackTrace();
            }


            Message message =new Message();
            Bundle data =new Bundle();
            data.putSerializable("networkmusicList",musics);
            message.setData(data);
            handler.sendMessage(message);



        }
    };

    Handler handler =new Handler()
    {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data =msg.getData();
            Music[] musics=(Music[])data.getSerializable("networkmusicList");
            List<Music> networkmusicList=null;
            //networkmusicList =new ArrayList<Music>();
            networkmusicList= Arrays.asList(musics);
            NetWorkMusicAdapter netWorkMusicAdapter =new NetWorkMusicAdapter(SearchRt.this,R.layout.music,networkmusicList);
            networkMusicListView.setAdapter(netWorkMusicAdapter);
            List<Music> finalNetworkmusicList = networkmusicList;
            networkMusicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Music[] music =new Music[finalNetworkmusicList.size()];
                    finalNetworkmusicList.toArray(music);
                    Intent intent =new Intent(SearchRt.this,Palyer.class);
                    intent.putExtra("music","Search");
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("Music",music);//Serializable序列化的意思，将对象转换为一连串的字节来表示
                    intent.putExtras(bundle);
                    intent.putExtra("position",i);
                    startActivity(intent);

                }
            });





        }
    };

    public class NetWorkMusicAdapter extends ArrayAdapter<Music>
    {
        private int resourceid;

        public NetWorkMusicAdapter(Context context, int resource, List<Music> objects) {
            super(context, resource, objects);
            resourceid=resource;
        }

        @NonNull
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
}
