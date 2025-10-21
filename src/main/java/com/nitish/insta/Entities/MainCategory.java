package com.nitish.insta.Entities;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MainCategory {
    @Id
    private Integer id;
    private String name;
}
