package ch.schoeb.opendatatransport.model;

public class ConnectionQuery {
    private String from;
    private String to;
    private boolean arrivalTime;
    private String date;
    private String time;
    private boolean train;
    private boolean tram;
    private boolean bus;
    private boolean ship;
    private int page;
    private int limit = 6;

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    public void setArrivalTime(boolean arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public boolean isArrivalTime() {
        return arrivalTime;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTrain(boolean train) {
        this.train = train;
    }

    public boolean isTrain() {
        return train;
    }

    public void setTram(boolean tram) {
        this.tram = tram;
    }

    public boolean isTram() {
        return tram;
    }

    public void setBus(boolean bus) {
        this.bus = bus;
    }

    public boolean isBus() {
        return bus;
    }

    public void setShip(boolean ship) {
        this.ship = ship;
    }

    public boolean isShip() {
        return ship;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }
}
