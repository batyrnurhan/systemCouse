package com.example.systemcouse.model;


import lombok.Data;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Transactional
@Cacheable
public class Score {

    public Score() {}

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "score_id", nullable = false)
    private Long scoreId;

    @NotNull
    @Column(name = "score_name", nullable = false)
    private String scoreName;

    public Score(Long scoreId,String scoreName) {
        this.scoreId = scoreId;
        this.scoreName = scoreName;
    }
}
