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
        //ʹ��Intent����õ�FirstActivity�������Ĳ���
        Intent intent = getIntent(); 
        
        myTextView = (TextView) findViewById(R.id.aa);
        myTextView.setText("get");
    }
}
