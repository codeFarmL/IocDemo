package com.example.liguangchun.iocdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liguangchun.iocdemo.annotation.ContentView;
import com.example.liguangchun.iocdemo.annotation.OnClick;
import com.example.liguangchun.iocdemo.annotation.OnLongClick;
import com.example.liguangchun.iocdemo.annotation.ViewInject;
import com.example.liguangchun.iocdemo.annotation.ViewInjectUtils;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.tv_text)
    private TextView mTextView;

    @OnClick({R.id.btn})
    public void onClick(View view) {
        Toast.makeText(this, "按下", Toast.LENGTH_SHORT).show();
    }

    @OnLongClick({R.id.btn_long})
    public boolean onLongClick(View view) {
        Toast.makeText(this, "长按", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewInjectUtils.inject(this);
        mTextView.setText("inject");
    }
}
