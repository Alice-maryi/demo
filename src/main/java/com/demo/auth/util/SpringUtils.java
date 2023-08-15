package com.demo.auth.util;

import java.util.Map;

public class SpringUtils {

    public static void main(String[] args) {

    }

    public static class State {
        private static int idCnt = 0;
        private int id;
        private int stateType;

        public State() {
            this.id = idCnt++;
        }
    }


}
