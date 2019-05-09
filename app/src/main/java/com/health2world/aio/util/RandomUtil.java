package com.health2world.aio.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * Created by lishiyou on 2018/10/23 0023.
 */

public class RandomUtil {

    public static int nextInt(int min, int max) {
        return new Random().nextInt(max) % (max - min + 1) + min;
    }

    public static float nextFloat(int scale, float min,float max) {
        float d = min + ((max - min) * new Random().nextFloat());
        BigDecimal bg = new BigDecimal(d).setScale(scale, RoundingMode.HALF_UP);
        return bg.floatValue() > 0f ? bg.floatValue() : 0.0f;
    }
}
