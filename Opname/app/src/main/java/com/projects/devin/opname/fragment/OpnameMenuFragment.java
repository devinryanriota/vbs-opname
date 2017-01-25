package com.projects.devin.opname.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.projects.devin.opname.R;
import com.projects.devin.opname.adapter.MenuGridAdapter;
import com.projects.devin.opname.apps.MainMenuActivity;
import com.projects.devin.opname.apps.OpnameRelasiActivity;
import com.projects.devin.opname.apps.OpnameReportActivity;
import com.projects.devin.opname.cls.MenuList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devin on 1/21/2017.
 */

public class OpnameMenuFragment extends Fragment {

    private View view;
    private GridView menuGrid;
    private List<MenuList> lsMenu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_opname_menu, container, false);

        ((MainMenuActivity)getActivity()).getSupportActionBar().setTitle("Opname Menu");
        initComponent();

        return view;
    }

    private void initComponent(){
        menuGrid = (GridView) view.findViewById(R.id.opname_menu_grid);
        lsMenu = new ArrayList<>();
        lsMenu.add(new MenuList(BitmapFactory.decodeResource(getResources(), R.drawable.ic_employee_attendance), "Opname"));
        lsMenu.add(new MenuList(BitmapFactory.decodeResource(getResources(), R.drawable.ic_employee_attendance), "Opname Report"));

        menuGrid.setAdapter(new MenuGridAdapter(getActivity(), lsMenu));
        menuGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(lsMenu.get(position).getTitle().equalsIgnoreCase("Opname")){
                    Intent i = new Intent(getActivity(), OpnameRelasiActivity.class);
                    startActivity(i);
                }
                else if(lsMenu.get(position).getTitle().equalsIgnoreCase("Opname Report")){
                    //opname report
                    Intent i = new Intent(getActivity(), OpnameReportActivity.class);
                    startActivity(i);
                }
            }
        });
    }
}
