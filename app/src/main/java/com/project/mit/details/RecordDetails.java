package com.project.mit.details;

public class RecordDetails {
    public String RecordID,
                    UserID,
                    LocationID,
                    LocationName,
                    LocationFullAddress,
                    RiskStatus,
                    ZoneStatus,
                    CreatedDateTime;

    public RecordDetails(String recordID, String userID,
                         String locationID,
                         String locationName,
                         String locationFullAddress,
                         String riskStatus,
                         String zoneStatus,
                         String createdDateTime) {
        RecordID = recordID;
        UserID = userID;
        LocationID = locationID;
        LocationName = locationName;
        LocationFullAddress = locationFullAddress;
        RiskStatus = riskStatus;
        ZoneStatus = zoneStatus;
        CreatedDateTime = createdDateTime;
    }

    public String getRecordID() {
        return RecordID;
    }

    public void setRecordID(String recordID) {
        RecordID = recordID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getLocationID() {
        return LocationID;
    }

    public void setLocationID(String locationID) {
        LocationID = locationID;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public String getLocationFullAddress() {
        return LocationFullAddress;
    }

    public void setLocationFullAddress(String locationFullAddress) {
        LocationFullAddress = locationFullAddress;
    }

    public String getRiskStatus() {
        return RiskStatus;
    }

    public void setRiskStatus(String riskStatus) {
        RiskStatus = riskStatus;
    }

    public String getZoneStatus() {
        return ZoneStatus;
    }

    public void setZoneStatus(String zoneStatus) {
        ZoneStatus = zoneStatus;
    }

    public String getCreatedDateTime() {
        return CreatedDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        CreatedDateTime = createdDateTime;
    }
}
