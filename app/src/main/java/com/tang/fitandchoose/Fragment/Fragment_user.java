package com.tang.fitandchoose.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tang.fitandchoose.Choose_detailActivity;
import com.tang.fitandchoose.R;
import com.tang.fitandchoose.ShipItem;
import com.tang.fitandchoose.Users_loginActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.Random;

/**
 * Created by Carson_Ho on 16/5/23.
 */
public class Fragment_user extends Fragment

{
    Button Button_choose_delete,Button_choose_logout;

    TextView TextView_email,TextView_name,TextView_sex,TextView_age,TextView_height,TextView_weight,TextView_BMR,TextView_delete,TextView_logout;
    //Button Button_choose_next;
    ImageView ImageView_choose_main_img;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String[] user;

    String now_date;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_users_detail_view, null);
        user = Objects.requireNonNull(getActivity().getIntent().getExtras()).getStringArray("user");
        now_date = Objects.requireNonNull(getActivity().getIntent().getExtras()).getString("now_date");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        set_choose();
    }

    public void set_choose() {
        TextView_email = (TextView) view.findViewById(R.id.users_detail_email);
        TextView_name = (TextView) view.findViewById(R.id.users_detail_name);
        TextView_sex = (TextView) view.findViewById(R.id.users_detail_sex);
        TextView_age = (TextView) view.findViewById(R.id.users_detail_age);
        TextView_height = (TextView) view.findViewById(R.id.users_detail_height);
        TextView_weight = (TextView) view.findViewById(R.id.users_detail_weight);
        TextView_BMR = (TextView) view.findViewById(R.id.textview_users_detail_BMR);
        TextView_delete = (TextView) view.findViewById(R.id.delete);
        TextView_logout = (TextView)  view.findViewById(R.id.logout);

        TextView_email.setText(user[0]);
        TextView_name.setText(user[1]);
        TextView_sex.setText(user[2]);
        TextView_age.setText(user[3]);
        TextView_height.setText(user[4]);
        TextView_weight.setText(user[5]);
        TextView_BMR.setText(user[6]);

        TextView_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity()).setTitle("確定登出？")
                        .setPositiveButton("取消",null).setNegativeButton("確定",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                final Bundle bundle = new Bundle();
                intent.setClass(getActivity(), Users_loginActivity.class);
                bundle.putInt("logout",1);
                intent.putExtras(bundle);
                startActivity(intent);
                    }}).create().show();
            }
        });
        TextView_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity()).setTitle("確定刪除帳戶？")
                        .setPositiveButton("取消",null).setNegativeButton("確定",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("user").document(user[0])
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent intent = new Intent();
                                        final Bundle bundle = new Bundle();
                                        intent.setClass(getActivity(), Users_loginActivity.class);
                                        bundle.putInt("logout",2);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                    }}).create().show();
            }
        });

    }


}