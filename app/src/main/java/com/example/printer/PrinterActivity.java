package com.example.printer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author yangsr
 * @time 2016年07月28日
 */
public class PrinterActivity extends Activity implements OnClickListener {
    private Button prinrerBtn, btnAz;
    private final String FILE_NAME = "rfgcfhjcd.xls";
    private final String APK_NAME = "PrinterShare.v.11.0.0.apk";
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.printer);
        context = PrinterActivity.this;
        prinrerBtn = (Button) findViewById(R.id.button1);
        btnAz = (Button) findViewById(R.id.btnAz);
        prinrerBtn.setOnClickListener(this);
        btnAz.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.button1:
                if (appIsInstalled(context, "com.dynamixsoftware.printershare.amazon")) {

                    printer();
                } else {
                    Toast.makeText(getApplication(), "软件未安装,先安装应用!", Toast.LENGTH_SHORT).show();
                    if (copyApkFromAssets(this, APK_NAME, Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+APK_NAME)) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setDataAndType(
                                Uri.parse("file://" + Environment.getExternalStorageDirectory()
                                        .getAbsolutePath() + "/"+APK_NAME),
                                "application/vnd.android.package-archive");
                        startActivity(intent);
                        printer();
                    }
                }
                break;
            case R.id.btnAz:
                if (copyApkFromAssets(this, APK_NAME, Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+APK_NAME)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(
                            Uri.parse("file://" + Environment.getExternalStorageDirectory()
                                    .getAbsolutePath() + "/"+APK_NAME),
                            "application/vnd.android.package-archive");
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    private void printer() {
        try {
            saveFile(FILE_NAME, PrinterActivity.this, R.raw.rfgcaqxcbg);// 文件目录res/raw
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 模板
        String aafileurl = Environment.getExternalStorageDirectory()
                + "/inspection/"+FILE_NAME;
        // 调用printershare软件来打印该文件
        File picture = new File(aafileurl);
        Uri data_uri = Uri.fromFile(picture);
        try {
            //String data_type = "application/msword";
            String data_type = "application/vnd.ms-excel";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setPackage("com.dynamixsoftware.printershare.amazon");// 未注册之前com.dynamixsoftware.printershare，注册后加上amazon
            i.setDataAndType(data_uri, data_type);
            startActivity(i);
        } catch (Exception e) {
            // 没有找到printershare
            Log.e("TAG", "没有找到printershare");
        }
    }

    /**
     * 将文件复制到SD卡，并返回该文件对应的数据库对象
     * @return
     * @throws IOException
     */
    public void saveFile(String fileName, Context context, int rawid)
            throws IOException {
        // 首先判断该目录下的文件夹是否存在
        File dir = new File(Environment.getExternalStorageDirectory()
                + "/inspection/");
        if (!dir.exists()) {
            // 文件夹不存在 ， 则创建文件夹
            dir.mkdirs();
            Toast.makeText(getApplication(), "文件夹不存在", Toast.LENGTH_SHORT)
                    .show();
        }
        // 判断目标文件是否存在
        File file1 = new File(dir, fileName);
        if (!file1.exists()) {
            file1.createNewFile(); // 创建文件
            Toast.makeText(getApplication(), "创建文件", Toast.LENGTH_SHORT).show();
        }
        // 开始进行文件的复制
        InputStream input = context.getResources().openRawResource(rawid); // 获取资源文件raw
        // 标号
        try {
            FileOutputStream out = new FileOutputStream(file1); // 文件输出流、用于将文件写到SD卡中
            // -- 从内存出去
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = (input.read(buffer))) != -1) { // 读取文件，-- 进到内存

                out.write(buffer, 0, len); // 写入数据 ，-- 从内存出
            }
            input.close();
            out.close(); // 关闭流
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 判断apk是否安装
    public static boolean appIsInstalled(Context context, String pageName) {
        try {
            context.getPackageManager().getPackageInfo(pageName, 0);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public boolean copyApkFromAssets(Context context, String fileName,
                                     String path) {
        boolean copyIsFinish = false;
        try {
            InputStream is = context.getAssets().open(APK_NAME);
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }

}
