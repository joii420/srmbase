package com.step.tool.utils;


/**
 * @author : Sun
 * @date : 2022/11/3  13:35
 */
public class JoiiPrint {

    public static final int DEFAULT = 38;
    public static final int BLACK = 30;
    public static final int RED = 31;
    public static final int GREEN = 32;
    public static final int YELLOW = 33;
    public static final int BLUE = 34;
    public static final int PURPLE = 35;
    public static final int CYAN = 36;
    public static final int WHITE = 37;

    public static void main(String[] options) {
        System.out.println("vbbv");
        black("黑色");
        System.out.println("bbb");
        red("红色");
        System.out.println("asda");
//        red("红色", UNDERLINE);
//        System.out.println("vvvv");
        white("白色");
//        purple("紫色", BOLDFACE, 6);
//        System.out.println("avadasd");
//        cyan("青色", BLUE);
        System.out.printf("\033[0%sm%s", 38, "asdasd");
        System.out.printf("\033[0%sm%s", 39, "asdasd");
        System.out.printf("\033[0%sm%s", 40, "asdasd");
        newLine();
    }

    /**
     * 粗体
     */
    private static final String BOLDFACE = "\033[1m";
    /**
     * 下划线
     */
    private static final String UNDERLINE = "\033[4m";
    /**
     * 背景色
     */
    private static final String BJ = "\033[4%sm";

    public static void black(String msg, Object... options) {
        prospectPrint(BLACK, msg, options);
    }

    public static void red(String msg, Object... options) {
        prospectPrint(RED, msg, options);
    }

    public static void green(String msg, Object... options) {
        prospectPrint(GREEN, msg, options);
    }


    public static void yellow(String msg, Object... options) {
        prospectPrint(YELLOW, msg, options);
    }

    public static void blue(String msg, Object... options) {
        prospectPrint(BLUE, msg, options);
    }

    public static void purple(String msg, Object... options) {
        prospectPrint(PURPLE, msg, options);
    }


    public static void cyan(String msg, Object... options) {
        prospectPrint(CYAN, msg, options);
    }

    public static void white(String msg, Object... options) {
        prospectPrint(WHITE, msg, options);
    }

    private static void prospectPrint(int color, String msg, Object... options) {
        if (options == null || options.length == 0 || options.length > 2) {
            options = new String[]{"", ""};
        } else if (options.length == 1) {
            if (options[0] instanceof Integer bj) {
                options = new Object[]{getBj(bj), ""};
            } else if (options[0] instanceof String type) {
                options = new Object[]{"", type};
            } else {
                options = new Object[]{"", ""};
            }
        } else if (options.length == 2) {
            if (options[0] instanceof Integer bj) {
                options[0] = getBj(bj);
            } else if (options[1] instanceof Integer bj) {
                options[1] = getBj(bj);
            }

        }
//        String format = String.format("\033[03%sm%s%s%s%n", color, options[0], options[1], msg);
//        System.out.println(format);
        System.out.printf("\033[0%sm%s%s%s", color, options[0], options[1], msg);
        newLine();
    }

    private static void newLine() {
        System.out.printf("\033[0%sm%n", DEFAULT);
    }

    private static String getBj(Integer bj) {
        return String.format(BJ, bj);
    }

}
