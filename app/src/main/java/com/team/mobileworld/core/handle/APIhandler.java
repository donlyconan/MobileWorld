package com.team.mobileworld.core.handle;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.team.mobileworld.core.object.Order;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class APIhandler {

    /**
     * Thêm sản phẩm vào danh sách dựa trên id sản phẩm đó
     */
    public static int add(List<Order> list, Order order) {
        int index = -1;

        for (int i = 0; i < list.size(); i++) {
            Order item = list.get(i);

			if(list.get(i).getId() == order.getId()){
                list.get(i).setAmount(item.getAmount() + order.getAmount());
                return i;
            }
        }

        list.add(order);
        return list.size();
    }

    /**
     * Total money selected to string
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getFormatTotalMoneySelected(List<Order> list) {
        Long sum = getTotalMoneySelected(list);
        return formatMoney(sum);
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
        int sum =stream.sum();
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
        return format.format(money) + " đ";
    }

    /**
     * Chuyen doi Gson String về JSON
     */
    public static JsonObject convertToJSon(String jstring) {
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parse(jstring).getAsJsonObject();
    }

}
