package com.srv.linker;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QrActivity extends AppCompatActivity implements View.OnClickListener {

    //VIEWS ...
    private ImageView cancelBtn, qrURLImage;
    private TextView urlTextView, tagTextView, timestampTextView, uniqueIDTextView;
    private QRGEncoder qrgEncoder;
    private Bitmap qrBitmap;

    //DATA ...
    private String userEmail = "";
    private String shareURL = "", shareTAG = "", shareTIMESTAMP = "", shareUNIQUEID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        initViews();

        if(FirebaseAuth.getInstance().getCurrentUser() != null){

            userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        }

        getDataFromIntent();

        //click events ...
        cancelBtn.setOnClickListener(this);
    }

    private void getDataFromIntent() {

        try{
            ArrayList<String> list = getIntent().getStringArrayListExtra("share_link");

            if(list != null && list.size() == 4){
                shareURL = list.get(0);
                shareTAG = list.get(1);
                shareTIMESTAMP = list.get(2);
                shareUNIQUEID = list.get(3);

                setTextViews();

            }

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setTextViews() {

        if(shareURL.matches("") || shareUNIQUEID.matches("") || shareTIMESTAMP.matches("")){
            return;
        }else{
            urlTextView.setText(shareURL);
            tagTextView.setText(shareTAG);
            timestampTextView.setText(shareTIMESTAMP);
            uniqueIDTextView.setText(shareUNIQUEID);

            //GENERATE QR Code ...
            //Assume that APP's current user is always there, NOT matches == ""

            String qrData = shareURL+"$SRV$"+shareTAG+"$SRV$"+shareUNIQUEID+"$SRV$"+userEmail;

            generateQR(qrData);
        }
    }

    private void generateQR(String text){
        try{

            WindowManager manager = (WindowManager)getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerDimension = width<height ? width:height;
            smallerDimension = smallerDimension * 3/4;
            qrgEncoder = new QRGEncoder(
                    text,
                    null,
                    QRGContents.Type.TEXT,
                    smallerDimension
            );

            //
            qrBitmap = qrgEncoder.encodeAsBitmap();
            qrURLImage.setImageBitmap(qrBitmap);

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private void initViews() {

        cancelBtn = (ImageView)findViewById(R.id.qr_cancel_btn);
        urlTextView = (TextView)findViewById(R.id.qr_url_name);
        tagTextView = (TextView)findViewById(R.id.qr_url_tag);
        timestampTextView = (TextView)findViewById(R.id.qr_url_timestamp);
        uniqueIDTextView = (TextView)findViewById(R.id.qr_url_unique_id);
        qrURLImage = (ImageView) findViewById(R.id.qr_url_image);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.qr_cancel_btn :

                onBackPressed();

                break;


        }
    }
}
