package pl.finsys.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "IP_ADDRESS")
public class IpAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private int id;

    @Column(name = "address")
    private String address;

    public IpAddress(String address) {
        this.address = address;
    }

    @OneToMany(mappedBy="IpAddress")
    private Set<LogEntry> logEntries;

    public Set<LogEntry> getLogEntries() {
        return logEntries;
    }

    public void setLogEntries(Set<LogEntry> logEntries) {
        this.logEntries = logEntries;
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


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
