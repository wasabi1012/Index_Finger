package com.example.indexfinger;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowPicture extends AppCompatActivity {
    //写真のURIリスト
    private List<String> photoUris;

    private SQLOpenHelper _helper;

    private String PictureFilePath = "";
    String selection = "name = ?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        //ViewPagerはAndroidxから追加された新機能、ギャラリーの表示に優れているらしい
        ViewPager viewPager = findViewById(R.id.viewPager);
        photoUris = new ArrayList<>();  // 新しいArrayListを初期化
        _helper = new SQLOpenHelper(this);
        //なんかうまくいかないので、受け取った名前データを使って検索をかける
        Intent intent = getIntent();
        String[] getNameText = new String[]{intent.getStringExtra("Name Machine")};
        SQLiteDatabase db = _helper.getWritableDatabase();
        Cursor c = db.query("ListTable",new String[]{"PictureFilePath"},selection,getNameText,null,null,null);
        boolean isEoF = c.moveToFirst();
        PictureFilePath = c.getString(0);
        isEoF = c.moveToNext();
        photoUris = Arrays.asList(PictureFilePath.split(","));
        c.close();
        Log.d("You can get many Pictures", photoUris.toString());
        PhotoPagerAdapter adapter = new PhotoPagerAdapter(getSupportFragmentManager(), photoUris);
        viewPager.setAdapter(adapter);
    }
}
