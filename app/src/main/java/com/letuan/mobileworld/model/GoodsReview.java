package com.letuan.mobileworld.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.letuan.mobileworld.activity.OrderActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GoodsReview implements Serializable {
    private String header;
    private List<Product> list;
    private int style;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public GoodsReview(String header, List<Product> list, int style) {
        this.header = header;
        this.list = list;
        this.style = style;
        list.addAll(createItems());
    }

    public GoodsReview() {
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Product> createItems() {
        List<Product> list = new ArrayList<>();
        Product product = new Product(1, "IPhone XI Mode 1823 - new",
                28940000,
                "https://www.google.com/search?q=iphone+x&rlz=1C1CHBF_enVN883VN883&sxsrf=ALeKk00ksDR2-KmISRpOyVSy1VX4BffIeg:15857" +
                        "35321945&source=lnms&tbm=isch&sa=X&ved=2ahUKEwjV2cm0_MboAhX763MBHQJsB9AQ_AUoAXoECAwQAw&biw=1366&bih=667#imgrc=Xw4AA8" +
                        "s2Jf2wxM", "Shop cần bán dòng Iphone X 64G Quốc Tế,đẹp 98-99%,zin,các chức năng đang hoàn hảo.\n" +
                "Máy đã qua sử dụng bên nước ngoài nhập về còn full chức năng.\n" +
                "\n" +
                "Tất cả các máy bán ra bảo hành 3 tháng phần cứng và hỗ trợ bảo hành 12 tháng.\n" +
                "\n" +
                "Khj bạn mua máy với giá trên shopee sẽ được tặng kèm :  \n" +
                "   _  Cóc cáp sạc tai nghe\n" +
                "   _  Miếng dán màn hình cường lực chống trầy xước và va đập.\n" +
                "   _ Ốp lưng dẻo bảo về điện thoại chống trầy xước\n" +
                "\n" +
                "Ship COD (trao máy mới nhận tiền)toàn quốc => Được kiểm tra hàng trước khj trả tiền nên đảm bảo yên tâm chất lượng uy tín.\n" +
                "\n" +
                "Liên hệ zalo: 0826644996 (hỗ trợ mọi thắc mắc hoặc phản hồi về sản phẩm của shop nhé.", 1);
        Random rd = new Random();
        for (int i = 1; i <= 10; i++) {
            product.setId(i);
            list.add(product);

            if (rd.nextInt() % 3 == 0) {
                Order order = new Order(product.getId(), product.getName(), (long) product.getPrice(), product.getImage(), rd.nextInt(9) + 1);
                OrderActivity.addProducts(order);
            }
        }
        return list;
    }


    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<Product> getList() {
        return list;
    }

    public void setList(List<Product> list) {
        this.list = list;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }
}
