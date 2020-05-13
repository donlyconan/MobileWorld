package com.team.mobileworld.core.handle;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Cache;
import com.squareup.picasso.Picasso;
import com.team.mobileworld.R;
import com.team.mobileworld.core.NetworkCommon;
import com.team.mobileworld.core.object.Message;
import com.team.mobileworld.core.object.Order;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Handler {
    public static int findByIdMS(int id, List<Message> list)
    {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id) return i;
        }
        return -1;
    }

    public static void addListMS(List<Message> list, List<Message> items)
    {
        for (Message item:items) {
            int i = findByIdMS(item.getId(), list);
            if(i != 0)
                list.set(i, item);
            else
                list.add(0,item);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static Notification createNotificationChannel(
            Activity activity
            , PendingIntent intent
            , String title
            , String content) {

        Notification builder = new Notification.Builder(activity)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(content)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_notifications, "Open", intent)
                .build();
        return builder;
    }

    /**
     * Tìm kiếm id của một sản phẩm có trong danh sách
     */
    public static int findById(int id, List<Order> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id) return i;
        }
        return -1;
    }

    /**
     * Đầu vào là 2 danh sách sản phẩm: list1, list2
     * Tìm một danh sách chứa những sản phẩm trùng nhau của 2 danh sách đã cho
     */
    public static List<Order> intersect(List<Order> biglist, List<Order> smalllist){
       List<Order> list = new ArrayList<>(smalllist.size());

       for (Order item : biglist){
           int index = findById(item.getId(), smalllist);

           if(index > -1)
               list.add(item);
       }

       return list;
    }

    /**
     * Thêm sản phẩm vào danh sách dựa trên id sản phẩm đó
     */
    public static void addItem(List<Order> list, Order order) {
        int index = findById(order.getId(), list);
        if (index > -1) {
            Order item = list.get(index);
            item.setAmount(item.getAmount() + order.getAmount());
        } else
            list.add(order);
    }

    /**
     * Tính tổng số tiền của một danh sách convert dạng string
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getFormatTotalMoneySelected(List<Order> list) {
        Long sum = getTotalMoneySelected(list);
        return formatMoney(sum);
    }

    /**
     * Lay tong so san pham
     */

    public static int totalSize(List<Order> list) {
        int sum = 0;
        for (Order item : list) sum += item.getAmount();
        return sum;
    }

    /**
     * Tính tổng số tiền có trong danh sách sản phẩm
     *
     * @param list
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Long getTotalMoney(List<Order> list) {
        LongStream stream = list.stream().mapToLong(Order::getTotalMoney);
        long sum = stream.sum();
        return sum;
    }

    /**
     * Tính tổng số lượng hàng hóa
     *
     * @param list
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int getTotalAmount(List<Order> list) {
        IntStream stream = list.stream().mapToInt(Order::getAmount);
        int sum = stream.sum();
        return sum;
    }


    /**
     * Lấy tổng số sản phầm được chọn
     *
     * @param list
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Order> getProductSeleted(List<Order> list) {
        List<Order> newList = new ArrayList<Order>(100);
        list.stream().filter(Order::isSelect).forEach(newList::add);
        return newList;
    }

    /**
     * Lấy tổng giá tiền của những sản phẩm được chọn
     *
     * @param list
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static long getTotalMoneySelected(List<Order> list) {
        Stream<Order> stream = list.stream().filter(Order::isSelect);
        return stream.mapToLong(Order::getTotalMoney).sum();
    }

    /***
     * Sửa đổi kiểu format
     */
    public static String formatMoney(Long money) {
        DecimalFormat format = new DecimalFormat("###,###,###");
        return String.format("%s đ",format.format(money));
    }

    /**
     * Chuyen doi Gson String về JSON
     */
    public static JsonObject convertToJSon(String jstring) {
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parse(jstring).getAsJsonObject();
    }


    /**
     * Load image from url
     */
    public static void loadImage(Activity activity, String url, final ImageView img) {
        Picasso.Builder builder = new Picasso.Builder(activity);

        builder.build().load(url).noFade()
                .placeholder(R.drawable.no_image_icon)
                .fit()
                .centerInside()
                .into(img);
    }

}
