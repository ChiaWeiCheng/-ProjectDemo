package com.example.yenti.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class cartoonone extends AppCompatActivity {
    Handler handler = new Handler();
    int layout_count=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cartoonone);
        handler.postDelayed(HandlerThread, 5000);


        /**/
    }



    Runnable HandlerThread = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if(layout_count==1){
                setContentView(R.layout.cartoontwo);
                layout_count=2;
                handler.postDelayed(HandlerThread, 5000);
            }
            else if(layout_count==2){
                setContentView(R.layout.cartoonthree);
                layout_count=3;
                handler.postDelayed(HandlerThread, 5000);
            }
            else{
                finish();
            }
        }
    };


}
