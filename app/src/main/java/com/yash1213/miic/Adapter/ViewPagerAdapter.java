package com.yash1213.miic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;
import com.yash1213.miic.Activity.MainActivity;
import com.yash1213.miic.R;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {
    int[] img;
    LayoutInflater inflater;
    Context context;
    int position = 3;
    ArrayList<String> imag;

    public ViewPagerAdapter(MainActivity mainActivity, ArrayList<String> imag) {
        this.context = mainActivity;
        this.imag = imag;
    }

    @Override
    public int getCount() {
        return imag.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemview = inflater.inflate(R.layout.item, container, false);
        imageView = (ImageView) itemview.findViewById(R.id.ima1);
        //imageView.setImageResource(img[position]);
        Picasso.get().load(imag.get(position)).placeholder(R.drawable.opening).into(imageView);
        //add item.xml to viewpager
        ((ViewPager) container).addView(itemview);
        return itemview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);
    }

    @Override
    public float getPageWidth(int position) {
        return 1.0f;   //it is used for set page widht of view pager
    }
}