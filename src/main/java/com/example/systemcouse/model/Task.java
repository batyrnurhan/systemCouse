package com.example.systemcouse.model;

import lombok.Data;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Transactional
@Cacheable
public class Task {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "task_id", nullable = false)
    private Long taskId;

    @NotNull
    @Column(name = "task_name", nullable = false)
    private String taskName;

    public Task(){}

    public Task(Long taskId,String taskName) {
        this.taskId = taskId;
        this.taskName = taskName;
    }
}
