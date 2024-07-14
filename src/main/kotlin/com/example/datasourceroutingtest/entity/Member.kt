package com.example.datasourceroutingtest.entity

data class Member (
    val id: Int,
    val name: String
) {
    override fun toString(): String {
        return "Member(id=$id, name='$name')"
    }
}
