package com.team.mobileworld.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.team.mobileworld.core.object.Account;
import com.team.mobileworld.core.object.Order;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database extends SQLiteOpenHelper {
    //Cac thuoc tinh tren bang co so du lieu.
    public static final String DATABASE_NAME = "MOBILE_WORLD";
    public static final String TABLE_CART = "Cart";
    public static final String TABLE_ACCOUNT = "Account";


    public Database(@Nullable Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }


    //Tao co so du lieu sqlite
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(SQLiteDatabase db) {
        final Map<String, String> account = new HashMap<>();
        account.put("username", "nvarchar(50) primary key");
        account.put("password", "nvarchar(100)");
        account.put("time", "number");

        final Map<String, String> order = new HashMap<>();
        order.put("id", "int primary key");
        order.put("name", "nvarchar(100)");
        order.put("image", "text");
        order.put("amount", "int");
        order.put("price", "int");

        String tablecart = makeTable(TABLE_CART, order);
        String tableaccount = makeTable(TABLE_ACCOUNT, account);

        Log.d("database", tablecart);
        Log.d("database", tableaccount);

        //Tao table
        db.execSQL(tablecart);
        db.execSQL(tableaccount);
    }

    //Cap nhat co so du lieu
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Xoa table contract da co
        db.execSQL("drop table if exists " + TABLE_ACCOUNT);

        db.execSQL("drop table if exists " + TABLE_CART);

        //Tao lai database
        onCreate(db);
    }


    public Account getAccount() {
        Account account = null;
        String sql = "select username,password from " + TABLE_ACCOUNT + " ORDER BY time DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        if (cur != null && cur.getCount() > 0)
            account = new Account(cur.getString(0), cur.getString(1));
        
        print("get Account=" + cur.getCount());
        db.close();
        return account;
    }

    public void clearAccount(){
        getWritableDatabase().execSQL("delete from "+ TABLE_ACCOUNT);
    }

    public long addAccount(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("password", password);
        cv.put("password", Calendar.getInstance().getTime().getTime());

        long insert = db.insert(TABLE_ACCOUNT, null,cv);
        print("add username=" + insert);
        db.close();
        
        return insert;
    }

    public long updatePassword(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("password", password);
        cv.put("time", Calendar.getInstance().getTime().getTime());

        long update = db.update(TABLE_ACCOUNT, cv,"username=?", new String[]{username});
        print("insert password=" + update);
        db.close();
        return update;
    }

    public int getSize(String name, String table)
    {
        String sql = "select " + name + " where" + table;
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);
        int index = cursor.getCount();
        return index;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String makeTable(String tablename, Map<String, String> params) {
        String table = "CREATE TABLE IF NOT EXISTS " + tablename + "(";
        List<String> keys = new ArrayList<String>();
        params.keySet().forEach(keys::add);

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String val = params.get(key);

            if (i < keys.size() - 1)
                table += key + " " + val + ", ";
            else
                table += key + " " + val + ");";
        }

        return table;

    }

    //Lay tat ca danh ba co trong db
    public List<Order> getAllCart() {
        List<Order> list = new ArrayList<>();
        //Truy van sql
        String sql = "select id,name,price,image,amount from " + TABLE_CART;
        //Lay doi tuong db sqlite
        SQLiteDatabase db = this.getReadableDatabase();
        //Chay cau truy van tra ve dang cursor
        Cursor cur = db.rawQuery(sql, null);
        //Tao ArrayList de tra ve
        print("current=" +  cur.toString() + "  \t\t\ttindex=" + cur.getCount());

        if (cur != null)
            while (cur.moveToNext()) {
                int id = cur.getInt(0);
                String name = cur.getString(1);
                int price = cur.getInt(2);
                String urlimage = cur.getString(3);
                int amount = cur.getInt(4);

                Order order = new Order(id, name, price, urlimage, amount);
                list.add(order);
            }

        db.close();
        return list;
    }

    public static void print(String text) {
        Log.d("userinfo", text);
    }

    public long addOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id", order.getId());
        cv.put("name", order.getName());
        cv.put("price", order.getPrice());
        cv.put("amount", order.getAmount());
        cv.put("image", order.getImage());

        long insert = db.insert(TABLE_CART, null, cv);
        print("insert = "+insert);
        db.close();
        return insert;
    }

    public long updateCart(int id, Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id", order.getId());
        cv.put("name", order.getName());
        cv.put("price", order.getPrice());
        cv.put("amount", order.getAmount());
        cv.put("image", order.getImage());


        long update = db.update(TABLE_CART, cv, "id=?", new String[]{String.valueOf(id)});
        print("update = " + update);
        db.close();
        return update;
    }

    public int deleleOrder(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int del = -1;
        String where = "id=";
        where += id == -1 ? "*" : id;
        del = db.delete(TABLE_CART, where,null);

        db.close();
        print("delete = " + del +"\t\t" + where);
        return del;
    }
}
