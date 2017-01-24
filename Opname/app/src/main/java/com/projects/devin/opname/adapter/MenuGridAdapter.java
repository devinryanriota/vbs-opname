package com.projects.devin.opname.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.projects.devin.opname.R;
import com.projects.devin.opname.cls.MenuList;

import java.util.List;

/**
 * Created by devin on 1/21/2017.
 */

public class MenuGridAdapter extends BaseAdapter {

    private Context context = null;
    private List<MenuList> lsMenu;

    public MenuGridAdapter(Context context, List<MenuList> menuList){
        this.context = context;
        this.lsMenu = menuList;
    }

    @Override
    public int getCount() {
        return lsMenu.size();
    }

    @Override
    public Object getItem(int position) {
        return lsMenu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.grid_menu, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.item_image);
        imageView.setImageBitmap(lsMenu.get(position).getImage());

        TextView textView = (TextView) convertView.findViewById(R.id.item_text);
        textView.setText(lsMenu.get(position).getTitle());

        return convertView;
    }
}
