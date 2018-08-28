package com.lhy.comm;

import com.alibaba.fastjson.JSON;

import org.junit.Test;

import java.util.List;

/**
 * Created by luohy on 2018/8/27.
 */
public class ExampleTest2 {
    @Test
    public void testFastJson() {
        System.out.println("\n一、json字符串与java对象互转------------------\n");
        //一、json字符串与java对象互转
        String json = "{\n" +
                "    \"id\": 2,\n" +
                "    \"imgPath\": \"http://9.jpg\",\n" +
                "    \"name\": \"猴哥\",\n" +
                "    \"price\": 12.3\n" +
                "}";
        //解析json
        shopInfo shopInfo = JSON.parseObject(json, shopInfo.class);
        System.out.println("json原字符串：\n" + json);
        System.out.println("\n转换后对象：\n" + shopInfo.toString());
        //生成json数据
        String s = JSON.toJSONString(shopInfo);
        System.out.println("\n对象转json字符串：\n" + s);


        System.out.println("\n二、json数组与java对象的集合互转------------------\n");
        //二、json数组与java对象的集合互转
        json = "[\n" +
                "    {\n" +
                "        \"id\": 1,\n" +
                "        \"imgPath\": \"http://img9.jpg\",\n" +
                "        \"name\": \"猴哥\",\n" +
                "        \"price\": 12.3\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": 2,\n" +
                "        \"imgPath\": \"http://img99.jpg\",\n" +
                "        \"name\": \"八戒\",\n" +
                "        \"price\": 63.2\n" +
                "    }\n" +
                "]";
        //解析json
        List<shopInfo> shopInfos = JSON.parseArray(json, shopInfo.class);
        System.out.println("\n转换前json原串：\n" + json);
        System.out.println("\n转换后对象数组：\n" + shopInfos.toString());

        //生成json数据
        s = JSON.toJSONString(shopInfos);
        System.out.println("\n转换后json：\n" + s);
    }

    public static class shopInfo {
        private int id;
        private String imgPath;
        private String name;
        private double price;

        public shopInfo(int id, String imgPath, String name, double price) {
            this.id = id;
            this.imgPath = imgPath;
            this.name = name;
            this.price = price;
        }

        public shopInfo() {
        }

        @Override
        public String toString() {
            return "shopInfo{" +
                    "id=" + id +
                    ", imgPath='" + imgPath + '\'' +
                    ", name='" + name + '\'' +
                    ", price=" + price +
                    '}';
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }
}
