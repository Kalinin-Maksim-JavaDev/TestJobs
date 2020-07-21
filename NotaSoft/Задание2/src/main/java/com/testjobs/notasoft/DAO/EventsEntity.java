package com.testjobs.notasoft.DAO;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Table(name = "events", schema = "public", catalog = "postgres")
public class EventsEntity {
    private String name;

    private Timestamp date = Timestamp.valueOf(LocalDateTime.now().minusDays((long) (Math.random() * 0)));
    private int typeclassid;
    private int id;

    private EventclassesEntity eventclass;

    public EventsEntity() {

    }

    public EventsEntity(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "name", nullable = true, length = -1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "date", nullable = true)
    public Timestamp getDate() {
        return date;
    }


    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Basic
    @Column(name = "typeclassid", nullable = false)
    public int getTypeclassid() {
        return typeclassid;
    }

    public void setTypeclassid(int typeclassid) {
        this.typeclassid = typeclassid;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "typeclassid", nullable = false, insertable = false, updatable = false)
    public EventclassesEntity getEventclass() {
        return eventclass;
    }

    public void setEventclass(EventclassesEntity clazz) {
        this.eventclass = clazz;
        this.typeclassid = clazz.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventsEntity that = (EventsEntity) o;
        return
                id == that.id &&
                        Objects.equals(name, that.name) &&
                        Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, date, id);
    }

    public String time() {
        return date.toLocalDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public LocalDate localDate() {
        return date.toLocalDateTime().toLocalDate();
    }


}
