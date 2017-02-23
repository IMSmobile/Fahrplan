package io.github.imsmobile.fahrplan.model;

import com.google.common.base.Objects;

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
    public boolean equals(Object obj) {
        return obj instanceof FavoriteModelItem && Objects.equal(((FavoriteModelItem) obj).getFrom().toLowerCase(), from.toLowerCase()) && Objects.equal(((FavoriteModelItem) obj).getTo().toLowerCase(), to.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(to.toLowerCase(), from.toLowerCase());
    }
}
