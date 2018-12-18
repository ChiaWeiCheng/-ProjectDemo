package com.example.yenti.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.os.Handler;

import java.io.IOException;
import java.util.HashMap;

public class fighting extends AppCompatActivity implements MediaPlayer.OnPreparedListener,MediaPlayer.OnErrorListener,MediaPlayer.OnCompletionListener {

    Handler handler = new Handler();
    boolean first_time_attack = true;
    boolean shout_attack = true;
    private static int onclicktime;
    long last_attack_time;
    int as, ad;
    int cont;
    int attack = 0;
    int contime;
    int action, actionsu, actionfa;
    int move;
    float dx;
    float dy;
    private float downX;    //按下时 的X坐标
    private float downY;    //按下时 的Y坐标
    int num1;
    int num2;
    long diff;
    HashMap<String, Integer> VoiceMap;
    Uri uri;
    MediaPlayer mper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gaming);
        action = 0;
        contime = 0;
        actionsu = 0;
        move = 0;
        actionsu = 0;//計算成功次數
        actionfa = 0;//計算失敗次數
        VoiceMap = new HashMap<String, Integer>();
        CreateVoiceData();
        Intent it = getIntent();
        num1 = it.getIntExtra("編號", 0);
        num2 = it.getIntExtra("次數", 0);

    }

    private void fight(int ad,float downX, float downY, float dx, float dy, int num1, int num2) {
        switch (num1) {
            case 2:
                System.out.println("進入事件");
                if (mper != null && mper.isPlaying()) {
                    mper.stop();
                }
                if (downX > 150 && downX < 350 && downY > 250 && downY < 450) {
                    System.out.println("採蘋果成功");
                    actionsu++;
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("applevoice");
                    //需+摘蘋果聲applevoice
                } else if (downX > 350 && downY > 450) {
                    System.out.println("沒點到");
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("leafvoice");
                    //需+撥草聲leafvoice
                }
                if (actionsu == num2) {
                    finish();
                }
                break;
            case 3:
                last_attack_time = System.currentTimeMillis();
                if (first_time_attack == true) {
                    handler.postDelayed(HandlerThread, 5000);
                    first_time_attack = false;
                }
                // 判斷攻擊是否有效
                if(attack==1){
                    if (dx / dy > 0.5 || dx / dy < -0.5) {
                        actionsu++;
                        contime = 0;
                        attack=0;
                        if (mper != null && mper.isPlaying()) {
                            mper.stop();
                        }
                        Sound("woodattack");
                    }
                        else
                        {
                            System.out.println("沒砍到");
                            actionfa++;
                            attack=0;
                            contime = 0;
                            dx = 0;
                            dy = 0;
                            if (mper != null && mper.isPlaying()) {
                                mper.stop();
                            }
                            Sound("prologue16");
                        }
                    }
                else if(attack==0){
                    System.out.println("沒砍到");
                    actionfa++;
                    contime = 0;
                    dx = 0;
                    dy = 0;
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("prologue16");
                }
                if (actionsu > num2) {

                    handler.removeCallbacks(HandlerThread);
                    finish();
                }
                if (actionfa > 4)   //如果失敗次數過多則重新開始//死掉條件增加
                {
                    actionsu = 0;
                    actionfa = 0;
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    //播放deadvoice
                    Sound("prologue15");


                }


                break;
            case 4:
                last_attack_time = System.currentTimeMillis();
                if (first_time_attack == true) {
                    handler.postDelayed(HandlerThread, 5000);
                    first_time_attack = false;
                }
                // 判斷攻擊是否有效
                if(attack==1){
                    if (dx / dy < 0.3 && dx / dy > -0.3) {
                        actionsu++;
                        contime = 0;
                        attack=0;
                        if (mper != null && mper.isPlaying()) {
                            mper.stop();
                        }
                        Sound("sectionone8");
                        contime = 0;
                    }
                    else
                    {
                        actionfa++;
                        attack=0;
                        contime = 0;
                        dx = 0;
                        dy = 0;
                        if (mper != null && mper.isPlaying()) {
                            mper.stop();
                        }
                        Sound("sectionone9");
                    }
                }
                else if(attack==0){
                    System.out.println("沒砍到");
                    actionfa++;
                    contime = 0;
                    dx = 0;
                    dy = 0;
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectionone9");
                }



                if (actionfa > 3) {
                    actionsu = 0;
                    actionfa = 0;
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Sound("sectionone7");
                        }
                    }, 1000);
                    // 需+播放音檔sectionone7
                    System.out.println("死掉囉");
                }
                if (actionsu > num2) {

                    handler.removeCallbacks(HandlerThread);
                    finish();
                }

                break;
            case 7:
                if (mper != null && mper.isPlaying()) {
                    mper.stop();
                }
                if (dx / dy > 0.5 || dx / dy < -0.5) {//判斷斬擊方向是否成功
                    actionsu++;
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectiontwo5");
                    //需+播放音檔sectiontwo5
                } else {
                    actionfa++;
                }
                if (actionsu > num2) {
                    handler.removeCallbacks(HandlerThread);
                    finish();
                }
                break;
            case 9:
                last_attack_time = System.currentTimeMillis();
                if (first_time_attack == true) {
                    handler.postDelayed(HandlerThread, 5000);
                    first_time_attack = false;
                }
                // 判斷攻擊是否有效
                if(attack==1){
                    if (dx / dy > 0.5 || dx / dy < -0.5) {
                        actionsu++;
                        contime = 0;
                        attack=0;
                        if (mper != null && mper.isPlaying()) {
                            mper.stop();
                        }
                        Sound("sectionthree3");
                        contime = 0;
                    }
                    else
                    {
                        System.out.println("沒砍到");
                        actionfa++;
                        attack=0;
                        contime = 0;
                        dx = 0;
                        dy = 0;
                        if (mper != null && mper.isPlaying()) {
                            mper.stop();
                        }
                        Sound("sectionthree2");
                    }
                }
                else if(attack==0){
                    System.out.println("沒砍到");
                    actionfa++;
                    contime = 0;
                    dx = 0;
                    dy = 0;
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectionthree2");
                }


                if (actionfa > 3) {

                    actionsu = 0;
                    actionfa = 0;
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectionthree1");
                    //提示聲sectionthree1
                }
                if (actionsu > num2) {
                    handler.removeCallbacks(HandlerThread);
                    finish();
                }
                break;
            case 11:
                shout_attack=true;
                last_attack_time = System.currentTimeMillis();
                if (first_time_attack == true) {
                    handler.postDelayed(HandlerThread, 5000);
                    first_time_attack = false;
                }
                // 判斷攻擊是否有效
                if(ad==1)
                {
                    if(dy/dx>0)
                    {
                        if (mper != null && mper.isPlaying()) {
                            mper.stop();
                        }
                        Sound("sectionfour62");
                        System.out.println("射左邊"+ad);
                        actionsu++;
                    }
                    else if(dy/dx<0)
                    {
                        if (mper != null && mper.isPlaying()) {
                            mper.stop();
                        }
                        Sound("sectionfour61");
                        System.out.println("射右邊"+ad);
                        actionfa++;
                    }
                }
                if(ad==2)
                {
                    if(dy/dx<0)
                    {
                        if (mper != null && mper.isPlaying()) {
                            mper.stop();
                        }
                        Sound("sectionfour62");
                        System.out.println("射左邊"+ad);
                        actionfa++;
                    }
                    else if(dy/dx>0)
                    {
                        if (mper != null && mper.isPlaying()) {
                            mper.stop();
                        }
                        Sound("sectionfour61");
                        System.out.println("射右邊"+ad);
                        actionsu++;
                    }
                }
                else
                {
                    if(dy/dx<0)
                    {
                        if (mper != null && mper.isPlaying()) {
                            mper.stop();
                        }
                        Sound("sectionfour62");
                        System.out.println("射左邊"+ad);
                        actionfa++;
                    }
                    else if(dy/dx>0)
                    {
                        if (mper != null && mper.isPlaying()) {
                            mper.stop();
                        }
                        Sound("sectionfour61");
                        System.out.println("射右邊"+ad);
                        actionfa++;
                    }
                }



                if (actionfa > 5) {
                    actionfa = 0;
                    actionsu = 0;
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectionfour4");
                    //播放提示聲表示重新開始 sectionfour4
                }
                if (actionsu > num2) {
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectionfour7");
                    handler.removeCallbacks(HandlerThread);
                    finish();
                }
                break;
            case 16:

                shout_attack=true;
                last_attack_time = System.currentTimeMillis();
                if (first_time_attack == true) {
                    handler.postDelayed(HandlerThread, 5000);
                    first_time_attack = false;
                }
                // 判斷攻擊是否有效
                if(ad==1)
                {
                    if(dy/dx>0)
                    {
                        Sound("sectionfour61");
                        System.out.println("射左邊"+ad);
                        actionsu++;
                    }
                    else if(dy/dx<0)
                    {
                        Sound("sectionfour62");
                        System.out.println("射右邊"+ad);
                        actionfa++;
                    }
                }
                if(ad==2)
                {
                    if(dy/dx<0)
                    {
                        Sound("sectionfour61");
                        System.out.println("射左邊"+ad);
                        actionfa++;
                    }
                    else if(dy/dx>0)
                    {
                        Sound("sectionfour62");
                        System.out.println("射右邊"+ad);
                        actionsu++;
                    }
                }
                else
                {
                    if(dy/dx<0)
                    {
                        Sound("sectionfour61");
                        System.out.println("射左邊"+ad);
                        actionfa++;
                    }
                    else if(dy/dx>0)
                    {
                        Sound("sectionfour62");
                        System.out.println("射右邊"+ad);
                        actionfa++;
                    }
                }

                if (actionfa > 4) {
                    actionfa = 0;
                    actionsu = 0;
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectionseven6");
                    //播放提示聲表示重新開始sectionseven6
                }


                if (mper != null && mper.isPlaying()) {
                    mper.stop();
                }
                //Sound("sectionseven8");
                //播放音檔sectionseven7,sectionseven8
                if (actionsu > num2) {
                    Sound("sectionseven7");
                    handler.removeCallbacks(HandlerThread);
                    finish();
                }
                break;
            case 17:
                if (mper != null && mper.isPlaying()) {
                    mper.stop();
                }
                if (downX > 0 && downY > 0) {
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectioneight3");
                    //播放sectioneight3
                }
                if (contime == 0) {
                    new Handler().postDelayed(new Runnable() {//在当前线程（也即主线程中）开启一个消息处理器，并在3秒后在主线程中执行，从而来更新UI
                        @Override
                        public void run() {
                            actionfa++;
                            contime = 0;
                        }
                    }, 5000);//5秒后发送
                }
                if (move == 0 && downX < 350) {
                    actionsu++;
                    move = 0;
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectioneight4");
                    //播放sectioneight4
                } else if (move == 1 && downX > 350) {
                    actionsu++;
                    move = 0;
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectioneight4");
                    //播放sectioneight4
                } else {
                    actionfa++;
                }
                if (actionsu > 50) {
                    handler.removeCallbacks(HandlerThread);
                    finish();
                }
                break;
            case 18:
                if (mper != null && mper.isPlaying()) {
                    mper.stop();
                }
                if (cont == 1) {
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectioneight10");
                    //播放sectioneight10
                }
                as = (int) (Math.random() * 5 + 2);
                new Handler().postDelayed(new Runnable() {//在当前线程（也即主线程中）开启一个消息处理器，并在3秒后在主线程中执行，从而来更新UI
                    @Override
                    public void run() {
                        if (contime == 0) {
                            new Handler().postDelayed(new Runnable() {//在当前线程（也即主线程中）开启一个消息处理器，并在3秒后在主线程中执行，从而来更新UI
                                @Override
                                public void run() {
                                    if (cont == 1) {
                                        actionsu++;
                                        contime = 0;
                                    } else {
                                        actionfa++;
                                        contime = 0;
                                    }
                                }
                            }, 2000);//3秒后发送
                        }
                        if (mper != null && mper.isPlaying()) {
                            mper.stop();
                        }
                        Sound("sectioneight11");
                        //需+播放音檔sectioneight11
                    }
                }, as * 1000);//2~5秒后发送
                if (actionfa == 3) {
                    Sound("deadvoice");
                    //deadvoice
                    actionfa = 0;
                    actionsu = 0;
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectioneight9");
                    //提示聲sectioneight9
                }
                if (actionsu > num2) {
                    handler.removeCallbacks(HandlerThread);
                    finish();
                }
                break;
            case 21:
                last_attack_time = System.currentTimeMillis();
                if (first_time_attack == true) {
                    handler.postDelayed(HandlerThread, 5000);
                    first_time_attack = false;
                }
                // 判斷攻擊是否有效
                if(attack==1){
                    if (dx / dy > 0.5 || dx / dy < -0.5) {
                        actionsu++;
                        contime = 0;
                        attack=0;
                        if (mper != null && mper.isPlaying()) {
                            mper.stop();
                        }
                        Sound("sectionthree3");
                        contime = 0;
                    }
                    else
                    {
                        System.out.println("沒砍到");
                        actionfa++;
                        attack=0;
                        contime = 0;
                        dx = 0;
                        dy = 0;
                        if (mper != null && mper.isPlaying()) {
                            mper.stop();
                        }
                        Sound("sectionthree2");
                    }
                }
                else if(attack==0){
                    System.out.println("沒砍到");
                    actionfa++;
                    contime = 0;
                    dx = 0;
                    dy = 0;
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectionthree2");
                }

                if (mper != null && mper.isPlaying()) {
                    mper.stop();
                }

                if (actionfa > 3) {
                    actionsu = 0;
                    actionfa = 0;
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectionnine15");
                    //提示聲sectionnine15
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Sound("deadvoice");
                        }
                    }, 2000);
                    //死亡聲deadvoice
                }
                if (actionsu > num2) {
                    handler.removeCallbacks(HandlerThread);
                    finish();
                }
                break;
            case 23:
                if (mper != null && mper.isPlaying()) {
                    mper.stop();
                }
                last_attack_time = System.currentTimeMillis();
                if (first_time_attack == true) {
                    handler.postDelayed(HandlerThread, 5000);
                    first_time_attack = false;
                }
                // 判斷攻擊是否有效
                if(attack==1){
                    if (dx / dy > 0.5 || dx / dy < -0.5) {
                        actionsu++;
                        contime = 0;
                        attack=0;
                        if (mper != null && mper.isPlaying()) {
                            mper.stop();
                        }
                        Sound("sectionten10");
                        contime = 0;
                    }
                    else
                    {
                        System.out.println("沒砍到");
                        actionfa++;
                        attack=0;
                        contime = 0;
                        dx = 0;
                        dy = 0;
                        if (mper != null && mper.isPlaying()) {
                            mper.stop();
                        }
                        Sound("sectionten11");
                    }
                }
                else if(attack==0){
                    System.out.println("沒砍到");
                    actionfa++;
                    contime = 0;
                    dx = 0;
                    dy = 0;
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectionten11");
                }


                if (mper != null && mper.isPlaying()) {
                    mper.stop();
                }

                if (actionfa > 3) {
                    Sound("deadvoice");//死亡聲deadvoice
                    actionsu = 0;
                    actionfa = 0;
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectionten8");
                    //提示聲sectionten8

                }
                if (actionsu > num2) {
                    handler.removeCallbacks(HandlerThread);
                    finish();
                }

                break;
            case 24:
                if (mper != null && mper.isPlaying()) {
                    mper.stop();
                }
                if (contime == 1) {
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectioneleven5");
                    //播放sectioneleven5
                    actionsu++;
                }
                if (actionsu > num2) {
                    handler.removeCallbacks(HandlerThread);
                    finish();
                }
                break;
            case 27:
                if (mper != null && mper.isPlaying()) {
                    mper.stop();
                }

                last_attack_time = System.currentTimeMillis();
                if (first_time_attack == true) {
                    handler.postDelayed(HandlerThread, 5000);
                    first_time_attack = false;
                }
                // 判斷攻擊是否有效
                if(attack==1){
                    if (dx / dy > 0.5 || dx / dy < -0.5) {
                        actionsu++;
                        contime = 0;
                        attack=0;
                        if (mper != null && mper.isPlaying()) {
                            mper.stop();
                        }
                        Sound("sectionten10");
                        contime = 0;
                    }
                    else
                    {
                        System.out.println("沒砍到");
                        actionfa++;
                        attack=0;
                        contime = 0;
                        dx = 0;
                        dy = 0;
                        if (mper != null && mper.isPlaying()) {
                            mper.stop();
                        }
                        Sound("sectionten11");
                    }
                }
                else if(attack==0){
                    System.out.println("沒砍到");
                    actionfa++;
                    contime = 0;
                    dx = 0;
                    dy = 0;
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectionten11");
                }

                if (actionfa > 4) {
                    actionsu = 0;
                    actionfa = 0;
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectiontwelve7");
                    //播放音檔sectiontwelve7
                }
                if (mper != null && mper.isPlaying()) {
                    mper.stop();
                }
                if (actionsu > num2) {
                    Sound("sectiontwelve9");
                    handler.removeCallbacks(HandlerThread);
                    //播放sectiontwelve9
                    finish();
                }
                break;
            case 28:
                if (mper != null && mper.isPlaying()) {
                    mper.stop();
                }
                move = 0;
                if (move == 0 && downX < 350) {
                    actionsu++;
                    move = 1;
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectiontwelve16");
                    //播放sectiontwelve16
                } else if (move == 1 && downX > 350) {
                    actionsu++;
                    move = 0;
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectiontwelve16");
                    //播放sectiontwelve16
                } else {
                    actionfa++;
                }
                if (action > 20) {
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectiontwelve15");
                    //播放提示聲sectiontwelve15
                    actionsu = 0;
                    actionfa = 0;
                }
                if (actionsu > num2) {
                    handler.removeCallbacks(HandlerThread);
                    finish();
                }
                break;
            case 30:
                if (mper != null && mper.isPlaying()) {
                    mper.stop();
                }

                Sound("sectiontwelve8");
                //播放sectiontwelve8
                if (mper != null && mper.isPlaying()) {
                    mper.stop();
                }

                last_attack_time = System.currentTimeMillis();
                if (first_time_attack == true) {
                    handler.postDelayed(HandlerThread, 5000);
                    first_time_attack = false;
                }
                // 判斷攻擊是否有效
                if(attack==1){
                    if (dx / dy > 0.5 || dx / dy < -0.5) {
                        actionsu++;
                        contime = 0;
                        attack=0;
                        if (mper != null && mper.isPlaying()) {
                            mper.stop();
                        }
                        Sound("sectionten10");
                        contime = 0;
                    }
                    else
                    {
                        System.out.println("沒砍到");
                        actionfa++;
                        attack=0;
                        contime = 0;
                        dx = 0;
                        dy = 0;
                        if (mper != null && mper.isPlaying()) {
                            mper.stop();
                        }
                        Sound("sectionten11");
                    }
                }
                else if(attack==0){
                    System.out.println("沒砍到");
                    actionfa++;
                    contime = 0;
                    dx = 0;
                    dy = 0;
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectionten11");
                }


                if (actionfa > 4) {
                    actionsu = 0;
                    actionfa = 0;
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectiontwelve7");
                    //播放音檔sectiontwelve7
                }
                if (mper != null && mper.isPlaying()) {
                    mper.stop();
                }
                Sound("sectiontwelve9");
                //播放sectiontwelve9
                if (actionsu > num2) {
                    handler.removeCallbacks(HandlerThread);
                    finish();
                }
                break;
            case 31:
                if (mper != null && mper.isPlaying()) {
                    mper.stop();
                }
                move = 0;

                if (move == 0 && downX < 350) {
                    actionsu++;
                    move = 1;
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectiontwelve16");
                    //播放sectiontwelve16
                } else if (move == 1 && downX > 350) {
                    actionsu++;
                    move = 0;
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectiontwelve16");
                    //播放sectiontwelve16
                } else {
                    actionfa++;
                }
                if (action > 20) {
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectiontwelve15");
                    //播放提示聲sectiontwelve15
                    actionsu = 0;
                    actionfa = 0;
                }
                if (actionsu > num2) {
                    finish();
                }
                break;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {

        String action = "";
        //在触发时回去到起始坐标
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //将按下时的坐标存储
                downX = x;
                downY = y;
                if(num1==11)
                {
                    if (mper != null && mper.isPlaying()) {
                        mper.stop();
                    }
                    Sound("sectionfour5");
                    System.out.println("有放音樂");
                }
                shout_attack=false;
                cont = 1;
                break;

            case MotionEvent.ACTION_UP:
                //获取到距离差
                dx = x - downX;
                dy = y - downY;
                contime = 1;
                cont = 0;
                fight(ad,downX, downY, dx, dy, num1, num2);
                //防止是按下也判断

                break;
        }

        System.out.println(dx + dy);
        return super.onTouchEvent(event);
    }

    private void CreateVoiceData() {
        VoiceMap.put("woodattack", R.raw.woodattack);
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

    }

    public void Sound(String location) {
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

    Runnable HandlerThread = new Runnable() {

        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            diff = currentTime - last_attack_time;
            as = (int) (Math.random() * 5 + 3);
            if (diff >= 7000) {
                actionfa++;
                if (mper != null && mper.isPlaying()) {
                    mper.stop();
                }

                System.out.println("過時操作");
                Sound("prologue16");
            }
                if (mper != null && mper.isPlaying()) {
                    mper.stop();
                }
                if(num1==3)
                {
                    Sound("prologue14");
                    //需+播放音檔怪物過來
                }

            attack=1;
            ad = (int) (Math.random() * 2 + 1);
            if(num1==22){
                if (mper != null && mper.isPlaying()) {
                    mper.stop();
                }
                Sound("sectionnine16");
            }
            if(num1==23)
            {
                if (mper != null && mper.isPlaying()) {
                    mper.stop();
                }
                Sound("sectionten9");
            }
            if(num1==27){
                if (mper != null && mper.isPlaying()) {
                    mper.stop();
                }
                Sound("sectiontwelve81");
            }
        if(num1==11){
             if (ad == 1) {
                if (mper != null && mper.isPlaying()) {
              mper.stop();
         }
         if(shout_attack==true)
         {
             if (mper != null && mper.isPlaying()) {
                 mper.stop();
             }
             Sound("preyl");
         }

           //播放獵物在左方音檔preyl
     }
     else if (ad == 2) {
        if (mper != null && mper.isPlaying()) {
                mper.stop();
        }
        if(shout_attack==true)
        {
            if (mper != null && mper.isPlaying()) {
                mper.stop();
            }

            Sound("preyr");
        }

        //播放獵物在左方音檔preyr
    }
}
            handler.postDelayed(HandlerThread, as*1000);
        }

    };

}
