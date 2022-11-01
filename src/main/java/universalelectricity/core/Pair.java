package universalelectricity.core;

public class Pair<L, R> {
    private final L left;
    private final R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getKey() {
        return this.left;
    }

    public R getValue() {
        return this.right;
    }

    public int hashCode() {
        return this.left.hashCode() ^ this.right.hashCode();
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair<Object, Object> pairo = (Pair<Object, Object>)o;
        return this.left.equals(pairo.getKey()) && this.right.equals(pairo.getValue());
    }
}
