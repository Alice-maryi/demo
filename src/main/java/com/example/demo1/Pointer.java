package com.example.demo1;

import org.junit.Test;

import java.util.Arrays;

/**
 * @Author: Y.C
 * @Date: 2021/6/24 3:08 下午
 */
public class Pointer {

    @Test
    public void test() {
        int[] arr = {1,2,3,4,5};
        int i = maxi(arr, 11);
        System.out.println(i);
        System.out.println(i);
        System.out.println(i);

    }

    static int maxi(int[] nums, int target) {
        int slow = 0, fast = 0;
        int sum = 0, min = nums.length + 1;
        while (fast < nums.length) {
            sum += nums[fast];
            if (sum >= target) {
                while (true) {
                    if (sum - nums[slow] >= target) sum -= nums[slow++]; else break;
                }
                min = Math.min(min, fast - slow + 1);
            }
            fast++;
        }

        if (min < nums.length + 1) return min;
        else return min;
    }
}
