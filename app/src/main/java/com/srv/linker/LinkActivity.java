package com.srv.linker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class LinkActivity extends AppCompatActivity {

    //VIEWS INITIALISATION ...
    private TabLayout tabLayout;
    private TabItem allTab, domainTab, protocolTab, groupTagTab;
    private ViewPager viewPager;
    private PageAdapter pageAdapter;
    private ImageView goBackBtn;

    //DATA INITIALISATION ...
    private int tabIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);

        initViews();

        try{
            tabIndex = getIntent().getIntExtra("tab_index", -1);
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //initialise PageAdapter obj ...
        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        //set adapter ...
        viewPager.setAdapter(pageAdapter);

        //
        setOnTabSelectedListener();

        //
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Click Events ...
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void setOnTabSelectedListener() {
        if(tabIndex != -1)
        viewPager.setCurrentItem(tabIndex);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()){

                    case 0 :
                        pageAdapter.notifyDataSetChanged();
                        break;
                    case 1 :
                        pageAdapter.notifyDataSetChanged();
                        break;
                    case 2 :
                        pageAdapter.notifyDataSetChanged();
                        break;
                    case 3 :
                        pageAdapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initViews() {
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        allTab = (TabItem)findViewById(R.id.tab_all);
        domainTab = (TabItem)findViewById(R.id.tab_domain);
        protocolTab = (TabItem)findViewById(R.id.tab_protocol);
        groupTagTab = (TabItem)findViewById(R.id.tab_group_tag);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        goBackBtn = (ImageView)findViewById(R.id.go_back_btn01);
    }
}
