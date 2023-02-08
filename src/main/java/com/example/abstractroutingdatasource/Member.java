package com.example.abstractroutingdatasource;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "tb_user")
@Getter
@Setter
public class Member {

    @Id
    String id;
    String userName;

    String address;
}
