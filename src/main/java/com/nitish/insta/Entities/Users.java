package com.nitish.insta.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    @Column(name="user_name",nullable = false,length = 50)
    private String userName;
    private String email;
    private String password;
    private String fullName;
    private Set<Integer> followers=new HashSet<>();
    private Set<Integer> following=new HashSet<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user_role",joinColumns = @JoinColumn(name="user",referencedColumnName = "userId"),inverseJoinColumns = @JoinColumn(name = "role",referencedColumnName = "roleId"))
    private Set<Role> roles=new HashSet<>();
}
