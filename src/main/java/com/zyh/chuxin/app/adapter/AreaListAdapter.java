package com.zyh.chuxin.app.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

/**
 * Created by zhyh on 2015/1/24.
 */
public class AreaListAdapter extends SimpleCursorAdapter {

    private Cursor cursor;

    public AreaListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int
            flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
