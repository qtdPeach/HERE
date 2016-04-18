package com.example.user.wase.controller;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.user.wase.model.Equipment;

import java.util.ArrayList;

/**
 * Created by user on 2016-04-18.
 */
public class EquipmentListAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    ArrayList<Equipment> arrEquipments;

    public EquipmentListAdapter(Context context, ArrayList<Equipment> arrEquipments) {
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrEquipments = arrEquipments;
    }

    @Override
    public int getCount() {
        return arrEquipments.size();
    }

    @Override
    public Object getItem(int position) {
        return arrEquipments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            int res = 0;
            switch (arrEquipments.get(position).getEquipmentType()) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                default:
                    break;
            }

            convertView = mInflater.inflate(res, parent, false);
        }

        //TODO: SWITCH-CASE for layout setting
        
        return convertView;
    }
}
