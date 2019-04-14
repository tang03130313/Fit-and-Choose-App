package com.tang.fitandchoose.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.tang.fitandchoose.Choose_detailActivity;
import com.tang.fitandchoose.R;
import com.tang.fitandchoose.ShipItem;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Random;
import com.google.android.gms.ads.MobileAds;

/**
 * Created by Carson_Ho on 16/5/23.
 */
public class Fragment_choose extends Fragment

{
    LinearLayout Button_choose_want,Button_choose_next;
    //Button Button_choose_next;
    ImageView ImageView_choose_main_img;



    ShipItem shipItem = new ShipItem();

    String[] choose_restaurant = new String[11],user;

    String uri="", pre="",now_date;
    SharedPreferences settings;
    int img_num = 0;      //0:choose_main,1:choose_add,2:choose_detail
    Random ran = new Random();
    boolean result;
    Drawable image;
    int imageResource;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_choose_main, null);
        user = Objects.requireNonNull(getActivity().getIntent().getExtras()).getStringArray("user");
        now_date = Objects.requireNonNull(getActivity().getIntent().getExtras()).getString("now_date");

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        set_choose();
    }

    public void main_random(){
        choose_restaurant = shipItem.random_restaurant();
        img_num = ran.nextInt(3);
        switch (img_num){
            case 0:
                uri = choose_restaurant[7];break;
            case 1:
                uri = choose_restaurant[8];break;
            case 2:
                uri = choose_restaurant[9];break;
            default:
                uri = choose_restaurant[10];break;
        }
        if(uri.equals(pre))
            main_random();
        pre = uri;
        imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
        image = getResources().getDrawable(imageResource);
        ImageView_choose_main_img.setImageDrawable(image);
    }

    public void set_choose() {
        Button_choose_next = (LinearLayout) view.findViewById(R.id.button_choose_next);
        Button_choose_want = (LinearLayout)  view.findViewById(R.id.button_choose_want);
        ImageView_choose_main_img = (ImageView) view.findViewById(R.id.imageView_choose_main_img);
        main_random();
        Button_choose_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_random();
            }
        });
        Button_choose_want.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				Intent intent = new Intent();
				final Bundle bundle = new Bundle();
				intent.setClass(getActivity().getApplication(), Choose_detailActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				bundle.putStringArray("choose_restaurant",choose_restaurant);
				bundle.putStringArray("user",user);
                bundle.putString("now_date",now_date);
				intent.putExtras(bundle);
				startActivity(intent);
            }
        });
        MobileAds.initialize(getActivity(), "ca-app-pub-8943200594190940~7230710887");
        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        //mAdView.setAdSize(AdSize.SMART_BANNER);
        AdRequest adRequest = new AdRequest.Builder().build();

        //加入你的設備代碼
        mAdView.loadAd(adRequest);

    }

    private Drawable loadImageFromURL(String url){
        try{
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable draw = Drawable.createFromStream(is, "src");
            return draw;
        }catch (Exception e) {
            //TODO handle error
            Log.i("loadingImg", e.toString());
            return null;
        }
    }

    public static Bitmap getBitmap(String path) throws IOException{

        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }


}