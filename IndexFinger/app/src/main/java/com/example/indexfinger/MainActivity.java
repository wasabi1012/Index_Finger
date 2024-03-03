package com.example.indexfinger;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.DecodeHintType;
import com.google.zxing.common.StringUtils;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private GS1CodeEdit gs1CodeEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gs1CodeEdit=(GS1CodeEdit) this.getApplication();
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

    public void onStartScanButtonClick(View view){
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
}