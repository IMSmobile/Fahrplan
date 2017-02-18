package io.github.imsmobile.fahrplan.model;

import org.joda.time.DateTime;

public class DepartureArrivalModel {

    private DateTime selectedDateTime;
    private boolean departure;

    public DepartureArrivalModel() {
        selectedDateTime = DateTime.now();
        departure = true;
    }

    public DepartureArrivalModel(DepartureArrivalModel model) {
        selectedDateTime = model.selectedDateTime;
        departure = model.departure;
    }


    public DateTime getSelectedDateTime() {
        return selectedDateTime;
    }

    public void setSelectedDateTime(DateTime selectedDateTime) {
        this.selectedDateTime = selectedDateTime;
    }

    public boolean isDeparture() {
        return departure;
    }

    public void setDeparture(boolean departure) {
        this.departure = departure;
    }

    public boolean isArrival() {
        return !isDeparture();
    }
}
