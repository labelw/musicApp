package com.example.wjy.mynewapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.wjy.mynewapplication.mypackage.*;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import test.Music;

public class Index extends AppCompatActivity {

    ViewPager viewPager; //页调器
    TabLayout tabLayout;  //标签-页调器
    ListView listView;
    private List<View> viewList=new ArrayList<View>();
    private List<String> stringList =new ArrayList<String>();
    View view1,view2;
    private ListView networkListView;
    private Button search;
    private List<IndexMusicList> indexMusicListList=new ArrayList<IndexMusicList>();
    private Button localSearch;
    private  Button isdisplay;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        Log.d("wjy", Environment.getExternalStorageState());
        CheckPermission();
        viewPager=(ViewPager)findViewById(R.id.viewpage);
        tabLayout=(TabLayout)findViewById(R.id.tablayout);
        InitViewList(viewList);
        viewPager.setAdapter(new MyPageAdapter(viewList));
        tabLayout.setupWithViewPager(viewPager);//把tablayout和viewpage联系起来
        tabLayout.getTabAt(0).setText("本地音乐");
        tabLayout.getTabAt(1).setText("网络音乐");
        listView=(ListView)view1.findViewById(R.id.list_view2);

        InitIndexMusicList(indexMusicListList);  //InitStringList(stringList);
        IndexMusicListAdapter indexMusicListAdapter=new IndexMusicListAdapter(this,R.layout.indexgd,indexMusicListList); //listAdapter listAdapter=new listAdapter(this,R.layout.sonview,stringList);
        listView.setAdapter(indexMusicListAdapter);           //listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent(Index.this,MusicList.class);
                Log.d("wjy",indexMusicListList.get(i).getMusicListName().trim()+"---------------------------------------------");

                switch (indexMusicListList.get(i).getMusicListName().trim())
                {

                    case "本地歌曲":
                       intent.putExtra("IndexSelect","LocalMusic");
                        break;
                    case "最近播放":
                        intent.putExtra("IndexSelect","RecentlyMusic");
                        break;
                    case "我的收藏":
                        intent.putExtra("IndexSelect","LoveMusic");
                        break;
                    default:
                        break;

                }
                startActivity(intent);
            }
        });
        search=(Button)findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(Index.this,Search.class);
                intent.putExtra("islocal","nolocal");
                startActivity(intent);

            }
        });
        localSearch =(Button)findViewById(R.id.localsearch);
        localSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(Index.this,Search.class);
                intent.putExtra("islocal","local");
                startActivity(intent);

            }
        });

        //ViewPager commendviewPager=(ViewPager)view2.findViewById(R.id.commend);
       /* new Thread(networkMusic).start();
        Button refresh =(Button)view2.findViewById(R.id.refreshButton);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(networkMusic).start();
            }
        });*/



    }
    /*private  void InitSecondViewList(List<View> viewList)
    {
        View view =getLayoutInflater().inflate(R.layout.commendpic,null);
        View view1=view.findViewById(R.id.commendImage);
        View view2=view.findViewById(R.id.commendImage);
        View view3=view.findViewById(R.id.commendImage);
        viewList.add(view1);
        viewList.add(view1);
        viewList.add(view1);


    }*/
    private void CheckPermission()
    {
        if(ContextCompat.checkSelfPermission(Index.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(Index.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(Index.this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(Index.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET,Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS},1);
        }
    }

    /*
     初始化List<View>,把布局加载到List<View>里面
     */
    private void InitViewList(List<View> viewList)
    {

        view1=getLayoutInflater().inflate(R.layout.page1_index,null);
        view2=getLayoutInflater().inflate(R.layout.page2_index,null);
        viewList.add(view1);
        viewList.add(view2);
    }

    private void InitIndexMusicList(List<IndexMusicList> indexMusicListList)
    {
        IndexMusicList indexMusicList1 =new IndexMusicList();
        indexMusicList1.setPicid(R.drawable.localmusic);
        indexMusicList1.setMusicListName("本地歌曲");
        IndexMusicList indexMusicList2 =new IndexMusicList();
        indexMusicList2.setPicid(R.drawable.recentlymusic);
        indexMusicList2.setMusicListName("最近播放");
        IndexMusicList indexMusicList3 =new IndexMusicList();
        indexMusicList3.setPicid(R.drawable.love);
        indexMusicList3.setMusicListName("我的收藏");
        indexMusicListList.add(indexMusicList1);
        indexMusicListList.add(indexMusicList2);
        indexMusicListList.add(indexMusicList3);
    }

    public  class IndexMusicList
    {
        int picid;
        String MusicListName;

        public int getPicid() {
            return picid;
        }

        public String getMusicListName() {
            return MusicListName;
        }

        public void setPicid(int picid) {
            this.picid = picid;
        }

        public void setMusicListName(String musicListName) {
            MusicListName = musicListName;
        }
    }

    private void InitStringList(List<String> stringList)
    {
        stringList.add("本地歌曲");
        stringList.add("最近播放");
        stringList.add("我的收藏");
        stringList.add("local music");

    }

    /*
    建立ViewPage适配器
     */
    public class MyPageAdapter extends PagerAdapter
    {
        private List<View> viewList;

        public MyPageAdapter(List<View> viewList) {
            this.viewList = viewList;
        }


        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
           container.removeView(viewList.get(position));

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    }
    /*
    首页歌单的ListView适配器
     */
    public class listAdapter extends ArrayAdapter<String>
    {
        private int resourceid;

        public listAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
           resourceid=resource;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=null;
           String s=getItem(position);
            ViewHolde viewHolde;
            if(convertView==null)
            {
                view= LayoutInflater.from(getContext()).inflate(resourceid,null);
                viewHolde= new ViewHolde();
                viewHolde.name=(TextView)view.findViewById(R.id.music);
                view.setTag(viewHolde);
            }
            else
            {
                view=convertView;
                viewHolde=(ViewHolde)view.getTag();
            }
            try
            {
                viewHolde.name.setText(s);
            }
            catch (NullPointerException e)
            {

            }
            return view;

        }
        class ViewHolde {
            TextView name;
        }

    }
    /*
    网络歌曲列表的ListView适配器
     */
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

    public class IndexMusicListAdapter extends ArrayAdapter<IndexMusicList>
    {
        private int resourceid;

        public IndexMusicListAdapter(Context context, int resource, List<IndexMusicList> objects) {
            super(context, resource, objects);
            resourceid=resource;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=null;
            IndexMusicList indexMusicList=getItem(position);
            ViewHolde viewHolde;
            if(convertView==null)
            {
                view= LayoutInflater.from(getContext()).inflate(resourceid,null);
                viewHolde=new ViewHolde();
                viewHolde.name=(TextView)view.findViewById(R.id.ListText);
                viewHolde.pic=(ImageView) view.findViewById(R.id.ListImage);
                view.setTag(viewHolde);
            }
            else
            {
                view=convertView;
                viewHolde=(ViewHolde)view.getTag();
            }
            try
            {
                viewHolde.name.setText(indexMusicList.getMusicListName());
                viewHolde.pic.setImageResource(indexMusicList.getPicid());
            }
            catch (NullPointerException e)
            {

            }
            return view;

        }
        class ViewHolde {
            ImageView pic;
            TextView name;
        }

    }

    Handler handler =new Handler()
    {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data =msg.getData();
            Music[] musics=(Music[])data.getSerializable("networkmusicList");
            List<Music> networkmusicList=null;
            networkmusicList =new ArrayList<Music>();
            networkmusicList= Arrays.asList(musics);
            networkListView=(ListView)view2.findViewById(R.id.networkmusic);
            NetWorkMusicAdapter netWorkMusicAdapter=new NetWorkMusicAdapter(Index.this,R.layout.music,networkmusicList);
            networkListView.setAdapter(netWorkMusicAdapter);
            List<Music> finalNetworkmusicList = networkmusicList;
            networkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Music[] music =new Music[finalNetworkmusicList.size()];
                    finalNetworkmusicList.toArray(music);
                    Intent intent =new Intent(Index.this,Palyer.class);
                    intent.putExtra("music","Index");
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("Music",music);//Serializable序列化的意思，将对象转换为一连串的字节来表示
                    intent.putExtra("position",i);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
            });




        }
    };

    Runnable networkMusic =new Runnable() {
        @Override
        public void run() {
            //List<Music> networkmusicList=null;
            Music[] musics=null;


                try {

                    Socket socket =new Socket(GETIp.getip(),10010);
                    OutputStream outputStream=socket.getOutputStream();
                    byte[] tag="NetworkMusicList".getBytes();
                    outputStream.write(tag);
                    InputStream inputStream =socket.getInputStream();
                    byte[] b=new byte[1024];
                    inputStream.read(b);
                    if(new String(b).trim().equals("search"))
                    {
                        outputStream=socket.getOutputStream();
                        outputStream.write("SELECT TOP 10 * FROM MusicPath ORDER BY NEWID()".getBytes());
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
}
