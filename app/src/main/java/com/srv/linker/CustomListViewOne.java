package com.srv.linker;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomListViewOne extends BaseAdapter {

    private Context context;
    private List<String> list;

    public CustomListViewOne(Context context, List<String> list) {
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

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View viewRow = layoutInflater.inflate(R.layout.custom_list_view_item01, null, false);

        TextView itemName = (TextView) viewRow.findViewById(R.id.list_view_item_name_text_view);

        if(this.list.size() > 0){

            if(i == 0){
                itemName.setTextColor(Color.parseColor("#88EBFF"));
            }else if(i%2 == 0){
                itemName.setTextColor(Color.parseColor("#FFD768"));
            }else if(i%3 == 0){
                itemName.setTextColor(Color.parseColor("#88EBFF"));
            }else{
                itemName.setTextColor(Color.parseColor("#FF68D7"));
            }

            itemName.setText(this.list.get(i));
        }
        return viewRow;
    }
}
