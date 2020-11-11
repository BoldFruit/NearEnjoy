package com.example.lib_neuqer_chat_ui.emoji;

import android.annotation.SuppressLint;
import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import io.netty.util.internal.StringUtil;

/**
 * Time:2020/3/20 13:52
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class EmojiDaoUtil {

    private static final String TAG = "EmojiDaoUtil";
    private String mFilePath;
    private Application application;
    //mDirPath：设置文件在手机中的存储位置，格式应该为/data/data/XXX,
    // mDbName：如果是传入.db文件的话需要传入/assets/xxx/xxx/...传入.db文件的具体位置
    private String mDirPath;
    //数据库名字
    private String mDbName;
    private String mDbPath;
    //你要存储的文件夹的名字，一般为包命
    private String mDirName;
    //你数据库里的表名
    private String mTableName;
    //数据库的列名
    private String[] mColumns;

    private EmojiDaoUtil() {

    }

    private static final class EmojiDaoHolder {
        private static EmojiDaoUtil INSTANCE = new EmojiDaoUtil();
    }

    public static EmojiDaoUtil getInstance() {
        return EmojiDaoHolder.INSTANCE;
    }


    public void init(Application application, String dirName, String dbName, String table, String[] columns) {
        this.application = application;
        this.mDirName = dirName;
        this.mDbName = dbName;
        this.mTableName = table;
        this.mColumns = columns;
        mDirPath = "data/data/" + mDirName + "/databases";
        mFilePath = copySqliteToDatabase();
    }



    private String copySqliteToDatabase() {

        if (StringUtil.isNullOrEmpty(mDirPath)) {
            Log.d(TAG, "copySqliteToDatabase: mDirPath is null");
            return "";
        }

        File dir = new File(mDirPath);
        
        if (!dir.exists() || !dir.isDirectory()) {
            if (dir.mkdir()) {
                Log.d(TAG, "copySqliteToDatabase: make dir success");
            } else {
                Log.d(TAG, "copySqliteToDatabase: failed to create dir");
            }
        }
        File file = new File(dir, mDbName);
        InputStream inputStream;
        OutputStream outputStream;

        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    //java getResourceAsStream的使用方法
                    //https://blog.csdn.net/WinWill2012/article/details/39641401
                    //网上用这个打开的方式比较少见
//                    inputStream = Objects.requireNonNull(application.getClassLoader())
//                            .getResourceAsStream(mDbName);
                    inputStream = application.getAssets().open(mDbName);
                    outputStream = new FileOutputStream(file);
                    byte[] readBytes = new byte[1024];
                    int len;
                    while ((len = inputStream.read(readBytes)) != -1) {
                        outputStream.write(readBytes, 0, len);
                    }
                    outputStream.flush();
                    inputStream.close();
                    outputStream.close();
                    return file.getPath();
                }

            } catch (IOException e) {
                Log.d(TAG, "copySqliteToDatabase: failed to read data from assets");
                e.printStackTrace();
            }

        } else {
            return file.getPath();
        }
        return null;
    }


    public List<EmojiBean> getEmojiList() {
        List<EmojiBean> list = new ArrayList<>();
        SQLiteDatabase db = SQLiteDatabase.openDatabase(mFilePath, null, SQLiteDatabase.OPEN_READONLY);
        @SuppressLint("Recycle") Cursor cursor = db.query(mTableName, mColumns, null, null, null, null, null);
        while(cursor.moveToNext()) {
            EmojiBean emojiBean = new EmojiBean();
            int id = cursor.getInt(1);
            int unicodeInt = cursor.getInt(0);
            emojiBean.setEmojiId(id);
            emojiBean.setUnicodeInt(unicodeInt);
            list.add(emojiBean);
        }

        return list;
    }

}
