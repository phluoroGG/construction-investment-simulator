package edu.csf.persistence.base;

public class Relationship<T1, T2> implements IdentityInterface<TwoLong> {
    private TwoLong ids;

    public Relationship(Long first, Long second) {
        ids = new TwoLong(first, second);
    }

    @Override
    public TwoLong getIdentity() {
        return ids;
    }

    @Override
    public void setIdentity(TwoLong identity) {
        ids = identity;
    }
}
