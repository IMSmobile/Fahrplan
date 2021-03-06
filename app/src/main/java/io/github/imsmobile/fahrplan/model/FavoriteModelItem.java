package io.github.imsmobile.fahrplan.model;

public class FavoriteModelItem {
    private final String from;
    private final String to;

    FavoriteModelItem(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @Override
    public String toString() {
        return from + " \u279C " + to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FavoriteModelItem that = (FavoriteModelItem) o;

        if (from != null ? !from.equalsIgnoreCase(that.from) : that.from != null) return false;
        return to != null ? to.equalsIgnoreCase(that.to) : that.to == null;

    }

    @Override
    public int hashCode() {
        int result = from != null ? from.toLowerCase().hashCode() : 0;
        result = 31 * result + (to != null ? to.toLowerCase().hashCode() : 0);
        return result;
    }
}
