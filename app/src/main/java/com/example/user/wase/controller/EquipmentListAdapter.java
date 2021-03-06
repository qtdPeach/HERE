package com.example.user.wase.controller;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.wase.R;
import com.example.user.wase.model.Equipment;

import org.w3c.dom.Text;

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
            res = R.layout.listitem_equipment;
            convertView = mInflater.inflate(res, parent, false);

        }

        ImageView eqTypeImage = (ImageView)convertView.findViewById(R.id.equiplist_img);
        TextView eqName = (TextView)convertView.findViewById(R.id.equiplist_name);
        TextView eqId = (TextView)convertView.findViewById(R.id.equiplist_id);
        TextView eqSensorId = (TextView)convertView.findViewById(R.id.equiplist_sensorid);

        switch (arrEquipments.get(position).getEquipmentType()) {
            case 0:
                eqTypeImage.setImageResource(R.mipmap.ic_setting_update_alarm);
                break;
            case 1:
                eqTypeImage.setImageResource(R.mipmap.ic_setting_best_interest);
                break;
            case 2:
                eqTypeImage.setImageResource(R.mipmap.ic_setting_user_information);
                break;
            case 3:
                break;
            default:
                break;
        }

        eqName.setText(arrEquipments.get(position).getEquipmentName());
        eqId.setText(arrEquipments.get(position).getEquipmentID());
        eqSensorId.setText(arrEquipments.get(position).getEquipmentSensorID());

        return convertView;
    }
}
