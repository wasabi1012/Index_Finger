package com.example.indexfinger;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.FileNotFoundException;

public class ShowPDF extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private static final int YOUR_REQUEST_CODE = 1;
    private DatabaseHelper _helper;

    private String PDFFilePaths = "";
    String selection = "name = ?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        _helper = new DatabaseHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showpdf);
        // パーミッションリクエストを付与する処理
        try {
            requestReadExternalStoragePermission();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void requestReadExternalStoragePermission() throws FileNotFoundException {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // パーミッションリクエストを実行
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            // 既にパーミッションが存在している場合の処理
            Log.d("Permission", "READ_EXTERNAL_STORAGE permission already granted");
            getPDFDataFromDataBase();
        }
    }

    // パーミッションリクエスト結果の処理
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            // パーミッションリクエストの結果を処理
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // パーミッションが許可された場合
                Log.d("Permission", "READ_EXTERNAL_STORAGE permission granted");
                getPDFDataFromDataBase();
            } else {
                // パーミッションが拒否された場合の処理
                Context context = getApplicationContext();
                Toast.makeText(context, "リクエストが拒否されました", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public void ActionPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, YOUR_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == YOUR_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedFileUri = data.getData();
            // ファイルのURLと一致するか確認
            if (selectedFileUri != null && selectedFileUri.toString().equals(PDFFilePaths)) {
                // ファイルのURLと一致する場合の処理
                loadPDF(selectedFileUri);
            } else {
                // 一致しない場合の処理
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("エラー");
                builder.setMessage("URLが一致していません!");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // OKボタンを押された時の処理
                        finish();
                    }
                });
                builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // キャンセルボタンが押されたときの処理
                        finish();
                    }
                });
                builder.show();
            }
        }
    }

    private void loadPDF(Uri uri) {
        PDFView pdfView = findViewById(R.id.packageInsertLink);
        pdfView.fromUri(uri).defaultPage(0).load();
    }

    public void getPDFDataFromDataBase() {
                Uri pdfData = null;
                Intent intent = getIntent();
                String[] PDFName = new String[]{intent.getStringExtra("PDF Name")};
                // 受け取った変数を使って検索をかける
                SQLiteDatabase db = _helper.getReadableDatabase();
                Cursor c = db.query("ListTable",new String[]{"PDFFilePath"},selection,PDFName,null,null,null);
                boolean isEoF = c.moveToFirst();
                PDFFilePaths = c.getString(0);
                isEoF = c.moveToNext();
                c.close();
                ActionPicker();
        }
    }
