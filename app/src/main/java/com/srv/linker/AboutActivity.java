package com.srv.linker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //Click events ...
        findViewById(R.id.go_back_btn03).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final TextView email = (TextView)findViewById(R.id.dev_email_copy);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyContact(email.getText().toString());
            }
        });

        final TextView linkedin = (TextView)findViewById(R.id.dev_linkedin_copy);
        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyContact("https://linkedin.com"+linkedin.getText().toString());
            }
        });

        final TextView insta = (TextView)findViewById(R.id.dev_insta_copy);
        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyContact("https://instagram.com/"+insta.getText().toString());
            }
        });
    }

    private void copyContact(String contact) {

        try{

            ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);

            if(clipboardManager != null){

                ClipData clipData = ClipData.newPlainText("result_msg_area", contact);

                if(clipData != null){

                    clipboardManager.setPrimaryClip(clipData);

                    Toast.makeText(this, contact, Toast.LENGTH_SHORT).show();
                }

            }

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
