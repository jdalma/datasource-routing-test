package com.example.datasourceroutingtest.controller

import com.example.datasourceroutingtest.service.MemberService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/members")
class MemberController(
    private val memberService: MemberService
) {

    @GetMapping
    fun findAll() = memberService.findAllSecondary()
}
