package org.temqua.databaseService.dataSets;

import java.sql.Timestamp;

public class LogDataSet {
    private Long id;
    private Timestamp requestTime;
    private Timestamp responseTime;
    private String fromLang;
    private String toLang;
    private String translatingWord;
    private String yandexResponse;
    private String statusMessage;
    private String ipAddress;

    public LogDataSet(Long id, Timestamp requestTime, Timestamp responseTime, String fromLang, String toLang, String translatingWord, String yandexResponse, String statusMessage, String ipAddress) {
        this.id = id;
        this.requestTime = requestTime;
        this.responseTime = responseTime;
        this.fromLang = fromLang;
        this.toLang = toLang;
        this.translatingWord = translatingWord;
        this.yandexResponse = yandexResponse;
        this.statusMessage = statusMessage;
        this.ipAddress = ipAddress;
    }

    public long getId() {
        return id;
    }

    public Timestamp getRequestTime() {
        return requestTime;
    }

    public String getFromLang() {
        return fromLang;
    }

    public String getToLang() {
        return toLang;
    }

    public String getTranslatingWord() {
        return translatingWord;
    }

    public String getYandexResponse() {
        return yandexResponse;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public Timestamp getResponseTime() {
        return responseTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public String toString() {
        return id +
                ", requestTime=" + requestTime +
                ", responseTime=" + responseTime +
                ", fromLang='" + fromLang + '\'' +
                ", toLang='" + toLang + '\'' +
                ", translatingWord='" + translatingWord + '\'' +
                ", yandexResponse='" + yandexResponse + '\'' +
                ", statusMessage='" + statusMessage + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                '}';
    }
}
