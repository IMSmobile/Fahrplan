package io.github.imsmobile.fahrplan.output;

import android.content.Context;

import ch.schoeb.opendatatransport.model.Connection;
import io.github.imsmobile.fahrplan.R;
import io.github.imsmobile.fahrplan.setting.Setting;

/**
 * Created by sandro on 25.02.2017.
 */

public class Occupancy {

    private final Context context;

    public Occupancy(Context context) {
        this.context = context;
    }

    public String buildOccupancy(Connection connection) {
        Setting setting = new Setting(context);
        StringBuilder occupancy = new StringBuilder();
        if((connection.getCapacity1st() != null) && setting.getBooleanSettings(R.string.setting_classes_first, true)) {
            occupancy.append(" 1.");
            occupancy.append((char) (getLowOccupanyIcon() + connection.getCapacity1st().intValue()*2));
        }
        if((connection.getCapacity2nd() != null) && setting.getBooleanSettings(R.string.setting_classes_second, true)) {
            occupancy.append(" 2.");
            occupancy.append((char) (getLowOccupanyIcon() + connection.getCapacity2nd().intValue()*2));
        }
        return occupancy.toString();
    }


    private char getLowOccupanyIcon() {
        return context.getString(R.string.icon_occupancy).charAt(0);
    }
}
