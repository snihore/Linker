package com.srv.linker;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.srv.linker.firestore.GetDataFromFireStore;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllTab extends Fragment implements AdapterView.OnClickListener {

    //Views
    private TextView finishTag;
    private ListView listView;
    private ProgressBar progressBar;
    private BottomSheetDialog bottomSheetDialog;

    private GetDataFromFireStore getDataFromFireStore = new GetDataFromFireStore();

    //Data variables ..
    private String shareURL = "";


    public AllTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_all_tab, container, false);

        initViews(view);

        try {
            getDataFromFireStore.getAllLinks(getContext(), listView, new ProgressAndResult(getContext(), finishTag, progressBar));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //list view item click event ...
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView url = (TextView)view.findViewById(R.id.list_view_item_url_name);
                if(url != null){
                    //when Item clicks ...
                    linkBottomSheet(url.getText().toString(), view);
                }

            }
        });

        return view;
    }

    private void initViews(View view) {
        listView = (ListView)view.findViewById(R.id.all_tab_list_view);
        finishTag = (TextView)view.findViewById(R.id.all_tab_progress_finish_tag);
        progressBar = (ProgressBar)view.findViewById(R.id.all_tab_progress_bar);
    }

    private void linkBottomSheet(String url, View view) {
        try{

            bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheet);

            View bottomSheetView = LayoutInflater.from(getContext())
                    .inflate(R.layout.link_bottom_sheet, (LinearLayout)view.findViewById(R.id.link_bottom_sheet_layout));

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
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

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
                Toast.makeText(getContext(), "retry", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "retry", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
