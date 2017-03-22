package com.sdrttnclskn.ajanda;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class NewTaskActivityFragment extends Fragment {

    public NewTaskActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_task, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
