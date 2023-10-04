package es.ucm.fdi.ici.c2122.practica4.grupo05.fuzzy;

public class PillStatus implements Comparable<PillStatus> {
    private int node;
    private boolean available;

    public PillStatus(int node, boolean available) {
        this.setNode(node);
        this.setAvailable(available);
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getNode() {
        return node;
    }

    public void setNode(int node) {
        this.node = node;
    }

    @Override
    public int compareTo(PillStatus o) {
        if (available != o.available)
            return Boolean.compare(available, o.available);
        else
            return Integer.compare(node, o.node);
    }
}
