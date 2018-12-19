package com.example.yenti.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MainActivity  extends Activity implements MediaPlayer.OnPreparedListener,MediaPlayer.OnErrorListener,MediaPlayer.OnCompletionListener {

    /** Called when the activity is first created. */
    static final String db_name="blind";
    static final String tb_script="script";
    static final String tb_talk="talk";
    static final String tb_voice="voice";
    static final String tb_word="word";
    static final String tb_map="map";
    static final String tb_next="next";
    static final String tb_save="save";
    static final String tb_tool="tool";
    static final String tb_bag="bag";
    SQLiteDatabase db;

    Cursor scriptloop;
    int mainloop;

    Uri uri;
    MediaPlayer mper;

    HashMap<String, Integer> VoiceMap ;
    HashMap<String, Integer> MapMap ;
    int face=0;
    String text=null;
    String[] array=null;
    String[][] maparray=null;
    int[] guy=null;
    int[] destination=null;
    int x,y;
    int pick;
    String canmove;
    String finish;
    String canarray[]=null;
    String soundarry[]=null;
    String soundlocation;
    public static long onclicktime ;
    public static long scripttime ;



    private String lognumber;
    public static int suballow;
    int[] record = null;

    private SeekBar volumeSeekbar = null;
    private AudioManager audioManager = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button Start = (Button)findViewById(R.id.Start);
        Start.getBackground().setAlpha(0);
        Start.setOnClickListener(btnListener);
        Button Log = (Button)findViewById(R.id.Log);
        Log.getBackground().setAlpha(0);
        Log.setOnClickListener(btnListener);
        Button Set = (Button)findViewById(R.id.Set);
        Set.getBackground().setAlpha(0);
        Set.setOnClickListener(btnListener);
        Button Leave = (Button)findViewById(R.id.Leave);
        Leave.getBackground().setAlpha(0);
        Leave.setOnClickListener(btnListener);
        TextView Subtitle=(TextView)findViewById(R.id.Subtitle);
        MapMap = new HashMap<String, Integer>();
        VoiceMap = new HashMap<String, Integer>();
        Createdatabase();
        MainActivity.onclicktime = -1;
        MainActivity.scripttime = -1;
        pick=0;
        MainActivity.suballow=0;
        record = new int[]{1,1};
        lognumber="";

        mainloop=0;


    }
    //按鈕控制
    private Button.OnClickListener btnListener = new Button.OnClickListener(){
        public void onClick(View v){
            layout(v);
        }
    };
    private void layout(View v) {
            switch(v.getId()) {
                case R.id.Start:
                    Intent itstart =new Intent(this,cartoonone.class);
                    startActivity(itstart);

                    setContentView(R.layout.game);
                    Button Menu= (Button)findViewById(R.id.Menu);
                    Menu.setOnClickListener(btnListener);
                    Button Backpack= (Button)findViewById(R.id.Backpack);
                    Backpack.setOnClickListener(btnListener);
                    Button printNext= (Button)findViewById(R.id.printNext);
                    printNext.setOnClickListener(btnListener);
                    printNext.getBackground().setAlpha(0);
                    TextView Subtitle=(TextView)findViewById(R.id.Subtitle);

                    record[0] = 1;
                    record[1] = 1;
                    mainloop=0;

                    break;
                case R.id.Log:
                    setContentView(R.layout.log);
                    ImageButton LogComeback= (ImageButton)findViewById(R.id.LogComeback);
                    LogComeback.setOnClickListener(btnListener);
                    Button Log1= (Button)findViewById(R.id.Log1);
                    Log1.getBackground().setAlpha(100);
                    Log1.setOnClickListener(btnListener);
                    Button Log2= (Button)findViewById(R.id.Log2);
                    Log2.getBackground().setAlpha(100);
                    Log2.setOnClickListener(btnListener);
                    Button Log3= (Button)findViewById(R.id.Log3);
                    Log3.getBackground().setAlpha(100);
                    Log3.setOnClickListener(btnListener);
                    System.out.println("開始讀資料庫");
                    Cursor save= db.rawQuery("SELECT Save_index,Save_name " +
                            " FROM "+tb_save,null);
                    System.out.println("資料庫讀完");
                    if(save.getCount()!=0){
                        if(save.moveToFirst()) {
                            do {
                                switch (save.getString(0)){
                                    case "1":
                                        System.out.println("開始寫名稱");
                                        Log1.setText(save.getString(1));
                                        break;
                                    case "2":
                                        Log2.setText(save.getString(1));
                                        break;
                                    case "3":
                                        Log3.setText(save.getString(1));
                                        break;
                                }
                            } while (save.moveToNext());
                        }
                    }
                    break;
                case R.id.Set:
                    setContentView(R.layout.set);
                    ImageButton SetComeback= (ImageButton)findViewById(R.id.SetComeback);
                    SetComeback.setOnClickListener(btnListener);
                    Button SubOn= (Button)findViewById(R.id.SubOn);
                    SubOn.setOnClickListener(btnListener);
                    Button SubOff= (Button)findViewById(R.id.SubOff);
                    SubOff.setOnClickListener(btnListener);
                    SeekBar seekBar=(SeekBar)findViewById(R.id.seekBar);
                    if(MainActivity.suballow==0){
                        SubOn.getBackground().setAlpha(50);
                        SubOff.getBackground().setAlpha(100);
                        SubOn.setEnabled(false);
                        SubOff.setEnabled(true);
                    }else{
                        SubOn.getBackground().setAlpha(100);
                        SubOff.getBackground().setAlpha(50);
                        SubOn.setEnabled(true);
                        SubOff.setEnabled(false);
                    }
                    initControls();
                    break;
                case R.id.Leave:
                    finish();
                    break;
                case R.id.Menu:
                    setContentView(R.layout.menu);
                    Button Save= (Button)findViewById(R.id.Save);
                    Save.getBackground().setAlpha(0);
                    Save.setOnClickListener(btnListener);
                    Button MenuComeback= (Button)findViewById(R.id.MenuComeback);
                    MenuComeback.getBackground().setAlpha(0);
                    MenuComeback.setOnClickListener(btnListener);
                    Button Comeback= (Button)findViewById(R.id.Comeback);
                    Comeback.getBackground().setAlpha(0);
                    Comeback.setOnClickListener(btnListener);
                    break;
                case R.id.Save:
                    setContentView(R.layout.logcover);
                    ImageButton CoverComeback= (ImageButton)findViewById(R.id.CoverComeback);
                    CoverComeback.setOnClickListener(btnListener);
                    Button Log1Cover= (Button)findViewById(R.id.Log1Cover);
                    Log1Cover.getBackground().setAlpha(100);
                    Log1Cover.setOnClickListener(btnListener);
                    Button Log2Cover= (Button)findViewById(R.id.Log2Cover);
                    Log2Cover.getBackground().setAlpha(100);
                    Log2Cover.setOnClickListener(btnListener);
                    Button Log3Cover= (Button)findViewById(R.id.Log3Cover);
                    Log3Cover.getBackground().setAlpha(100);
                    Log3Cover.setOnClickListener(btnListener);
                    System.out.println("開始讀資料庫");
                    save= db.rawQuery("SELECT Save_index,Save_name " +
                            " FROM "+tb_save,null);
                    System.out.println("資料庫讀完");
                    if(save.getCount()!=0){
                        if(save.moveToFirst()) {
                            do {
                                switch (save.getString(0)){
                                    case "1":
                                        System.out.println("開始寫名稱");
                                        Log1Cover.setText(save.getString(1));
                                        break;
                                    case "2":
                                        Log2Cover.setText(save.getString(1));
                                        break;
                                    case "3":
                                        Log3Cover.setText(save.getString(1));
                                        break;
                                }
                            } while (save.moveToNext());
                        }
                    }
                    break;
                case R.id.MenuComeback:
                    setContentView(R.layout.main);
                    Button Start = (Button)findViewById(R.id.Start);
                    Start.getBackground().setAlpha(0);
                    Start.setOnClickListener(btnListener);
                    Button Log = (Button)findViewById(R.id.Log);
                    Log.getBackground().setAlpha(0);
                    Log.setOnClickListener(btnListener);
                    Button Set = (Button)findViewById(R.id.Set);
                    Set.getBackground().setAlpha(0);
                    Set.setOnClickListener(btnListener);
                    Button Leave = (Button)findViewById(R.id.Leave);
                    Leave.getBackground().setAlpha(0);
                    Leave.setOnClickListener(btnListener);
                    break;
                case R.id.Comeback:
                    setContentView(R.layout.game);
                    Menu= (Button)findViewById(R.id.Menu);
                    Menu.setOnClickListener(btnListener);
                    Backpack= (Button)findViewById(R.id.Backpack);
                    Backpack.setOnClickListener(btnListener);
                    printNext= (Button)findViewById(R.id.printNext);
                    printNext.setOnClickListener(btnListener);
                    printNext.getBackground().setAlpha(0);
                    Subtitle=(TextView)findViewById(R.id.Subtitle);
                    break;
                case R.id.Backpack:
                    setContentView(R.layout.backpack);
                    ImageButton BackpackComeback= (ImageButton)findViewById(R.id.BackpackComeback);
                    BackpackComeback.setOnClickListener(btnListener);
                    TextView BackpackText=(TextView)findViewById(R.id.BackpackText);
                    String bagtext="";
                    Cursor bag= db.rawQuery("SELECT * FROM "+tb_bag+ " WHERE User_id = "+lognumber,null);
                    if(bag.getCount()!=0) {
                        if (bag.moveToFirst()) {
                            do {
                                Cursor tool = db.rawQuery("SELECT * FROM " + tb_tool + " WHERE Tool_id= " + bag.getString(1), null);
                                if (tool.moveToFirst()) {
                                    do {
                                        bagtext=""+ tool.getString(1) + "\n     " + tool.getString(2)+ "\n";
                                        BackpackText.setText("背包內容\n" + bagtext);
                                        Sound(tool.getString(3));
                                    } while (tool.moveToNext());
                                }
                            } while (bag.moveToNext());
                        }
                    }
                    break;
                case R.id.CoverComeback:
                    setContentView(R.layout.game);
                    Menu= (Button)findViewById(R.id.Menu);
                    Menu.setOnClickListener(btnListener);
                    Backpack= (Button)findViewById(R.id.Backpack);
                    Backpack.setOnClickListener(btnListener);
                    printNext= (Button)findViewById(R.id.printNext);
                    printNext.setOnClickListener(btnListener);
                    printNext.getBackground().setAlpha(0);
                    Subtitle=(TextView)findViewById(R.id.Subtitle);
                    break;
                case R.id.LogComeback:
                    setContentView(R.layout.main);
                    Start = (Button)findViewById(R.id.Start);
                    Start.getBackground().setAlpha(0);
                    Start.setOnClickListener(btnListener);
                    Log = (Button)findViewById(R.id.Log);
                    Log.getBackground().setAlpha(0);
                    Log.setOnClickListener(btnListener);
                    Set = (Button)findViewById(R.id.Set);
                    Set.getBackground().setAlpha(0);
                    Set.setOnClickListener(btnListener);
                    Leave = (Button)findViewById(R.id.Leave);
                    Leave.getBackground().setAlpha(0);
                    Leave.setOnClickListener(btnListener);
                    break;
                case R.id.Log1:
                    setContentView(R.layout.logdetailed);
                    ImageButton DetailedComeback= (ImageButton)findViewById(R.id.DetailedComeback);
                    DetailedComeback.setOnClickListener(btnListener);
                    Button LogUse = (Button)findViewById(R.id.LogUse);
                    LogUse.getBackground().setAlpha(0);
                    LogUse.setOnClickListener(btnListener);
                    TextView Detailed=(TextView)findViewById(R.id.Detailed);
                    lognumber="1";
                    save= db.rawQuery("SELECT *" +
                            " FROM "+tb_save+" WHERE Save_index = "+lognumber,null);
                    if(save.getCount()!=0){
                        if(save.moveToFirst()) {
                            Cursor next= db.rawQuery("SELECT Next_content FROM "+tb_next+
                                            " WHERE Next_chapter_now = "+save.getString(1)+" AND Next_section_now = "+save.getString(2)
                                    ,null);
                            if(next.getCount()!=0){
                                if(next.moveToFirst()) {
                                    Detailed.setText(next.getString(0));
                                }
                            }
                        }
                    }
                    break;
                case R.id.Log2:
                    setContentView(R.layout.logdetailed);
                    DetailedComeback= (ImageButton)findViewById(R.id.DetailedComeback);
                    DetailedComeback.setOnClickListener(btnListener);
                    LogUse = (Button)findViewById(R.id.LogUse);
                    LogUse.getBackground().setAlpha(0);
                    LogUse.setOnClickListener(btnListener);
                    Detailed=(TextView)findViewById(R.id.Detailed);
                    lognumber="2";
                    save= db.rawQuery("SELECT *" +
                            " FROM "+tb_save+" WHERE Save_index = "+lognumber,null);
                    if(save.getCount()!=0){
                        if(save.moveToFirst()) {
                            Cursor next= db.rawQuery("SELECT Next_content FROM "+tb_next+
                                            " WHERE Next_chapter_now = "+save.getString(1)+" AND Next_section_now = "+save.getString(2)
                                    ,null);
                            if(next.getCount()!=0){
                                if(next.moveToFirst()) {
                                    Detailed.setText(next.getString(0));
                                }
                            }
                        }
                    }
                    break;
                case R.id.Log3:
                    setContentView(R.layout.logdetailed);
                    DetailedComeback= (ImageButton)findViewById(R.id.DetailedComeback);
                    DetailedComeback.setOnClickListener(btnListener);
                    LogUse = (Button)findViewById(R.id.LogUse);
                    LogUse.getBackground().setAlpha(0);
                    LogUse.setOnClickListener(btnListener);
                    Detailed=(TextView)findViewById(R.id.Detailed);
                    lognumber="3";
                    save= db.rawQuery("SELECT *" +
                            " FROM "+tb_save+" WHERE Save_index = "+lognumber,null);
                    if(save.getCount()!=0){
                        if(save.moveToFirst()) {
                            Cursor next= db.rawQuery("SELECT Next_content FROM "+tb_next+
                                            " WHERE Next_chapter_now = "+save.getString(1)+" AND Next_section_now = "+save.getString(2)
                                    ,null);
                            if(next.getCount()!=0){
                                if(next.moveToFirst()) {
                                    Detailed.setText(next.getString(0));
                                }
                            }
                        }
                    }
                    break;
                case R.id.DetailedComeback:
                    setContentView(R.layout.log);
                    LogComeback= (ImageButton)findViewById(R.id.LogComeback);
                    LogComeback.setOnClickListener(btnListener);
                    Log1= (Button)findViewById(R.id.Log1);
                    Log1.getBackground().setAlpha(100);
                    Log1.setOnClickListener(btnListener);
                    Log2= (Button)findViewById(R.id.Log2);
                    Log2.getBackground().setAlpha(100);
                    Log2.setOnClickListener(btnListener);
                    Log3= (Button)findViewById(R.id.Log3);
                    Log3.getBackground().setAlpha(100);
                    Log3.setOnClickListener(btnListener);
                    System.out.println("開始讀資料庫");
                    save= db.rawQuery("SELECT Save_index,Save_name " +
                            " FROM "+tb_save,null);
                    System.out.println("資料庫讀完");
                    if(save.getCount()!=0){
                        if(save.moveToFirst()) {
                            do {
                                switch (save.getString(0)){
                                    case "1":
                                        System.out.println("開始寫名稱");
                                        Log1.setText(save.getString(1));
                                        break;
                                    case "2":
                                        Log2.setText(save.getString(1));
                                        break;
                                    case "3":
                                        Log3.setText(save.getString(1));
                                        break;
                                }
                            } while (save.moveToNext());
                        }
                    }
                    break;
                case R.id.SetComeback:
                    setContentView(R.layout.main);
                    Start = (Button)findViewById(R.id.Start);
                    Start.getBackground().setAlpha(0);
                    Start.setOnClickListener(btnListener);
                    Log = (Button)findViewById(R.id.Log);
                    Log.getBackground().setAlpha(0);
                    Log.setOnClickListener(btnListener);
                    Set = (Button)findViewById(R.id.Set);
                    Set.getBackground().setAlpha(0);
                    Set.setOnClickListener(btnListener);
                    Leave = (Button)findViewById(R.id.Leave);
                    Leave.getBackground().setAlpha(0);
                    Leave.setOnClickListener(btnListener);
                    break;
                case R.id.Log1Cover:
                    setContentView(R.layout.naming);
                    ImageButton NameComeback= (ImageButton)findViewById(R.id.NameComeback);
                    NameComeback.setOnClickListener(btnListener);
                    Button LogNaming = (Button)findViewById(R.id.LogNaming);
                    LogNaming.getBackground().setAlpha(0);
                    LogNaming.setOnClickListener(btnListener);
                    EditText editText =(EditText)findViewById(R.id.editText);
                    lognumber="1";
                    editText.setText("記錄檔  "+lognumber);
                    break;
                case R.id.Log2Cover:
                    setContentView(R.layout.naming);
                    NameComeback= (ImageButton)findViewById(R.id.NameComeback);
                    NameComeback.setOnClickListener(btnListener);
                    LogNaming = (Button)findViewById(R.id.LogNaming);
                    LogNaming.getBackground().setAlpha(0);
                    LogNaming.setOnClickListener(btnListener);
                    editText =(EditText)findViewById(R.id.editText);;
                    lognumber="2";
                    editText.setText("記錄檔  "+lognumber);
                    break;
                case R.id.Log3Cover:
                    setContentView(R.layout.naming);
                    NameComeback= (ImageButton)findViewById(R.id.NameComeback);
                    NameComeback.setOnClickListener(btnListener);
                    LogNaming = (Button)findViewById(R.id.LogNaming);
                    LogNaming.getBackground().setAlpha(0);
                    LogNaming.setOnClickListener(btnListener);
                    editText =(EditText)findViewById(R.id.editText);;
                    lognumber="3";
                    editText.setText("記錄檔  "+lognumber);
                    break;
                case R.id.LogNaming:
                    editText =(EditText)findViewById(R.id.editText);;
                    Save(lognumber, String.valueOf(editText.getText()));
                    setContentView(R.layout.logcover);
                    CoverComeback= (ImageButton)findViewById(R.id.CoverComeback);
                    CoverComeback.setOnClickListener(btnListener);
                    Log1Cover= (Button)findViewById(R.id.Log1Cover);
                    Log1Cover.getBackground().setAlpha(100);
                    Log1Cover.setOnClickListener(btnListener);
                    Log2Cover= (Button)findViewById(R.id.Log2Cover);
                    Log2Cover.getBackground().setAlpha(100);
                    Log2Cover.setOnClickListener(btnListener);
                    Log3Cover= (Button)findViewById(R.id.Log3Cover);
                    Log3Cover.getBackground().setAlpha(100);
                    Log3Cover.setOnClickListener(btnListener);
                    System.out.println("開始讀資料庫");
                    save= db.rawQuery("SELECT Save_index,Save_name " +
                            " FROM "+tb_save,null);
                    System.out.println("資料庫讀完");
                    if(save.getCount()!=0){
                        if(save.moveToFirst()) {
                            do {
                                switch (save.getString(0)){
                                    case "1":
                                        System.out.println("開始寫名稱");
                                        Log1Cover.setText(save.getString(1));
                                        break;
                                    case "2":
                                        Log2Cover.setText(save.getString(1));
                                        break;
                                    case "3":
                                        Log3Cover.setText(save.getString(1));
                                        break;
                                }
                            } while (save.moveToNext());
                        }
                    }
                    break;
                case R.id.NameComeback:
                    setContentView(R.layout.logcover);
                    CoverComeback= (ImageButton)findViewById(R.id.CoverComeback);
                    CoverComeback.setOnClickListener(btnListener);
                    Log1Cover= (Button)findViewById(R.id.Log1Cover);
                    Log1Cover.getBackground().setAlpha(100);
                    Log1Cover.setOnClickListener(btnListener);
                    Log2Cover= (Button)findViewById(R.id.Log2Cover);
                    Log2Cover.getBackground().setAlpha(100);
                    Log2Cover.setOnClickListener(btnListener);
                    Log3Cover= (Button)findViewById(R.id.Log3Cover);
                    Log3Cover.getBackground().setAlpha(100);
                    Log3Cover.setOnClickListener(btnListener);
                    System.out.println("開始讀資料庫");
                    save= db.rawQuery("SELECT Save_index,Save_name " +
                            " FROM "+tb_save,null);
                    System.out.println("資料庫讀完");
                    if(save.getCount()!=0){
                        if(save.moveToFirst()) {
                            do {
                                switch (save.getString(0)){
                                    case "1":
                                        System.out.println("開始寫名稱");
                                        Log1Cover.setText(save.getString(1));
                                        break;
                                    case "2":
                                        Log2Cover.setText(save.getString(1));
                                        break;
                                    case "3":
                                        Log3Cover.setText(save.getString(1));
                                        break;
                                }
                            } while (save.moveToNext());
                        }
                    }
                    break;
                case R.id.BackpackComeback:
                    setContentView(R.layout.game);
                    Menu= (Button)findViewById(R.id.Menu);
                    Menu.setOnClickListener(btnListener);
                    Backpack= (Button)findViewById(R.id.Backpack);
                    Backpack.setOnClickListener(btnListener);
                    printNext= (Button)findViewById(R.id.printNext);
                    printNext.setOnClickListener(btnListener);
                    printNext.getBackground().setAlpha(0);
                    Subtitle=(TextView)findViewById(R.id.Subtitle);
                    break;
                case R.id.SubOn:
                    suballow=0;
                    SubOn= (Button)findViewById(R.id.SubOn);
                    SubOn.setEnabled(false);
                    SubOff= (Button)findViewById(R.id.SubOff);
                    SubOff.setEnabled(true);
                    SubOn.getBackground().setAlpha(50);
                    SubOff.getBackground().setAlpha(100);
                    break;
                case R.id.SubOff:
                    suballow=1;
                    SubOn= (Button)findViewById(R.id.SubOn);
                    SubOn.setEnabled(true);
                    SubOff= (Button)findViewById(R.id.SubOff);
                    SubOff.setEnabled(false);
                    SubOn.getBackground().setAlpha(100);
                    SubOff.getBackground().setAlpha(50);
                    break;
                case R.id.LogUse:
                    record[0]=1;
                    record[1]=1;
                    save= db.rawQuery("SELECT *" +
                        " FROM "+tb_save+" WHERE Save_index = "+lognumber,null);
                    if(save.getCount()!=0){
                        if(save.moveToFirst()) {
                            record[0]= Integer.parseInt(save.getString(1));
                            record[1]= Integer.parseInt(save.getString(2));
                            mainloop=0;
                        }
                    }
                    setContentView(R.layout.game);
                    Menu= (Button)findViewById(R.id.Menu);
                    Menu.setOnClickListener(btnListener);
                    Backpack= (Button)findViewById(R.id.Backpack);
                    Backpack.setOnClickListener(btnListener);
                    printNext= (Button)findViewById(R.id.printNext);
                    printNext.setOnClickListener(btnListener);
                    printNext.getBackground().setAlpha(0);
                    Subtitle=(TextView)findViewById(R.id.Subtitle);
                    break;
                case R.id.Pick:
                    setContentView(R.layout.game);
                    Menu= (Button)findViewById(R.id.Menu);
                    Menu.setOnClickListener(btnListener);
                    Backpack= (Button)findViewById(R.id.Backpack);
                    Backpack.setOnClickListener(btnListener);
                    printNext= (Button)findViewById(R.id.printNext);
                    printNext.setOnClickListener(btnListener);
                    printNext.getBackground().setAlpha(0);
                    Subtitle=(TextView)findViewById(R.id.Subtitle);
                    pick=0;
                    break;
                case R.id.printNext:
                    if(MainActivity.onclicktime==-1){
                        MainActivity.onclicktime=System.currentTimeMillis();
                    }else{
                        long currenttime=System.currentTimeMillis();
                        if(currenttime-MainActivity.onclicktime<1000){
                            return;
                        }else{
                            MainActivity.onclicktime=System.currentTimeMillis();
                        }
                    }
                    Subtitle=(TextView)findViewById(R.id.Subtitle);
                    Subtitle.setText("");
                    MainLoop();
                    break;
                case R.id.NoPick:
                    setContentView(R.layout.game);
                    Menu= (Button)findViewById(R.id.Menu);
                    Menu.setOnClickListener(btnListener);
                    Backpack= (Button)findViewById(R.id.Backpack);
                    Backpack.setOnClickListener(btnListener);
                    printNext= (Button)findViewById(R.id.printNext);
                    printNext.setOnClickListener(btnListener);
                    printNext.getBackground().setAlpha(0);
                    Subtitle=(TextView)findViewById(R.id.Subtitle);
                    pick=1;
                    record[0] = 2;
                    record[1] = 13;
                    mainloop=0;
                    break;
                case R.id.Go:
                    if(MainActivity.onclicktime==-1){
                        MainActivity.onclicktime=System.currentTimeMillis();
                    }else{
                        long currenttime=System.currentTimeMillis();
                        if(currenttime-MainActivity.onclicktime<1000){
                            return;
                        }else{
                            MainActivity.onclicktime=System.currentTimeMillis();
                        }
                    }
                    switch(face){
                        case 0:
                            if(mper!= null && mper.isPlaying()){
                                mper.stop();
                            }
                            if((guy[0]-1)<0 || Integer.parseInt(maparray[guy[0]-1][guy[1]])!=Integer.parseInt(canmove)) {
                                Sound("sisobstacle");
                            }else{
                                guy[0]--;
                                System.out.println("座標改變");
                                for (int i = -1; i <= 1; i++) {
                                    for (int j = -1; j <= 1; j++) {
                                        if(guy[0] + i<y && guy[0] + i>=0 && guy[1] + j<x && guy[1] + j>=0){
                                            if (Integer.parseInt(maparray[guy[0] + i][guy[1] + j]) == Integer.parseInt(finish)) {
                                                if (mper != null && mper.isPlaying()) {
                                                    mper.stop();
                                            }
                                                System.out.println("到終點了");
                                                setContentView(R.layout.game);
                                                Menu= (Button)findViewById(R.id.Menu);
                                                Menu.setOnClickListener(btnListener);
                                                Backpack= (Button)findViewById(R.id.Backpack);
                                                Backpack.setOnClickListener(btnListener);
                                                printNext= (Button)findViewById(R.id.printNext);
                                                printNext.setOnClickListener(btnListener);
                                                printNext.getBackground().setAlpha(0);
                                                Subtitle=(TextView)findViewById(R.id.Subtitle);
                                                return;
                                            }
                                        }
                                    }

                                }
                                Sound("map"+maparray[guy[0]][guy[1]]);
                                System.out.println("播放現在");
                                for (int i = -2; i <= 2; i++) {
                                    for (int j = -2; j <= 2; j++) {
                                        for(int k=0;k<soundarry.length;k++){
                                            if(guy[0] + i<y && guy[0] + i>=0 && guy[1] + j<x && guy[1] + j>=0){
                                                System.out.println("i"+i+",j"+j+",k"+k+",位置"+(guy[0] + i)+","+(guy[1] + j));
                                                if (Integer.parseInt(maparray[guy[0] + i][guy[1] + j]) == Integer.parseInt(soundarry[k])) {
                                                    soundlocation="map"+soundarry[k];
                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable(){
                                                        @Override
                                                        public void run() {
                                                            //過兩秒後要做的事情
                                                            System.out.println("播放周圍1");
                                                            Sound(soundlocation);
                                                            System.out.println("播放周圍2");
                                                        }}, 2000);
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            System.currentTimeMillis();
                            break;
                        case 1:
                            if(mper!= null && mper.isPlaying()){
                                mper.stop();
                            }
                            if(guy[1]+1>x || Integer.parseInt(maparray[guy[0]][guy[1]+1])!=Integer.parseInt(canmove)) {
                                Sound("sisobstacle");
                            }else {
                                guy[1]++;
                                System.out.println("座標改變");
                                for (int i = -1; i <= 1; i++) {
                                    for (int j = -1; j <= 1; j++) {
                                        if(guy[0] + i<y && guy[0] + i>=0 && guy[1] + j<x && guy[1] + j>=0){
                                            if (Integer.parseInt(maparray[guy[0] + i][guy[1] + j]) == Integer.parseInt(finish)) {
                                                if (mper != null && mper.isPlaying()) {
                                                    mper.stop();
                                                }System.out.println("到終點了");
                                                setContentView(R.layout.game);
                                                Menu= (Button)findViewById(R.id.Menu);
                                                Menu.setOnClickListener(btnListener);
                                                Backpack= (Button)findViewById(R.id.Backpack);
                                                Backpack.setOnClickListener(btnListener);
                                                printNext= (Button)findViewById(R.id.printNext);
                                                printNext.setOnClickListener(btnListener);
                                                printNext.getBackground().setAlpha(0);
                                                Subtitle=(TextView)findViewById(R.id.Subtitle);
                                                System.out.println("畫面");
                                                return;
                                            }
                                        }
                                    }
                                }
                                Sound("map"+maparray[guy[0]][guy[1]]);
                                System.out.println("播放現在");
                                for (int i = -2; i <= 2; i++) {
                                    for (int j = -2; j <= 2; j++) {
                                        for(int k=0;k<soundarry.length;k++){
                                            if(guy[0] + i<y && guy[0] + i>=0 && guy[1] + j<x && guy[1] + j>=0){
                                                System.out.println("i"+i+",j"+j+",k"+k+",位置"+(guy[0] + i)+","+(guy[1] + j));
                                                if (Integer.parseInt(maparray[guy[0] + i][guy[1] + j]) == Integer.parseInt(soundarry[k])) {
                                                    soundlocation="map"+soundarry[k];
                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable(){
                                                        @Override
                                                        public void run() {
                                                            //過兩秒後要做的事情
                                                            System.out.println("播放周圍1");
                                                            Sound(soundlocation);
                                                            System.out.println("播放周圍2");
                                                        }}, 2000);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        case 2:
                            if(mper!= null && mper.isPlaying()){
                                mper.stop();
                            }
                            if(guy[0]+1>y || Integer.parseInt(maparray[guy[0]+1][guy[1]])!=Integer.parseInt(canmove)) {
                                Sound("sisobstacle");
                            }else {
                                guy[0]++;
                                System.out.println("座標改變");
                                for (int i = -1; i <= 1; i++) {
                                    for (int j = -1; j <= 1; j++) {
                                        if(guy[0] + i<y && guy[0] + i>=0 && guy[1] + j<x && guy[1] + j>=0){
                                            if (Integer.parseInt(maparray[guy[0] + i][guy[1] + j]) == Integer.parseInt(finish)) {
                                                if (mper != null && mper.isPlaying()) {
                                                    mper.stop();
                                                }System.out.println("到終點了");
                                                setContentView(R.layout.game);
                                                Menu= (Button)findViewById(R.id.Menu);
                                                Menu.setOnClickListener(btnListener);
                                                Backpack= (Button)findViewById(R.id.Backpack);
                                                Backpack.setOnClickListener(btnListener);
                                                printNext= (Button)findViewById(R.id.printNext);
                                                printNext.setOnClickListener(btnListener);
                                                printNext.getBackground().setAlpha(0);
                                                Subtitle=(TextView)findViewById(R.id.Subtitle);
                                                return;
                                            }
                                        }
                                    }
                                }
                                Sound("map"+maparray[guy[0]][guy[1]]);
                                System.out.println("播放現在");
                                for (int i = -2; i <= 2; i++) {
                                    for (int j = -2; j <= 2; j++) {
                                        for(int k=0;k<soundarry.length;k++){
                                            if(guy[0] + i<y && guy[0] + i>=0 && guy[1] + j<x && guy[1] + j>=0){
                                                System.out.println("i"+i+",j"+j+",k"+k+",位置"+(guy[0] + i)+","+(guy[1] + j));
                                                if (Integer.parseInt(maparray[guy[0] + i][guy[1] + j]) == Integer.parseInt(soundarry[k])) {
                                                    soundlocation="map"+soundarry[k];
                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable(){
                                                        @Override
                                                        public void run() {
                                                            //過兩秒後要做的事情
                                                            System.out.println("播放周圍1");
                                                            Sound(soundlocation);
                                                            System.out.println("播放周圍2");
                                                        }}, 2000);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        case 3:
                            if(mper!= null && mper.isPlaying()){
                                mper.stop();
                            }
                            if(guy[1]-1<0 || Integer.parseInt(maparray[guy[0]][guy[1]-1])!=Integer.parseInt(canmove)) {
                                Sound("sisobstacle");
                            }else {
                                guy[1]--;
                                System.out.println("座標改變");
                                for (int i = -1; i <= 1; i++) {
                                    for (int j = -1; j <= 1; j++) {
                                        if(guy[0] + i<y && guy[0] + i>=0 && guy[1] + j<x && guy[1] + j>=0){
                                            if (Integer.parseInt(maparray[guy[0] + i][guy[1] + j]) == Integer.parseInt(finish)) {
                                                if (mper != null && mper.isPlaying()) {
                                                    mper.stop();
                                                }
                                                System.out.println("到終點了");
                                                setContentView(R.layout.game);
                                                Menu= (Button)findViewById(R.id.Menu);
                                                Menu.setOnClickListener(btnListener);
                                                Backpack= (Button)findViewById(R.id.Backpack);
                                                Backpack.setOnClickListener(btnListener);
                                                printNext= (Button)findViewById(R.id.printNext);
                                                printNext.setOnClickListener(btnListener);
                                                printNext.getBackground().setAlpha(0);
                                                Subtitle=(TextView)findViewById(R.id.Subtitle);
                                                return;
                                            }
                                        }
                                    }
                                }
                                Sound("map"+maparray[guy[0]][guy[1]]);
                                System.out.println("播放現在");
                                for (int i = -2; i <= 2; i++) {
                                    for (int j = -2; j <= 2; j++) {
                                        for(int k=0;k<soundarry.length;k++){
                                            if(guy[0] + i<y && guy[0] + i>=0 && guy[1] + j<x && guy[1] + j>=0){
                                                System.out.println("i"+i+",j"+j+",k"+k+",位置"+(guy[0] + i)+","+(guy[1] + j));
                                                if (Integer.parseInt(maparray[guy[0] + i][guy[1] + j]) == Integer.parseInt(soundarry[k])) {
                                                    soundlocation="map"+soundarry[k];
                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable(){
                                                        @Override
                                                        public void run() {
                                                            //過兩秒後要做的事情
                                                            System.out.println("播放周圍1");
                                                            Sound(soundlocation);
                                                            System.out.println("播放周圍2");
                                                        }}, 2000);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                    }
                    break;
                case R.id.left:
                    if(MainActivity.onclicktime==-1){
                        MainActivity.onclicktime=System.currentTimeMillis();
                    }else{
                        long currenttime=System.currentTimeMillis();
                        if(currenttime-MainActivity.onclicktime<1000){
                            return;
                        }else{
                            MainActivity.onclicktime=System.currentTimeMillis();
                        }
                    }
                    if(face-1<0){
                        face=3;
                    }else{
                        face--;
                    }
                    break;
                case R.id.right:
                    if(MainActivity.onclicktime==-1){
                        MainActivity.onclicktime=System.currentTimeMillis();
                    }else{
                        long currenttime=System.currentTimeMillis();
                        if(currenttime-MainActivity.onclicktime<1000){
                            return;
                        }else{
                            MainActivity.onclicktime=System.currentTimeMillis();
                        }
                    }
                    if(face+1>3){
                        face=0;
                    }else{
                        face++;
                    }
                    break;
                case R.id.Compass:
                    if(MainActivity.onclicktime==-1){
                        MainActivity.onclicktime=System.currentTimeMillis();
                    }else{
                        long currenttime=System.currentTimeMillis();
                        if(currenttime-MainActivity.onclicktime<3000){
                            return;
                        }else{
                            MainActivity.onclicktime=System.currentTimeMillis();
                        }
                    }
                    if(Math.abs(destination[0]-guy[0])>Math.abs(destination[1]-guy[1])){
                        if(destination[0]-guy[0]>0){
                            if(face==0){
                                if(mper!= null && mper.isPlaying()){
                                    mper.stop();
                                }
                                Sound("sisbehind");
                            }else if(face==1){
                                if(mper!= null && mper.isPlaying()){
                                    mper.stop();
                                }
                                Sound("sisright");
                            }else if(face==2){
                                if(mper!= null && mper.isPlaying()){
                                    mper.stop();
                                }
                                Sound("sisfront");
                            }else if(face==3){
                                if(mper!= null && mper.isPlaying()){
                                    mper.stop();
                                }
                                Sound("sisleft");
                            }
                        }else if(destination[0]-guy[0]<0) {
                            if (face == 0) {
                                Sound("sisfront");
                            } else if (face == 1) {
                                Sound("sisleft");
                            } else if (face == 2) {
                                Sound("sisbehind");
                            } else if (face == 3) {
                                Sound("sisright");
                            }
                        }
                    }else if(Math.abs(destination[1]-guy[1])>Math.abs(destination[0]-guy[0])){
                    if (destination[1]-guy[1] > 0) {
                        if (face == 0) {
                            Sound("sisright");
                        } else if (face == 1) {
                            Sound("sisfront");
                        } else if (face == 2) {
                            Sound("sisleft");
                        } else if (face == 3) {
                            Sound("sisbehind");
                        }
                    } else if (Math.abs(destination[0]-guy[0]) < 0) {
                        if (face == 0) {
                            Sound("sisleft");
                        } else if (face == 1) {
                            Sound("sisbehind");
                        } else if (face == 2) {
                            Sound("sisright");
                        } else if (face == 3) {
                            Sound("sisfront");
                        }
                    }
                }
                break;
            }
        }

    //建立資料庫
    private void Createdatabase() {

        db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
        CreateScriptTable ();
        CreateTalkTable ();
        CreateVoiceTable ();
        CreateWordTable ();
        CreateMapTable ();
        CreateNextTable ();
        CreateSaveTable ();
        CreateBagTable ();
        CreateToolTable ();
        CreateVoiceData();
        CreateMapData();
    }
    private void CreateScriptTable (){
        String createScriptTable = "CREATE TABLE IF NOT EXISTS " + tb_script +
                "(Script_index_num INTEGER, " +
                "Script_chapter_id INTEGER, " +
                "Script_section_id INTEGER, " +
                "Script_id INTEGER, " +
                "Script_attribute INTEGER, " +
                "Script_parameter INTEGER, " +
                "Script_content NVARCHAR(20), " +
                "Script_time INTEGER)";
        db.execSQL(createScriptTable);
        Cursor script= db.rawQuery("SELECT * FROM "+tb_script,null);
        if(script.getCount()==0){
            try {
                InputStream is = getResources().openRawResource(R.raw.script);
                String scripttext = readTextFromSDcard(is);
                String[] scriptdata = scripttext.split("[,;\n]");
                for(int i = 0 ; i<scriptdata.length ; i=i+8){
                    ContentValues cv = new ContentValues();
                    cv.put("Script_index_num",scriptdata[i]);
                    cv.put("Script_chapter_id",scriptdata[i+1]);
                    cv.put("Script_section_id",scriptdata[i+2]);
                    cv.put("Script_id",scriptdata[i+3]);
                    cv.put("Script_attribute",scriptdata[i+4]);
                    cv.put("Script_parameter",scriptdata[i+5]);
                    cv.put("Script_content",scriptdata[i+6]);
                    cv.put("Script_time",scriptdata[i+7]);
                    long a = db.insert(tb_script,null,cv);
                    System.out.println("字串i="+i);
                    System.out.println("db值="+a);
                }
                script= db.rawQuery("SELECT * FROM "+tb_script,null);
                System.out.println("scriptdata.length="+scriptdata.length);
                if(script.moveToFirst()) {
                    do {
                        for (int j = 0; j < scriptdata.length; j++) {
                            System.out.println(script.getString(j));
                        }
                    } while (script.moveToNext());
                }
            }catch (Exception e) {
                System.out.println("Read File Error!");
            }
            script = db.rawQuery("SELECT * FROM "+tb_script,null);
        }
    }
    private void CreateTalkTable (){
        String createTalkTable = "CREATE TABLE IF NOT EXISTS " + tb_talk +
                "(Talk_id INTEGER, " +
                "Talk_name NVARCHAR(20), " +
                "Talk_voice_name NVARCHAR(20))";
        db.execSQL(createTalkTable);
        Cursor talk= db.rawQuery("SELECT * FROM "+tb_talk,null);
        if(talk.getCount()==0){
            try {
                InputStream is = getResources().openRawResource(R.raw.talk);
                String talktext = readTextFromSDcard(is);
                String[] talkdata = talktext.split("[,;\n]");
                for(int i = 1 ; i<talkdata.length ; i=i+3){
                    ContentValues cv = new ContentValues();
                    cv.put("Talk_id",talkdata[i-1]);
                    cv.put("Talk_name",talkdata[i]);
                    cv.put("Talk_voice_name",talkdata[i+1]);
                    db.insert(tb_talk,null,cv);
                    System.out.println(i);
                }
                talk= db.rawQuery("SELECT * FROM "+tb_talk,null);
                if(talk.moveToFirst()) {
                    do {
                        for (int j = 0; j < talkdata.length; j++) {
                            System.out.println(talk.getString(j));
                        }
                    } while (talk.moveToNext());
                }
            }catch (Exception e) {
                System.out.println("Read File Error!");
            }
            talk = db.rawQuery("SELECT * FROM "+tb_talk,null);
        }
    }
    private void CreateVoiceTable (){
        String createVoiceTable = "CREATE TABLE IF NOT EXISTS " + tb_voice +
                "(Voice_index INTEGER, " +
                "Voice_content NVARCHAR(10), " +
                "Voice_file NVARCHAR(10))";
        db.execSQL(createVoiceTable);
        Cursor voice= db.rawQuery("SELECT * FROM "+tb_voice,null);
        if(voice.getCount()==0){
            try {
                InputStream is = getResources().openRawResource(R.raw.voice);
                String voicetext = readTextFromSDcard(is);
                String[] voicedata = voicetext.split("[,;\n]");
                for(int i = 1 ; i<voicedata.length ; i=i+3){
                    ContentValues cv = new ContentValues();
                    cv.put("Voice_index",voicedata[i-1]);
                    cv.put("Voice_content",voicedata[i]);
                    cv.put("Voice_file",voicedata[i+1]);
                    db.insert(tb_voice,null,cv);
                    System.out.println(i);
                }
                voice= db.rawQuery("SELECT * FROM "+tb_voice,null);
                if(voice.moveToFirst()) {
                    do {
                        for (int j = 0; j < voicedata.length; j++) {
                            System.out.println(voice.getString(j));
                        }
                    } while (voice.moveToNext());
                }
            }catch (Exception e) {
                System.out.println("Read File Error!");
            }
            voice = db.rawQuery("SELECT * FROM "+tb_voice,null);
        }
    }
    private void CreateWordTable (){
        String createWordTable = "CREATE TABLE IF NOT EXISTS " + tb_word +
                "(Word_id INTEGER, " +
                "Word_content NVARCHAR(50)," +
                "Word_description_file NVARCHAR(10))";
        db.execSQL(createWordTable);
        Cursor word= db.rawQuery("SELECT * FROM "+tb_word,null);
        if(word.getCount()==0){
            try {
                InputStream is = getResources().openRawResource(R.raw.word);
                String wordtext = readTextFromSDcard(is);
                String[] worddata = wordtext.split("[,;\n]");
                for(int i = 1 ; i<worddata.length ; i=i+3){
                    ContentValues cv = new ContentValues();
                    cv.put("Word_id",worddata[i-1]);
                    cv.put("Word_content",worddata[i]);
                    cv.put("Word_description_file",worddata[i+1]);
                    db.insert(tb_word,null,cv);
                    System.out.println(i);
                }
                word= db.rawQuery("SELECT * FROM "+tb_word,null);
                if(word.moveToFirst()) {
                    do {
                        for (int j = 0; j < worddata.length; j++) {
                            System.out.println(word.getString(j));
                        }
                    } while (word.moveToNext());
                }
            }catch (Exception e) {
                System.out.println("Read File Error!");
            }
            word = db.rawQuery("SELECT * FROM "+tb_word,null);
        }
    }
    private void CreateMapTable (){
        String createMapTable = "CREATE TABLE IF NOT EXISTS " + tb_map +
                "(Map_index INTEGER, " +
                "Map_content NVARCHAR(10), " +
                "Map_file NVARCHAR(10))";
        db.execSQL(createMapTable);
        Cursor map= db.rawQuery("SELECT * FROM "+tb_map,null);
        if(map.getCount()==0){
            try {
                InputStream is = getResources().openRawResource(R.raw.map);
                String maptext = readTextFromSDcard(is);
                String[] mapdata = maptext.split("[,;\n]");
                for(int i = 0 ; i<mapdata.length ; i=i+3){
                    ContentValues cv = new ContentValues();
                    cv.put("Map_index",mapdata[i]);
                    cv.put("Map_content",mapdata[i+1]);
                    cv.put("Map_file",mapdata[i+2]);
                    db.insert(tb_map,null,cv);
                    System.out.println(i);
                }
                map= db.rawQuery("SELECT * FROM "+tb_map,null);
                if(map.moveToFirst()) {
                    do {
                        for (int j = 0; j < mapdata.length; j++) {
                            System.out.println(map.getString(j));
                        }
                    } while (map.moveToNext());
                }
            }catch (Exception e) {
                System.out.println("Read File Error!");
            }
            map = db.rawQuery("SELECT * FROM "+tb_map,null);
        }
    }
    private void CreateNextTable (){
        String createNextTable = "CREATE TABLE IF NOT EXISTS " + tb_next +
                "(Next_index_num INTEGER, " +
                "Next_chapter_now INTEGER, " +
                "Next_section_now INTEGER, " +
                "Next_chapter_id INTEGER, " +
                "Next_nextsection_id INTEGER," +
                "Next_content NVARCHAR(50))";
        db.execSQL(createNextTable);
        Cursor next= db.rawQuery("SELECT * FROM "+tb_next,null);
        if(next.getCount()==0){
            try {
                InputStream is = getResources().openRawResource(R.raw.next);
                String nexttext = readTextFromSDcard(is);
                String[] nextdata = nexttext.split("[,;\n]");
                for(int i = 0 ; i<nextdata.length ; i=i+6){
                    ContentValues cv = new ContentValues();
                    cv.put("Next_index_num",nextdata[i]);
                    cv.put("Next_chapter_now",nextdata[i+1]);
                    cv.put("Next_section_now",nextdata[i+2]);
                    cv.put("Next_chapter_id",nextdata[i+3]);
                    cv.put("Next_nextsection_id",nextdata[i+4]);
                    cv.put("Next_content",nextdata[i+5]);
                    db.insert(tb_next,null,cv);
                    System.out.println(i);
                }
                next= db.rawQuery("SELECT * FROM "+tb_next,null);
                if(next.moveToFirst()) {
                    do {
                        for (int j = 0; j < nextdata.length; j++) {
                            System.out.println(next.getString(j));
                        }
                    } while (next.moveToNext());
                }
            }catch (Exception e) {
                System.out.println("Read File Error!");
            }
            next = db.rawQuery("SELECT * FROM "+tb_next,null);
        }
    }
    private void CreateSaveTable (){
        String createSaveTable = "CREATE TABLE IF NOT EXISTS " + tb_save +
                "(Save_index INTEGER, " +
                "Save_chapter INTEGER, " +
                "Save_section INTEGER," +
                "Save_name NVARCHAR(20))";
        db.execSQL(createSaveTable);
        Cursor save= db.rawQuery("SELECT * FROM "+tb_save,null);
        if(save.getCount()==0){
            ContentValues cv = new ContentValues();
            cv.put("Save_index","1");
            cv.put("Save_chapter","2");
            cv.put("Save_section","4");
            cv.put("Save_name","測試紀錄檔 1");
            db.insert(tb_save,null,cv);
            cv.put("Save_index","2");
            cv.put("Save_chapter","2");
            cv.put("Save_section","11");
            cv.put("Save_name","測試紀錄檔 2");
            db.insert(tb_save,null,cv);
            save= db.rawQuery("SELECT * FROM "+tb_save,null);
                if(save.moveToFirst()) {
                    do {
                        for (int j = 0; j < 4; j++) {
                            System.out.println(save.getString(j));
                        }
                    } while (save.moveToNext());
                }

            save = db.rawQuery("SELECT * FROM "+tb_save,null);
        }
    }
    private void CreateBagTable (){
        String createBagTable = "CREATE TABLE IF NOT EXISTS " + tb_bag +
                "(Bag_id INTEGER, " +
                "Tool_id INTEGER," +
                "User_id INTEGER)";
        db.execSQL(createBagTable);
        Cursor bag= db.rawQuery("SELECT * FROM "+tb_bag,null);


    }
    private void CreateToolTable (){
        String createToolTable = "CREATE TABLE IF NOT EXISTS " + tb_tool +
                "(Tool_id INTEGER, " +
                "Tool_name NVARCHAR(10), " +
                "Tool_content NVARCHAR(20), " +
                "Tool_voice NVARCHAR(10))";
        db.execSQL(createToolTable);
        Cursor tool= db.rawQuery("SELECT * FROM "+tb_tool,null);
        if(tool.getCount()==0){
            try {
                InputStream is = getResources().openRawResource(R.raw.tool);
                String tooltext = readTextFromSDcard(is);
                String[] tooldata = tooltext.split("[,;\n]");
                for(int i = 0 ; i<tooldata.length ; i=i+4){
                    ContentValues cv = new ContentValues();
                    cv.put("Tool_id",tooldata[i]);
                    cv.put("Tool_name",tooldata[i+1]);
                    cv.put("Tool_content",tooldata[i+2]);
                    cv.put("Tool_voice",tooldata[i+3]);
                    db.insert(tb_tool,null,cv);
                    System.out.println(i);
                }
                tool= db.rawQuery("SELECT * FROM "+tb_tool,null);
                if(tool.moveToFirst()) {
                    do {
                        for (int j = 0; j < tooldata.length; j++) {
                            System.out.println(tool.getString(j));
                        }
                    } while (tool.moveToNext());
                }
            }catch (Exception e) {
                System.out.println("Read File Error!");
            }
            tool = db.rawQuery("SELECT * FROM "+tb_tool,null);
        }
    }
    private void CreateVoiceData() {
        VoiceMap.put("prologuetalk1", R.raw.prologuetalk1);
        VoiceMap.put("prologuetalk2", R.raw.prologuetalk2);
        VoiceMap.put("prologuetalk3", R.raw.prologuetalk3);
        VoiceMap.put("prologuetalk4", R.raw.prologuetalk4);
        VoiceMap.put("prologuetalk5", R.raw.prologuetalk5);
        VoiceMap.put("prologuetalk6", R.raw.prologuetalk6);
        VoiceMap.put("prologuetalk7", R.raw.prologuetalk7);
        VoiceMap.put("prologuetalk8", R.raw.prologuetalk8);
        VoiceMap.put("prologuetalk9", R.raw.prologuetalk9);
        VoiceMap.put("prologuetalk10", R.raw.prologuetalk10);
        VoiceMap.put("prologuetalk11", R.raw.prologuetalk11);
        VoiceMap.put("prologuetalk12", R.raw.prologuetalk12);
        VoiceMap.put("prologuetalk13", R.raw.prologuetalk13);
        VoiceMap.put("prologuetalk14", R.raw.prologuetalk14);
        VoiceMap.put("sectiononetalk1", R.raw.sectiononetalk1);
        VoiceMap.put("sectiononetalk2", R.raw.sectiononetalk2);
        VoiceMap.put("sectiononetalk3", R.raw.sectiononetalk3);
        VoiceMap.put("sectiononetalk4", R.raw.sectiononetalk4);
        VoiceMap.put("sectiononetalk5", R.raw.sectiononetalk5);
        VoiceMap.put("sectiononetalk6", R.raw.sectiononetalk6);
        VoiceMap.put("sectiononetalk7", R.raw.sectiononetalk7);
        VoiceMap.put("sectiononetalk8", R.raw.sectiononetalk8);
        VoiceMap.put("sectiononetalk9", R.raw.sectiononetalk9);
        VoiceMap.put("sectiononetalk10", R.raw.sectiononetalk10);
        VoiceMap.put("sectiononetalk11", R.raw.sectiononetalk11);
        VoiceMap.put("sectiononetalk12", R.raw.sectiononetalk12);
        VoiceMap.put("sectiononetalk13", R.raw.sectiononetalk13);
        VoiceMap.put("sectiononetalk14", R.raw.sectiononetalk14);
        VoiceMap.put("sectiononetalk15", R.raw.sectiononetalk15);
        VoiceMap.put("sectiononetalk16", R.raw.sectiononetalk16);
        VoiceMap.put("sectiontwotalk1", R.raw.sectiontwotalk1);
        VoiceMap.put("sectiontwotalk2", R.raw.sectiontwotalk2);
        VoiceMap.put("sectiontwotalk3", R.raw.sectiontwotalk3);
        VoiceMap.put("sectiontwotalk4", R.raw.sectiontwotalk4);
        VoiceMap.put("sectiontwotalk5", R.raw.sectiontwotalk5);
        VoiceMap.put("sectionthreetalk1", R.raw.sectionthreetalk1);
        VoiceMap.put("sectionthreetalk2", R.raw.sectionthreetalk2);
        VoiceMap.put("sectionthreetalk3", R.raw.sectionthreetalk3);
        VoiceMap.put("sectionthreetalk4", R.raw.sectionthreetalk4);
        VoiceMap.put("sectionthreetalk5", R.raw.sectionthreetalk5);
        VoiceMap.put("sectionfourtalk1", R.raw.sectionfourtalk1);
        VoiceMap.put("sectionfourtalk2", R.raw.sectionfourtalk2);
        VoiceMap.put("sectionfourtalk3", R.raw.sectionfourtalk3);
        VoiceMap.put("sectionfourtalk4", R.raw.sectionfourtalk4);
        VoiceMap.put("sectionfourtalk5", R.raw.sectionfourtalk5);
        VoiceMap.put("sectionfourtalk6", R.raw.sectionfourtalk6);
        VoiceMap.put("sectionfivetalk1", R.raw.sectionfivetalk1);
        VoiceMap.put("sectionfivetalk2", R.raw.sectionfivetalk2);
        VoiceMap.put("sectionfivetalk3", R.raw.sectionfivetalk3);
        VoiceMap.put("sectionfivetalk4", R.raw.sectionfivetalk4);
        VoiceMap.put("sectionfivetalk5", R.raw.sectionfivetalk5);
        VoiceMap.put("sectionfivetalk6", R.raw.sectionfivetalk6);
        VoiceMap.put("sectionfivetalk7", R.raw.sectionfivetalk7);
        VoiceMap.put("sectionfivetalk8", R.raw.sectionfivetalk8);
        VoiceMap.put("sectionfivetalk9", R.raw.sectionfivetalk9);
        VoiceMap.put("sectionfivetalk10", R.raw.sectionfivetalk10);
        VoiceMap.put("sectionfivetalk11", R.raw.sectionfivetalk11);
        VoiceMap.put("sectionfivetalk12", R.raw.sectionfivetalk12);
        VoiceMap.put("sectionfivetalk13", R.raw.sectionfivetalk13);
        VoiceMap.put("sectionfivetalk14", R.raw.sectionfivetalk14);
        VoiceMap.put("sectionfivetalk15", R.raw.sectionfivetalk15);
        VoiceMap.put("sectionfivetalk16", R.raw.sectionfivetalk16);
        VoiceMap.put("sectionfivetalk17", R.raw.sectionfivetalk17);
        VoiceMap.put("sectionfivetalk18", R.raw.sectionfivetalk18);
        VoiceMap.put("sectionsixtalk1", R.raw.sectionsixtalk1);
        VoiceMap.put("sectionsixtalk2", R.raw.sectionsixtalk2);
        VoiceMap.put("sectionsixtalk3", R.raw.sectionsixtalk3);
        VoiceMap.put("sectionsixtalk4", R.raw.sectionsixtalk4);
        VoiceMap.put("sectionsixtalk5", R.raw.sectionsixtalk5);
        VoiceMap.put("sectionsixtalk6", R.raw.sectionsixtalk6);
        VoiceMap.put("sectionsixtalk7", R.raw.sectionsixtalk7);
        VoiceMap.put("sectionsixtalk8", R.raw.sectionsixtalk8);
        VoiceMap.put("sectionsixtalk9", R.raw.sectionsixtalk9);
        VoiceMap.put("sectionsixtalk10", R.raw.sectionsixtalk10);
        VoiceMap.put("sectionsixtalk11", R.raw.sectionsixtalk11);
        VoiceMap.put("sectionsixtalk12", R.raw.sectionsixtalk12);
        VoiceMap.put("sectionsixtalk13", R.raw.sectionsixtalk13);
        VoiceMap.put("sectionsixtalk14", R.raw.sectionsixtalk14);
        VoiceMap.put("sectionsixtalk15", R.raw.sectionsixtalk15);
        VoiceMap.put("sectionsixtalk16", R.raw.sectionsixtalk16);
        VoiceMap.put("sectionsixtalk17", R.raw.sectionsixtalk17);
        VoiceMap.put("sectionsixtalk18", R.raw.sectionsixtalk18);
        VoiceMap.put("sectionseventalk1", R.raw.sectionseventalk1);
        VoiceMap.put("sectionseventalk2", R.raw.sectionseventalk2);
        VoiceMap.put("sectionseventalk3", R.raw.sectionseventalk3);
        VoiceMap.put("sectionseventalk4", R.raw.sectionseventalk4);
        VoiceMap.put("sectionseventalk5", R.raw.sectionseventalk5);
        VoiceMap.put("sectionseventalk6", R.raw.sectionseventalk6);
        VoiceMap.put("sectionseventalk7", R.raw.sectionseventalk7);
        VoiceMap.put("sectioneighttalk1", R.raw.sectioneighttalk1);
        VoiceMap.put("sectioneighttalk2", R.raw.sectioneighttalk2);
        VoiceMap.put("sectioneighttalk3", R.raw.sectioneighttalk3);
        VoiceMap.put("sectioneighttalk4", R.raw.sectioneighttalk4);
        VoiceMap.put("sectioneighttalk5", R.raw.sectioneighttalk5);
        VoiceMap.put("sectioneighttalk6", R.raw.sectioneighttalk6);
        VoiceMap.put("sectioneighttalk7", R.raw.sectioneighttalk7);
        VoiceMap.put("sectioneighttalk8", R.raw.sectioneighttalk8);
        VoiceMap.put("sectioneighttalk9", R.raw.sectioneighttalk9);
        VoiceMap.put("sectioneighttalk10", R.raw.sectioneighttalk10);
        VoiceMap.put("sectioneighttalk11", R.raw.sectioneighttalk11);
        VoiceMap.put("sectioneighttalk12", R.raw.sectioneighttalk12);
        VoiceMap.put("sectioneighttalk13", R.raw.sectioneighttalk13);
        VoiceMap.put("sectioneighttalk14", R.raw.sectioneighttalk14);
        VoiceMap.put("sectioneighttalk15", R.raw.sectioneighttalk15);
        VoiceMap.put("sectioneighttalk16", R.raw.sectioneighttalk16);
        VoiceMap.put("sectioneighttalk17", R.raw.sectioneighttalk17);
        VoiceMap.put("sectioneighttalk18", R.raw.sectioneighttalk18);
        VoiceMap.put("sectioneighttalk19", R.raw.sectioneighttalk19);
        VoiceMap.put("sectioneighttalk20", R.raw.sectioneighttalk20);
        VoiceMap.put("sectioneighttalk21", R.raw.sectioneighttalk21);
        VoiceMap.put("sectioneighttalk22", R.raw.sectioneighttalk22);
        VoiceMap.put("sectionninetalk1", R.raw.sectionninetalk1);
        VoiceMap.put("sectionninetalk2", R.raw.sectionninetalk2);
        VoiceMap.put("sectionninetalk3", R.raw.sectionninetalk3);
        VoiceMap.put("sectionninetalk4", R.raw.sectionninetalk4);
        VoiceMap.put("sectionninetalk5", R.raw.sectionninetalk5);
        VoiceMap.put("sectionninetalk6", R.raw.sectionninetalk6);
        VoiceMap.put("sectionninetalk7", R.raw.sectionninetalk7);
        VoiceMap.put("sectionninetalk8", R.raw.sectionninetalk8);
        VoiceMap.put("sectiontentalk1", R.raw.sectiontentalk1);
        VoiceMap.put("sectiontentalk2", R.raw.sectiontentalk2);
        VoiceMap.put("sectiontentalk3", R.raw.sectiontentalk3);
        VoiceMap.put("sectiontentalk4", R.raw.sectiontentalk4);
        VoiceMap.put("sectiontentalk5", R.raw.sectiontentalk5);
        VoiceMap.put("sectiontentalk6", R.raw.sectiontentalk6);
        VoiceMap.put("sectiontentalk7", R.raw.sectiontentalk7);
        VoiceMap.put("sectiontentalk8", R.raw.sectiontentalk8);
        VoiceMap.put("sectiontentalk9", R.raw.sectiontentalk9);
        VoiceMap.put("sectiontentalk10", R.raw.sectiontentalk10);
        VoiceMap.put("sectiontentalk11", R.raw.sectiontentalk11);
        VoiceMap.put("sectiontentalk12", R.raw.sectiontentalk12);
        VoiceMap.put("sectioneleventtalk1", R.raw.sectioneleventtalk1);
        VoiceMap.put("sectioneleventtalk2", R.raw.sectioneleventtalk2);
        VoiceMap.put("sectioneleventtalk3", R.raw.sectioneleventtalk3);
        VoiceMap.put("sectioneleventtalk4", R.raw.sectioneleventtalk4);
        VoiceMap.put("sectioneleventtalk5", R.raw.sectioneleventtalk5);
        VoiceMap.put("sectioneleventtalk6", R.raw.sectioneleventtalk6);
        VoiceMap.put("sectioneleventtalk7", R.raw.sectioneleventtalk7);
        VoiceMap.put("sectioneleventtalk8", R.raw.sectioneleventtalk8);
        VoiceMap.put("sectioneleventtalk9", R.raw.sectioneleventtalk9);
        VoiceMap.put("sectioneleventtalk10", R.raw.sectioneleventtalk10);
        VoiceMap.put("sectioneleventtalk11", R.raw.sectioneleventtalk11);
        VoiceMap.put("sectioneleventtalk12", R.raw.sectioneleventtalk12);
        VoiceMap.put("sectioneleventtalk13", R.raw.sectioneleventtalk13);
        VoiceMap.put("sectioneleventtalk14", R.raw.sectioneleventtalk14);
        VoiceMap.put("sectioneleventtalk15", R.raw.sectioneleventtalk15);
        VoiceMap.put("sectioneleventtalk16", R.raw.sectioneleventtalk16);
        VoiceMap.put("sectioneleventtalk17", R.raw.sectioneleventtalk17);
        VoiceMap.put("sectioneleventtalk18", R.raw.sectioneleventtalk18);
        VoiceMap.put("sectiontwelvetalk1", R.raw.sectiontwelvetalk1);
        VoiceMap.put("sectiontwelvetalk2", R.raw.sectiontwelvetalk2);
        VoiceMap.put("sectiontwelvetalk3", R.raw.sectiontwelvetalk3);
        VoiceMap.put("sectiontwelvetalk4", R.raw.sectiontwelvetalk4);
        VoiceMap.put("sectiontwelvetalk5", R.raw.sectiontwelvetalk5);
        VoiceMap.put("sectiontwelvetalk6", R.raw.sectiontwelvetalk6);
        VoiceMap.put("sectiontwelvetalk7", R.raw.sectiontwelvetalk7);
        VoiceMap.put("sectiontwelvetalk8", R.raw.sectiontwelvetalk8);
        VoiceMap.put("sectiontwelvetalk9", R.raw.sectiontwelvetalk9);
        VoiceMap.put("sectiontwelvetalk10", R.raw.sectiontwelvetalk10);
        VoiceMap.put("sectiontwelvetalk11", R.raw.sectiontwelvetalk11);
        VoiceMap.put("sectiontwelvetalk12", R.raw.sectiontwelvetalk12);
        VoiceMap.put("sectiontwelvetalk13", R.raw.sectiontwelvetalk13);
        VoiceMap.put("sectiontwelvetalk14", R.raw.sectiontwelvetalk14);
        VoiceMap.put("sectiontwelvetalk15", R.raw.sectiontwelvetalk15);
        VoiceMap.put("sectiontwelvetalk16", R.raw.sectiontwelvetalk16);
        VoiceMap.put("sectiontwelvetalk17", R.raw.sectiontwelvetalk17);
        VoiceMap.put("sectiontwelvetalk18", R.raw.sectiontwelvetalk18);
        VoiceMap.put("sectiontwelvetalk19", R.raw.sectiontwelvetalk19);
        VoiceMap.put("sectiontwelvetalk20", R.raw.sectiontwelvetalk20);
        VoiceMap.put("sectiontwelvetalk21", R.raw.sectiontwelvetalk21);
        VoiceMap.put("sectiontwelvetalk22", R.raw.sectiontwelvetalk22);
        VoiceMap.put("sectiontwelvetalk23", R.raw.sectiontwelvetalk23);
        VoiceMap.put("sectiontwelvetalk24", R.raw.sectiontwelvetalk24);
        VoiceMap.put("sectiontwelvetalk25", R.raw.sectiontwelvetalk25);
        VoiceMap.put("sectiontwelvetalk26", R.raw.sectiontwelvetalk26);
        VoiceMap.put("sectiontwelvetalk27", R.raw.sectiontwelvetalk27);
        VoiceMap.put("sectiontwelvetalk28", R.raw.sectiontwelvetalk28);
        VoiceMap.put("sectionthirteentalk1", R.raw.sectionthirteentalk1);
        VoiceMap.put("sectionthirteentalk2", R.raw.sectionthirteentalk2);
        VoiceMap.put("sectionthirteentalk3", R.raw.sectionthirteentalk3);
        VoiceMap.put("sectionthirteentalk4", R.raw.sectionthirteentalk4);
        VoiceMap.put("sectionthirteentalk5", R.raw.sectionthirteentalk5);
        VoiceMap.put("sectionthirteentalk6", R.raw.sectionthirteentalk6);
        VoiceMap.put("sectionthirteentalk7", R.raw.sectionthirteentalk7);
        VoiceMap.put("sectionthirteentalk8", R.raw.sectionthirteentalk8);
        VoiceMap.put("sectionthirteentalk9", R.raw.sectionthirteentalk9);
        VoiceMap.put("sectionthirteentalk10", R.raw.sectionthirteentalk10);
        VoiceMap.put("sectionthirteentalk11", R.raw.sectionthirteentalk11);
        VoiceMap.put("sectionthirteentalk12", R.raw.sectionthirteentalk12);
        VoiceMap.put("sectionthirteentalk13", R.raw.sectionthirteentalk13);
        VoiceMap.put("sectionthirteentalk14", R.raw.sectionthirteentalk14);
        VoiceMap.put("sectionthirteentalk15", R.raw.sectionthirteentalk15);
        VoiceMap.put("sectionthirteentalk16", R.raw.sectionthirteentalk16);
        VoiceMap.put("sectionthirteentalk17", R.raw.sectionthirteentalk17);
        VoiceMap.put("sectionthirteentalk18", R.raw.sectionthirteentalk18);
        VoiceMap.put("sectionthirteentalk19", R.raw.sectionthirteentalk19);
        VoiceMap.put("sectionthirteentalk20", R.raw.sectionthirteentalk20);
        VoiceMap.put("sectionthirteentalk21", R.raw.sectionthirteentalk21);
        VoiceMap.put("sectionthirteentalk22", R.raw.sectionthirteentalk22);
        VoiceMap.put("sectionthirteentalk23", R.raw.sectionthirteentalk23);
        VoiceMap.put("sectionfourteentalk1", R.raw.sectionfourteentalk1);
        VoiceMap.put("sectionfourteentalk2", R.raw.sectionfourteentalk2);
        VoiceMap.put("sectionfourteentalk3", R.raw.sectionfourteentalk3);
        VoiceMap.put("sectionfourteentalk4", R.raw.sectionfourteentalk4);
        VoiceMap.put("sectionfourteentalk5", R.raw.sectionfourteentalk5);
        VoiceMap.put("sectionfourteentalk6", R.raw.sectionfourteentalk6);
        VoiceMap.put("sectionfourteentalk7", R.raw.sectionfourteentalk7);
        VoiceMap.put("sectionfourteentalk8", R.raw.sectionfourteentalk8);
        VoiceMap.put("sectionfourteentalk9", R.raw.sectionfourteentalk9);
        VoiceMap.put("sectionfourteentalk10", R.raw.sectionfourteentalk10);
        VoiceMap.put("sectionfourteentalk11", R.raw.sectionfourteentalk11);
        VoiceMap.put("sectionfourteentalk12", R.raw.sectionfourteentalk12);
        VoiceMap.put("sectionfourteentalk13", R.raw.sectionfourteentalk13);
        VoiceMap.put("sectionfourteentalk14", R.raw.sectionfourteentalk14);
        VoiceMap.put("sectionfourteentalk15", R.raw.sectionfourteentalk15);
        VoiceMap.put("sectionfourteentalk16", R.raw.sectionfourteentalk16);
        VoiceMap.put("sectionfourteentalk17", R.raw.sectionfourteentalk17);
        VoiceMap.put("sectionfourteentalk18", R.raw.sectionfourteentalk18);
        VoiceMap.put("sectionfourteentalk19", R.raw.sectionfourteentalk19);
        VoiceMap.put("sectionfourteentalk20", R.raw.sectionfourteentalk20);
        VoiceMap.put("sectionfourteentalk21", R.raw.sectionfourteentalk21);
        VoiceMap.put("sectionfourteentalk22", R.raw.sectionfourteentalk22);
        VoiceMap.put("sectionfourteentalk23", R.raw.sectionfourteentalk23);
        VoiceMap.put("sectionfourteentalk24", R.raw.sectionfourteentalk24);
        VoiceMap.put("sectionfourteentalk25", R.raw.sectionfourteentalk25);
        VoiceMap.put("sectionfourteentalk26", R.raw.sectionfourteentalk26);
        VoiceMap.put("sectionfourteentalk27", R.raw.sectionfourteentalk27);
        VoiceMap.put("sectionfourteentalk28", R.raw.sectionfourteentalk28);
        VoiceMap.put("sectionfourteentalk29", R.raw.sectionfourteentalk29);
        VoiceMap.put("sectionfourteentalk30", R.raw.sectionfourteentalk30);
        VoiceMap.put("sectionfourteentalk31", R.raw.sectionfourteentalk31);
        VoiceMap.put("sectionfourteentalk32", R.raw.sectionfourteentalk32);
        VoiceMap.put("sectionfourteentalk33", R.raw.sectionfourteentalk33);
        VoiceMap.put("sectionfourteentalk34", R.raw.sectionfourteentalk34);
        VoiceMap.put("sectionfourteentalk35", R.raw.sectionfourteentalk35);
        VoiceMap.put("prologue1", R.raw.prologue1);
        VoiceMap.put("prologue2", R.raw.prologue2);
        VoiceMap.put("prologue3", R.raw.prologue3);
        VoiceMap.put("prologue4", R.raw.prologue4);
        VoiceMap.put("prologue5", R.raw.prologue5);
        VoiceMap.put("prologue6", R.raw.prologue6);
        VoiceMap.put("prologue7", R.raw.prologue7);
        VoiceMap.put("prologue8", R.raw.prologue8);
        VoiceMap.put("prologue9", R.raw.prologue9);
        VoiceMap.put("prologue10", R.raw.prologue10);
        VoiceMap.put("prologue11", R.raw.prologue11);
        VoiceMap.put("prologue12", R.raw.prologue12);
        VoiceMap.put("prologue13", R.raw.prologue13);
        VoiceMap.put("prologue14", R.raw.prologue14);
        VoiceMap.put("prologue15", R.raw.prologue15);
        VoiceMap.put("prologue16", R.raw.prologue16);
        VoiceMap.put("prologue17", R.raw.prologue17);
        VoiceMap.put("prologue18", R.raw.prologue18);
        VoiceMap.put("prologue19", R.raw.prologue19);
        VoiceMap.put("prologue20", R.raw.prologue20);
        VoiceMap.put("prologue21", R.raw.prologue21);
        VoiceMap.put("map3754", R.raw.map3754);
        VoiceMap.put("map1245", R.raw.map1245);
        VoiceMap.put("map3850", R.raw.map3850);
        VoiceMap.put("map3981", R.raw.map3981);
        VoiceMap.put("map1635", R.raw.map1635);
        VoiceMap.put("prologue27", R.raw.prologue27);
        VoiceMap.put("map2005", R.raw.map2005);
        VoiceMap.put("prologue29", R.raw.prologue29);
        VoiceMap.put("prologue30", R.raw.prologue30);
        VoiceMap.put("prologue31", R.raw.prologue31);
        VoiceMap.put("sectionone1", R.raw.sectionone1);
        VoiceMap.put("sectionone2", R.raw.sectionone2);
        VoiceMap.put("sectionone3", R.raw.sectionone3);
        VoiceMap.put("sectionone4", R.raw.sectionone4);
        VoiceMap.put("sectionone5", R.raw.sectionone5);
        VoiceMap.put("sectionone6", R.raw.sectionone6);
        VoiceMap.put("sectionone7", R.raw.sectionone7);
        VoiceMap.put("sectionone8", R.raw.sectionone8);
        VoiceMap.put("sectionone9", R.raw.sectionone9);
        VoiceMap.put("sectionone10", R.raw.sectionone10);
        VoiceMap.put("map4090", R.raw.map4090);
        VoiceMap.put("map4037", R.raw.map4037);
        VoiceMap.put("map3975", R.raw.map3975);
        VoiceMap.put("map426", R.raw.map426);
        VoiceMap.put("map608", R.raw.map608);
        VoiceMap.put("sectiontwo1", R.raw.sectiontwo1);
        VoiceMap.put("map2800", R.raw.map2800);
        VoiceMap.put("map4122", R.raw.map4122);
        VoiceMap.put("sectiontwo4", R.raw.sectiontwo4);
        VoiceMap.put("sectiontwo5", R.raw.sectiontwo5);
        VoiceMap.put("sectiontwo6", R.raw.sectiontwo6);
        VoiceMap.put("sectiontwo7", R.raw.sectiontwo7);
        VoiceMap.put("map5424", R.raw.map5424);
        VoiceMap.put("sectionthree1", R.raw.sectionthree1);
        VoiceMap.put("sectionthree2", R.raw.sectionthree2);
        VoiceMap.put("sectionthree3", R.raw.sectionthree3);
        VoiceMap.put("sectionthree4", R.raw.sectionthree4);
        VoiceMap.put("sectionthree5", R.raw.sectionthree5);
        VoiceMap.put("sectionthree6", R.raw.sectionthree6);
        VoiceMap.put("sectionthree7", R.raw.sectionthree7);
        VoiceMap.put("map1004", R.raw.map1004);
        VoiceMap.put("map4770", R.raw.map4770);
        VoiceMap.put("map1936", R.raw.map1936);
        VoiceMap.put("sectionfour1", R.raw.sectionfour1);
        VoiceMap.put("sectionfour2", R.raw.sectionfour2);
        VoiceMap.put("sectionfour3", R.raw.sectionfour3);
        VoiceMap.put("sectionfour4", R.raw.sectionfour4);
        VoiceMap.put("sectionfour5", R.raw.sectionfour5);
        VoiceMap.put("sectionfour61", R.raw.sectionfour61);
        VoiceMap.put("sectionfour7", R.raw.sectionfour7);
        VoiceMap.put("sectionfour8", R.raw.sectionfour8);
        VoiceMap.put("sectionfour9", R.raw.sectionfour9);
        VoiceMap.put("map4960", R.raw.map4960);
        VoiceMap.put("map5787", R.raw.map5787);
        VoiceMap.put("map5214", R.raw.map5214);
        VoiceMap.put("map4083", R.raw.map4083);
        VoiceMap.put("sectionfive1", R.raw.sectionfive1);
        VoiceMap.put("map1689", R.raw.map1689);
        VoiceMap.put("map9829", R.raw.map9829);
        VoiceMap.put("map7535", R.raw.map7535);
        VoiceMap.put("map9093", R.raw.map9093);
        VoiceMap.put("map10049", R.raw.map10049);
        VoiceMap.put("map36", R.raw.map36);
        VoiceMap.put("sectionfive8", R.raw.sectionfive8);
        VoiceMap.put("sectionfive9", R.raw.sectionfive9);
        VoiceMap.put("map5699", R.raw.map5699);
        VoiceMap.put("map7521", R.raw.map7521);
        VoiceMap.put("map10474", R.raw.map10474);
        VoiceMap.put("map3876", R.raw.map3876);
        VoiceMap.put("sectionsix2", R.raw.sectionsix2);
        VoiceMap.put("sectionsix3", R.raw.sectionsix3);
        VoiceMap.put("sectionsix4", R.raw.sectionsix4);
        VoiceMap.put("sectionsix5", R.raw.sectionsix5);
        VoiceMap.put("sectionseven1", R.raw.sectionseven1);
        VoiceMap.put("sectionseven2", R.raw.sectionseven2);
        VoiceMap.put("sectionseven3", R.raw.sectionseven3);
        VoiceMap.put("sectionseven4", R.raw.sectionseven4);
        VoiceMap.put("map721", R.raw.map721);
        VoiceMap.put("sectionseven6", R.raw.sectionseven6);
        VoiceMap.put("sectionseven7", R.raw.sectionseven7);
        VoiceMap.put("sectionseven8", R.raw.sectionseven8);
        VoiceMap.put("sectioneight1", R.raw.sectioneight1);
        VoiceMap.put("sectioneight2", R.raw.sectioneight2);
        VoiceMap.put("sectioneight3", R.raw.sectioneight3);
        VoiceMap.put("sectioneight4", R.raw.sectioneight4);
        VoiceMap.put("sectioneight5", R.raw.sectioneight5);
        VoiceMap.put("sectioneight6", R.raw.sectioneight6);
        VoiceMap.put("sectioneight7", R.raw.sectioneight7);
        VoiceMap.put("sectioneight8", R.raw.sectioneight8);
        VoiceMap.put("sectioneight9", R.raw.sectioneight9);
        VoiceMap.put("sectioneight10", R.raw.sectioneight10);
        VoiceMap.put("sectioneight11", R.raw.sectioneight11);
        VoiceMap.put("sectionnine1", R.raw.sectionnine1);
        VoiceMap.put("sectionnine2", R.raw.sectionnine2);
        VoiceMap.put("map3378", R.raw.map3378);
        VoiceMap.put("map11203", R.raw.map11203);
        VoiceMap.put("map4246", R.raw.map4246);
        VoiceMap.put("sectionnine8", R.raw.sectionnine8);
        VoiceMap.put("sectionnine9", R.raw.sectionnine9);
        VoiceMap.put("sectionnine10", R.raw.sectionnine10);
        VoiceMap.put("sectionnine11", R.raw.sectionnine11);
        VoiceMap.put("map4701", R.raw.map4701);
        VoiceMap.put("sectionnine13", R.raw.sectionnine13);
        VoiceMap.put("sectionnine14", R.raw.sectionnine14);
        VoiceMap.put("sectionnine15", R.raw.sectionnine15);
        VoiceMap.put("sectionnine16", R.raw.sectionnine16);
        VoiceMap.put("sectionnine17", R.raw.sectionnine17);
        VoiceMap.put("sectionten1", R.raw.sectionten1);
        VoiceMap.put("sectionten2", R.raw.sectionten2);
        VoiceMap.put("sectionten3", R.raw.sectionten3);
        VoiceMap.put("sectionten4", R.raw.sectionten4);
        VoiceMap.put("map5805", R.raw.map5805);
        VoiceMap.put("sectionten6", R.raw.sectionten6);
        VoiceMap.put("sectionten7", R.raw.sectionten7);
        VoiceMap.put("sectionten8", R.raw.sectionten8);
        VoiceMap.put("sectionten9", R.raw.sectionten9);
        VoiceMap.put("sectionten10", R.raw.sectionten10);
        VoiceMap.put("sectionten11", R.raw.sectionten11);
        VoiceMap.put("sectioneleven1", R.raw.sectioneleven1);
        VoiceMap.put("sectioneleven2", R.raw.sectioneleven2);
        VoiceMap.put("sectioneleven3", R.raw.sectioneleven3);
        VoiceMap.put("sectioneleven4", R.raw.sectioneleven4);
        VoiceMap.put("sectioneleven5", R.raw.sectioneleven5);
        VoiceMap.put("sectioneleven7", R.raw.sectioneleven7);
        VoiceMap.put("map5538", R.raw.map5538);
        VoiceMap.put("sectiontwelve1", R.raw.sectiontwelve1);
        VoiceMap.put("sectiontwelve2", R.raw.sectiontwelve2);
        VoiceMap.put("sectiontwelve3", R.raw.sectiontwelve3);
        VoiceMap.put("sectiontwelve4", R.raw.sectiontwelve4);
        VoiceMap.put("sectiontwelve5", R.raw.sectiontwelve5);
        VoiceMap.put("sectiontwelve6", R.raw.sectiontwelve6);
        VoiceMap.put("sectiontwelve7", R.raw.sectiontwelve7);
        VoiceMap.put("sectiontwelve8", R.raw.sectiontwelve8);
        VoiceMap.put("sectiontwelve9", R.raw.sectiontwelve9);
        VoiceMap.put("sectiontwelve10", R.raw.sectiontwelve10);
        VoiceMap.put("sectiontwelve11", R.raw.sectiontwelve11);
        VoiceMap.put("sectiontwelve12", R.raw.sectiontwelve12);
        VoiceMap.put("sectiontwelve13", R.raw.sectiontwelve13);
        VoiceMap.put("sectiontwelve15", R.raw.sectiontwelve15);
        VoiceMap.put("sectiontwelve16", R.raw.sectiontwelve16);
        VoiceMap.put("sectiontwelve17", R.raw.sectiontwelve17);
        VoiceMap.put("sectiontwelve18", R.raw.sectiontwelve18);
        VoiceMap.put("map10716", R.raw.map10716);
        VoiceMap.put("map737", R.raw.map737);
        VoiceMap.put("sectiontwelve22", R.raw.sectiontwelve22);
        VoiceMap.put("sectiontwelve23", R.raw.sectiontwelve23);
        VoiceMap.put("sectiontwelve24", R.raw.sectiontwelve24);
        VoiceMap.put("sectionfourteen1", R.raw.sectionfourteen1);
        VoiceMap.put("sectionfourteen2", R.raw.sectionfourteen2);
        VoiceMap.put("sectionfourteen3", R.raw.sectionfourteen3);
        VoiceMap.put("map861", R.raw.map861);
        VoiceMap.put("sectionfourteen5", R.raw.sectionfourteen5);
        VoiceMap.put("sectionfourteen6", R.raw.sectionfourteen6);
        VoiceMap.put("sectionfourteen7", R.raw.sectionfourteen7);
        VoiceMap.put("sectionfourteen8", R.raw.sectionfourteen8);
        VoiceMap.put("os1", R.raw.os1);
        VoiceMap.put("os2", R.raw.os2);
        VoiceMap.put("os3", R.raw.os3);
        VoiceMap.put("os4", R.raw.os4);
        VoiceMap.put("os5", R.raw.os5);
        VoiceMap.put("os6", R.raw.os6);
        VoiceMap.put("os7", R.raw.os7);
        VoiceMap.put("os8", R.raw.os8);
        VoiceMap.put("os9", R.raw.os9);
        VoiceMap.put("os10", R.raw.os10);
        VoiceMap.put("os11", R.raw.os11);
        VoiceMap.put("os12", R.raw.os12);
        VoiceMap.put("os13", R.raw.os13);
        VoiceMap.put("os14", R.raw.os14);
        VoiceMap.put("applevoice", R.raw.applevoice);
        VoiceMap.put("map9264", R.raw.map9264);
        VoiceMap.put("leafvoice", R.raw.leafvoice);
        VoiceMap.put("sectionfour62", R.raw.sectionfour62);
        VoiceMap.put("preyr", R.raw.preyr);
        VoiceMap.put("preyl", R.raw.preyl);
        VoiceMap.put("sectiontwelve81", R.raw.sectiontwelve81);
        VoiceMap.put("sisfront", R.raw.sisfront);
        VoiceMap.put("sisleft", R.raw.sisleft);
        VoiceMap.put("sisright", R.raw.sisright);
        VoiceMap.put("sisbehind", R.raw.sisbehind);
        VoiceMap.put("sisobstacle", R.raw.sisobstacle);
        VoiceMap.put("knife", R.raw.knife);
        VoiceMap.put("plant", R.raw.plant);
    }
    private void CreateMapData() {
        MapMap.put("map1", R.raw.map1);
        MapMap.put("map2", R.raw.map2);
        MapMap.put("map3", R.raw.map3);
        MapMap.put("map4", R.raw.map4);
        MapMap.put("map5", R.raw.map5);
        MapMap.put("map6", R.raw.map6);
        MapMap.put("map7", R.raw.map7);
        MapMap.put("map8", R.raw.map8);
        MapMap.put("map9", R.raw.map9);
        MapMap.put("map10", R.raw.map10);
        MapMap.put("map11", R.raw.map11);
        MapMap.put("map12", R.raw.map12);
        MapMap.put("map13", R.raw.map13);
        MapMap.put("map14", R.raw.map14);
        MapMap.put("map15", R.raw.map15);
    }

    //主迴圈
    private void MainLoopp() {
        Cursor script= db.rawQuery("SELECT Script_attribute, Script_parameter ,Script_time" +
                " FROM "+tb_script+
                " WHERE Script_chapter_id = "+record[0]+" and Script_section_id = "+record[1],null);
        if(script.moveToFirst()) {
            int Script_time= 1000*Integer.parseInt(script.getString(2));

            do {
                switch (script.getString(0)) {
                    case "1":
                        Talk(script.getInt(1));
                        break;
                    case "2":
                        Voice(script.getInt(1));
                        break;
                    case "3":
                        Word(script.getInt(1));
                        break;
                    case "4":
                        Map(script.getInt(1));
                        break;
                    case "5":
                        Instruction(script.getInt(1));
                        break;
                    case "6":
                        Next(script.getInt(1));
                        script= db.rawQuery("SELECT Script_attribute, Script_parameter ,Script_time" +
                                " FROM "+tb_script+
                                " WHERE Script_chapter_id = "+record[0]+" and Script_section_id = "+record[1],null);
                        script.moveToFirst();
                        break;
                    case "7":
                        Tool(script.getInt(1));
                        break;
                    default:
                        break;
                }
                if(pick==1){
                    script.moveToNext();
                    pick=0;
                }
                MainActivity.scripttime=-1;
                long currenttime=System.currentTimeMillis();
                do{
                    if(MainActivity.scripttime==-1){
                        MainActivity.scripttime=System.currentTimeMillis();
                    }
                    currenttime=System.currentTimeMillis();
                }while (currenttime-MainActivity.scripttime<Script_time);
            } while (script.moveToNext());
        }
    }
    private void MainLoop() {
        int loopctrl=0;
        while (loopctrl==0){
            if(mainloop==0){
                scriptloop= db.rawQuery("SELECT Script_attribute, Script_parameter ,Script_time" +
                        " FROM "+tb_script+
                        " WHERE Script_chapter_id = "+record[0]+" and Script_section_id = "+record[1],null);
                mainloop=1;
                scriptloop.moveToFirst();
            }
            int Script_time= 1000*Integer.parseInt(scriptloop.getString(2));
            switch (scriptloop.getString(0)) {
                case "1":
                    Talk(scriptloop.getInt(1));
                    loopctrl=1;
                    break;
                case "2":
                    Voice(scriptloop.getInt(1));
                    loopctrl=1;
                    break;
                case "3":
                    Word(scriptloop.getInt(1));
                    loopctrl=1;
                    break;
                case "4":
                    Map(scriptloop.getInt(1));
                    loopctrl=0;
                    break;
                case "5":
                    Instruction(scriptloop.getInt(1));
                    loopctrl=1;
                    break;
                case "6":
                    Next(scriptloop.getInt(1));
                    System.out.println("換章");
                    scriptloop= db.rawQuery("SELECT Script_attribute, Script_parameter ,Script_time" +
                            " FROM "+tb_script+
                            " WHERE Script_chapter_id = "+record[0]+" and Script_section_id = "+record[1],null);

                    System.out.println("讀完劇本");
                    scriptloop.moveToFirst();
                    loopctrl=0;
                    System.out.println("結束");
                    break;
                case "7":
                    Tool(scriptloop.getInt(1));
                    loopctrl=0;
                    break;
            }
            if(pick==1){
                scriptloop.moveToNext();
                pick=0;
            }
            scriptloop.moveToNext();
            /*MainActivity.scripttime=-1;
            long currenttime=System.currentTimeMillis();
            do{
                if(MainActivity.scripttime==-1){
                    MainActivity.scripttime=System.currentTimeMillis();
                }
                currenttime=System.currentTimeMillis();
            }while (currenttime-MainActivity.scripttime<Script_time);*/

        }
    }
    //Loop呼叫各項功能
    private void Talk(int Script_parameter) {
        TextView Subtitle = findViewById(R.id.Subtitle);
        Cursor talk= db.rawQuery("SELECT * FROM "+tb_talk+" WHERE Talk_id = "+Script_parameter,null);
        if(talk.moveToFirst()) {
            if(mper!= null && mper.isPlaying()){
                mper.stop();
            }
            Sound(talk.getString(2));
            if(suballow ==1){
                return;
            }
            Subtitle.setText(talk.getString(1));
        }
    }
    private void Voice(int Script_parameter) {
        Cursor voice= db.rawQuery("SELECT Voice_file FROM "+tb_voice+" WHERE Voice_index = "+Script_parameter ,null);
        if(voice.moveToFirst()) {
            if(mper!= null && mper.isPlaying()){
                mper.stop();
            }
            Sound(voice.getString(0));
        }
    }
    private void Word(int Script_parameter) {
        TextView Subtitle = findViewById(R.id.Subtitle);
        Cursor word = db.rawQuery("SELECT Word_content, Word_description_file  " +
                " FROM " + tb_word + " WHERE Word_id = " + Script_parameter, null);

        if(word.moveToFirst()) {
            if(word.getString(1)!="havenot"){
                if(mper!= null && mper.isPlaying()){
                    mper.stop();
                }
                Sound(word.getString(1));
            }
            if(suballow ==1){
                return;
            }
            Subtitle.setText(word.getString(0));
        }
    }
    private void Map(int Script_parameter) {
        System.out.println("map start:");
        try {
            Cursor mapfile= db.rawQuery("SELECT Map_file" +
                    " FROM "+tb_map+" WHERE Map_index = "+Script_parameter,null);
            HashMap<String, Integer> map_file = new HashMap<>();
            if(mapfile.moveToFirst()) {
                System.out.println("檔案名稱:"+MapMap.get(mapfile.getString(0)));
                map_file.put("path", MapMap.get(mapfile.getString(0)));
            }
            InputStream is = getResources().openRawResource(map_file.get("path"));
            text = readTextFromSDcard(is);
            array = text.split("\n");
            maparray = new String[array.length][array.length];
            guy = new int[2];
            if(mapfile.moveToFirst()) {

                for(int i=0;i<array.length;i++){
                    array[i]=array[i].trim();
                    String single_int[]=array[i].split(",");
                    for(int j=0;j<single_int.length;j++){
                        maparray[i][j]=single_int[j];
                    }
                }
            }
        }catch (Exception e) {
            System.out.println("Read File Error!");
        }
        System.out.println("map end");

    }
    private void Instruction(int Script_parameter) {

        switch (Script_parameter) {  //這是劇本參數 也就是會去教每一個資料表的第X筆資料 簡單說就是每一個資料表的編號
            case 1:
                System.out.println("move start:");
                guy[0]= 13;
                guy[1]= 5;
                x=10;
                y=15;
                canmove="1635";
                finish="2005";
                soundarry=new String[4];
                soundarry[0]="3754";
                soundarry[1]="1245";
                soundarry[2]="3850";
                soundarry[3]="3981";
                destination=new int[2];
                destination[0]=4;
                destination[1]=5;
                System.out.println("map 宣告1:");
                //canarray[]=null;
                MainActivity.onclicktime = -1;
                setContentView(R.layout.move);
                Button Menu= (Button)findViewById(R.id.Menu);
                Menu.setOnClickListener(btnListener);
                Menu.getBackground().setAlpha(100);
                Button Backpack= (Button)findViewById(R.id.Backpack);
                Backpack.setOnClickListener(btnListener);
                Backpack.getBackground().setAlpha(100);
                Button Compass= (Button)findViewById(R.id.Compass);
                Compass.setOnClickListener(btnListener);
                Compass.getBackground().setAlpha(100);
                Button Go= (Button)findViewById(R.id.Go);
                Go.setOnClickListener(btnListener);
                Go.getBackground().setAlpha(100);
                Button left= (Button)findViewById(R.id.left);
                left.setOnClickListener(btnListener);
                left.getBackground().setAlpha(100);
                Button right= (Button)findViewById(R.id.right);
                right.setOnClickListener(btnListener);
                right.getBackground().setAlpha(100);
                break;
            case 2:
                Sound("prologue11");
                Intent it2 =new Intent(this,fighting.class);
                it2.putExtra("編號",2);
                it2.putExtra("次數",1);
                startActivity(it2);
                    break;

            case 3:
                Sound("prologue15");
                Intent it3 =new Intent(this,fighting.class);
                it3.putExtra("編號",3);
                it3.putExtra("次數",2);
                startActivity(it3);
                    break;

            case 4:
                Sound("sectionone7");
                Intent it4 =new Intent(this,fighting.class);
                it4.putExtra("編號",4);
                it4.putExtra("次數",3);
                startActivity(it4);
                break;

            case 5:
                guy[0]= 13;
                guy[1]= 4;
                x=10;
                y=15;
                canmove="608";
                finish="903";
                soundarry=new String[4];
                soundarry[0]="4090";
                soundarry[1]="4037";
                soundarry[2]="3975";
                soundarry[3]="426";
                destination=new int[2];
                destination[0]=4;
                destination[1]=4;
                //canarray[]=null;
                MainActivity.onclicktime = -1;
                setContentView(R.layout.move);
                Menu= (Button)findViewById(R.id.Menu);
                Menu.setOnClickListener(btnListener);
                Menu.getBackground().setAlpha(100);
                Backpack= (Button)findViewById(R.id.Backpack);
                Backpack.setOnClickListener(btnListener);
                Backpack.getBackground().setAlpha(100);
                Compass= (Button)findViewById(R.id.Compass);
                Compass.setOnClickListener(btnListener);
                Compass.getBackground().setAlpha(100);
                Go= (Button)findViewById(R.id.Go);
                Go.setOnClickListener(btnListener);
                Go.getBackground().setAlpha(100);
                left= (Button)findViewById(R.id.left);
                left.setOnClickListener(btnListener);
                left.getBackground().setAlpha(100);
                right= (Button)findViewById(R.id.right);
                right.setOnClickListener(btnListener);
                right.getBackground().setAlpha(100);
                break;
            case 6:
                guy[0]= 7;
                guy[1]= 5;
                x=10;
                y=15;
                canmove="2800";
                finish="410";
                soundarry=new String[1];
                soundarry[0]="4122";
                destination=new int[2];
                destination[0]=11;
                destination[1]=0;
                //canarray[]=null;
                MainActivity.onclicktime = -1;
                setContentView(R.layout.move);
                Menu= (Button)findViewById(R.id.Menu);
                Menu.setOnClickListener(btnListener);
                Menu.getBackground().setAlpha(100);
                Backpack= (Button)findViewById(R.id.Backpack);
                Backpack.setOnClickListener(btnListener);
                Backpack.getBackground().setAlpha(100);
                Compass= (Button)findViewById(R.id.Compass);
                Compass.setOnClickListener(btnListener);
                Compass.getBackground().setAlpha(100);
                Go= (Button)findViewById(R.id.Go);
                Go.setOnClickListener(btnListener);
                Go.getBackground().setAlpha(100);
                left= (Button)findViewById(R.id.left);
                left.setOnClickListener(btnListener);
                left.getBackground().setAlpha(100);
                right= (Button)findViewById(R.id.right);
                right.setOnClickListener(btnListener);
                right.getBackground().setAlpha(100);
                break;
           case 7:
               Sound("sectiontwo4");
               Intent it7 =new Intent(this,fighting.class);
               it7.putExtra("編號",7);
               it7.putExtra("次數",10);
               startActivity(it7);
                break;

            case 8:
                guy[0]= 13;
                guy[1]= 8;
                x=10;
                y=15;
                canmove="5424";
                finish="3867";
                destination=new int[2];
                destination[0]=4;
                destination[1]=3;
                //canarray[]=null;
                MainActivity.onclicktime = -1;
                setContentView(R.layout.move);
                Menu= (Button)findViewById(R.id.Menu);
                Menu.setOnClickListener(btnListener);
                Menu.getBackground().setAlpha(100);
                Backpack= (Button)findViewById(R.id.Backpack);
                Backpack.setOnClickListener(btnListener);
                Backpack.getBackground().setAlpha(100);
                Compass= (Button)findViewById(R.id.Compass);
                Compass.setOnClickListener(btnListener);
                Compass.getBackground().setAlpha(100);
                Go= (Button)findViewById(R.id.Go);
                Go.setOnClickListener(btnListener);
                Go.getBackground().setAlpha(100);
                left= (Button)findViewById(R.id.left);
                left.setOnClickListener(btnListener);
                left.getBackground().setAlpha(100);
                right= (Button)findViewById(R.id.right);
                right.setOnClickListener(btnListener);
                right.getBackground().setAlpha(100);
                break;
            case 9:
                Sound("sectionthree1");
                Intent it9 =new Intent(this,fighting.class);
               it9.putExtra("編號",9);
               it9.putExtra("次數",8);
               startActivity(it9);
                break;
            case 10:
                guy[0]= 12;
                guy[1]= 0;
                x=10;
                y=15;
                canmove="1004";
                finish="4971";
                soundarry=new String[2];
                soundarry[0]="4770";
                soundarry[1]="1936";
                destination=new int[2];
                destination[0]=4;
                destination[1]=4;
                //canarray[]=null;
                MainActivity.onclicktime = -1;
                setContentView(R.layout.move);
                Menu= (Button)findViewById(R.id.Menu);
                Menu.setOnClickListener(btnListener);
                Menu.getBackground().setAlpha(100);
                Backpack= (Button)findViewById(R.id.Backpack);
                Backpack.setOnClickListener(btnListener);
                Backpack.getBackground().setAlpha(100);
                Compass= (Button)findViewById(R.id.Compass);
                Compass.setOnClickListener(btnListener);
                Compass.getBackground().setAlpha(100);
                Go= (Button)findViewById(R.id.Go);
                Go.setOnClickListener(btnListener);
                Go.getBackground().setAlpha(100);
                left= (Button)findViewById(R.id.left);
                left.setOnClickListener(btnListener);
                left.getBackground().setAlpha(100);
                right= (Button)findViewById(R.id.right);
                right.setOnClickListener(btnListener);
                right.getBackground().setAlpha(100);
                break;
            case 11:
                Sound("sectionfour4");
                Intent it11 =new Intent(this,fighting.class);
               it11.putExtra("編號",11);
               it11.putExtra("次數",3);
               startActivity(it11);
                break;
            case 12:
                guy[0]= 2;
                guy[1]= 7;
                x=10;
                y=15;
                canmove="4960";
                finish="5787";
                soundarry=new String[2];
                soundarry[0]="4083";
                soundarry[1]="5214";
                destination=new int[2];
                destination[0]=10;
                destination[1]=6;
                //canarray[]=null;
                MainActivity.onclicktime = -1;
                setContentView(R.layout.move);
                Menu= (Button)findViewById(R.id.Menu);
                Menu.setOnClickListener(btnListener);
                Menu.getBackground().setAlpha(100);
                Backpack= (Button)findViewById(R.id.Backpack);
                Backpack.setOnClickListener(btnListener);
                Backpack.getBackground().setAlpha(100);
                Compass= (Button)findViewById(R.id.Compass);
                Compass.setOnClickListener(btnListener);
                Compass.getBackground().setAlpha(100);
                Go= (Button)findViewById(R.id.Go);
                Go.setOnClickListener(btnListener);
                Go.getBackground().setAlpha(100);
                left= (Button)findViewById(R.id.left);
                left.setOnClickListener(btnListener);
                left.getBackground().setAlpha(100);
                right= (Button)findViewById(R.id.right);
                right.setOnClickListener(btnListener);
                right.getBackground().setAlpha(100);
                break;
            case 13:
                guy[0]= 7;
                guy[1]= 13;
                x=15;
                y=10;
                canmove="1689";
                finish="1201";
                soundarry=new String[4];
                soundarry[0]="7535";
                soundarry[1]="9829";
                soundarry[2]="9093";
                soundarry[3]="10049";
                destination=new int[2];
                destination[0]=3;
                destination[1]=3;
                //canarray[]=null;
                MainActivity.onclicktime = -1;
                setContentView(R.layout.move);
                Menu= (Button)findViewById(R.id.Menu);
                Menu.setOnClickListener(btnListener);
                Menu.getBackground().setAlpha(100);
                Backpack= (Button)findViewById(R.id.Backpack);
                Backpack.setOnClickListener(btnListener);
                Backpack.getBackground().setAlpha(100);
                Compass= (Button)findViewById(R.id.Compass);
                Compass.setOnClickListener(btnListener);
                Compass.getBackground().setAlpha(100);
                Go= (Button)findViewById(R.id.Go);
                Go.setOnClickListener(btnListener);
                Go.getBackground().setAlpha(100);
                left= (Button)findViewById(R.id.left);
                left.setOnClickListener(btnListener);
                left.getBackground().setAlpha(100);
                right= (Button)findViewById(R.id.right);
                right.setOnClickListener(btnListener);
                right.getBackground().setAlpha(100);
                break;
            case 14:
                guy[0]= 5;
                guy[1]= 2;
                x=15;
                y=10;
                canmove="3876";
                finish="11203";
                soundarry=new String[3];
                soundarry[0]="7521";
                soundarry[1]="5699";
                soundarry[2]="10474";
                destination=new int[2];
                destination[0]=6;
                destination[1]=14;
                //canarray[]=null;
                MainActivity.onclicktime = -1;
                setContentView(R.layout.move);
                Menu= (Button)findViewById(R.id.Menu);
                Menu.setOnClickListener(btnListener);
                Menu.getBackground().setAlpha(100);
                Backpack= (Button)findViewById(R.id.Backpack);
                Backpack.setOnClickListener(btnListener);
                Backpack.getBackground().setAlpha(100);
                Compass= (Button)findViewById(R.id.Compass);
                Compass.setOnClickListener(btnListener);
                Compass.getBackground().setAlpha(100);
                Go= (Button)findViewById(R.id.Go);
                Go.setOnClickListener(btnListener);
                Go.getBackground().setAlpha(100);
                left= (Button)findViewById(R.id.left);
                left.setOnClickListener(btnListener);
                left.getBackground().setAlpha(100);
                right= (Button)findViewById(R.id.right);
                right.setOnClickListener(btnListener);
                right.getBackground().setAlpha(100);
                break;
            case 15:
                guy[0]= 8;
                guy[1]= 1;
                x=15;
                y=10;
                canmove="721";
                finish="10690";
                soundarry=new String[1];
                soundarry[0]="4534";
                destination=new int[2];
                destination[0]=2;
                destination[1]=12;
                //canarray[]=null;
                MainActivity.onclicktime = -1;
                setContentView(R.layout.move);
                Menu= (Button)findViewById(R.id.Menu);
                Menu.setOnClickListener(btnListener);
                Menu.getBackground().setAlpha(100);
                Backpack= (Button)findViewById(R.id.Backpack);
                Backpack.setOnClickListener(btnListener);
                Backpack.getBackground().setAlpha(100);
                Compass= (Button)findViewById(R.id.Compass);
                Compass.setOnClickListener(btnListener);
                Compass.getBackground().setAlpha(100);
                Go= (Button)findViewById(R.id.Go);
                Go.setOnClickListener(btnListener);
                Go.getBackground().setAlpha(100);
                left= (Button)findViewById(R.id.left);
                left.setOnClickListener(btnListener);
                left.getBackground().setAlpha(100);
                right= (Button)findViewById(R.id.right);
                right.setOnClickListener(btnListener);
                right.getBackground().setAlpha(100);
                break;
           case 16:
               Sound("sectionseven6");
               Intent it16 =new Intent(this,fighting.class);
               it16.putExtra("編號",16);
               it16.putExtra("次數",9);
               startActivity(it16);
                break;
             case 17:
                 Sound("sectioneight2");
                Intent it17 =new Intent(this,fighting.class);
               it17.putExtra("編號",17);
               it17.putExtra("次數",50);
               startActivity(it17);
                break;
            case 18:
                Sound("sectioneight9");
                Intent it18 =new Intent(this,fighting.class);
               it18.putExtra("編號",18);
               it18.putExtra("次數",8);
               startActivity(it18);
                break;
            case 19:
                guy[0]= 4;
                guy[1]= 3;
                x=15;
                y=10;
                canmove="4246";
                finish="3378";
                soundarry=new String[2];
                soundarry[0]="3790";
                soundarry[1]="11203";
                soundarry[2]="4246";
                soundarry[3]="9264";
                destination=new int[2];
                destination[0]=7;
                destination[1]=12;
                //canarray[]=null;
                MainActivity.onclicktime = -1;
                setContentView(R.layout.move);
                Menu= (Button)findViewById(R.id.Menu);
                Menu.setOnClickListener(btnListener);
                Menu.getBackground().setAlpha(100);
                Backpack= (Button)findViewById(R.id.Backpack);
                Backpack.setOnClickListener(btnListener);
                Backpack.getBackground().setAlpha(100);
                Compass= (Button)findViewById(R.id.Compass);
                Compass.setOnClickListener(btnListener);
                Compass.getBackground().setAlpha(100);
                Go= (Button)findViewById(R.id.Go);
                Go.setOnClickListener(btnListener);
                Go.getBackground().setAlpha(100);
                left= (Button)findViewById(R.id.left);
                left.setOnClickListener(btnListener);
                left.getBackground().setAlpha(100);
                right= (Button)findViewById(R.id.right);
                right.setOnClickListener(btnListener);
                right.getBackground().setAlpha(100);
                break;
            case 20:
                guy[0]= 7;
                guy[1]= 1;
                x=15;
                y=10;
                canmove="4701";
                finish="4000";
                destination=new int[2];
                destination[0]=3;
                destination[1]=12;

                //canarray[]=null;
                MainActivity.onclicktime = -1;
                setContentView(R.layout.move);
                Menu= (Button)findViewById(R.id.Menu);
                Menu.setOnClickListener(btnListener);
                Menu.getBackground().setAlpha(100);
                Backpack= (Button)findViewById(R.id.Backpack);
                Backpack.setOnClickListener(btnListener);
                Backpack.getBackground().setAlpha(100);
                Compass= (Button)findViewById(R.id.Compass);
                Compass.setOnClickListener(btnListener);
                Compass.getBackground().setAlpha(100);
                Go= (Button)findViewById(R.id.Go);
                Go.setOnClickListener(btnListener);
                Go.getBackground().setAlpha(100);
                left= (Button)findViewById(R.id.left);
                left.setOnClickListener(btnListener);
                left.getBackground().setAlpha(100);
                right= (Button)findViewById(R.id.right);
                right.setOnClickListener(btnListener);
                right.getBackground().setAlpha(100);
                break;
            case 21:
                Sound("sectionnine15");
                Intent it21 =new Intent(this,fighting.class);
                it21.putExtra("編號",21);
                it21.putExtra("次數",5);
                startActivity(it21);
                break;
            case 22:
                guy[0]= 13;
                guy[1]= 0;
                x=10;
                y=15;
                canmove="5805";
                finish="5058";
                destination=new int[2];
                destination[0]=3;
                destination[1]=8;

                //canarray[]=null;
                MainActivity.onclicktime = -1;
                setContentView(R.layout.move);
                Menu= (Button)findViewById(R.id.Menu);
                Menu.setOnClickListener(btnListener);
                Menu.getBackground().setAlpha(100);
                Backpack= (Button)findViewById(R.id.Backpack);
                Backpack.setOnClickListener(btnListener);
                Backpack.getBackground().setAlpha(100);
                Compass= (Button)findViewById(R.id.Compass);
                Compass.setOnClickListener(btnListener);
                Compass.getBackground().setAlpha(100);
                Go= (Button)findViewById(R.id.Go);
                Go.setOnClickListener(btnListener);
                Go.getBackground().setAlpha(100);
                left= (Button)findViewById(R.id.left);
                left.setOnClickListener(btnListener);
                left.getBackground().setAlpha(100);
                right= (Button)findViewById(R.id.right);
                right.setOnClickListener(btnListener);
                right.getBackground().setAlpha(100);
                break;
            case 23:
                Sound("sectionten8");
                Intent it23 =new Intent(this,fighting.class);
                it23.putExtra("編號",23);
                it23.putExtra("次數",8);
                startActivity(it23);
                break;
            case 24:
                Sound("sectioneleven4");
                Intent it24 =new Intent(this,fighting.class);
                it24.putExtra("編號",24);
                it24.putExtra("次數",4);
                startActivity(it24);
                break;
            case 25:
                guy[0]= 8;
                guy[1]= 8;
                x=10;
                y=10;
                canmove="5538";
                finish="5530";
                destination=new int[2];

                destination[0]=2;
                destination[1]=2;
                //canarray[]=null;
                MainActivity.onclicktime = -1;
                setContentView(R.layout.move);
                Menu= (Button)findViewById(R.id.Menu);
                Menu.setOnClickListener(btnListener);
                Menu.getBackground().setAlpha(100);
                Backpack= (Button)findViewById(R.id.Backpack);
                Backpack.setOnClickListener(btnListener);
                Backpack.getBackground().setAlpha(100);
                Compass= (Button)findViewById(R.id.Compass);
                Compass.setOnClickListener(btnListener);
                Compass.getBackground().setAlpha(100);
                Go= (Button)findViewById(R.id.Go);
                Go.setOnClickListener(btnListener);
                Go.getBackground().setAlpha(100);
                left= (Button)findViewById(R.id.left);
                left.setOnClickListener(btnListener);
                left.getBackground().setAlpha(100);
                right= (Button)findViewById(R.id.right);
                right.setOnClickListener(btnListener);
                right.getBackground().setAlpha(100);
                break;
            case 26:
                setContentView(R.layout.choose);
                Menu= (Button)findViewById(R.id.Menu);
                Menu.setOnClickListener(btnListener);
                Menu.getBackground().setAlpha(100);
                Backpack= (Button)findViewById(R.id.Backpack);
                Backpack.setOnClickListener(btnListener);
                Backpack.getBackground().setAlpha(100);
                Button Pick= (Button)findViewById(R.id.Pick);
                Pick.setOnClickListener(btnListener);
                Pick.getBackground().setAlpha(100);
                Button NoPick= (Button)findViewById(R.id.NoPick);
                NoPick.setOnClickListener(btnListener);
                NoPick.getBackground().setAlpha(100);
                break;
            case 27:
                Sound("sectiontwelve7");
                Intent it27 =new Intent(this,fighting.class);
                it27.putExtra("編號",27);
                it27.putExtra("次數",12);
                startActivity(it27);
                break;
            case 28:
                Sound("sectiontwelve15");
                Intent it28 =new Intent(this,fighting.class);
                it28.putExtra("編號",28);
                it28.putExtra("次數",60);
                startActivity(it28);
                break;
            case 29:
                guy[0]= 3;
                guy[1]= 1;
                x=15;
                y=10;
                canmove="737";
                finish="8094";
                soundarry=new String[1];
                soundarry[0]="10716";
                destination=new int[2];
                destination[0]=7;
                destination[1]=11;
                //canarray[]=null;
                MainActivity.onclicktime = -1;
                setContentView(R.layout.move);
                Menu= (Button)findViewById(R.id.Menu);
                Menu.setOnClickListener(btnListener);
                Menu.getBackground().setAlpha(100);
                Backpack= (Button)findViewById(R.id.Backpack);
                Backpack.setOnClickListener(btnListener);
                Backpack.getBackground().setAlpha(100);
                Compass= (Button)findViewById(R.id.Compass);
                Compass.setOnClickListener(btnListener);
                Compass.getBackground().setAlpha(100);
                Go= (Button)findViewById(R.id.Go);
                Go.setOnClickListener(btnListener);
                Go.getBackground().setAlpha(100);
                left= (Button)findViewById(R.id.left);
                left.setOnClickListener(btnListener);
                left.getBackground().setAlpha(100);
                right= (Button)findViewById(R.id.right);
                right.setOnClickListener(btnListener);
                right.getBackground().setAlpha(100);
                break;
            case 30:
                Sound("sectiontwelve7");
                Intent it30 =new Intent(this,fighting.class);
                it30.putExtra("編號",30);
                it30.putExtra("次數",12);
                startActivity(it30);
                break;
            case 31:
                Sound("sectiontwelve15");
                Intent it31 =new Intent(this,fighting.class);
                it31.putExtra("編號",31);
                it31.putExtra("次數",60);
                startActivity(it31);
                break;
            case 32:
                guy[0]= 3;
                guy[1]= 1;
                x=15;
                y=10;
                canmove="737";
                finish="8094";
                soundarry=new String[1];
                soundarry[0]="10716";
                destination=new int[2];
                destination[0]=7;
                destination[1]=11;
                //canarray[]=null;
                MainActivity.onclicktime = -1;
                setContentView(R.layout.move);
                Menu= (Button)findViewById(R.id.Menu);
                Menu.setOnClickListener(btnListener);
                Menu.getBackground().setAlpha(100);
                Backpack= (Button)findViewById(R.id.Backpack);
                Backpack.setOnClickListener(btnListener);
                Backpack.getBackground().setAlpha(100);
                Compass= (Button)findViewById(R.id.Compass);
                Compass.setOnClickListener(btnListener);
                Compass.getBackground().setAlpha(100);
                Go= (Button)findViewById(R.id.Go);
                Go.setOnClickListener(btnListener);
                Go.getBackground().setAlpha(100);
                left= (Button)findViewById(R.id.left);
                left.setOnClickListener(btnListener);
                left.getBackground().setAlpha(100);
                right= (Button)findViewById(R.id.right);
                right.setOnClickListener(btnListener);
                right.getBackground().setAlpha(100);
                break;
            case 33:
                guy[0]= 8;
                guy[1]= 1;
                x=15;
                y=10;
                canmove="861";
                finish="1493";
                destination=new int[2];
                destination[0]=2;
                destination[1]=12;
                //canarray[]=null;
                MainActivity.onclicktime = -1;
                setContentView(R.layout.move);
                Menu= (Button)findViewById(R.id.Menu);
                Menu.setOnClickListener(btnListener);
                Menu.getBackground().setAlpha(100);
                Backpack= (Button)findViewById(R.id.Backpack);
                Backpack.setOnClickListener(btnListener);
                Backpack.getBackground().setAlpha(100);
                Compass= (Button)findViewById(R.id.Compass);
                Compass.setOnClickListener(btnListener);
                Compass.getBackground().setAlpha(100);
                Go= (Button)findViewById(R.id.Go);
                Go.setOnClickListener(btnListener);
                Go.getBackground().setAlpha(100);
                left= (Button)findViewById(R.id.left);
                left.setOnClickListener(btnListener);
                left.getBackground().setAlpha(100);
                right= (Button)findViewById(R.id.right);
                right.setOnClickListener(btnListener);
                right.getBackground().setAlpha(100);
                break;
        }


    }
    private void Next(int Script_parameter) {
        System.out.println("進Ne");
        Cursor next= db.rawQuery("SELECT * FROM "+tb_next+" " +
                "WHERE Next_index_num = "+Script_parameter,null);
        if(next.moveToFirst()) {

            System.out.println("進讀取");
            record[0]= Integer.parseInt(next.getString(3));
            record[1]= Integer.parseInt(next.getString(4));
            System.out.println("讀取玩");
        }
    }
    private void Tool(int Script_parameter) {
        Cursor tool= db.rawQuery("SELECT * FROM "+tb_tool+" " +
                "WHERE Tool_id = "+Script_parameter,null);
        if(tool.moveToFirst()) {
            ContentValues cv = new ContentValues();
            cv.put("Bag_id",lognumber);
            cv.put("Tool_id",tool.getString(0));
            cv.put("User_id",lognumber);
            db.insert(tb_bag,null,cv);
        }
    }
    private void Save(String lognumber,String logname) {
        ContentValues cv = new ContentValues();
        db.delete(tb_save, "Save_index =" + lognumber, null);
        cv.put("Save_index",lognumber);
        cv.put("Save_chapter",record[0]);
        cv.put("Save_section",record[1]);
        cv.put("Save_name",logname);
        db.insert(tb_save,null,cv);
        System.out.println("logname,"+logname);
        Cursor save= db.rawQuery("SELECT * FROM "+tb_save,null);
        if(save.moveToFirst()) {
            do {
                for (int j = 0; j < 4; j++) {
                    System.out.println(j+","+save.getString(j));
                }
            } while (save.moveToNext());
        }
        ContentValues values = new ContentValues();
        values.put("User_id", lognumber);
        db.update(tb_bag, values, "User_id = " + 0, null);
    }

    //工具型函數
    private String readTextFromSDcard(InputStream is) throws Exception {
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer("");
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
            buffer.append("\n");
        }
        return buffer.toString();
    }
    public void Sound(String location){
        HashMap<String, Integer> location_Sound = new HashMap<>();
        location_Sound.put("path", VoiceMap.get(location));
        uri = Uri.parse("android.resource://" + getPackageName() + "/" + location_Sound.get("path"));
        mper = new MediaPlayer();
        mper.setOnPreparedListener(this);
        mper.setOnErrorListener(this);
        mper.setOnCompletionListener(this);
        try {
            mper.reset();
            mper.setDataSource(this, uri);
            mper.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onCompletion(MediaPlayer mediaPlayer) {
        mper.seekTo(0);
    }
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }
    public void onPrepared(MediaPlayer mediaPlayer) {
        mper.start();
    }

    private void initControls(){
        try
        {
            volumeSeekbar = (SeekBar)findViewById(R.id.seekBar);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeSeekbar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));

            volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                public void onStopTrackingTouch(SeekBar arg0)
                {
                }

                public void onStartTrackingTouch(SeekBar arg0)
                {
                }

                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}