package com.example.indexfinger;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ShowScanResultActivity extends AppCompatActivity {

    private GS1CodeEdit gs1CodeEdit;

    //レイアウトと関連付け
    Button searchButton;
    //データベースヘルパーオブジェクト
    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_scan_result);


        GS1CodeEdit gs1CodeEdit = (GS1CodeEdit) this.getApplication();
        String GS1CodeNum = gs1CodeEdit.getGS1CodeAll();
//        String metaGS1CodeNum=gs1CodeEdit.getMetaGS1Code();
        String codeFormatName = gs1CodeEdit.getCodeFormat();
        String printedCode = gs1CodeEdit.getContentFirstAI();

        TextView textGS1CodeNum = findViewById(R.id.gs1code);
//        TextView textMetaGS1CodeNum=findViewById(R.id.metaGS1Code);
        TextView textCodeFormatName = findViewById(R.id.codeFormat);
        TextView textRawToReal = findViewById(R.id.printedCode);

        textGS1CodeNum.setText(GS1CodeNum);
//        textMetaGS1CodeNum.setText(metaGS1CodeNum);
        textCodeFormatName.setText(codeFormatName);
        textRawToReal.setText(printedCode);
        //ボタンを押して検索をかける
        searchButton = findViewById(R.id.SearchButton);
        //検索ボタンが押されたときの処理
        View.OnClickListener CodeSearchEvent = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //コンソールにボタンが押されたことを表示する
                Log.d("SearchButton", "onClick: Search Button");
                //入力したコードを取得する
                EditText input = findViewById(R.id.printedCode);
                //textを取得
                String inputStr = input.getText().toString();
                //コードが事前に入力したものと同じかどうかを判定するif文
                if ("04987350364234".equals(inputStr)) {
                    //第二画面への遷移
                    Intent intent = new Intent(ShowScanResultActivity.this, SerchActivity.class);
                    startActivity(intent);
                } else if ("04987892116131".equals(inputStr)) {
                    //別画面への遷移
                    Intent intent = new Intent(ShowScanResultActivity.this, Searchactivity.class);
                    startActivity(intent);
                } else if ("04987892063398".equals(inputStr)) {
                    //別画面への遷移
                    Intent intent = new Intent(ShowScanResultActivity.this, Searchactivity2.class);
                    startActivity(intent);
                } else if ("04987669605042".equals(inputStr)) {
                    //別画面への遷移
                    Intent intent = new Intent(ShowScanResultActivity.this, Searchactivity3.class);
                    startActivity(intent);
                }
                }
        };
        searchButton.setOnClickListener(CodeSearchEvent);
    }

    public void onPackageInsertButtonClick(View view){
        Intent packageInsertIntent = new Intent(getApplicationContext(),packageInsertContentActivity.class);
        startActivity(packageInsertIntent);
        //btnPackageInsertLink.setVisibility(View.INVISIBLE);
    }

}