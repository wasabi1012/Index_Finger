package com.example.indexfinger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLOpenHelper extends SQLiteOpenHelper {
    //データベースのファイル名
    private static final String DATABASE_NAME = "information.db";
    //バージョン情報の定数フィールド
    private static final int DATABASE_VERSION = 1;

    //コンストラクタ
    public SQLOpenHelper(Context context){
    //親クラスのコンストラクタの呼び出し
    super(context,DATABASE_NAME,null,DATABASE_VERSION);
}
    @Override
    public void onCreate(SQLiteDatabase db){
        //テーブル作成のためのSQL文字列の作成
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE information (");
        sb.append("_id INTEGER PRIMARY KEY,");
        sb.append("place TEXT,");
        sb.append("name TEXT,");
        sb.append("GTIN TEXT,");
        sb.append("information TEXT,");
        sb.append("FileName TEXT,");
        sb.append("PictureFilePath VARCHAR(100),");
        sb.append("PDFFilePath VARCHAR(100)");
        sb.append(")");
        String sql = sb.toString();
        //SQLの実行
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}
