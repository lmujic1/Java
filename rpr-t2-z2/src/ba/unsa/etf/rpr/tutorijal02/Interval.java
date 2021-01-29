package ba.unsa.etf.rpr.tutorijal02;

import java.util.Locale;

public class Interval {
    private double pocetna, krajnja;
    private boolean p1, p2;

    public Interval(double pocetna, double krajnja, boolean p1, boolean p2) {
        if (pocetna > krajnja) throw new IllegalArgumentException("Pocetna tacka veca od krajnje.");
        this.pocetna = pocetna;
        this.krajnja = krajnja;
        this.p1 = p1;
        this.p2 = p2;
    }

    public Interval() {
        this.pocetna = 0;
        this.krajnja = 0;
        this.p1 = false;
        this.p2 = false;
    }

    public boolean isNull() {
        if (this.pocetna == 0 && this.krajnja == 0 && !this.p1 && !this.p2) return true;
        return false;
    }

    public boolean isIn(double tacka) {
        if (this.p1 && this.p2 && tacka >= this.pocetna && tacka <= this.krajnja) return true;
        else if (this.p1 && !this.p2 && tacka >= this.pocetna && tacka < this.krajnja) return true;
        else if (!this.p1 && this.p2 && tacka > this.pocetna && tacka <= this.krajnja) return true;
        else if (!this.p1 && !this.p2 && tacka > this.pocetna && tacka < this.krajnja) return true;
        return false;
    }

    public Interval intersect(Interval interval) {
        double t1, t2;
        boolean k1, k2;
        if (this.pocetna >= interval.pocetna) {
            t1 = this.pocetna;
            k1 = this.p1;
        } else {
            t1 = interval.pocetna;
            k1 = interval.p1;
        }
        if (this.krajnja <= interval.krajnja) {
            t2 = this.krajnja;
            k2 = this.p2;
        } else {
            t2 = interval.krajnja;
            k2 = interval.p2;
        }
        return new Interval(t1, t2, k1, k2);
    }

    public static Interval intersect(Interval interval1, Interval interval2) {
        double t1, t2;
        boolean k1, k2;
        if (interval1.pocetna >= interval2.pocetna) {
            t1 = interval1.pocetna;
            k1 = interval1.p1;
        } else {
            t1 = interval2.pocetna;
            k1 = interval2.p1;
        }
        if (interval1.krajnja <= interval2.krajnja) {
            t2 = interval1.krajnja;
            k2 = interval1.p2;
        } else {
            t2 = interval2.krajnja;
            k2 = interval2.p2;
        }
        return new Interval(t1, t2, k1, k2);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && getClass() == obj.getClass()) {
            Interval i = (Interval) obj;
            return pocetna == i.pocetna && krajnja == i.krajnja && p1 == i.p1 && p2 == i.p2;
        }
        return false;
    }

    @Override
    public String toString() {
        if (p1 && p2) {
            return String.format(Locale.US, "[%.1f,%.1f]", pocetna, krajnja);
        } else if (p1 && !p2) {
            return String.format(Locale.US, "[%.1f,%.1f)", pocetna, krajnja);
        } else if (!p1 && p2) {
            return String.format(Locale.US, "(%.1f,%.1f]", pocetna, krajnja);
        } else if (!p1 || !p2) {
            if (pocetna != 0 && krajnja != 0) {
                return String.format(Locale.US, "(%.1f,%.1f)", pocetna, krajnja);
            }
        }
        return "()";
    }
}
