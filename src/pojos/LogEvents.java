/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

/**
 *
 * @author sajid.ali
 */
public class LogEvents {
     private int eventId;
    private String eventType;
    private int eventSeverity;

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setEventSeverity(int eventSeverity) {
        this.eventSeverity = eventSeverity;
    }

    public void setEventMessage(String eventMessage) {
        this.eventMessage = eventMessage;
    }

    public void setEventTime(int eventTime) {
        this.eventTime = eventTime;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
    private String eventMessage;
    private int eventTime;
    private Integer userId;
    private String hostName;
}
