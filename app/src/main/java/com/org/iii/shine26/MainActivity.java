package com.org.iii.shine26;


import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private GridView gridView;
    private MyAdapter myAdapter;
    private MyAdapter2 myAdapter2;
    private int[] balls = {R.drawable.ball1,R.drawable.ball2,
            R.drawable.ball3,R.drawable.ball4,R.drawable.ball5,
            R.drawable.ball6,R.drawable.ball7,R.drawable.ball8,
            R.drawable.ball9,R.drawable.ball10};
    private LinkedList<String> foodNo;
    private UIHandler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new UIHandler();
        gridView = (GridView)findViewById(R.id.gridView);
        //initGridView();

        getJSON();

    }

    private void initGridView(){
        myAdapter = new MyAdapter(this);
        myAdapter2 = new MyAdapter2(this);

        gridView.setAdapter(myAdapter2);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("brad", "i = " +i);
            }
        });
    }

    private void getJSON(){
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL("http://data.fda.gov.tw/cacheData/19_3.json");
                    HttpURLConnection conn =
                            (HttpURLConnection) url.openConnection();
                    conn.connect();
                    BufferedReader reader =
                            new BufferedReader(
                                    new InputStreamReader(conn.getInputStream()));
                    String line = reader.readLine();
                    reader.close();

                    parseJSON(line);
                }catch(Exception e){
                    Log.v("brad", e.toString());
                }
            }
        }.start();
    }

    private void parseJSON(String json){
        foodNo = new LinkedList<>();
        try {
            JSONArray root = new JSONArray(json);
            for (int i=0; i<root.length(); i++){
                JSONArray sub = root.getJSONArray(i);
                JSONObject noObj = sub.getJSONObject(0);
                String StringNo = noObj.getString("許可證字號");
                Log.v("brad", StringNo);
                foodNo.add(StringNo);
            }
            handler.sendEmptyMessage(0);
        }catch(Exception e){
            Log.v("brad", e.toString());
        }

    }

    public void change(View v){
        gridView.setNumColumns(3);
    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        MyAdapter(Context context){this.context = context;}

        @Override
        public int getCount() {
            return 30;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ImageView img;
            if (view == null){
                img = new ImageView(context);
                img.setLayoutParams(new GridView.LayoutParams(185, 185));
                img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            }else{
                img = (ImageView)view;
            }
            img.setImageResource(balls[(int)(Math.random()*10)]);
            return img;
        }
    }

    private class MyAdapter2 extends BaseAdapter {
        private Context context;
        MyAdapter2(Context context){this.context = context;}

        @Override
        public int getCount() {
            return balls.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null){
                LayoutInflater flater = LayoutInflater.from(context);
                view = flater.inflate(R.layout.layout_item,null);
            }
            ImageView img = (ImageView)view.findViewById(R.id.item_img);
            TextView title = (TextView)view.findViewById(R.id.item_title);

            if (i == 7){
                img.setImageResource(R.drawable.b0);
            }else {
                img.setImageResource(balls[(int)(Math.random()*10)]);
            }
            title.setText(foodNo.get(i));

            if (i % 2 == 0){
                view.setBackgroundColor(Color.YELLOW);
                Log.v("brad", "i = " + i);
            }


            return view;
        }
    }

    private class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initGridView();
        }
    }


}