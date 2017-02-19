package ch.schoeb.opendatatransport;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.schoeb.opendatatransport.model.ConnectionList;
import ch.schoeb.opendatatransport.model.ConnectionQuery;
import ch.schoeb.opendatatransport.model.StationList;

public class OnlineOpenTransportRepository implements IOpenTransportRepository {
    @Override
    public StationList findStations(String query) throws OpenDataTransportException {
        String url = buildFindStationsUrl(query);
        String json = GetJson(url);

        Gson gson = new Gson();
        return gson.fromJson(json, StationList.class);
    }

    private String buildFindStationsUrl(String query) {
        String url = null;
        try {
            url = "http://transport.opendata.ch/v1/locations?query=" + URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }

        return url;
    }

    @Override
    public ConnectionList searchConnections(String from, String to) throws OpenDataTransportException {
        return searchConnections(from, to, null, null, null, false);
    }

    public ConnectionList searchConnections(String from, String to, String via, String date, String time, Boolean isArrivalTime) throws OpenDataTransportException {
        String url = buildSearchConnectionUrl(from, to, via, date, time, Collections.<String>emptyList(), isArrivalTime);
        String json = GetJson(url);

        Gson gson = new Gson();
        return gson.fromJson(json, ConnectionList.class);
    }

    public ConnectionList searchConnections(String from, String to, String via, String date, String time,List<String> transportations, Boolean isArrivalTime) throws OpenDataTransportException {
        String url = buildSearchConnectionUrl(from, to, via, date, time,transportations, isArrivalTime);
        String json = GetJson(url);

        Gson gson = new Gson();
        return gson.fromJson(json, ConnectionList.class);
    }

    @Override
    public ConnectionList searchConnections(ConnectionQuery query) throws OpenDataTransportException {
        return searchConnections(query.getFrom(), query.getTo(), null, query.getDate(), query.getTime(), getTransporations(query), query.isArrivalTime());
    }

    private List<String> getTransporations(ConnectionQuery query) {
        List<String> transportations = new ArrayList<>();
        if (query.isTrain()) {
            transportations.add("ice_tgv_rj");
            transportations.add("ec_ic");
            transportations.add("ir");
            transportations.add("re_d");
            transportations.add("s_sn_r");
            transportations.add("cableway");
            transportations.add("arz_ext");
        }
        if (query.isTram()) {
            transportations.add("tramway_underground");
        }
        if (query.isBus()) {
            transportations.add("bus");

        }
        if (query.isShip()) {
            transportations.add("ship");
        }
        return transportations;
    }

    private String GetJson(String url) throws OpenDataTransportException {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine = "";
            StringBuilder sb = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new OpenDataTransportException("Could not connect to Open Transport API");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String buildSearchConnectionUrl(String from, String to, String via, String date, String time, List<String> transportations,  Boolean isArrivalTime) {
        String url = null;
        try {
            url = "http://transport.opendata.ch/v1/connections?from=" + URLEncoder.encode(from, "UTF-8") + "&to=" + URLEncoder.encode(to, "UTF-8");

            if (via != null && via != "") {
                url += "&via[]=" + via;
            }

            if (date != null && date != "") {
                url += "&date=" + date;
            }

            if (time != null && time != "") {
                url += "&time=" + time;
            }

            if (isArrivalTime) {
                url += "&isArrivalTime=1";
            }
            if (!transportations.isEmpty()) {
                url += "&" + Joiner.on("&").join(Lists.transform(transportations, transportationParamSuffix));
            }

        } catch (UnsupportedEncodingException e) {

        }
        return url;
    }

    private Function<String, String> transportationParamSuffix = new Function<String, String>() {
        public String apply(String transportation) {
            return "transportations[]=" + transportation;
        }
    };
}
