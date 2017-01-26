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
import com.projects.devin.opname.apps.ChangePasswordActivity;
import com.projects.devin.opname.apps.MainMenuActivity;
import com.projects.devin.opname.apps.PaperConfigActivity;
import com.projects.devin.opname.apps.PrinterConfigActivity;
import com.projects.devin.opname.cls.MenuList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devin on 1/21/2017.
 */

public class ConfigurationMenuFragment extends Fragment {
    private View view;
    private GridView menuGrid;
    private List<MenuList> lsMenu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_configuration_menu, container, false);

        ((MainMenuActivity)getActivity()).getSupportActionBar().setTitle("Configuration Menu");
        initComponent();

        return view;
    }

    private void initComponent(){
        menuGrid = (GridView) view.findViewById(R.id.configuration_menu_grid);
        lsMenu = new ArrayList<>();
        lsMenu.add(new MenuList(BitmapFactory.decodeResource(getResources(), R.drawable.ic_change_password), "Change Password"));
        lsMenu.add(new MenuList(BitmapFactory.decodeResource(getResources(), R.drawable.ic_printer), "Printer"));
        lsMenu.add(new MenuList(BitmapFactory.decodeResource(getResources(), R.drawable.ic_paper), "Paper"));

        menuGrid.setAdapter(new MenuGridAdapter(getActivity(), lsMenu));
        menuGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(lsMenu.get(position).getTitle().equalsIgnoreCase("Change Password")){
                    Intent i = new Intent(getActivity(), ChangePasswordActivity.class);
                    startActivity(i);
                }
                else if(lsMenu.get(position).getTitle().equalsIgnoreCase("Printer")){
                    Intent i = new Intent(getActivity(), PrinterConfigActivity.class);
                    startActivity(i);
                }
                else if(lsMenu.get(position).getTitle().equalsIgnoreCase("Paper")){
                    Intent i = new Intent(getActivity(), PaperConfigActivity.class);
                    startActivity(i);
                }
            }
        });
    }
}
