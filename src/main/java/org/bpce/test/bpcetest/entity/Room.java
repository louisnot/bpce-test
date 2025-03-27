package org.bpce.test.bpcetest.entity;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int capacity;
    private boolean ecran;
    private boolean pieuvre;
    private boolean webcam;
    private boolean tableau;

    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private List<Meeting> meetings;
}
