package com.tang.fitandchoose;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Scene;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Random;

public class Choose_detailActivity extends Activity {
    LinearLayout Button_choose_detail_go;
    Button Button_choose_detail_back;
    ImageView Choose_detail_1;
    TextView Choose_detail_2,Choose_detail_3,Choose_detail_4,Choose_detail_0;

    String[] choose_restaurant,user;

    String phone,now_date;

    SharedPreferences settings;
    boolean result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose_detail);

        Button_choose_detail_back = (Button) findViewById(R.id.button_choose_detail_back);
        Button_choose_detail_go = (LinearLayout) findViewById(R.id.button_choose_detail_go);
        Choose_detail_1 = (ImageView) findViewById(R.id.choose_detail_1);
        Choose_detail_0 = (TextView) findViewById(R.id.choose_detail_0);
        Choose_detail_2 = (TextView) findViewById(R.id.choose_detail_2);
        Choose_detail_3 = (TextView) findViewById(R.id.choose_detail_3);
        Choose_detail_4 = (TextView) findViewById(R.id.choose_detail_4);
        MobileAds.initialize(Choose_detailActivity.this, "ca-app-pub-8943200594190940~7230710887");
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
        int imageResource = getResources().getIdentifier(choose_restaurant[5], null, getPackageName());
        Drawable image = getResources().getDrawable(imageResource);
        Choose_detail_1.setImageDrawable(image);
        Choose_detail_0.setText(choose_restaurant[0]);
        Choose_detail_2.setText(choose_restaurant[2]);
        Choose_detail_3.setText(choose_restaurant[4]);
        Choose_detail_4.setText(choose_restaurant[3]);
    }

    public void set(){
        Bundle bundle_pre = this.getIntent().getExtras();
        if(bundle_pre != null) {
            choose_restaurant = bundle_pre.getStringArray("choose_restaurant");
            user = bundle_pre.getStringArray("user");
            now_date = bundle_pre.getString("now_date");
        }
        initial();
        Button_choose_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });
        Button_choose_detail_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                final Bundle bundle = new Bundle();
                intent.setClass(Choose_detailActivity.this, Fit_searchActivity.class);
                intent.setAction(Intent.ACTION_SEARCH);
                intent.putExtra(SearchManager.QUERY, choose_restaurant[0]);
                bundle.putStringArray("user",user);
                bundle.putString("cate","早餐");
                bundle.putString("now_date",now_date);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        Choose_detail_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q= "+choose_restaurant[0]);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        Choose_detail_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+Choose_detail_4.getText()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        Choose_detail_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                final Bundle bundle = new Bundle();
                intent.setClass(Choose_detailActivity.this, Choose_sceneActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                bundle.putStringArray("choose_restaurant",choose_restaurant);
                bundle.putStringArray("user",user);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


}
