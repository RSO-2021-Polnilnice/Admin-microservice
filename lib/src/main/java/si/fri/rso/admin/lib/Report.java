package si.fri.rso.admin.lib;

public class Report {

    private Integer id;
    private Integer ocenaId;
    private Integer userId;
    private String komentar;
    private Double timestamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOcenaId() {
        return ocenaId;
    }

    public void setOcenaId(Integer ocenaId) {
        this.ocenaId = ocenaId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public Double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Double timestamp) {
        this.timestamp = timestamp;
    }
}
