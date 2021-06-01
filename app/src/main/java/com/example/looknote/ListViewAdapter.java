package com.example.looknote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    ArrayList<ListViewitem> items = new ArrayList<ListViewitem>();
    Context context;

    public void addItem(ListViewitem item){
        items.add(item);
    }

    public void delItem(int i){
        items.remove(i);
    }

    @Override
    public int getCount(){ //ArrayList의 사이즈 리턴
        return items.size();
    }

    @Override
    public Object getItem(int position){ //해당 position에 있는 item을 return
        return items.get(position);
    }

    @Override
    public long getItemId(int position){ // position값 return
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        context = parent.getContext();
        ListViewitem ListViewitem = items.get(position);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_recommand, parent, false);
        }

        TextView textView;
        ImageView imageView;
        textView = convertView.findViewById(R.id.date_num);
        textView.setText(ListViewitem.getDate_num());
        imageView = convertView.findViewById(R.id.satisf);
        imageView.setImageResource(ListViewitem.getStaisfImage(ListViewitem.getSatisf()));
        textView = convertView.findViewById(R.id.top_c);
        textView.setText(ListViewitem.getTop_c());
        textView = convertView.findViewById(R.id.bottom_c);
        textView.setText(ListViewitem.getBottom_c());
        textView = convertView.findViewById(R.id.acc_c);
        textView.setText(ListViewitem.getAcc());
        textView = convertView.findViewById(R.id.diary_c);
        textView.setText(ListViewitem.getDiary());
        textView = convertView.findViewById(R.id.max_tem);
        textView.setText(ListViewitem.getMax_tem());
        textView = convertView.findViewById(R.id.min_tem);
        textView.setText(ListViewitem.getMin_tem());
        textView = convertView.findViewById(R.id.sky);
        textView.setText(ListViewitem.getSky());

        return convertView;
    }
}
