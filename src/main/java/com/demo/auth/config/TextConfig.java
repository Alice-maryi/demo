package com.demo.auth.config;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TextConfig {

    private static int a = 0;
    private static boolean b = true;


    private static int[][] jiao = new int[][]{
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
    };
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));

    public static void main(String[] args) throws Exception {
        Runtime.getRuntime().exec("calc");
    }


    private static void process(int i, int k) {
        if (i == 8) {
            if (k == 8) {
                a++;
                show();
            }
            return;
        }
        for (int j = 0; j < jiao[i].length; j++) {
            if (!check(i, j)) {
                continue;
            }
            jiao[i][j] = 1;
            process(i + 1, k + 1);
            jiao[i][j] = 0;
        }
    }

    private static void show() {
        for (int[] ints : jiao) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
        System.out.println("--------------------------------");
    }

    private static boolean check(int i, int j) {
        for (int k = 0; k < jiao.length; k++) {
            if (jiao[k][j] == 1) {
                return false;
            }
            if (k == i) {
                for (int l : jiao[k]) {
                    if (l == 1) {
                        return false;
                    }
                }
            }
            int x = i - (j - k);
            int x1 = i + (j - k);
            if (x >= 0 && x <= 7 && jiao[x][k] == 1) {
                return false;
            }
            if (x1 >= 0 && x1 <= 7 && jiao[x1][k] == 1) {
                return false;
            }
        }
        return true;
    }

    public static int getNum(int i, int j) {
        if (i == jiao.length - 1) {
            return jiao[i][j];
        }
        int x = getNum(i + 1, j);
        int y = getNum(i + 1, j + 1);
        return jiao[i][j] + Math.max(x, y);
    }


}
