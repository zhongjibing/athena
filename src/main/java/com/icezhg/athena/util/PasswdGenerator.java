package com.icezhg.athena.util;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by zhongjibing on 2022/12/28.
 */
public class PasswdGenerator {

    private static final char[][] DICT = {
            {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'},
            {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
                    'v', 'w', 'x', 'y', 'z'},
            {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
                    'V', 'W', 'X', 'Y', 'Z'},
            {'`', '@', '#', '%', '^', '&', '_', ';', '?', '-', '=', '+'}
    };

    private static final int CHECK = (1 << DICT.length) - 1;

    private static final String SEED = "hello, world!";

    private static final int DEFAULT_LENGTH = 6;

    private final Random random;

    private final int length;

    public PasswdGenerator() {
        this(DEFAULT_LENGTH);
    }

    public PasswdGenerator(int length) {
        this.length = length;
        this.random = new SecureRandom((SEED + System.currentTimeMillis()).getBytes());
    }

    public String generate() {
        char[] buf = new char[length];
        while (true) {
            int check = 0, pos = 0;
            while (pos < buf.length) {
                int dictIdx = random.nextInt(DICT.length);
                char[] dict = DICT[dictIdx];
                char chr = dict[random.nextInt(dict.length)];
                buf[pos++] = chr;

                check |= 1 << dictIdx;
            }

            if (check == CHECK) {
                break;
            }
        }
        return new String(buf);
    }
}
