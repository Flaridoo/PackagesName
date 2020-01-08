package com.zph.appsflyertest;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String TAG = "zph";
    TextView tv;
    String packages = "";
    Button btn_jump;
    Button btn_show;
    
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
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < packageNames.size(); i++)
        {
            packages += ("\n" + packageNames.get(i));
            list.add(i + ": " + packageNames.get(i));
        }
        ///可以一直添加，在真机运行后可以下拉列表
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String _name = parent.getItemAtPosition(position).toString().split(" ")[1];
                jumpApp(_name);
            }
        });
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
                String myAppInfo =packageInfo.packageName;
                if (packageInfo.applicationInfo.loadIcon(getPackageManager()) == null) {
                    continue;
                }
                myAppInfos.add(myAppInfo);
            }
        }catch (Exception e){
            Log.e(TAG,"===============获取应用包信息失败");
        }
        return myAppInfos;
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


    void jumpApp(String packageName) {

        Log.d(TAG, "jumpApp: " + packageName);
        if(packageName.equals("")) {

            toasts("请输入包名思密达");
        } else if (!packageName.matches("^(com.[A-Za-z.]+)$")) {
            toasts("请检查包名格式哟");
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
