package pl.finsys.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ADDRESS")
public class Address implements java.io.Serializable {

    private int id;
    private String ip;
    private Set<Log> logs = new HashSet<>(0);

    public Address() {
    }

    public Address(String ip) {
        this.ip = ip;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ADDRESS_ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "address")
    public Set<Log> getLogs() {
        return logs;
    }

    public void setLogs(Set<Log> logs) {
        this.logs = logs;
    }

    @Column(name = "IP")
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    //    @ManyToMany(cascade = {CascadeType.ALL}, mappedBy = "ipAddresses")
//    private List<StatusDescription> statusDescriptions = new ArrayList<>();
//
//    public List<StatusDescription> getStatusDescriptions() {
//        return statusDescriptions;
//    }
//
//    public void setStatusDescriptions(List<StatusDescription> statusDescriptions) {
//        this.statusDescriptions = statusDescriptions;
//    }
//



}
