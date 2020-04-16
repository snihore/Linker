package com.srv.linker;

import android.content.Context;
import android.icu.util.Calendar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.srv.linker.data.Link;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CustomListViewTwo extends BaseAdapter {

    private Context context;
    private List<Link> list;

    public CustomListViewTwo(Context context, List<Link> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = LayoutInflater.from(this.context);

        View viewRow =  layoutInflater.inflate(R.layout.custom_list_view_item02, null, false);

        TextView urlName = (TextView) viewRow.findViewById(R.id.list_view_item_url_name);
        TextView urlTag = (TextView) viewRow.findViewById(R.id.list_view_item_url_tag);
        TextView urlTimestamp = (TextView)viewRow.findViewById(R.id.list_view_item_url_timestamp);

        if(this.list.size()>0){
            Link link = this.list.get(i);

            if(link != null){

                SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy H:mm");
                Date date = new Date(link.getTimestamp());

                urlName.setText(link.getUrlName());
                urlTag.setText(link.getUrlTag());

                if(date != null){

                    urlTimestamp.setText(dt.format(date));
                }
            }
        }


        return viewRow;
    }
}
