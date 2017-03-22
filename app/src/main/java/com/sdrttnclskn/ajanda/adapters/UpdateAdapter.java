package com.sdrttnclskn.ajanda.adapters;

import android.database.Cursor;

import com.sdrttnclskn.ajanda.model.Task;

import java.util.ArrayList;

public interface UpdateAdapter {
    public void updateTaskArrayAdapter(ArrayList<Task> tasks);
    public void updateTaskArrayAdapter(Cursor cursor);
    public void update();
}
