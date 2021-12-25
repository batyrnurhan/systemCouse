package com.example.systemcouse.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
@Transactional
@Cacheable
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User_ {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "user_name", nullable = false)
    private String userName;

    @NotNull
    @Column(name = "last_session", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate lastSession;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="score_id")
    private Score score;

    public User_() {

    }

    public User_(Long userId, String userName, LocalDate lastSession, Score score) {
        this.userId = userId;
        this.userName = userName;
        this.lastSession = lastSession;
        this.score = score;
    }
}
