package io.github.imsmobile.fahrplan.output;

import android.content.Context;

import ch.schoeb.opendatatransport.model.Journey;
import io.github.imsmobile.fahrplan.R;

public class JourneyPrinter {

    public String getJourneyText(Context context, Journey journey) {
            String category =  journey.getCategory();
            switch(category) {
                case "S":
                    return category + journey.getNumber();
                case "T":
                    return  context.getString(R.string.icon_tram) + journey.getNumber();
                case "BUS":
                case "NFB":
                    return context.getString(R.string.icon_bus) + journey.getNumber();
                default:
                    return category;
            }
    }
}
