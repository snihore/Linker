package com.srv.linker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.srv.linker.firestore.StoreURL;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    //VIEW VERIABLES ...
    private ImageView menuBtn, menu;
    private BottomSheetDialog bottomSheetDialog;
    private Button linkSaveBtn;
    private EditText linkEditText, tagEditText;
    private ProgressBar progressBar;
    private TextView userAccEmail;
    private ImageView pasteBtn;

    //DATA VERIABLES ...
    private GoogleSignInClient googleSignInClient;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        firebase();

        //SignIn by GOOGLE
        signInSetUp();

        if(mAuth.getCurrentUser() != null){
            String email = mAuth.getCurrentUser().getEmail();

            if(email != null){
                userAccEmail.setText(email);
            }
        }

        //CLICK EVENTS ...
        menuBtn.setOnClickListener(this);
        menu.setOnClickListener(this);
        linkSaveBtn.setOnClickListener(this);
        pasteBtn.setOnClickListener(this);


    }


    private void firebase() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void signInSetUp() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
    }


    private void initViews() {
        menuBtn = (ImageView)findViewById(R.id.ham_menu_btn);
        menu = (ImageView)findViewById(R.id.menu_btn);
        linkEditText = (EditText)findViewById(R.id.link_edit_text);
        tagEditText = (EditText)findViewById(R.id.tag_edit_text);
        linkSaveBtn = (Button)findViewById(R.id.link_save_btn);
        progressBar = (ProgressBar)findViewById(R.id.main_progress_bar);
        userAccEmail = (TextView)findViewById(R.id.user_acc_email);
        pasteBtn = (ImageView)findViewById(R.id.paste_btn);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.ham_menu_btn:
                bottomSheet();
                break;

            case R.id.menu_btn:
                openMenu();
                break;

            case R.id.bottom_sheet_all:
                Toast.makeText(this, "ALL", Toast.LENGTH_SHORT).show();

                if(bottomSheetDialog != null){
                    Intent intent = new Intent(getApplicationContext(), LinkActivity.class);
                    intent.putExtra("tab_index", 0);
                    bottomSheetDialog.dismiss();
                    startActivity(intent);
                }

                break;

            case R.id.bottom_sheet_domain:
                Toast.makeText(this, "DOMAIN", Toast.LENGTH_SHORT).show();

                if(bottomSheetDialog != null){
                    Intent intent = new Intent(getApplicationContext(), LinkActivity.class);
                    intent.putExtra("tab_index", 1);
                    bottomSheetDialog.dismiss();
                    startActivity(intent);
                }

                break;

            case R.id.bottom_sheet_group_tag:
                Toast.makeText(this, "GROUP TAG", Toast.LENGTH_SHORT).show();

                if(bottomSheetDialog != null){
                    Intent intent = new Intent(getApplicationContext(), LinkActivity.class);
                    intent.putExtra("tab_index", 2);
                    bottomSheetDialog.dismiss();
                    startActivity(intent);
                }

                break;

            case R.id.bottom_sheet_protocol:
                Toast.makeText(this, "PROTOCOL", Toast.LENGTH_SHORT).show();

                if(bottomSheetDialog != null){
                    Intent intent = new Intent(getApplicationContext(), LinkActivity.class);
                    intent.putExtra("tab_index", 3);
                    bottomSheetDialog.dismiss();
                    startActivity(intent);
                }

                break;

            case R.id.link_save_btn:
                saveLinkToFireStore();
                break;

            case R.id.paste_btn:
                pasteLinkToEditText();
                break;

        }

    }

    private void pasteLinkToEditText() {

        ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        String pasteData = "";

        if(clipboardManager != null){

            ClipData.Item item = clipboardManager.getPrimaryClip().getItemAt(0);

            if(item != null && item.getText() != null){

                pasteData = (String)item.getText();

                if(pasteData != null && !pasteData.matches("")){
                    linkEditText.setText(pasteData);
                    Toast.makeText(this, "paste it", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void saveLinkToFireStore() {
        String urlName = linkEditText.getText().toString().trim();
        String urlTag = tagEditText.getText().toString().trim();

        if(urlName.matches("")){
            Toast.makeText(this, "Enter URL ...", Toast.LENGTH_SHORT).show();
            return;
        }

        StoreURL storeURL = null;

        try{
            if(urlTag.matches("")){
                storeURL = new StoreURL(new ProgressAndResult(this, linkSaveBtn, progressBar), urlName);
            }else{
                storeURL = new StoreURL(new ProgressAndResult(this, linkSaveBtn, progressBar), urlName, urlTag);
            }

            if(storeURL != null){

                storeURL.init();
                linkEditText.setText("");
                tagEditText.setText("");
            }else{
                Toast.makeText(this, "Link not saved", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void openMenu() {

        try{
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.menu_dialog);

            //VIEWS ...
            LinearLayout logOutBtn = (LinearLayout)dialog.findViewById(R.id.logout_btn);

            //CLICK EVENTS ...
            logOutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    if(mAuth.getCurrentUser() != null){
                        mAuth.signOut();
                    }
                    googleSignInClient.signOut();
                    Toast.makeText(MainActivity.this, "LOGED OUT", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                    startActivity(intent);
                    finish();

                }
            });

            dialog.show();

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void bottomSheet() {
        try{

            bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheet);

            View bottomSheetView = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.bottom_sheet, (LinearLayout)findViewById(R.id.bottom_sheet_layout));

            bottomSheetView.findViewById(R.id.bottom_sheet_all).setOnClickListener(this);
            bottomSheetView.findViewById(R.id.bottom_sheet_domain).setOnClickListener(this);
            bottomSheetView.findViewById(R.id.bottom_sheet_group_tag).setOnClickListener(this);
            bottomSheetView.findViewById(R.id.bottom_sheet_protocol).setOnClickListener(this);

            bottomSheetDialog.setContentView(bottomSheetView);

            bottomSheetDialog.show();

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}
