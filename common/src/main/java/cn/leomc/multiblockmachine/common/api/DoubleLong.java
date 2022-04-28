package cn.leomc.multiblockmachine.common.api;


public class DoubleLong {

    public long longValue = 0;
    public double doubleValue = 0;

    public static DoubleLong of(long longValue) {
        DoubleLong doubleLong = new DoubleLong();
        doubleLong.longValue = longValue;
        doubleLong.doubleValue = longValue;
        return doubleLong;
    }


    public static DoubleLong of(double doubleValue) {
        DoubleLong doubleLong = new DoubleLong();
        doubleLong.doubleValue = doubleValue;
        doubleLong.longValue = (long) doubleValue;
        return doubleLong;
    }


    public static DoubleLong of(DoubleLong doubleLongValue) {
        DoubleLong doubleLong = new DoubleLong();
        doubleLong.doubleValue = doubleLongValue.doubleValue;
        doubleLong.longValue = doubleLongValue.longValue;
        return doubleLong;
    }

    public void add(double value) {
        this.doubleValue += value;
        this.longValue += value;
    }

    public void add(long value) {
        this.doubleValue += value;
        this.longValue += value;
    }

    public void add(DoubleLong value) {
        this.doubleValue += value.doubleValue;
        this.longValue += value.longValue;
    }

    public void subtract(DoubleLong value) {
        this.doubleValue -= value.doubleValue;
        this.longValue -= value.longValue;
    }

}
