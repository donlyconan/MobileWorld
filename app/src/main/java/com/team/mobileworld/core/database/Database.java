package com.team.mobileworld.core.database;

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
    public static final int DEL_ALL = -1;


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
        order.put("slmax", "int");
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
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "select username,password,time from " + TABLE_ACCOUNT + " ORDER BY time DESC";
        Cursor cur = db.rawQuery(sql, null);

        if (cur != null && cur.moveToFirst()) {
            account = new Account(cur.getString(0), cur.getString(1));
        }

        print("Lay thong tin tai khoan: " + account);
        db.close();
        return account;
    }

    public void clearAccount() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + TABLE_ACCOUNT);
        db.close();
    }

    public boolean addAccount(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("password", password);
        cv.put("time", Calendar.getInstance().getTime().getTime());
        long insert = -1;
        if (hasAccount(username, db)) {
            updatePassword(username, password);
            insert = 10;
        } else {
            insert = db.insert(TABLE_ACCOUNT, null, cv);
        }
        print("Luu tai khoan: " + cv.toString() + "\t" + result((int) insert));
        db.close();
        return insert > 0;
    }

    public boolean hasAccount(String username, SQLiteDatabase db) {
        int index = 0;
        String sql = String.format("select username from " + TABLE_ACCOUNT + " where username=\"%s\"", username);
        Cursor cursor = db.rawQuery(sql, null);
        index = cursor.getCount();
        print("has account: " + index + "\\" + (index > 0));
        cursor.close();
        return index > 0;
    }

    public boolean updatePassword(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("password", password);
        cv.put("time", Calendar.getInstance().getTime().getTime());

        long update = db.update(TABLE_ACCOUNT, cv, "username=?", new String[]{username});
        print("tai khoan: " + cv.toString() + "\t" + result((int) update));
        db.close();
        return update > 0;
    }

    public int getSize(String name, String table) {
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
        //Truy van sql
        String sql = "select id,name,price,image,amount, slmax from " + TABLE_CART;
        //Lay doi tuong db sqlite
        SQLiteDatabase db = this.getReadableDatabase();
        //Chay cau truy van tra ve dang cursor
        Cursor cur = db.rawQuery(sql, null);
        //Tao ArrayList de tra ve
        List<Order> list = new ArrayList<>(10);

        print("So luong trong kho: " + cur.getCount());

        if (cur != null)
            while (cur.moveToNext()) {
                int id = cur.getInt(0);
                String name = cur.getString(1);
                int price = cur.getInt(2);
                String urlimage = cur.getString(3);
                int amount = cur.getInt(4);
                int slmax = cur.getInt(5);

                Order order = new Order(id, name, price, urlimage, amount);
                order.setSlmax(slmax);
                list.add(order);
            }

        cur.close();
        db.close();
        return list;
    }


    public String getString(String text) {
        return "\"" + text + "\"";
    }

    public static void print(String text) {
        Log.d("userinfo", text);
    }

    public boolean addOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id", order.getId());
        cv.put("name", order.getName());
        cv.put("price", order.getPrice());
        cv.put("amount", order.getAmount());
        cv.put("image", order.getImage());
        cv.put("slmax", order.getSlmax());


        long insert = db.insert(TABLE_CART, null, cv);

        print("Insert Order: " + order + "\t" + result((int) insert));
        db.close();
        return insert > 0;
    }

    public boolean updateCart(int id, Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id", order.getId());
        cv.put("name", order.getName());
        cv.put("price", order.getPrice());
        cv.put("amount", order.getAmount());
        cv.put("image", order.getImage());
        cv.put("slmax", order.getSlmax());


        long update = db.update(TABLE_CART, cv, "id=" + id, null);
        print("Update Order: " + order + "\t" + result((int) update));
        db.close();
        return update > 0;
    }

    public void updateUnit(int id, int soluong) {
        String sql = String.format("Update %s Set amount = amount+%s where id = %s", TABLE_CART,
                soluong, id);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
        print(String.format("Update don hang %s: %s", id, soluong));
        db.close();
    }

    public boolean contain(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(id) from " + TABLE_CART + " where id=" + id, null);
        int amount = cursor.getCount();
        cursor.close();
        ;
        db.close();
        return amount > 0;
    }


    public boolean deleleOrder(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int del = 0;
        if (id == DEL_ALL) {
            db.execSQL("delete from " + TABLE_CART);
            del = 1000;
        } else
            del = db.delete(TABLE_CART, "id=" + id, null);

        db.close();
        print("Delete Order:  " + id + "\t" + result(del));
        return del > 0;
    }

    public boolean deleteOrders(List<Order> ids) {
        SQLiteDatabase db = getWritableDatabase();
        int del = 0;

        for (Order item : ids)
            del += db.delete(TABLE_CART, "id=" + item.getId(), null);
        db.close();
        print("Delete Order:  " + del + "\t" + result(del));
        return del > 0;
    }

    public static String result(int index) {
        if (index > 0)
            return "Success";
        else
            return "Faile";
    }
}
