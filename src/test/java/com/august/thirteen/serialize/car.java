package com.august.thirteen.serialize;

import java.io.Serializable;

//集成Serializable才能被序列化
public class car implements Serializable {
    private static final long serialVersionUID = 1L;//该字段必须是静态 (static)、最终 (final) 的 long 型字段
    private static String name="herCar";//static关键字修饰不被序列化
    private String color;
    private String addtip;
    private transient String brand;//transient关键字修饰不被序列化

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        car.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getAddtip() {
        return addtip;
    }

    public void setAddtip(String addtip) {
        this.addtip = addtip;
    }

    @Override
    public String toString() {
        return "car{" +
                "color='" + color + '\'' +
                ", brand='" + brand + '\'' +
                ", name='" + name + '\'' +
                ", addtip='" + addtip + '\'' +
                '}';
    }
}
