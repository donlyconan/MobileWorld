package com.team.mobileworld.core.handle;

import com.team.mobileworld.core.object.ItemList;
import com.team.mobileworld.core.object.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ItemTest {

    public static List<String> getUrlImage()
    {
        return Arrays.asList("https://www.google.com/search?q=Laptop&rlz=1C1CHBF_enVN883VN883&sxsrf=ALeKk01shRxI-Y8z9dUDgPfTMfIQE4vs0g:1586816610828&source=lnms&tbm=isch&sa=X&ved=2ahUKEwi60aXDuOboAhWCd94KHdaqAM4Q_AUoAXoECA4QAw&biw=1366&bih=667#imgrc=Ei10mrNZzjsa1M"
        ,"https://www.google.com/search?q=Laptop&rlz=1C1CHBF_enVN883VN883&sxsrf=ALeKk01shRxI-Y8z9dUDgPfTMfIQE4vs0g:1586816610828&source=lnms&tbm=isch&sa=X&ved=2ahUKEwi60aXDuOboAhWCd94KHdaqAM4Q_AUoAXoECA4QAw&biw=1366&bih=667#imgrc=DZOVRkWwXzODVM"
        ,"https://www.google.com/search?q=Laptop&rlz=1C1CHBF_enVN883VN883&sxsrf=ALeKk01shRxI-Y8z9dUDgPfTMfIQE4vs0g:1586816610828&source=lnms&tbm=isch&sa=X&ved=2ahUKEwi60aXDuOboAhWCd94KHdaqAM4Q_AUoAXoECA4QAw&biw=1366&bih=667#imgrc=Q1NMCF9Qv4nugM"
        ,"https://www.google.com/search?q=Laptop&rlz=1C1CHBF_enVN883VN883&sxsrf=ALeKk01shRxI-Y8z9dUDgPfTMfIQE4vs0g:1586816610828&source=lnms&tbm=isch&sa=X&ved=2ahUKEwi60aXDuOboAhWCd94KHdaqAM4Q_AUoAXoECA4QAw&biw=1366&bih=667#imgrc=Ojgrallof482tM"
        ,"https://www.google.com/search?q=Laptop&rlz=1C1CHBF_enVN883VN883&sxsrf=ALeKk01shRxI-Y8z9dUDgPfTMfIQE4vs0g:1586816610828&source=lnms&tbm=isch&sa=X&ved=2ahUKEwi60aXDuOboAhWCd94KHdaqAM4Q_AUoAXoECA4QAw&biw=1366&bih=667#imgrc=WkBFQWrwfLIIQM"
        ,"https://www.google.com/search?q=Laptop&rlz=1C1CHBF_enVN883VN883&sxsrf=ALeKk01shRxI-Y8z9dUDgPfTMfIQE4vs0g:1586816610828&source=lnms&tbm=isch&sa=X&ved=2ahUKEwi60aXDuOboAhWCd94KHdaqAM4Q_AUoAXoECA4QAw&biw=1366&bih=667#imgrc=0vRBGtHIRtMF9M"
        ,"https://www.google.com/search?q=dien+thoai&tbm=isch&ved=2ahUKEwjL2qLquOboAhURAaYKHet3AwAQ2-cCegQIABAA&oq=dien+thoai&gs_lcp=CgNpbWcQAzIECAAQQzICCAAyAggAMgIIADICCAAyAggAMgIIADICCAAyAggAMgIIAFCSEViOH2CuIWgAcAB4AIABfIgBvAeSAQM4LjKYAQCgAQGqAQtnd3Mtd2l6LWltZw&sclient=img&ei=tOaUXsuwIpGCmAXr7w0&bih=667&biw=1366&rlz=1C1CHBF_enVN883VN883#imgrc=V-92MQ5dIAkOiM",
                "https://www.google.com/search?q=dien+thoai&tbm=isch&ved=2ahUKEwjL2qLquOboAhURAaYKHet3AwAQ2-cCegQIABAA&oq=dien+thoai&gs_lcp=CgNpbWcQAzIECAAQQzICCAAyAggAMgIIADICCAAyAggAMgIIADICCAAyAggAMgIIAFCSEViOH2CuIWgAcAB4AIABfIgBvAeSAQM4LjKYAQCgAQGqAQtnd3Mtd2l6LWltZw&sclient=img&ei=tOaUXsuwIpGCmAXr7w0&bih=667&biw=1366&rlz=1C1CHBF_enVN883VN883#imgrc=4pHouGIOuN7R-M"
        ,"https://www.google.com/search?q=dien+thoai&tbm=isch&ved=2ahUKEwjL2qLquOboAhURAaYKHet3AwAQ2-cCegQIABAA&oq=dien+thoai&gs_lcp=CgNpbWcQAzIECAAQQzICCAAyAggAMgIIADICCAAyAggAMgIIADICCAAyAggAMgIIAFCSEViOH2CuIWgAcAB4AIABfIgBvAeSAQM4LjKYAQCgAQGqAQtnd3Mtd2l6LWltZw&sclient=img&ei=tOaUXsuwIpGCmAXr7w0&bih=667&biw=1366&rlz=1C1CHBF_enVN883VN883#imgrc=ns32nVf4-ArGoM");
    }

    public static List<String> getProductName()
    {
        return Arrays.asList(
                "Product x80001 Mode 41", "Laptop Dell 5440", "Laptop Dell 7710", "Iphone 7 Plus"
                ,"Iphone XS Max","IPhone XI R Mode 4704 (Like New)"
        );
    }

    public static List<ItemList> getItemList()
    {
        final String[] title = new String[]{"Sản phẩm mới", "Sản phẩm Nổi bật", "Danh sách mặt hàng"};
        Random rd = new Random();
        List<String> names = getProductName();
        List<String> urls = getUrlImage();
        List<ItemList> lists = new ArrayList<>();

        for(int i = 0; i <= 2; i++)
        {
            final List<Product> products = new ArrayList<>();

            for (int j = 0; j <= 19; j++)
            {
                String name = names.get(rd.nextInt(names.size()));
                String url = urls.get(rd.nextInt(urls.size()));
                Product product = getProduct(i * 20 + j + 100000, rd, name, url);
                products.add(product);
            }
            ItemList itemList = new ItemList(title[i].toUpperCase(), products, i);

            lists.add(itemList);
        }

        return lists;
    }

    public static Product getProduct(int id,Random rd, String name, String url)
    {
        Product product = new Product(id, name
        , (int) (rd.nextLong() + 5000000), url, "No decription", rd.nextInt(2), rd.nextInt(100) + 10);
        return product;
    }
}
