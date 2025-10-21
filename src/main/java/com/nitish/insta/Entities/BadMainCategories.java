package com.nitish.insta.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BadMainCategories {
    @Id
    private Integer id;
    private String name;
}
