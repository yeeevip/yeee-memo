package vip.yeee.memo.demo.netty.heartcheck.common.entity;

/**
 * description......
 * @author https://www.yeee.vip
 */
public class MonitorVo {

    private String clientId;

    private String dateList;

    private Long valueList;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getDateList() {
        return dateList;
    }

    public void setDateList(String dateList) {
        this.dateList = dateList;
    }


    public Long getValueList() {
        return valueList;
    }

    public void setValueList(Long valueList) {
        this.valueList = valueList;
    }

    @Override
    public String toString() {
        return "MonitorVo{" +
                "clientId='" + clientId + '\'' +
                ", dateList='" + dateList + '\'' +
                ", valueList=" + valueList +
                '}';
    }
}
