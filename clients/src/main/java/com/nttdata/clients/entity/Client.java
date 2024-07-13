package com.nttdata.clients.entity;

import com.nttdata.clients.model.Person;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "usuarios")
public class Client extends Person {

    @Column(name = "password")
    private String password;

    @Column(name = "estado")
    private Integer estado;
}
