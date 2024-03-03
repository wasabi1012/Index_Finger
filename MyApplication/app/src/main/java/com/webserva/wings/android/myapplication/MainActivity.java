package com.webserva.wings.android.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity{

    //レイアウトと関連付け
    Button searchButton;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ボタンを押して検索をかける
        searchButton = findViewById(R.id.SearchButton);
        //検索ボタンが押されたときの処理
        View.OnClickListener CodeSearchEvent = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //コンソールにボタンが押されたことを表示する
                Log.d("SearchButton","onClick: Search Button");
                //入力したコードを取得する
                EditText input = findViewById(R.id.etInput);
                //textを取得
                String inputStr = input.getText().toString();
                //コードが事前に入力したものと同じかどうかを判定するif文
                if ("04987350364234".equals(inputStr)) {
                    //第二画面への遷移
                    Intent intent = new Intent(MainActivity.this, SerchActivity.class);
                    startActivity(intent);
                } else if ("04987892116131".equals(inputStr)) {
                    //別画面への遷移
                    Intent intent = new Intent(MainActivity.this,Searchactivity.class);
                    startActivity(intent);
                } else if ("04987892063398".equals(inputStr)) {
                    //別画面への遷移
                    Intent intent = new Intent(MainActivity.this,Searchactivity2.class);
                    startActivity(intent);
                }
            }
        };
        searchButton.setOnClickListener(CodeSearchEvent);
    }
    }