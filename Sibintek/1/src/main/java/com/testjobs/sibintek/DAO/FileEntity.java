package com.testjobs.sibintek.DAO;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Table(name = "files", schema = "sibintek", catalog = "postgres")
public class FileEntity {

    private int id;
    private String description;
    private String autor;
    private Timestamp date = Timestamp.valueOf(LocalDateTime.now().minusDays((long) (Math.random() * 0)));

    public FileEntity() {

    }

    public FileEntity(String description, String autor) {
        this.description = description;
        this.autor = autor;
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
    
    @Basic
    @Column(name = "description", nullable = true, length = -1)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @Basic
    @Column(name = "autor", nullable = true, length = -1)
    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    @Basic
    @Column(name = "date", nullable = true)
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
      
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileEntity that = (FileEntity) o;
        return
                id == that.id &&
                        Objects.equals(description, that.description) &&
                        Objects.equals(autor, that.autor) &&
                        Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, autor, date, id);
    }

    public String time() {
        return date.toLocalDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public LocalDate localDate() {
        return date.toLocalDateTime().toLocalDate();
    }


}
