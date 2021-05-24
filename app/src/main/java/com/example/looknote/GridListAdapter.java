package com.example.looknote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GridListAdapter extends BaseAdapter {
    ArrayList<Griditem> items = new ArrayList<Griditem>();
    Context context;

    public void addItem(Griditem item){
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
        Griditem griditem = items.get(position);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.griditem, parent, false);
        }

        TextView nameText = convertView.findViewById(R.id.nameText);
        TextView numberText = convertView.findViewById(R.id.degreeText);
        ImageView feelImg = convertView.findViewById(R.id.feelImg);

        nameText.setText(griditem.getName());
        numberText.setText(griditem.getNumber());
        feelImg.setImageResource(griditem.getImgno());

        return convertView;
    }
}
