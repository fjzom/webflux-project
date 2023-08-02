package com.webflux.project.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("clients")
public class Client {

    public static final String INSERT_INTO_SQL = "INSERT INTO testdb.clients (name) VALUES ('%s')";
    @Id
    private Long id;
    @JsonProperty("name")
    private String name;

    public static String buildInsertQuery(Client client){
//       return clientList.stream().map(c-> String.format(INSERT_INTO_SQL, c.getName())).toString();
       return  String.format(INSERT_INTO_SQL, client.getName());

    }
}
