package org.jails.util;

public class Tuple<A,B> {
    A value0;
    B value1;

    public Tuple(A value0, B value1) {
        this.value0 = value0;
        this.value1 = value1;
    }

    public A get0() {
        return value0;
    }

    public B get1() {
        return value1;
    }

    public void set0(A value0) {
        this.value0 = value0;
    }

    public void set1(B value1) {
        this.value1 = value1;
    }
}
