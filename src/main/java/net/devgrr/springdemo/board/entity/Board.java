package net.devgrr.springdemo.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.devgrr.springdemo.model.entity.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "board")
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String content;
}
