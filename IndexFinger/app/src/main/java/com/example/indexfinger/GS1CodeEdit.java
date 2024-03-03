package com.example.indexfinger;

import android.app.Application;

public class GS1CodeEdit extends Application {
    private String GS1CodeAll;
    private String metaGS1Code;
    private String codeFormat;
    private int rawByteNum;
    private String packageInsertUrl;

    private int characterCounter;
    private String printedCode;

    private String gtinNum;



    private String firstAI;
    private String contentFirstAI;
    private String secondAI;
    private String contentSecondAI;
    private String thirdAI;
    private String contentThirdAI;
    private String fourthAI;
    private String contentFourthAI;
    private String fifthAI;
    private String contentFifthAI;
    private String sixthAI;
    private String contentSixthAI;
    private String seventhAI;
    private String contentSeventhAI;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    //GS1コードと生データを入力(@MainActivity)
    //GS1コードの内容
    public void setGS1CodeAll(String GS1CodeNum){
        GS1CodeAll=GS1CodeNum;
    }
    //コードのフォーマット *FNC1の場所同定に使う
    public void setCodeFormat(String codeFormatName){
        codeFormat=codeFormatName;
    }
    //添付文書のリダイレクト先のURL
    public void setPackageInsertUrl(String packageInsertUrlStr){
    packageInsertUrl=packageInsertUrlStr;
    }
    //メタデータの内容・String型(16進数) *
//    public void setMetaGS1Code(String metaGS1CodeNum){metaGS1Code=metaGS1CodeNum;}
    //メタデータの内容・byte[]型(16進数) *

//    //GS1コードの総文字数 *
//    public void setRawByteNum(int rawByteLength){
//        rawByteNum=rawByteLength;
//    }


    //関連データ(GS1コードとAI)を出力する関数(@ShowScanResultActivity)
    public String getGS1CodeAll() {
        return GS1CodeAll;
    }
//    public String getMetaGS1Code(){
//        return metaGS1Code;
//    }
    public String getCodeFormat(){
        return codeFormat;
    }
    public String getPackageInsertUrl(){
        return packageInsertUrl;
    }
//    public String getPrintedCode(){
//        characterCounter=0;
//
//
//        printedCode="";
//        return printedCode;
//    }
    public String getContentFirstAI(){
        if(GS1CodeAll.length()>17){
            gtinNum=GS1CodeAll.substring(2,16);
        }
        else{
            gtinNum="ERROR";
        }
        return gtinNum;
    }


}
