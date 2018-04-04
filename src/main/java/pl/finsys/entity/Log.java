package pl.finsys.entity;

import javax.persistence.*;

@Entity
@Table(name = "LOG")
public class Log implements java.io.Serializable {
    private int id;
    private String dateTime;
    private int status;
    private Address address;

    public Log(String format, int status, Address address) {
    }

    public Log(String dateTime, int status) {
        this.dateTime = dateTime;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "LOG_ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADDRESS_ID")
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Column(name = "DATETIME")
    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Column(name = "STATUS")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}