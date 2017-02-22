package ch.schoeb.opendatatransport;

import ch.schoeb.opendatatransport.model.ConnectionList;
import ch.schoeb.opendatatransport.model.ConnectionQuery;
import ch.schoeb.opendatatransport.model.StationList;

public interface IOpenTransportRepository {

    StationList findStations(String query) throws OpenDataTransportException;

    ConnectionList searchConnections(String from, String to) throws OpenDataTransportException;

    ConnectionList searchConnections(String from, String to, String via, String date, String time, Boolean isArrivalTime) throws OpenDataTransportException;

    ConnectionList searchConnections(ConnectionQuery query) throws OpenDataTransportException;
}
