package com.august.thirteen.serialize;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

import static org.springframework.util.SerializationUtils.serialize;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SerializeTest {

    @Test
    public void serializableTest() throws Exception{
      /*  car car = new car();
        car.setBrand("audi");
        car.setColor("black");
        serializeObject(car);*/
        car model=(car)deserializeObject();
        System.out.println(model.toString());
    }

    private Object deserializeObject() throws Exception{
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("d:/serializableTest.txt"));
        Object o = inputStream.readObject();
        System.out.println("反序列化成功");
        inputStream.close();
        return o;
    }

    private void serializeObject(Object car) throws Exception {
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("d:/serializableTest.txt"));
        outputStream.writeObject(car);
        System.out.println("序列化成功");
        outputStream.close();
    }
}
