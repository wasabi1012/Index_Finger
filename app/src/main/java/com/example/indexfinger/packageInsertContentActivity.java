package com.example.indexfinger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class packageInsertContentActivity extends AppCompatActivity {


    private WebView packageInsertLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_insert_content);


        GS1CodeEdit gs1CodeEdit=(GS1CodeEdit) this.getApplication();
        String gtinNum=gs1CodeEdit.getContentFirstAI();

        WebView packageInsertLink=(WebView) findViewById(R.id.packageInsertLink);
        WebSettings packageInsertLinkSetting=packageInsertLink.getSettings();
        packageInsertLinkSetting.setJavaScriptEnabled(true);
        packageInsertLinkSetting.setBuiltInZoomControls(true);
        packageInsertLink.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains("PDF")==true||url.contains("pdf")==true){
                    String redirectedUrlPdf="https://docs.google.com/gview?embedded=true&url="+url;
                    view.loadUrl(redirectedUrlPdf);
                    gs1CodeEdit.setPackageInsertUrl(redirectedUrlPdf);

                }else{
                    view.loadUrl(url);
                }
                return true;
            }
        });
        packageInsertLink.loadUrl("https://www.pmda.go.jp/PmdaSearch/bookSearch/01/"+gtinNum+"?user=1");


    }

//    public void onPackageInsertDownloadButtonClick(View view){
//        PrintManager printManager=(PrintManager)Context.
//        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
//        startActivity(intent);
//    }


}