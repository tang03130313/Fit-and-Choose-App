package com.tang.fitandchoose;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Scene;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.lang.Character;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class Users_detailActivity extends Activity {
    LinearLayout Button_users_detail_finish;
    TextView Textview_users_detail_BMR;
    EditText EditText_users_detail_name,EditText_users_detail_weight,EditText_users_detail_age,EditText_users_detail_height;
    RadioGroup radioGroup;
    RadioButton rb_boy,rb_gril;

    String[] user = new String[7];

    String message;

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    int user_state;
    boolean result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_users_detail);



        Textview_users_detail_BMR = (TextView) findViewById(R.id.textview_users_detail_BMR);
        EditText_users_detail_name = (EditText) findViewById(R.id.users_detail_name);
        radioGroup = (RadioGroup)findViewById(R.id.rg);
        rb_boy = (RadioButton) findViewById(R.id.rb_boy);
        rb_gril = (RadioButton) findViewById(R.id.rb_gril);
        EditText_users_detail_age = (EditText) findViewById(R.id.users_detail_age);
        EditText_users_detail_height = (EditText) findViewById(R.id.users_detail_height);
        EditText_users_detail_weight = (EditText) findViewById(R.id.users_detail_weight);
        Button_users_detail_finish = (LinearLayout) findViewById(R.id.users_detail_finish);

        set();
        EditText_users_detail_name.addTextChangedListener(NameTextWatcher);
        EditText_users_detail_age.addTextChangedListener(AgeTextWatcher);
        EditText_users_detail_height.addTextChangedListener(HeightTextWatcher);
        EditText_users_detail_weight.addTextChangedListener(WeightTextWatcher);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       return super.onOptionsItemSelected(item);
    }


    private TextWatcher NameTextWatcher = new TextWatcher() {
        //THE INPUT ELEMENT IS ATTACHED TO AN EDITABLE,
        //THEREFORE THESE METHODS ARE CALLED WHEN THE TEXT IS CHANGED

        public void onTextChanged(CharSequence s,
                                  int start, int before, int count){
            //CATCH AN EXCEPTION WHEN THE INPUT IS NOT A NUMBER
            try {
                user[1] = s.toString();
                set_bmr();
            }catch (NumberFormatException e){
                result = false;
            }
        }
        public void afterTextChanged(Editable s) {}
        public void beforeTextChanged(CharSequence s,
                                      int start, int count, int after){}
    };
    private TextWatcher AgeTextWatcher = new TextWatcher() {
        //THE INPUT ELEMENT IS ATTACHED TO AN EDITABLE,
        //THEREFORE THESE METHODS ARE CALLED WHEN THE TEXT IS CHANGED

        public void onTextChanged(CharSequence s,
                                  int start, int before, int count){
            //CATCH AN EXCEPTION WHEN THE INPUT IS NOT A NUMBER
            try {
                user[3]= s.toString();
                set_bmr();
            }catch (NumberFormatException e){
                result = false;
            }
        }
        public void afterTextChanged(Editable s) {
            result = false;
        }
        public void beforeTextChanged(CharSequence s,
                                      int start, int count, int after){}
    };
    private TextWatcher HeightTextWatcher = new TextWatcher() {
        //THE INPUT ELEMENT IS ATTACHED TO AN EDITABLE,
        //THEREFORE THESE METHODS ARE CALLED WHEN THE TEXT IS CHANGED

        public void onTextChanged(CharSequence s,
                                  int start, int before, int count){
            //CATCH AN EXCEPTION WHEN THE INPUT IS NOT A NUMBER
            try {
                user[4] = s.toString();
                set_bmr();
            }catch (NumberFormatException e){
                result = false;
            }
        }
        public void afterTextChanged(Editable s) {}
        public void beforeTextChanged(CharSequence s,
                                      int start, int count, int after){}
    };
    private TextWatcher WeightTextWatcher = new TextWatcher() {

        public void onTextChanged(CharSequence s,
                                  int start, int before, int count){
            try {
                user[5]= s.toString();
                set_bmr();
            }catch (NumberFormatException e){
                result = false;
            }
        }
        public void afterTextChanged(Editable s) {}
        public void beforeTextChanged(CharSequence s,
                                      int start, int count, int after){}
    };

    public void set_bmr(){
        if(!check()) {
            if (user[2].equals("男"))
                user[6] = "" + ((13.7 * Double.valueOf(user[5])) + (5.0 * Double.valueOf(user[4])) - (6.8 * Double.valueOf(user[3])) + 66);
            else if (user[2].equals("女"))
                user[6] = "" + ((9.6 * Double.valueOf(user[5])) + (1.8 * Double.valueOf(user[4])) - (4.7 * Double.valueOf(user[3])) + 655);
            Textview_users_detail_BMR.setText(user[6]);
        }
        else
            Textview_users_detail_BMR.setText("0");
    }

    public void initial(){
        EditText_users_detail_name.setText("");
        EditText_users_detail_age.setText("");
        EditText_users_detail_height.setText("");
        EditText_users_detail_weight.setText("");
        Textview_users_detail_BMR.setText("0");
        rb_boy.setChecked(true);
        user[2] = "男";
    }

    public static boolean isNumer(String str){
        Pattern pattern=Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public boolean check(){
        boolean temp = false;
        message = " ";
        if(user[1].equals("0")){
            message += "名字 ";
            temp = true;
        }
        if( !user[2].equals("男") && !user[2].equals("女") ){
            message += user[1];
            message += "性別 ";
            temp = true;
        }
        if(user[3].equals("0") || !isNumer(user[3])){
            message += "年齡 ";
            temp = true;
        }
        if(user[4].equals("0") || !isNumer(user[4]) || user[4].length() != 3 ){
            message += "身高 ";
            temp = true;
        }
        if(user[5].equals("0") || !isNumer(user[5]) || user[5].length() > 3 || user[5].length() < 2){
            message += "體重 ";
            temp = true;
        }
        return temp;
    }

    public void set() {
        Bundle bundle_pre = this.getIntent().getExtras();
        if(bundle_pre != null)
            user = bundle_pre .getStringArray("user");
        if(user[1].equals("0"))
            initial();
        else{
            EditText_users_detail_name.setText(user[1]);
            EditText_users_detail_age.setText(user[3]);
            EditText_users_detail_height.setText(user[4]);
            EditText_users_detail_weight.setText(user[5]);
            set_bmr();
            Textview_users_detail_BMR.setText(user[6]);
            if(user[2].equals("男")) {
                rb_boy.setChecked(true);
            }
            else if(user[2].equals("女"))
                rb_gril.setChecked(true);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_boy)
                    user[2] = "男";
                else if (checkedId == R.id.rb_gril)
                    user[2] = "女";
                set_bmr();
            }
        });

        Button_users_detail_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check() ){
                    new AlertDialog.Builder(Users_detailActivity.this).setTitle(message+"不能為空或格式有錯")
                            .setPositiveButton("了解",null).setNegativeButton("",null).create().show();
                }
                else {
                    ProgressDialog progressDialog=new ProgressDialog(Users_detailActivity.this);
                    progressDialog.setMessage("處理中,請稍候...");
                    progressDialog.show();
                    Map<String, Object> user_insert = new HashMap<>();
                    user_insert.put("name", user[1]);
                    if(user[2] == "男")
                        user_insert.put("sex", true);
                    else
                        user_insert.put("sex", false);
                    user_insert.put("age", user[3]);
                    user_insert.put("height", user[4]);
                    user_insert.put("weight", user[5]);
                    user_insert.put("bmr", user[6]);
                    db.collection("user").document(user[0]).collection("data") .add(user_insert);
                    db.collection("user").document(user[0]).collection("information")
                            .add(user_insert)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });

                    Intent intent = new Intent();
                    intent.setClass(Users_detailActivity.this, Users_loginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                        finish();
                }
            }
        });
    }
}
