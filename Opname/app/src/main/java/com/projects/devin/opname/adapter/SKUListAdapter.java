package com.projects.devin.opname.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.projects.devin.opname.R;
import com.projects.devin.opname.cls.Opname;
import com.projects.devin.opname.cls.SKU;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by devin on 1/27/2017.
 */

public class SKUListAdapter extends BaseAdapter {

    private Context context = null;
    private List<SKU> lsSKU;
    private TextView itemText, isbnText, skuText, judulText, distributorText, hargaJualText;

    public SKUListAdapter(Context context, List<SKU> lsSKU){
        this.context = context;
        this.lsSKU = lsSKU;
    }

    @Override
    public int getCount() {
        return lsSKU.size();
    }

    @Override
    public Object getItem(int position) {
        return lsSKU.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_sku, parent, false);
        }

        itemText = (TextView) convertView.findViewById(R.id.item_no_text);
        isbnText = (TextView) convertView.findViewById(R.id.isbn_textview);
        skuText = (TextView) convertView.findViewById(R.id.sku_textview);
        judulText = (TextView) convertView.findViewById(R.id.judul_textview);
        distributorText = (TextView) convertView.findViewById(R.id.distributor_textview);
        hargaJualText = (TextView) convertView.findViewById(R.id.harga_jual_textview);

        itemText.setText("Item #" + (position + 1) + "");
        isbnText.setText(lsSKU.get(position).getISBN());
        skuText.setText(lsSKU.get(position).getSKU());
        judulText.setText(lsSKU.get(position).getJudul());
        distributorText.setText(lsSKU.get(position).getDistributor());
        hargaJualText.setText(lsSKU.get(position).getHargaJual().toString());

        return convertView;
    }
}
