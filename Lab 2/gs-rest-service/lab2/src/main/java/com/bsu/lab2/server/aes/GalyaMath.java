package com.bsu.lab2.server.aes;

/**
 * Created by Artem Pesetsky
 *
 * @sinse 15.10.2017
 */
public class GalyaMath {
    public static int mul(int a, int b) {
        switch (b) {
            case 0x01:
                return a;
            case 0x02:
                return mulTwo(a);
            case 0x03:
                return mulThree(a);
            case 0x09:
                return mulNine(a);
            case 0x0b:
                return mulB(a);
            case 0x0d:
                return mulD(a);
            case 0x0e:
                return mulE(a);
            default:
                throw new IllegalArgumentException("Error - " + b);
        }
    }

    private static int mulTwo(int a)
    {
        /*if (Integer.compareUnsigned(a, 0x80) < 0) {
            return a << 1;
        } else {
            int tmp = (a << 1) ^ 0x1b;
            if (Integer.compareUnsigned(tmp, 0xff) > 0) {
                return Integer.divideUnsigned(tmp, 0x100);
            }
            return tmp;
        }*/
        return FFmulX2(a);
    }

    private static int mulThree(int a)
    {
        int tmp = mulTwo(a) ^ a;
        if (Integer.compareUnsigned(tmp, 0xff) > 0) {
            return Integer.divideUnsigned(tmp, 0x100);
        }
        return tmp;
    }

    private static int mulNine(int a)
    {
        int tmp = mulTwo(mulTwo(mulTwo(a))) ^ a;
        if (Integer.compareUnsigned(tmp, 0xff) > 0) {
            return Integer.divideUnsigned(tmp, 0x100);
        }
        return tmp;
    }

    private static int mulB(int a)
    {
        int tmp = mulNine(a) ^ mulTwo(a);
        if (Integer.compareUnsigned(tmp, 0xff) > 0) {
            return Integer.divideUnsigned(tmp, 0x100);
        }
        return tmp;
    }

    private static int mulD(int a)
    {
        int tmp = mulNine(a) ^ mulTwo(mulTwo(a));
        if (Integer.compareUnsigned(tmp, 0xff) > 0) {
            return Integer.divideUnsigned(tmp, 0x100);
        }
        return tmp;
    }

    private static int mulE(int a)
    {
        int tmp = mulTwo(mulTwo(mulTwo(a))) ^ mulTwo(mulTwo(a)) ^ mulTwo(a);
        if (Integer.compareUnsigned(tmp, 0xff) > 0) {
            return Integer.divideUnsigned(tmp, 0x100);
        }
        return tmp;
    }

    private static final int m4 = 0xC0C0C0C0;
    private static final int m5 = 0x3f3f3f3f;

    private static int FFmulX2(int x)
    {
        int t0  = (x & m5) << 2;
        int t1  = (x & m4);
        t1 ^= (t1 >>> 1);
        return t0 ^ (t1 >>> 2) ^ (t1 >>> 5);
    }
}
