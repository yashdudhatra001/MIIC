package com.yash1213.miic.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yash1213.miic.R;

public class LibraryAdapter extends BaseAdapter {

    private Context mContext;
    private final String[] string;
    private final int[] Imageid;
    private final String[] links;

    public LibraryAdapter(Context c,String[] string,int[] Imageid,String[] links ) {
        mContext = c;
        this.Imageid = Imageid;
        this.string = string;
        this.links = links;
    }

    @Override
    public int getCount() {
        return string.length;
    }

    @Override
    public Object getItem(int p) {
        return null;
    }

    @Override
    public long getItemId(int p) {
        return 0;
    }

    @Override
    public View getView(final int p, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.library_item, null);
            TextView textView = (TextView) grid.findViewById(R.id.album_title_main);
            ImageView imageView = (ImageView)grid.findViewById(R.id.no_image_placeholder);
            RelativeLayout rv = (RelativeLayout)grid.findViewById(R.id.library_layout);
            textView.setText(string[p]);
            imageView.setImageResource(Imageid[p]);
            rv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, ""+links[p], Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            grid = (View) convertView;
        }
        return grid;
    }
}