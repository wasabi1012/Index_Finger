package com.example.indexfinger;


import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class GetInformationFromTable extends AppCompatActivity implements View.OnClickListener{
    //このアクティビティではテーブルに情報を追加する処理を行う
    //今回はお試しなので、登録するのは機器名称、GTIN、ファイルパス、不具合情報、添付文章のみとする
    Button OpenGalleryButton;
    Button SaveButton;
    Button OpenPDFButton;
    String PictureFilePath = "";
    String PDFFilePath = "";
    private SQLOpenHelper dbHelper;
    private static final int PICK_IMAGES_REQUEST_CODE = 1;
    private ActivityResultLauncher<String> launcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insertinformation);
        dbHelper = new SQLOpenHelper(this);
        OpenGalleryButton = findViewById(R.id.editTextTextPersonName5);
        OpenGalleryButton.setOnClickListener(this);
        SaveButton = findViewById(R.id.editTextTextPersonName6);
        SaveButton.setOnClickListener(this);
        OpenPDFButton = findViewById(R.id.editTextTextPersonName4);
        OpenPDFButton.setOnClickListener(this);
        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if(result != null){
                            PDFFilePath = String.valueOf(result);
                            //Log.d("Uri", String.valueOf(result));
                            Log.d("ファイルパスが挿入できているかの確認",PDFFilePath);
                        }
                    }
                });
    }
    public void onClick(View v){
        int id = v.getId();
        if(id == R.id.editTextTextPersonName5){
            OpenGalleryActivity();
        }else if (id == R.id.editTextTextPersonName6){
            SaveButtonActivity();
        }else if(id == R.id.editTextTextPersonName4){
            GetPDFFile();
        }
    }
    //このアクティビティでは次の処理を行う
    //①機器名称の登録
    //②GTINの登録
    //③ファイルパスの登録
    //④不具合情報の登録
    //⑤保管場所の登録

    //①機器名称の登録について
    //これは保存ボタンを押した際にEditTextの内容を取得する形にする
    public void SaveButtonActivity(){
        EditText nameText = findViewById(R.id.editTextTextPersonName2);
        EditText codeText = findViewById(R.id.editTextTextPersonName);
        EditText InformationText = findViewById(R.id.editTextTextPersonName7);
        EditText PlaceText = findViewById(R.id.editTextTextPersonName3);
        //この処理の中には①、②、④、⑤を主に行う
        //まず、EditTextの内容を取得する
        String name = nameText.getText().toString();
        String GTIN = codeText.getText().toString();
        String informationNote = InformationText.getText().toString();
        String Place = PlaceText.getText().toString();
        //PDFファイルパスの名前を日本語に変換する処理を行う
        String decodeFilePathText = decodeString(PDFFilePath);
        assert decodeFilePathText != null;
        File file = new File(decodeFilePathText);
        String fileName = file.getName();
        //次に、SQLテーブルに挿入する前準備を行う
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String SQLInsert = "INSERT INTO ListTable (name, GTIN, information, PictureFilePath, PDFFilePath, place, FileName)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?)";
        SQLiteStatement stmt = db.compileStatement(SQLInsert);
        //変数をバインド
        stmt.bindString(1,name);
        stmt.bindString(2,GTIN);
        stmt.bindString(3,informationNote);
        stmt.bindString(4,PictureFilePath);
        //filePath
        stmt.bindString(5,PDFFilePath);
        //保管場所を保存するコード
        stmt.bindString(6,Place);
        //ファイル名を保存するコード
        stmt.bindString(7,fileName);
        stmt.executeInsert();
        // メイン画面へ遷移
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void OpenGalleryActivity(){
        //この処理では③を行う
        //Fragmentは実装しないが、情報が格納されているのかを確認する画面としてMultipleを突っ込んでおく
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        startActivityForResult(Intent.createChooser(intent,"SelectPictures"),PICK_IMAGES_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGES_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            List<String> filePaths = new ArrayList<String>();

            if(data.getData() != null){
                filePaths.add(data.getData().toString());
            }else{
                ClipData clipData = data.getClipData();
                assert clipData != null;
                int clipItemCount = clipData.getItemCount();
                for(int i = 0; i < clipItemCount; i++){
                    ClipData.Item item = clipData.getItemAt(i);
                    Uri itemUri = (item != null) ? item.getUri() : null;
                    final int modeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
                    assert itemUri != null;
                    getContentResolver().takePersistableUriPermission(itemUri,modeFlags);
                    filePaths.add(itemUri.toString());
                }
            }
            //Logを出して確かめる(確認済み)
            //Log.d("ImagePicture", filePaths.toString());
            // TODO: filePathsをReturnして、保持したままにしておく
            PictureFilePath = TextUtils.join(",",filePaths);
            Log.d("ファイルパスが挿入できているかの確認",PictureFilePath);
            }
        }
    public void GetPDFFile(){
        launcher.launch("application/pdf");
    }
    private String getFilePathFromUri(Uri uri){
        return FileUtils.getFilePath(this,uri);
    }
    public static class FileUtils {
        public static String getPathFromUri(Context context, Uri uri) {
            if ("content".equals(uri.getScheme())) {
                return getPathFromContentUri(context, uri);
            } else if ("file".equals(uri.getScheme())) {
                return uri.getPath();
            }
            return null;
        }

        private static String getPathFromContentUri(Context context, Uri uri) {
            String filePath = null;
            ContentResolver contentResolver = context.getContentResolver();
            try (Cursor cursor = contentResolver.query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                    filePath = cursor.getString(columnIndex);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return filePath;
        }

        public static String getPathFromDocumentUri(Context context, Uri uri) {
            String documentId = DocumentsContract.getDocumentId(uri);
            String[] split = documentId.split(":");
            String type = split[0];

            if ("primary".equalsIgnoreCase(type)) {
                return context.getExternalFilesDir(null) + "/" + split[1];
            }

            return null;
        }

        public static String getFilePath(Context context, Uri uri) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                return getPathFromDocumentUri(context, uri);
            } else {
                return getPathFromUri(context, uri);
            }
        }
    }
    private static String decodeString(String encodedString) {
        try {
            // エンコードされた文字列を指定されたエンコーディングでデコード
            return URLDecoder.decode(encodedString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // エンコーディングがサポートされていない場合の例外処理
            e.printStackTrace();
            return null; // または他のデフォルトの処理を行う
        }
    }
}
