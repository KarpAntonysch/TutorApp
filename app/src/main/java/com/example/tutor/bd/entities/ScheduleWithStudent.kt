package com.example.tutor.bd.entities

import androidx.room.Embedded
import androidx.room.Relation

data class ScheduleWithStudent(
    @Embedded val scheduleEntity: ScheduleEntity,
    @Relation(
        parentColumn ="studentId",
        entityColumn = "id"
    )
    val ListOfStudentEntity: StudentEntity
    )