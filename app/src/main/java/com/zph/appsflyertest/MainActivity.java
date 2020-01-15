package com.zph.appsflyertest;

import android.arch.lifecycle.ViewModelStore;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String TAG = "zph";
    TextView tv;
    Button btn_jump;
    Button btn_show;

    boolean isListViewOk;
    ZhuAdapter zhuAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_show = findViewById(R.id.button);
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tv = findViewById(R.id.textView);
                showClick();

            }
        });

        btn_jump = findViewById(R.id.button2);
        btn_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Jump.class);
                startActivity(intent);
            }
        });
        btn_jump.setVisibility(View.GONE);
    }
    void showClick() {
        showPackageNames();
    }

    void toasts(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    void showPackageNames() {
        List<String> packageNames = getPackageNames();
        Collections.sort(packageNames);

        ScrollView scrollView2 = findViewById(R.id.scrollView2);
        ListView listView = findViewById(R.id.listView1);
        List<String> nameList = new ArrayList<String>();
        for(int i = 0; i < packageNames.size(); i++)
        {
            nameList.add(packageNames.get(i));
        }

//        //可以一直添加，在真机运行后可以下拉列表
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String _name = parent.getItemAtPosition(position).toString().split(" ")[1];
//                jumpApp(_name);
//            }
//        });


        setListView(listView, nameList);

        setListViewHeightBasedOnChildren(listView);

        btn_show.setVisibility(View.GONE);
    }
    private List<String> getPackageNames() {
        List<String> myAppInfos = new ArrayList<String>();
        try {
            List<PackageInfo> packageInfos = getPackageManager().getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
                //过滤掉系统app
                if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                    continue;
                }
                Drawable drawable = packageInfo.applicationInfo.loadIcon(getPackageManager());
                if (drawable != null) {

                    List<String> names = new ArrayList<>();
                    names.add("com.yymoon.xzn2");
                    names.add("com.nsxq.huawei");
                    names.add("com.nsxqnb.mi");
                    names.add("com.yinyuewangluo.nvshenxingqiu.vivo");
                    names.add("com.yymoon.nsxq.aligames");
                    names.add("com.yymoon.xzn2.papa");
                    names.add("com.tencent.tmgp.angellegion");
                    names.add("com.yymoon.angellegion.bilibili");
                    names.add("com.angellegion.mz-meizu");
                    names.add("com.yymoon.angellegion.m4399");
                    if(names.contains(packageInfo.packageName)) {
                        myAppInfos.add(packageInfo.packageName + "\nVersion: " + packageInfo.versionName);
                    }
                }
            }
        }catch (Exception e){
            Log.e(TAG,"===============获取应用包信息失败");
        }
        return myAppInfos;
    }

    void setDrawable(Drawable drawable, ImageView imageView, String packageName) {

        try
        {
            Drawable icon = getPackageManager().getApplicationIcon(packageName);
            imageView.setImageDrawable(icon);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); //统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        //listView.getDividerHeight()获取子项间分隔符占用的高度
        //params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    void setListView(final ListView listView, final List<String> nameList) {

        //建立一个list数组对象data，data中存放的是Map对象
        List<Map<String,Object>> items=new ArrayList<Map<String,Object>>();
        //向Map对象中写入值，前面的是数据列名，后面是具体的数据
        for(int i = 0; i < nameList.size(); i++) {

            Map<String,Object> map1=new HashMap<String, Object>();
            map1.put("name", nameList.get(i));
            map1.put("icon", R.mipmap.ic_launcher);
            items.add(map1);
        }


//        //为listview装配适配器
//        ListAdapter listAdapter = new SimpleAdapter(this, items, R.layout.list_item, new String[]{"name","icon"}, new int[]{R.id.tv1 , R.id.iv});
//        listView.setAdapter(listAdapter);
//        //通过R.layout.list_item这个参数找到list_item,然后把data里面的数据显示在它上面各自对应的控件上，再一起作为一行在listview上显示出来

        if(zhuAdapter==null){
            zhuAdapter = new ZhuAdapter(this, items, this);
        }else {
            //刷新适配器,不用每次都new SongAdapter(this,songArrayList)
            zhuAdapter.notifyDataSetChanged();
        }
        listView.setAdapter(zhuAdapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG, "onItemClick: id: " + id);
                if (view instanceof ViewGroup) {
                    ViewGroup vp = (ViewGroup) view;
                    for (int i = 0; i < vp.getChildCount(); i++) {
                        View viewchild = vp.getChildAt(i);

                        Log.d(TAG, "onItemClick: view type: " +  viewchild.getClass());

                        if(viewchild instanceof ImageView) {

                        } else if(viewchild instanceof LinearLayout) {

                            ViewGroup vp2 = (ViewGroup) viewchild;
                            for(int j = 0; j < vp2.getChildCount(); j++) {

                                View viewChildChild = vp2.getChildAt(j);
                                Log.d(TAG, "onItemClick: view type: " +  viewChildChild.getClass());

                                if(viewChildChild instanceof TextView) {

                                    String _str = ((TextView) viewChildChild).getText() + "";
                                    Log.d(TAG, "onItemClick: viewChildChild: " +  _str);

                                    try{

                                        String packageName =  _str.split("\n")[0].replace(",","");
                                        jumpApp(packageName);
                                    } catch (Exception e) {
                                        Log.d(TAG, "onItemClick: " + e.toString());
                                    }
                                }
                            }

                        }

                    }
                }

            }
        });

    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return super.getViewModelStore();
    }

    void jumpApp(String packageName) {

        Log.d(TAG, "jumpApp: " + packageName);
        if(packageName.equals("")) {

            toasts("请输入包名思密达");
        } else if (!packageName.matches("^(com.[A-Za-z.0-9]+)$")) {
            toasts("请检查包名格式哟: " + packageName);
        } else {

            try{
                PackageManager packageManager = this.getPackageManager();
                Intent intent= packageManager.getLaunchIntentForPackage(packageName);
                startActivity(intent);

            } catch (Exception e) {
                toasts("跳转错误，请检查包名。 " + e.toString());
            }
        }

    }
}
