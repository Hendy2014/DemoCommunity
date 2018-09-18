package com.lhy.comm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by luohy on 2018/8/30.
 */

public class SortTest {
    @Test
    public void testDirectInsertSort() {
        int[] array = getArray();
        directInsertSort(array);
        System.out.println(Arrays.toString(array));
    }

    public int[] getArray(){
        List<Integer> list = getArrayList();
        int[] array = new int[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }

        return array;
    }

    public ArrayList<Integer> getArrayList(){
        ArrayList<Integer> array = new ArrayList<Integer>();

        for (int i = 0; i < 32; i++) {
            array.add(i);
        }

        Collections.shuffle(array);

        return array;
    }

    public void directInsertSort(int[] array) {
        //
        for (int i = 0; i < array.length; i++) {//n次
            for (int j = 0; j < i; j++) {//n/2次
                if(array[i] < array[j]){
                    int temp=array[i];
                    System.arraycopy(array,j,array,j+1,i-j);
                    array[j]=temp;
                }
            }
        }
    }

    @Test
    public void testBubbleSort() {
        int[] array = getArray();
        int i, j;
        int[] a = array;
        int n = a.length;

        //遍历O(n^2) = n * n/2
        //交换, 最坏O(n^2)
        for (i = n - 1; i > 0; i--) {//n次
            for (j = 0; j < i; j++) {//n/2次
                if (a[j] > a[j + 1]) {
                    int tmp = a[j+1];
                    a[j + 1] = a[j];
                    a[j] = tmp;
                }
            }
        }

        System.out.println(Arrays.toString(array));
    }
}
