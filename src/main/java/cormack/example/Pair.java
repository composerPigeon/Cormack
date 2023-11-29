package cormack.example;

public class Pair<A, B> {
    private final A _item1;
    private final B _item2;
    public Pair(A first, B second) {
        _item1 = first;
        _item2 = second;
    }

    public A getFirst() {
        return _item1;
    }

    public B getSecond() {
        return _item2;
    }

}
