package com.nitish.insta.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "devices")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    private String deviceId;
    private String deviceName;
    private String os;
    private String notificationToken;
    private Date lastActive;
    @ManyToMany(mappedBy = "devices", fetch = FetchType.LAZY)
    private Set<Users> users = new HashSet<>();
}
