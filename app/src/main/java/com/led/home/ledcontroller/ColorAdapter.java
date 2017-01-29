package com.led.home.ledcontroller;

import android.content.Context;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.led.home.db.Color;
import com.led.home.db.ColorReaderDbHelper;
import com.led.home.exceptions.DBException;

import java.util.ArrayList;
import java.util.List;

public class ColorAdapter extends BaseAdapter {
    private Context mContext;
    private List<Color> colors;

    public ColorAdapter(Context c) {
        mContext = c;
    }

    public void init() {
        colors = new ArrayList<Color>();
        colors.add(new Color(android.graphics.Color.parseColor("#F74040"), "00bdf5", true)); // red
        colors.add(new Color(android.graphics.Color.parseColor("#91F29D"), "5ac7bf", true)); // green
        colors.add(new Color(android.graphics.Color.parseColor("#91ECF2"), "81c7bf", true)); // blue
        colors.add(new Color(android.graphics.Color.parseColor("#F291EA"), "d7c7bf", true)); // pink
        colors.add(new Color(android.graphics.Color.parseColor("#C791F2"), "c166f2", true)); // purple
        colors.add(new Color(android.graphics.Color.parseColor("#F6FAB4"), "2cddd6", true)); // yellow
        try {
            List<Color> customColors = ColorReaderDbHelper.getInstance(mContext).getAllColors();
            colors.addAll(customColors);
        } catch (DBException e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
    }

    public int getCount() {
        return colors.size();
    }

    public Object getItem(int position) {
        return colors.get(position);
    }

    public long getItemId(int position) {
        return colors.get(position).getId();
    }

    // create a new Button for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        Color color = colors.get(position);
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setBackgroundColor(color.getColor());
            GridView.LayoutParams params = new GridView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            GridView mGridView = (GridView)parent;
            params.height = mGridView.getColumnWidth();
            imageView.setLayoutParams(params);

        } else {
            imageView = (ImageView)convertView;
        }
        return imageView;
    }

    public List<Color> getColors() {
        return colors;
    }
}
