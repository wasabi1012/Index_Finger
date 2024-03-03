package com.example.indexfinger;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.DecodeHintType;
import com.google.zxing.common.StringUtils;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private SQLOpenHelper _helper;
    private GS1CodeEdit gs1CodeEdit;
    Button scanButton;
    Button ShowTableButton;
    Button DeleteTableButton;
    Button GetInformationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanButton = (Button)findViewById(R.id.button);
        scanButton.setOnClickListener(this);
        ShowTableButton = (Button)findViewById(R.id.button2);
        ShowTableButton.setOnClickListener(this);
        DeleteTableButton = (Button)findViewById(R.id.button3);
        DeleteTableButton.setOnClickListener(this);
        GetInformationButton = (Button)findViewById(R.id.InsertInformationButton);
        GetInformationButton.setOnClickListener(this);
        gs1CodeEdit=(GS1CodeEdit) this.getApplication();
        _helper = new SQLOpenHelper(this);
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null)
                {
                    Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                }
                else
                {
                    //コードのデータ
                    String GS1CodeNum=result.getContents();
                    //コードの生データ(byte[])
//                    byte[] barcodeRawBytes= result.getRawBytes();
//                    int rawDataNum=barcodeRawBytes.length;

                    //コードの生データ(int[])*******************************************未確認
//                    int[] barcodeRawInt = new int[rawDataNum];
//                    for(int i=0;i< rawDataNum;i++){
//                        barcodeRawInt[i]=barcodeRawBytes[i];
//                    }

//                    String metaGS1CodeFormFragment="%02x,";
//
//                    int i=0;
//                    String metaGS1CodeNum=String.format(metaGS1CodeFormFragment,barcodeRawBytes[i]);
//                    for(i=1;i<rawDataNum;i++){
//                        String fragmentMetaGS1CodeNum=String.format(metaGS1CodeFormFragment,barcodeRawBytes[i]);
//                        metaGS1CodeNum=metaGS1CodeNum.concat(fragmentMetaGS1CodeNum);
//                    }

//                    metaGS1CodeNum=metaGS1CodeNum.substring(0,metaGS1CodeNum.length()-1);

                    //コードのフォーマット
                    String codeFormat= result.getFormatName();

                    //GS1CodeEdit.javaに変数を代入
                    gs1CodeEdit.setGS1CodeAll(GS1CodeNum);
//                    gs1CodeEdit.setMetaGS1Code(metaGS1CodeNum);
                    gs1CodeEdit.setCodeFormat(codeFormat);
//                    gs1CodeEdit.setRawByteNum(rawDataNum);


                    Intent successIntent=new Intent(getApplicationContext(),ShowScanResultActivity.class);
                    startActivity(successIntent);
                }
            });
    //ボタンの処理がごちゃごちゃしないようにonClickをつけておく
    public void onClick(View view){
        if(view.getId() == R.id.button){
            onStartScanButtonClick();
        }
        if(view.getId() == R.id.button2){
            onMoveScreenButtonClick();
        }
        if(view.getId() == R.id.button3){
            onDeleteButtonClick();
        }
        if(view.getId() == R.id.InsertInformationButton);
            GetInformation();
    }
    public void onStartScanButtonClick(){
        ScanOptions scanOptions=new ScanOptions();
        //scanOptions.setDesiredBarcodeFormats(ScanOptions.CODE_128);
        //scanOptions.setDesiredBarcodeFormats(ScanOptions.DATA_MATRIX);
        scanOptions.setPrompt("Let's Scan a GS1 Code on a Medical Device");
        scanOptions.setOrientationLocked(true);
        scanOptions.setCaptureActivity(MyCaptureActivity.class);
        scanOptions.setBeepEnabled(false);
        scanOptions.setTorchEnabled(false);
        barcodeLauncher.launch(scanOptions);
    }
    public void GetInformation(){
        //SQLテーブルに情報を登録するボタン
        Intent intent = new Intent(MainActivity.this, GetInformationFromTable.class);
        //テーブルに情報を登録する画面に遷移
        startActivity(intent);
    }
    public void onMoveScreenButtonClick(){
        //テーブルを閲覧するボタン
        //インテントオブジェクトを生成する
        Intent intent = new Intent(MainActivity.this,SQLListActivity.class);
        //メイン画面からテーブル一覧の画面へ遷移
        startActivity(intent);
    }
    public void onDeleteButtonClick(){
        //テーブルの内容を削除する
        SQLiteDatabase db = _helper.getWritableDatabase();
        String SQLDelete = "DROP TABLE IF EXISTS information";
        SQLiteStatement stmt = db.compileStatement(SQLDelete);
        stmt.executeUpdateDelete();
        //その後、再度テーブルを作成
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
    }