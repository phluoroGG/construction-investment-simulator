package edu.csf.persistence.base;

public class TwoLong {
    public Long first;
    public Long second;

    public TwoLong(Long first, Long second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return first + ", " + second;
    }
}
