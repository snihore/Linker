package com.srv.linker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {

    private int totalTabs;

    public PageAdapter(@NonNull FragmentManager fm, int totalTabs) {
        super(fm);
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0 :
                return new AllTab();
            case 1 :
                return new DomainTab();
            case 2 :
                return new GroupTagTab();
            case 3 :
                return new ProtocolTab();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
