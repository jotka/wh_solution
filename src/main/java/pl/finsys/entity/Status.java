package pl.finsys.entity;

import pl.finsys.HttpErrorCodes;

import javax.persistence.*;

@Entity
@Table(name = "ADDRESS")
public class Status {

    private int id;
    private int number;
    private String description;

    public Status() {
    }

    public Status(String description) {
        this.description = description;
    }

    public Status(int number) {
        this.setNumber(number);
        this.setDescription(HttpErrorCodes.getErrorCodeDescription(String.valueOf(number)));
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "STATUS_ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "NUMBER")
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
