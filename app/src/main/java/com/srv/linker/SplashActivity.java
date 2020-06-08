package com.srv.linker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

//    private SignInButton signInButton;
//    private GoogleSignInClient googleSignInClient;
    private EditText email, password;
    private Button signUpBtn, logInBtn;
    private FirebaseAuth mAuth;

    //DATA INITIALISATION ...
//    private static final int SIGN_IN_REQ_CODE = 1786;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initViews();
        firebase();

        //CHECK USER ...
        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        //SignIn by GOOGLE
//        signInSetUp();

        //CLICK EVENTS ...
        signUpBtn.setOnClickListener(this);
        logInBtn.setOnClickListener(this);
//        signInButton.setOnClickListener(this);
    }

    private void firebase() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void initViews() {
//        signInButton = (SignInButton)findViewById(R.id.sign_in_btn);
        email = (EditText)findViewById(R.id.email_edit_text);
        password = (EditText)findViewById(R.id.password_edit_text);
        signUpBtn = (Button)findViewById(R.id.sign_up_btn);
        logInBtn = (Button)findViewById(R.id.login_btn);
    }

//    private void signInSetUp() {
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
//    }

//    private void signIn() {
//
//        Intent signInIntent = googleSignInClient.getSignInIntent();
//
//        startActivityForResult(signInIntent, SIGN_IN_REQ_CODE);
//    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.sign_up_btn:
                emailSignUp();
                break;

            case R.id.login_btn:
                emailLogIn();
                break;

//            case R.id.sign_in_btn:
////                signIn();
//
//                break;
        }
    }

    private boolean validateInput(){

        if(email.getText().toString().trim().equals("") || password.getText().toString().trim().equals("")){
            return false;
        }

        return true;
    }

    private void emailLogIn() {
        if(!validateInput()){
            Toast.makeText(this, "Please fill all the input fields", Toast.LENGTH_SHORT).show();
            return;
        }

        logInBtn.setText("Loading");
        mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                logInBtn.setText("Login");
                if(authResult != null && authResult.getUser() != null){

                    if(authResult.getUser().getEmail().equals(email.getText().toString().trim())){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(SplashActivity.this, "LogIn Failed, Try Again", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                    }
                }else{
                    Toast.makeText(SplashActivity.this, "LogIn Failed, Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                logInBtn.setText("Login");
                e.printStackTrace();
                Toast.makeText(SplashActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void emailSignUp() {

        if(!validateInput()){
            Toast.makeText(this, "Please fill all the input fields", Toast.LENGTH_SHORT).show();
            return;
        }

        signUpBtn.setText("Loading");
        mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                signUpBtn.setText("Signup");
                if(authResult != null && authResult.getUser() != null){

                    if(authResult.getUser().getEmail().equals(email.getText().toString().trim())){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(SplashActivity.this, "SignUp Failed, Try Again", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                    }
                }else{
                    Toast.makeText(SplashActivity.this, "SignUp Failed, Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                signUpBtn.setText("Signup");
                e.printStackTrace();
                Toast.makeText(SplashActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode){
//
//            case SIGN_IN_REQ_CODE:
//
//                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//                handleSignInResult(task);
//
//                break;
//        }
//    }

//    private void handleSignInResult(Task<GoogleSignInAccount> task) {
//
//        try{
//
//            GoogleSignInAccount account = task.getResult(ApiException.class);
//
//            FirebaseGoogleAuth(account);
//        }catch (Exception e){
//            Toast.makeText(this, "SignIn Failed", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//            FirebaseGoogleAuth(null);
//        }
//    }

//    private void FirebaseGoogleAuth(GoogleSignInAccount account) {
//
//        if(account != null){
//
//            AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//
//            mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if(task.isSuccessful() && task.getResult().getUser() != null){
//                        Toast.makeText(SplashActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }else{
//                        Toast.makeText(SplashActivity.this, "SignIn Failed", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
//    }
}
