package com.srv.linker;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.srv.linker.firestore.GetDataFromFireStore;


/**
 * A simple {@link Fragment} subclass.
 */
public class DomainTab extends Fragment {

    //VIEWS INITIALISATION ...
    private ListView listView;
    private TextView finishTag;
    private ProgressBar progressBar;

    private GetDataFromFireStore getDataFromFireStore = new GetDataFromFireStore();


    public DomainTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_domain_tab, container, false);

        initViews(view);

        try{
            getDataFromFireStore.getDomains(getContext(), listView, new ProgressAndResult(getContext(), finishTag, progressBar));

            //ListView ITEM click event ...
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    TextView textView = view.findViewById(R.id.list_view_item_name_text_view);

                    Intent intent = new Intent(getContext(), LinksViewActivity.class);
                    intent.putExtra("domain_name", textView.getText().toString());
                    startActivity(intent);

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }


        return view;
    }



    private void initViews(View view){

        listView = (ListView)view.findViewById(R.id.domain_names_list_view);
        progressBar = (ProgressBar)view.findViewById(R.id.domain_progress_bar);
        finishTag = (TextView)view.findViewById(R.id.domain_progress_finish_tag);
    }
}
