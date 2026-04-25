package com.example.bolalarakademiyasi.entity;

import com.example.bolalarakademiyasi.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Where(clause = "active = true")
public class Student extends BaseEntity {

    @Column(name = "first_name",  nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private Integer age;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String password;

    private String imgUrl;

    private int coin;

    @ManyToOne
    private User parent;

    @ManyToOne
    private Class sinf;

}
