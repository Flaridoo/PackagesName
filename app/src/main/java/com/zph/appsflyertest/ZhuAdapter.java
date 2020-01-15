package com.zph.appsflyertest;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZhuAdapter extends MyBaseAdapter<Map<String,Object>>{
    private Context context;
    private List zhuArrayList;
    private MainActivity mainActivity;

    ZhuAdapter(Context c, List<Map<String,Object>> arrayList, MainActivity ac){

        super(arrayList);

        context=c;
        zhuArrayList = arrayList;
        mainActivity = ac;
    }

    //重用了convertView，很大程度上的减少了内存的消耗。通过判断convertView是否为null，

    // 是的话就需要产生一个视图出来，然后给这个视图数据，最后将这个视图返回给底层，呈献给用户。

    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView==null){

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView =inflater.inflate(R.layout.list_item,null);

            viewHolder = new ViewHolder();
            viewHolder.singerImageView =convertView.findViewById(R.id.iv);
            viewHolder.songInfoTextView =convertView.findViewById(R.id.tv1);
            viewHolder.singerTextView =convertView.findViewById(R.id.tv2);

            convertView.setTag(viewHolder);
        }else {

            viewHolder =(ViewHolder)convertView.getTag();
        }

        String _name = (String)((HashMap)zhuArrayList.get(position)).get("name");
        viewHolder.singerImageView.setImageResource((Integer)((HashMap)zhuArrayList.get(position)).get("icon"));
        viewHolder.singerTextView.setText(_name);

        try
        {
            String packageName = _name.split("\n")[0].replace(",","");
            Drawable icon = mainActivity.getPackageManager().getApplicationIcon(packageName);
            viewHolder.singerImageView.setImageDrawable(icon);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            Log.e(mainActivity.TAG, "getView: " + e.toString());
        }

        return convertView;
    }
}



//避免了就是每次在getVIew的时候，都需要重新的findViewById，

// 重新找到控件，然后进行控件的赋值以及事件相应设置。这样其实在做重复的事情)

class ViewHolder{

    ImageView singerImageView;
    TextView songInfoTextView;
    TextView singerTextView;
}

