package com.example.indexfinger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

public class Searchactivity3 extends AppCompatActivity {
    SQLOpenHelper _helper;
    String MachineName1 = "";
    String Note1 = "";
    String Number1 = "";
    String PicturePath1 = "";

    //この画面が表示されるのは特定のIDが入力されたときのみ
    //よって、画面にはその情報のみを表示すればよい

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchactivity3);
        //部品を取得する
        TextView id = (TextView)findViewById(R.id.tv_lb_IDNumber);
        ImageView PictPath = (ImageView) findViewById(R.id.imageView4);
        //TextView PictPath = (TextView)findViewById(R.id.etNote2) ;
        TextView MachineName = (TextView) findViewById(R.id.NameOfMachine);
        TextView Note = (TextView) findViewById(R.id.etNote1);
        //DBヘルパーオブジェクトを生成
        _helper = new SQLOpenHelper(this);
        SQLiteDatabase db = _helper.getReadableDatabase();
        //SQLテーブルに入れた情報を、変数として呼び出す
        Cursor c = db.query(
                "information",
                new String[] { "name", "note", "number", "PicturePath"},
                null, null, null, null, null);
        boolean isEoF = c.moveToFirst();
        while(isEoF){
            MachineName1 = c.getString(0);
            Note1 = c.getString(1);
            Number1 = c.getString(2);
            PicturePath1 = c.getString(3);
            isEoF = c.moveToNext();
        }
        c.close();
        //picture変数を挿入
        //取得した変数の内容を画面部品にセット
        id.setText("ID:" + Number1);
        MachineName.setText("名称:" + MachineName1);
        Note.setText(Note1);
        //PictPath.setText(PicturePath1);
        //stringを画像に変換
        //Intent intent = getIntent();
        //if(intent != null){
            //PicturePath1 = intent.getStringExtra("PicturePath");
            //Bitmap bitmap = BitmapFactory.decodeFile(PicturePath1);
            //PictPath.setImageBitmap(bitmap);
        //}
        //Uri uri = null;
        //if(PicturePath1 != null){
          //  uri = Uri.parse(PicturePath1);
           // try{
          //      Bitmap bitmap = getBitmapFromUri(uri);
            //  PictPath.setImageBitmap(bitmap);
          // }catch(IOException e){
               // e.printStackTrace();
           //}
           // }
       }


   // private Bitmap getBitmapFromUri(Uri uri) throws IOException{
       // ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri,"r");
     //   FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
    //    Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
    //    parcelFileDescriptor.close();
     //  return image;
    //}

    @Override
    protected void onDestroy() {
        //DBヘルパーオブジェクト
        _helper.close();
        super.onDestroy();
    }

}