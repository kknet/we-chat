package xyz.mxd.wechat.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.tim.common.packets.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import xyz.mxd.wechat.AppStart;
import xyz.mxd.wechat.activity.MainActivity;

public class TIMSQLiteOpenHelper extends SQLiteOpenHelper {

    //得到一个MySQLiteOpenHelper对象
    private static final TIMSQLiteOpenHelper sqLiteOpenHelper;

    static {
        sqLiteOpenHelper = new TIMSQLiteOpenHelper(AppStart.context, "db_t-im", null, 2);
    }
    /**
     * @param context： 上下文
     * @param name：数据库的名称
     * @param factory： null代表使用系统默认的游标
     * @param version：数据库文件的版本号，必须大于等于1
     */
    private TIMSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    /**
     * 用于创建表的结构
     * 第一次使用MySQLiteOpenHelper类时，如果表结构没有被创建就执行一次，创建表的结构，如果表的结构创建了就不执行
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("tag", "on create");
        String sql = "create table t_user (_id integer primary key, name varchar(20), phone varchar(255) ,password varchar(255))";
        db.execSQL(sql);
        String sql1 = "create table t_wechat (_id integer primary key,createTime long, " +
                "avatar varchar(255), nick varchar(255), msg varchar(255), 'from' varchar(255))";
        db.execSQL(sql1);
        String sql2 = "create table t_history_message (_id integer primary key, 'from' varchar(255), " +
                "'to' varchar(255), msgType varchar(255), chatType varchar(255), content " +
                "varchar(255), groupId varchar(255), createTime long, id varchar(255))";
        db.execSQL(sql2);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /*
     * ************************************ CRUD **************************************************/


    public static void insertUser(String userid, String password){

        //拿到SQLiteDateBase对象
        SQLiteDatabase dataBase = sqLiteOpenHelper.getReadableDatabase();
        //向表中插入数据
        String sql= "insert into t_user (name,phone,password) values (?,?,?)";
        dataBase.execSQL(sql, new Object[]{"t-im"+new Random().nextInt(100),
                (userid + ""),
                (password + "")});
        //关闭资源
        dataBase.close();
    }
    public static void deleteUser(String phone){
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        String sql = "delete from t_user where phone = ?";
        database.execSQL(sql , new Object []{phone});
        //关闭资源
        database.close();
    }

    public static void updateUser(String userid, String password){
        //得到SQLiteDataBase对象
        SQLiteDatabase database = sqLiteOpenHelper.getReadableDatabase();
        String sql = "update t_user set password = ? where phone = ?";
        database.execSQL(sql , new Object[]{password,userid});
        //关闭资源
        database.close();
    }

    public static List<String> queryUser(){
        List<String> list = new ArrayList<>();
        //得到SQLiteDataBase对象
        SQLiteDatabase database = sqLiteOpenHelper.getReadableDatabase();
        //查询年龄大于50，且desc排序
        String sql = "select phone, password from t_user";
        Cursor cursor = database.rawQuery(sql, new String[]{});

        while(cursor.moveToNext()){
            list.add(cursor.getString(0));
            list.add(cursor.getString(1));
        }
        //释放cursor
        cursor.close();
        //关闭资源
        database.close();
        return list;
    }




}