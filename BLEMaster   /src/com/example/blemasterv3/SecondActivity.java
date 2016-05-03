package com.example.blemasterv3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends Activity {
    private TextView myTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other);
        //使用Intent对象得到FirstActivity传递来的参数
        Intent intent = getIntent(); 
        
        myTextView = (TextView) findViewById(R.id.aa);
        myTextView.setText("get");
    }
}
