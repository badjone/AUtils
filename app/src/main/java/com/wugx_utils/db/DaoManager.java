package com.wugx_utils.db;


import com.blankj.utilcode.util.Utils;
import com.wugx_utils.db.update.MyOpernHelper;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 * 常用操作符如下
 * <p>
 * - eq()：==
 * - noteq()：!=
 * - gt()： >
 * - lt()：<
 * - ge：>=
 * - le:<=
 * - like()：包含
 * - between：俩者之间
 * - in：在某个值内
 * - notIn：不在某个值内
 *
 * @author wugx
 * @data 2018/5/26.
 */

public class DaoManager {
    private static final String DB_NAME = "AUtils_db";

    //多线程中要被共享的使用volatile关键字修饰
    private static DaoManager manager;
    private static DaoMaster sDaoMaster;
    private static DaoMaster.DevOpenHelper sHelper;
    private static DaoSession sDaoSession;

    /**
     * 单例模式获得操作数据库对象
     *
     * @return
     */
    public static DaoManager getInstance() {
        if (manager == null) {
            synchronized (DaoManager.class) {
                if (manager == null) {
                    manager = new DaoManager();
                }
            }
        }
        return manager;
    }

    public void initGreenDao() {
        getDaoSession();
    }

    /**
     * 判断是否有存在数据库，如果没有则创建
     *
     * @return
     */
    public DaoMaster getDaoMaster() {
        if (sDaoMaster == null) {
            DaoMaster.DevOpenHelper helper = new MyOpernHelper(Utils.getApp(), DB_NAME, null);
            sDaoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return sDaoMaster;
    }

    /**
     * 完成对数据库的添加、删除、修改、查询操作，仅仅是一个接口
     *
     * @return
     */
    public DaoSession getDaoSession() {
        if (sDaoSession == null) {
            if (sDaoMaster == null) {
                sDaoMaster = getDaoMaster();
            }
            sDaoSession = sDaoMaster.newSession();
        }
        return sDaoSession;
    }

    /**
     * 打开输出日志，默认关闭
     */
    public void setDebug() {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    /**
     * 关闭所有的操作，数据库开启后，使用完毕要关闭
     */
    public void closeConnection() {
        closeHelper();
        closeDaoSession();
    }

    public void closeHelper() {
        if (sHelper != null) {
            sHelper.close();
            sHelper = null;
        }
    }

    public void closeDaoSession() {
        if (sDaoSession != null) {
            sDaoSession.clear();
            sDaoSession = null;
        }
    }
}
