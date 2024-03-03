package com.example.indexfinger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class ShowScanResultActivity extends AppCompatActivity {

    private GS1CodeEdit gs1CodeEdit;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_scan_result);


        GS1CodeEdit gs1CodeEdit=(GS1CodeEdit) this.getApplication();
        String GS1CodeNum=gs1CodeEdit.getGS1CodeAll();
//        String metaGS1CodeNum=gs1CodeEdit.getMetaGS1Code();
        String codeFormatName=gs1CodeEdit.getCodeFormat();
        String printedCode=gs1CodeEdit.getContentFirstAI();

        TextView textGS1CodeNum=findViewById(R.id.gs1code);
//        TextView textMetaGS1CodeNum=findViewById(R.id.metaGS1Code);
        TextView textCodeFormatName=findViewById(R.id.codeFormat);
        TextView textRawToReal=findViewById(R.id.printedCode);

        textGS1CodeNum.setText(GS1CodeNum);
//        textMetaGS1CodeNum.setText(metaGS1CodeNum);
        textCodeFormatName.setText(codeFormatName);
        textRawToReal.setText(printedCode);

    }

    public void onPackageInsertButtonClick(View view){
        Intent packageInsertIntent=new Intent(getApplicationContext(),packageInsertContentActivity.class);
        startActivity(packageInsertIntent);

        //btnPackageInsertLink.setVisibility(View.INVISIBLE);
    }

}