package com.projects.devin.opname.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.projects.devin.opname.R;
import com.projects.devin.opname.cls.Opname;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by devin on 1/23/2017.
 */

public class OpnameListAdapter extends BaseAdapter {

    private Context context = null;
    private List<Opname> lsOpname;
    private TextView itemnoText, relasiText, rakText, skuText, isbnText, judulText, distributorText, hargaText, qtyText, datetimeText, loginNameText;

    public OpnameListAdapter(Context context, List<Opname> lsOpname){
        this.context = context;
        this.lsOpname = lsOpname;
    }

    @Override
    public int getCount() {
        return lsOpname.size();
    }

    @Override
    public Object getItem(int position) {
        return lsOpname.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_opname, parent, false);
        }

        itemnoText = (TextView) convertView.findViewById(R.id.item_no_text);
        relasiText = (TextView) convertView.findViewById(R.id.relasi_textview);
        rakText = (TextView) convertView.findViewById(R.id.rak_textview);
        skuText = (TextView) convertView.findViewById(R.id.sku_textview);
        isbnText = (TextView) convertView.findViewById(R.id.isbn_textview);
        judulText = (TextView) convertView.findViewById(R.id.judul_textview);
        distributorText = (TextView) convertView.findViewById(R.id.distributor_textview);
        hargaText = (TextView) convertView.findViewById(R.id.harga_textview);
        qtyText = (TextView) convertView.findViewById(R.id.qty_textview);
        datetimeText = (TextView) convertView.findViewById(R.id.date_time_textview);
        loginNameText = (TextView) convertView.findViewById(R.id.login_name_textview);

        itemnoText.setText("Item #" + (position + 1) + "");
        relasiText.setText(lsOpname.get(position).getRelasi());
        rakText.setText(lsOpname.get(position).getKodeRak());
        skuText.setText(lsOpname.get(position).getSKU());
        isbnText.setText(lsOpname.get(position).getISBN());
        judulText.setText(lsOpname.get(position).getJudul());
        distributorText.setText(lsOpname.get(position).getDistributor());
        hargaText.setText(lsOpname.get(position).getHarga().toString());
        qtyText.setText(lsOpname.get(position).getQty().toString());
        datetimeText.setText(lsOpname.get(position).getDateTime());
        loginNameText.setText(lsOpname.get(position).getLoginName());

        return convertView;
    }
}
