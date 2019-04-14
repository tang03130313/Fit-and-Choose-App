package com.tang.fitandchoose;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tang.fitandchoose.Data.Data_User_Detail;
import com.tang.fitandchoose.Data.MyDBHelper;
import com.tang.fitandchoose.item.Item_user_detail;


public class Users_loginActivity extends Activity {
    LinearLayout LinearLayout_button_send;
    TextView TextView_to_resister;
    EditText mEmailField,mPasswordField;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean result;
    String[] user_data = new String[7];
    Time t;
    String now_date;
    private static SQLiteDatabase database;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    // 資料庫名稱
    public static final String DATABASE_NAME = "mydatabase_test6.db";
    private static final int RC_SIGN_IN = 9001;
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    public static int VERSION = 9;
    int logout = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_login);

        mEmailField = (EditText) findViewById(R.id.fieldEmail);
        mPasswordField =(EditText)  findViewById(R.id.fieldPassword);
        TextView_to_resister = (TextView) findViewById(R.id.to_register);
        LinearLayout_button_send =(LinearLayout)  findViewById(R.id.button_send);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();
        Bundle bundle_pre = this.getIntent().getExtras();
        if(bundle_pre != null)
            logout =  bundle_pre.getInt("logout");
        if(logout == 1)
            signOut();
        else if(logout == 2)
            signDelete();
        set();
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(mAuth.getCurrentUser() != null)
            login_old();
    }

    private void signDelete() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        currentUser.delete();
        mAuth.signOut();
    }

    private void signOut() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.signOut();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
       // getMenuInflater().inflate(R.menu.question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       //  Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
       // if (id == R.id.action_settings) {
           // return true;
       // }
       return super.onOptionsItemSelected(item);
    }

    public void initial(){

    }

    public void set() {
        TextView_to_resister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //final Bundle bundle = new Bundle();
                intent.setClass(Users_loginActivity.this, Users_registerActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout_button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
            }
        });
    }

    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            user_data[0] = user.getEmail();
                            set_sql();
                            login_new();
                        } else {
                            Toast.makeText(Users_loginActivity.this, "帳號或密碼錯誤.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    public void login_old(){
        ProgressDialog progressDialog=new ProgressDialog(Users_loginActivity.this);
        progressDialog.setMessage("處理中,請稍候...");
        progressDialog.show();
        t=new Time("UTC+8");
        t.setToNow();
        String temp2 = ((t.month+1) < 10) ? "0"+(t.month+1) : ""+ (t.month+1);
        String temp = (t.monthDay < 10) ? "0"+t.monthDay : ""+ t.monthDay;
        now_date = "" + t.year + "-" + temp2 + "-" + temp;
        FirebaseUser user = mAuth.getCurrentUser();
        settings = getSharedPreferences("user_1", 0);
        user_data[0] = user.getEmail();
        user_data[1] = settings.getString("user1_name", "0");
        user_data[2] = settings.getString("user1_sex", "0");
        user_data[3] = settings.getString("user1_age", "0");
        user_data[4] = settings.getString("user1_height", "0");
        user_data[5] = settings.getString("user1_weight", "0");
        user_data[6] = settings.getString("user1_bmr", "0");
        Intent intent = new Intent();
        final Bundle bundle = new Bundle();
        intent.setClass(Users_loginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        bundle.putStringArray("user", user_data);
        bundle.putString("now_date", now_date);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void login_new(){
        ProgressDialog progressDialog=new ProgressDialog(Users_loginActivity.this);
        progressDialog.setMessage("處理中,請稍候...");
        progressDialog.show();
        t=new Time("UTC+8");
        t.setToNow();
        String temp2 = ((t.month+1) < 10) ? "0"+(t.month+1) : ""+ (t.month+1);
        String temp = (t.monthDay < 10) ? "0"+t.monthDay : ""+ t.monthDay;
        now_date = "" + t.year + "-" + temp2 + "-" + temp;
        FirebaseUser user = mAuth.getCurrentUser();
        user_data[0] = user.getEmail();
        db.collection("user").document(user.getEmail()).collection("information")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                user_data[1] = document.getString("name");
                                boolean temp =  document.getBoolean("sex");
                                if(temp)
                                    user_data[2] = "男";
                                else
                                    user_data[2] = "女";
                                user_data[3] = ""+""+document.getDouble("age").intValue();
                                user_data[4] = ""+document.getDouble("height").intValue();
                                user_data[5] = ""+document.getDouble("weight").intValue();
                                user_data[6] = ""+document.getDouble("bmr");
                                settings = getSharedPreferences("user", 0);
                                editor = settings.edit();
                                editor.putString("user1_name", user_data[1]);
                                editor.putString("user1_sex", user_data[2]);
                                editor.putString("user1_age", user_data[3]);
                                editor.putString("user1_height", user_data[4]);
                                editor.putString("user1_weight", user_data[5]);
                                editor.putString("user1_bmr", user_data[6]);
                                result = editor.commit();
                                Intent intent = new Intent();
                                final Bundle bundle = new Bundle();
                                intent.setClass(Users_loginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                bundle.putStringArray("user", user_data);
                                bundle.putString("now_date", now_date);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        } else {

                        }
                    }
                });
    }

    public void set_sql(){
        database = new MyDBHelper(Users_loginActivity.this, DATABASE_NAME, null, VERSION).getWritableDatabase();
        database.execSQL("DROP TABLE IF EXISTS " + Data_User_Detail.TABLE_NAME);
        database.execSQL(Data_User_Detail.CREATE_TABLE);
        final Data_User_Detail sql_data_user_detail = new Data_User_Detail(Users_loginActivity.this);
        final Item_user_detail[] temp = new Item_user_detail[1];
        db.collection("user").document(user_data[0]).collection("data")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    temp[0] = new Item_user_detail(0, user_data[0], document.getString("date"), document.getString("category"), document.getString("item"), document.getDouble("kcal").intValue(), document.getDouble("number").intValue());
                                    sql_data_user_detail.insert(temp[0]);
                                }
                        } else {

                        }
                    }
                });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 9001) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
               // updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                           // Log.d(TAG, "signInWithCredential:success");
                            Intent intent = new Intent();
                            //final Bundle bundle = new Bundle();
                            intent.setClass(Users_loginActivity.this, Users_detailActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                           // Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

}
