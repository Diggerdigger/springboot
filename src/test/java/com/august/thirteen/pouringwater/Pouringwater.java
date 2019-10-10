package com.august.thirteen.pouringwater;
/*

8    5     3
8    0     0
5    0     3
5    3     0
2    3     3
2    5     1
7    1     0
4    1     3
4    4     0

*/

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Pouringwater {

    public static int num=0;
    public static int count=0;
    public static final int[] rule={8,5,3};

    @Test
    public void test(){

        Action action = new Action();
        action.bucket[0]=8;
        action.bucket[1]=0;
        action.bucket[2]=0;

        fun(action);

        System.out.println("一共有"+num+"种方法");

    }

    private void fun(Action action) {
        //System.out.println(++count);
        /*for (int[] a:action.rout) {
            if(a[0]==action.bucket[0]&&a[1]==action.bucket[1]){
                return;
            }
        }*/
        for (int[] r : action.rout) {
            if(isequal(r, action.bucket))	//如果两个水桶水相同，即走到了回路，停止
                return;
        }
        {
            int[] add = new int[3];
            for (int p=0; p < action.bucket.length;p++) {
                add[p]=action.bucket[p];
            }
            action.rout.add(add);
        }
        if(action.bucket[0]==4&&action.bucket[1]==4){
            for (int[] a:action.rout) {
                System.out.print(a[0]+":"+a[1]+":"+a[2]+":"+"-->");
            }
            System.out.println("");
            num++;
            return;
        }
        if(canPour(0,1,action)){
            Action tem = new Action();
            copy(tem,action);
            pour(0,1,action);
            fun(action);
            action=tem;
        }
        if(canPour(0,2,action)){
            Action tem = new Action();
            copy(tem,action);
            pour(0,2,action);
            fun(action);
            action=tem;
        }
        if(canPour(1,0,action)){
            Action tem = new Action();
            copy(tem,action);
            pour(1,0,action);
            fun(action);
            action=tem;
        }
        if(canPour(1,2,action)){
            Action tem = new Action();
            copy(tem,action);
            pour(1,2,action);
            fun(action);
            action=tem;
        }
        if(canPour(2,0,action)){
            Action tem = new Action();
            copy(tem,action);
            pour(2,0,action);
            fun(action);
            action=tem;
        }
        if(canPour(2,1,action)){
            Action tem = new Action();
            copy(tem,action);
            pour(2,1,action);
            fun(action);
            action=tem;
        }
    }

    public static boolean isequal(int[] a1 ,int[] a2){
        if(a1.length!=a2.length)
            return false;
        for (int i = 0; i < a2.length; i++) {
            if(a1[i]!=a2[i])
                return false;
        }
        return true;
    }

    public static void pour(int from, int to, Action action) {
        int remain=rule[to]-action.bucket[to];
        if(action.bucket[from]>=remain){
            action.bucket[from]-=remain;
            action.bucket[to]=rule[to];
        }else{
            action.bucket[to]+=action.bucket[from];
            action.bucket[from]=0;
        }
    }
    /*public static void pour(int from,int to,Action action){
        //分两种，多倒少，少倒多
        int remain = rule[to]-action.bucket[to];
        if(action.bucket[from]>=remain){		//是否多倒少
            action.bucket[from]-=remain;
            action.bucket[to]=rule[to];
        }else{	//少倒多情况
            action.bucket[to]+=action.bucket[from];
            action.bucket[from]=0;
        }
    }*/

    /*private void copy(Action tem, Action action) {
        for (int i=0;i<action.bucket.length;i++) {
            tem.bucket[i]=action.bucket[i];
        }
        for (int[] o:action.rout) {
            int[] t=new int[3];
            for(int i=1;i<o.length;i++){
                t[i]=o[i];
            }
            tem.rout.add(t);
        }
    }*/

    public static void copy(Action tem,Action action){
        for (int i = 0; i < action.bucket.length; i++) {
            tem.bucket[i]=action.bucket[i];
        }
        for (int[] r : action.rout) {
            int[] t = new int[3];
            for (int i = 0; i < r.length; i++) {
                t[i]=r[i];
            }
            tem.rout.add(t);
        }
    }

    private boolean canPour(int from, int to, Action action) {
        return from!=to&&action.bucket[from]>0&&action.bucket[to]<rule[to];
    }

    public  class  Action{
        int[] bucket={8,0,0};
        List<int[]> rout=new ArrayList<>();

        @Override
        public String toString() {
            return "Action{" +
                    "bucket=" + Arrays.toString(bucket) +
                    ", rout=" + rout +
                    '}';
        }
    }
}
