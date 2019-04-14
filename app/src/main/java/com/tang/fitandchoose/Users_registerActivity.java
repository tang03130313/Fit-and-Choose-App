package com.tang.fitandchoose;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Users_registerActivity extends Activity {
    LinearLayout LinearLayout_button_send;
    EditText mEmailField,mPasswordField;
    FirebaseAuth mAuth;
    boolean result;
    String[] user_data = new String[7];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_account);

        mEmailField = (EditText) findViewById(R.id.fieldEmail);
        mPasswordField =(EditText)  findViewById(R.id.fieldPassword);
        LinearLayout_button_send =(LinearLayout)  findViewById(R.id.button_send);
        mAuth = FirebaseAuth.getInstance();

        set();
    }


    @Override
    public void onStart() {
        super.onStart();
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
        initial();
        LinearLayout_button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
            }
        });
    }

    private void createAccount(String email, String password) {
        if (!validateForm()) {
            return;
        }

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            ProgressDialog progressDialog=new ProgressDialog(Users_registerActivity.this);
                            progressDialog.setMessage("處理中,請稍候...");
                            progressDialog.show();
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            user_data[0] = user.getEmail();
                            user_data[1] = "0"; user_data[2] = "男"; user_data[3] = "0"; user_data[4] = "0"; user_data[5] = "0"; user_data[6] = "0";
                            Intent intent = new Intent();
                            final Bundle bundle = new Bundle();
                            intent.setClass(Users_registerActivity.this, Users_detailActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            bundle.putStringArray("user", user_data);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Users_registerActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        // [END create_user_with_email]
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
