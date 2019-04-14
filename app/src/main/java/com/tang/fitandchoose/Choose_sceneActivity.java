package com.tang.fitandchoose;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Random;

public class Choose_sceneActivity extends Activity {

    LinearLayout Button_button_pre,Button_button_next;
    ImageView ImageView_imageView_img;

    String[] choose_restaurant=new String[11],user;

    String uri="", pre="";
    SharedPreferences settings;
    int img_num = 7;      //0:choose_main,1:choose_add,2:choose_detail
    Random ran = new Random();
    boolean result;
    int imageResource;
    Drawable image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_scene);

        Button_button_pre = (LinearLayout) findViewById(R.id.button_pre);
        Button_button_next = (LinearLayout) findViewById(R.id.button_next);
        ImageView_imageView_img = (ImageView) findViewById(R.id.imageView_img);
        MobileAds.initialize(Choose_sceneActivity.this, "ca-app-pub-8943200594190940~7230710887");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        //mAdView.setAdSize(AdSize.SMART_BANNER);
        AdRequest adRequest = new AdRequest.Builder().build();

        //加入你的設備代碼
        mAdView.loadAd(adRequest);
        set();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }


    public void initial(){
        uri =  choose_restaurant[img_num];
        if(img_num == 7)
            Button_button_pre.setVisibility(View.INVISIBLE);
        else
            Button_button_pre.setVisibility(View.VISIBLE);
        if(img_num == 10)
            Button_button_next.setVisibility(View.INVISIBLE);
        else
            Button_button_next.setVisibility(View.VISIBLE);
        imageResource = getResources().getIdentifier(uri, null, this.getPackageName());
        image = getResources().getDrawable(imageResource);
        ImageView_imageView_img.setImageDrawable(image);
    }

    public void set() {
        Bundle bundle_pre = this.getIntent().getExtras();
        if(bundle_pre != null) {
            user = bundle_pre.getStringArray("user");
            choose_restaurant = bundle_pre.getStringArray("choose_restaurant");
        }
        initial();
        Button_button_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(img_num > 7){
                    img_num--;
                    initial();
                }
            }
        });
        Button_button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(img_num < 10){
                    img_num++;
                    initial();
                }
            }
        });
    }

}
