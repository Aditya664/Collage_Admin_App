package com.example.accal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LogineActivity extends AppCompatActivity {

    private EditText useremail,userpass;
    private TextView txtshow;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private RelativeLayout loginbtn;
    private String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logine);
        sharedPreferences = this.getSharedPreferences("Login",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if(sharedPreferences.getString("isLogin","false").equals("yes")){
            openDash();
        }

        useremail = findViewById(R.id.useremail);
        userpass = findViewById(R.id.userpassword);
        txtshow =  findViewById(R.id.text_show);
        loginbtn = findViewById(R.id.Loginbtn);

        txtshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userpass.getInputType() ==  144){
                    userpass.setInputType(129);
                    txtshow.setText("Hide");
                }else {
                    userpass.setInputType(144);
                    txtshow.setText("Show");
                }

                userpass.setSelection(userpass.getText().length());
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });




    }

    private void validateData() {

        email = useremail.getText().toString();
        password = userpass.getText().toString();


        if(email.isEmpty()){
            useremail.setError("required");
            useremail.requestFocus();
        }else if(password.isEmpty()){
            userpass.setError("requireed");
            userpass.requestFocus();
        }else if(email.equals("Admin@aditya.com")&&password.equals("Aditya@123")){
            editor.putString("isLogin","yes");
            editor.commit();
            openDash();
        }else {
            Toast.makeText(this, "Please Check Email and Password again", Toast.LENGTH_SHORT).show();
        }
    }

    private void openDash() {

        startActivity(new Intent(LogineActivity.this,MainActivity.class));
        finish();
    }
}