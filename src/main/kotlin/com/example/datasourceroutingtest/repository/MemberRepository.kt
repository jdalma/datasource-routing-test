package com.example.datasourceroutingtest.repository

import com.example.datasourceroutingtest.entity.Member
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.lang.RuntimeException
import javax.sql.DataSource

@Repository
class MemberRepository (dataSource: DataSource) {
    private val template: JdbcTemplate = JdbcTemplate(dataSource)
    private val rowMapper: RowMapper<Member> = RowMapper<Member> { rs, _ ->
        Member(rs.getInt("id"), rs.getString("name"))
    }
    fun findById(id: Int): Member {
        val sql = "select id, name from members where id = ?"
        return template.queryForObject(sql, rowMapper, id)
                ?: throw RuntimeException("조회 결과가 존재하지 않습니다. Member id = $id")
    }

    fun findAll(): List<Member> {
        val sql = "select id, name from members"
        return template.query(sql, rowMapper)
    }
}
