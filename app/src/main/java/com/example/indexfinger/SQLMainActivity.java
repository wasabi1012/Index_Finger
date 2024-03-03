package com.example.indexfinger;

import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.DocumentsProvider;
import android.provider.MediaStore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.PreparedStatement;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.io.PrintStream;

public class SQLMainActivity extends AppCompatActivity implements View.OnClickListener{
    //選択されたテーブルのIDを表すフィールド
    private int _InformationId = -1;
    //選択された名前を表すフィールド
    private String _InformationName = "";
    //データベースヘルパーオブジェクト
    private SQLOpenHelper _helper;
    Button btnPictureSave;
    String ErrorMassage = "GS1コードが入力されていません!";

    private static final int RESULT_PICK_FILENAME = 1;
    private ImageView imageView;
    InputStream inputStream;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlitedatabase);
        //不具合情報を入力するリストを取得
        ListView lvInformation = findViewById(R.id.lvInformation);
        //lvInformationにリスナを登録
        lvInformation.setOnItemClickListener(new ListItemClickListener());
        //DBヘルパーオブジェクトを生成
        _helper = new SQLOpenHelper(this);
        btnPictureSave = (Button)findViewById(R.id.btnPictureSave);
        btnPictureSave.setOnClickListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.layout.activity_sqlitedatabase,menu);
        return true;
    }
    public void onClick(View v){
        if(v.getId() == R.id.btnPictureSave) {
            //紐づけを成立させるため、GTINを入力しているかどうかを判定する
            //よって、GTINが入力されていない場合はメッセージと共に処理を中断するようにする
            //ifの中にifを入れ子にすることで、他のボタン処理を追加する際にもやりやすくする
            //まず、レイアウト上にあるテキストボックスから、文字が入力されているかを確認する
            EditText tvIDNumber = findViewById(R.id.tvIDNumber);
            int number = tvIDNumber.length();
            //判定基準はGTINコードの桁数で判断したいけど、拡張性を考えて0とそれ以外で判定する
            if (number == 0) {
                //エラーメッセージを表示して、処理を終了する
                Toast toast = Toast.makeText(this,ErrorMassage,Toast.LENGTH_LONG);
                toast.show();
                finish();
            } else {
                pickFilenameFromGallery();
            }
        }
    }
    private void pickFilenameFromGallery(){
        //PicturesForSQLTableから、画像登録の処理を引用
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        startActivityForResult(Intent.createChooser(intent,"Select Pictures"),RESULT_PICK_FILENAME);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(resultCode, resultCode, data);
        //ギャラリーからファイルパスを取得
        if (requestCode == RESULT_PICK_FILENAME) {
            Uri uri = data.getData();
            List<String> filePaths = new ArrayList<String>();

            if (data.getData() != null) {
                filePaths.add(data.getData().toString());
            } else {
                ClipData clipData = data.getClipData();
                assert clipData != null;
                int clipItemCount = clipData.getItemCount();
                for (int i = 0; i < clipItemCount; i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    Uri itemUri = (item != null) ? item.getUri() : null;
                    final int modeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
                    getContentResolver().takePersistableUriPermission(itemUri, modeFlags);
                    filePaths.add(itemUri.toString());
                }
            }
            //SQLテーブルに取得したファイルパスを挿入
            //データベース接続オブジェクトを取得
            SQLiteDatabase db = _helper.getWritableDatabase();
            String SQLInsert = "INSERT INTO information (PictureFilePath, number) VALUES (?, ?)";
            SQLiteStatement stmt = db.compileStatement(SQLInsert);
            //GTINを入力しているのかの判定
            EditText tvIDNumber = findViewById(R.id.tvIDNumber);
            int number = tvIDNumber.length();
            //判定基準はGTINコードの桁数で判断したいけど、拡張性を考えて0とそれ以外で判定する
            if (number == 0) {
                //エラーメッセージを表示して、処理を終了する
                Toast toast = Toast.makeText(this, ErrorMassage, Toast.LENGTH_LONG);
                toast.show();
                finish();
            } else {
                //変数のバインド
                for (String filePath : filePaths) {
                    stmt.bindString(1, filePath);
                    stmt.bindLong(2,number);
                    //SQL文の実行
                    stmt.execute();
                }
            }
        }
    }

    @Override
    protected void onDestroy(){
        //DBヘルパーオブジェクト
        _helper.close();
        super.onDestroy();
    }


    //保存ボタンがタップされた時に行う処理の記述
    public void onSaveButtonClick(View view) {
        //テーブルを取得
        EditText etNote = findViewById(R.id.etNote);
        EditText tvIDNumber = findViewById(R.id.tvIDNumber);
        //入力されたデータを取得
        String note = etNote.getText().toString();
        //このアプリはGS1コードの利活用を目的としているので、紐づけの判定基準としてGTINを用いる
        String number = tvIDNumber.getText().toString();
        if (number.equals("")) {
            //GTINが未入力ならメッセージを表示して処理を終了する
            Toast toast = Toast.makeText(this,ErrorMassage,Toast.LENGTH_LONG);
            toast.show();
            finish();
        } else {
            //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
            SQLiteDatabase db = _helper.getWritableDatabase();
            //まず、リストで選択されたデータを削除し、その後インサートを行う
            //削除用SQL文字列を用意
            String sqlDelete = "DELETE FROM information WHERE _id = ?";
            //文字列を元にプリペアドステートメントを取得
            SQLiteStatement stmt = db.compileStatement(sqlDelete);
            //変数のバインド
            stmt.bindLong(1, _InformationId);
            //削除SQLの実行
            stmt.executeUpdateDelete();

            //インサート用SQL文字列の用意
            String sqlInsert = "INSERT INTO information (_id, name, note, number) VALUES (?, ?, ?, ?)";
            //SQL文字列を元にプリペアドステートメントを取得
            stmt = db.compileStatement(sqlInsert);
            //変数のバインド
            stmt.bindLong(1, _InformationId);
            stmt.bindString(2, _InformationName);
            stmt.bindString(3, note);
            stmt.bindString(4, number);
            //インサートSQLの実行
            stmt.executeInsert();
            //テーブルの入力値を消去
            etNote.setText("");
            //選択している名称を未選択に変更
            TextView tvInformationName = findViewById(R.id.tvInformationName);
            tvInformationName.setText(getString(R.string.tv_name));
            //入力したIDの入力値を消去
            tvIDNumber.setText("");
            //保存ボタンをタップできないように変更
            Button btnSave = findViewById(R.id.btnSave);
            btnSave.setEnabled(false);
            //画像の保存ボタンをタップできないように変更
            Button PictureSave = findViewById(R.id.btnPictureSave);
            PictureSave.setEnabled(false);
            //確認画像を見えないように変化
            //findViewById(R.id.image_view).setVisibility(View.INVISIBLE);
        }
    }

    //リストがタップされたときの処理を記述
    private class ListItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent,View view, int position,long id){
            //タップされた行番号を代入
            _InformationId = position;
            //タップされた行のデータを取得して代入
            _InformationName = (String) parent.getItemAtPosition(position);
            //機器の名称を表示するTextViewに表示名を設定する
            TextView tvInformationName = findViewById(R.id.tvInformationName);
            tvInformationName.setText(_InformationName);
            //同様に、IDを表示するTextViewにIDを表示する
            //まず、タップされた行のIDを取得する
            SQLiteDatabase dbd = _helper.getWritableDatabase();
            //ID取得するためのSQL文を用意
            String sqlSearch = "SELECT number FROM information WHERE _id = " + _InformationId;
            //SQL文の実行
            Cursor cursor1 = dbd.rawQuery(sqlSearch,null);
            //データベースから取得した値を格納する変数の準備
            String informationIdNumber = "";
            //SQL実行の戻り値であるカーソルオブジェクトをループさせてデータを取得
            while(cursor1.moveToNext()){
                int idxNumber = cursor1.getColumnIndex("number");
                informationIdNumber = cursor1.getString(idxNumber);
            }
            //保存ボタンをタップできるように設定
            Button btnSave = findViewById(R.id.btnSave);
            btnSave.setEnabled(true);
            //画像保存ボタンをタップできるように変更
            Button btnPicture = findViewById(R.id.btnPictureSave);
            btnPicture.setEnabled(true);

            //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
            SQLiteDatabase db = _helper.getWritableDatabase();
            //主キーによる検索SQL文字列の用意
            String sql = "SELECT * FROM information WHERE _id = " + _InformationId;
            //SQLの実行
            Cursor cursor = db.rawQuery(sql,null);
            //データベースから取得した値を格納する変数の用意。データが存在しなかったときの初期値も用意
            String information = "";
            //SQL実行の戻り値であるカーソルオブジェクトをループさせてデータを取得
            while(cursor.moveToNext()){
                //カラムのインデックス値を取得
                int idxNote = cursor.getColumnIndex("note");
                //カラムのインデックス値を元に実際のデータを取得する
                information = cursor.getString(idxNote);
            }
            //EditTextの各画面部品を取得しデータベースの値の反映
            EditText etNote = findViewById(R.id.etNote);
            etNote.setText(information);
            //IDを画面上に出力
            TextView etNumber = findViewById(R.id.tvIDNumber);
            etNumber.setText(String.valueOf(informationIdNumber));
        }
    }
}
