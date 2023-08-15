package com.demo.auth.util;

public class Client2 {

    public static class RegExpNodeBase {

        // 当前项是否匹配0次
        boolean optional;
        // 当前项是否匹配超过1次
        boolean multiple;

    }

    public static class RegExpCharSet {
        char from;
        char to;
    }

    public static class RegExpGroup {
        RegExpNodeBase[] sequence;
    }

    public static class RegExpSelection {
        RegExpNodeBase[] selection;
    }

}
