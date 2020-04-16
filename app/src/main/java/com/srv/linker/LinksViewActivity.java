package com.srv.linker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.srv.linker.firestore.GetDataFromFireStore;

public class LinksViewActivity extends AppCompatActivity implements View.OnClickListener {

    //Views ...
    private TextView linkerNameTextView, finishTag;
    private ImageView goBack;
    private ListView listView;
    private ProgressBar progressBar;
    private BottomSheetDialog bottomSheetDialog;

    private GetDataFromFireStore getDataFromFireStore = new GetDataFromFireStore();

    //Data variables ..
    private String shareURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links_view);

        initViews();

        try{

            String domainName = getIntent().getStringExtra("domain_name");
            String protocolName = getIntent().getStringExtra("protocol_name");

            if(domainName != null){
                //Set ActionBar Title ...
                linkerNameTextView.setText(domainName);

                //Get Data From FireStore ...
                getDataFromFireStore.getDomainLinks(domainName, this, listView, new ProgressAndResult(this, finishTag, progressBar));
            }

            if(protocolName != null){
                //Set ActionBar Title ...
                linkerNameTextView.setText(protocolName);

                //Get Data From FireStore ...
                getDataFromFireStore.getProtocolLinks(protocolName, this, listView, new ProgressAndResult(this, finishTag, progressBar));
            }

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //Click Events ...
        goBack.setOnClickListener(this);

        //Item Click Event ...

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView url = (TextView)view.findViewById(R.id.list_view_item_url_name);
                if(url != null){
                    //when Item clicks ...
                    linkBottomSheet(url.getText().toString());
                }
            }
        });
    }

    private void initViews() {
        linkerNameTextView = (TextView)findViewById(R.id.linker_name_text_view);
        goBack = (ImageView) findViewById(R.id.go_back_btn02);
        listView = (ListView)findViewById(R.id.links_list_view);
        finishTag = (TextView)findViewById(R.id.links_view_progress_finish_tag);
        progressBar = (ProgressBar)findViewById(R.id.links_view_progress_bar);
    }

    private void linkBottomSheet(String url) {
        try{

            bottomSheetDialog = new BottomSheetDialog(LinksViewActivity.this, R.style.BottomSheet);

            View bottomSheetView = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.link_bottom_sheet, (LinearLayout)findViewById(R.id.link_bottom_sheet_layout));

            bottomSheetView.findViewById(R.id.link_bottom_sheet_open).setOnClickListener(this);
            bottomSheetView.findViewById(R.id.link_bottom_sheet_delete).setOnClickListener(this);
            bottomSheetView.findViewById(R.id.link_bottom_sheet_share).setOnClickListener(this);
            bottomSheetView.findViewById(R.id.link_bottom_sheet_modify).setOnClickListener(this);

            TextView urlTextView = (TextView) bottomSheetView.findViewById(R.id.link_bottom_sheet_url);
            urlTextView.setText(url);

            //Set Share URL
            shareURL = url;

            bottomSheetDialog.setContentView(bottomSheetView);

            bottomSheetDialog.show();

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.go_back_btn02:
                onBackPressed();
                break;

            case R.id.link_bottom_sheet_share:
                linkShareOps();
                break;
                
            case R.id.link_bottom_sheet_open:
                openBrowserAndAccessURL();
                break;
        }
    }

    private void openBrowserAndAccessURL() {
        try{
            if(!shareURL.matches("")){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(shareURL));
                bottomSheetDialog.dismiss();
                startActivity(intent);
            }else{
                Toast.makeText(this, "retry", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void linkShareOps() {

        try{
            if(!shareURL.matches("")){
                Intent intent2 = new Intent(); intent2.setAction(Intent.ACTION_SEND);
                intent2.setType("text/plain");
                intent2.putExtra(Intent.EXTRA_TEXT, shareURL);
                bottomSheetDialog.dismiss();
                startActivity(Intent.createChooser(intent2, "Share URL via"));
            }else {
                Toast.makeText(this, "retry", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
