package com.wugx_utils.db.update;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.wugx_utils.db.DaoMaster;
import com.wugx_utils.db.TestBeanDao;

import org.greenrobot.greendao.database.Database;

/**
 * 数据库升级类
 *
 * @author wugx
 * @data 2018/5/26.
 */

public class MyOpernHelper extends DaoMaster.DevOpenHelper {

    public MyOpernHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        if (oldVersion < newVersion) {

            //升级过程需要升级的类，如果是两个，参照下面
//            MigrationHelper.getInstance().migrate(db, TestBeanDao.class);

            MigrationHelper.getInstance().migrate(db, TestBeanDao.class);
        }
    }
}
