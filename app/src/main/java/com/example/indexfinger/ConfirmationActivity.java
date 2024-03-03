package com.example.indexfinger;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfirmationActivity extends AppCompatActivity implements View.OnClickListener{
    //このアクティビティでは、SQLテーブルに登録されている情報を検索して表示を行う
    private static final String Column_Name = "name";
    private static final String GTIN_Column = "GTIN";
    private static final String Information_Column = "information";
    private static final String Picture_FilePath_Column = "PictureFilePath";
    private static final String PDF_FilePath_Column = "PDFFilePath";
    private static final String Place_Column = "place";

    private static final String FileName_Column = "FileName";
    Button ShowPictureButton;
    Button ShowPDFButton;
    private static List<String> PictureFilePath = new ArrayList<>();
    private static String PDFFilePath;
    private String nameOfMachine = "";

    private String name = "";
    private String gtin = "";

    private String information = "";

    private String pictureFilePath = "";

    private String pdfFilePath = "";

    private String Place = "";

    private String FileName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showinformation);
        ShowPictureButton = findViewById(R.id.btnShowPicture);
        ShowPictureButton.setOnClickListener(this);
        ShowPDFButton = findViewById(R.id.btnShowPDF);
        ShowPDFButton.setOnClickListener(this);
        TextView NameText = findViewById(R.id.gs1code);
        TextView GTINText = findViewById(R.id.metaGS1Code);
        TextView InformationText = findViewById(R.id.codeFormat);
        TextView PlaceText = findViewById(R.id.PlaceFormat);
        TextView FileNameText = findViewById(R.id.FileNameFormat);
        SQLOpenHelper databaseHelper = new SQLOpenHelper(this);
        //遷移元のデータを取得
        Intent intent = getIntent();
        if(intent != null){
            String selectedData = intent.getStringExtra("selectedData");
            assert selectedData != null;
            Log.d("取得したデータ",selectedData);
            // ここに取得したデータに対する処理を書く
            //todo:selectedDataから取得したデータを使ってSQLテーブルの検索を行い、内容を確認する
            String[] projection = {Column_Name, GTIN_Column, Information_Column, Picture_FilePath_Column, PDF_FilePath_Column, Place_Column, FileName_Column};
            String selection = "name = ?";  // WHERE句
            String[] selectionArgs = {selectedData};  // 対象の行を特定する値
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            Cursor cursor = db.query("ListTable", projection, selection, selectionArgs, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    // 列のインデックスまたは列名を使用してデータを取得
                    int nameIndex = cursor.getColumnIndex(Column_Name);
                    if(nameIndex != -1) {
                        name = cursor.getString(nameIndex);
                    }
                    int gtinIndex = cursor.getColumnIndex(GTIN_Column);
                    if(gtinIndex != -1) {
                        gtin = cursor.getString(gtinIndex);
                    }
                    int informationIndex = cursor.getColumnIndex(Information_Column);
                    if(informationIndex != -1) {
                        information = cursor.getString(informationIndex);
                    }
                    int PictureFilePathIndex = cursor.getColumnIndex(Picture_FilePath_Column);
                    if(PictureFilePathIndex != -1) {
                        pictureFilePath = cursor.getString(PictureFilePathIndex);
                    }
                    int PDFFilePathIndex = cursor.getColumnIndex(PDF_FilePath_Column);
                    if(PDFFilePathIndex != -1) {
                        pdfFilePath = cursor.getString(PDFFilePathIndex);
                    }
                    int PlaceIndex = cursor.getColumnIndex(Place_Column);
                    if(PlaceIndex != -1) {
                        Place = cursor.getString(PlaceIndex);
                    }
                    int FileNameIndex = cursor.getColumnIndex(FileName_Column);
                    if(FileNameIndex != -1){
                        FileName = cursor.getString(FileNameIndex);
                    }
                    Log.d("Data", "Name: " + name + ", GTIN: " + gtin + ", Information: " + information +
                            ", Picture File Path: " + pictureFilePath + ", PDF File Path: " + pdfFilePath + ", Place:" + Place +
                            ", FileName:" + FileName);

                    NameText.setText("名称: " + name);
                    GTINText.setText("GTIN: " + gtin);
                    InformationText.setText(information);
                    PlaceText.setText("保管場所: " + Place);
                    FileNameText.setText("ファイル名: " + FileName);
                    PictureFilePath = Arrays.asList(pictureFilePath);
                    PDFFilePath = pdfFilePath;
                    nameOfMachine = name;
                } while (cursor.moveToNext());{

                }
            }
            // カーソルを閉じる
            cursor.close();
            }
        }
    public void onClick(View v){
        int id = v.getId();
        if(id == R.id.btnShowPicture){
            Log.d("写真のファイルパスが正常に挿入されているかどうかの確認", PictureFilePath.toString());
            //写真を閲覧するボタンの処理
            Intent intent = new Intent(ConfirmationActivity.this, ShowPicture.class);
            intent.putExtra("Name Machine", nameOfMachine);
            startActivity(intent);
        }else if(id == R.id.btnShowPDF){
            Log.d("PDFファイルパスを正常に挿入しているかの確認", PDFFilePath);
            //PDFを閲覧するボタン
            Intent intent = new Intent(ConfirmationActivity.this, ShowPDF.class);
            intent.putExtra("PDF Name", nameOfMachine);
            startActivity(intent);
        }
    }
    }
