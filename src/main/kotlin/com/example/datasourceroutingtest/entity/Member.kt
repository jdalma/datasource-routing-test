package com.example.datasourceroutingtest.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

@Entity
@Table(name = "members")
class Member (
    @Id
    @Column(nullable = false)
    var id: Int,

    @Column(nullable = false)
    val name: String
) {
    override fun toString(): String {
        return "Member(id=$id, name='$name')"
    }
}
