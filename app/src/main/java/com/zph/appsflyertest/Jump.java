package com.zph.appsflyertest;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Jump extends Activity {

    String packageName = "";
    EditText tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump);

        tv = findViewById(R.id.textView3);
        Button btn = findViewById(R.id.button3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpApp();
            }
        });

        Button btn_check = findViewById(R.id.button4);
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                packageName = tv.getText().toString();
                toasts("packageName: " + packageName);
                if (packageName.equals("")) {

                    toasts("请输入包名思密达");
                } else if (!packageName.matches("^(com.[A-Za-z.]+)$")) {
                    toasts("请检查包名格式哟");
                } else {
                    toasts(packageName);
                }
            }
        });
    }

    void jumpApp() {

         packageName = tv.getText().toString();
         toasts("packageName: " + packageName);
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

    void toasts(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
