package com.juri.TypingGameWorld.Member.handler

import com.juri.TypingGameWorld.Member.domain.Member
import com.juri.TypingGameWorld.Member.service.MemberService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class MemberHandler(private val memberSerivce: MemberService) {
    
    fun join(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(Member::class.java)
            .flatMap(memberSerivce::saveMember)
            .flatMap {
                accepted().build()
            }

    fun login(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(Member::class.java)
            .flatMap(memberSerivce::findMember)
            .flatMap {
                ok().build()
            }.switchIfEmpty(status(HttpStatus.NOT_FOUND).build())

    fun duplicateIdCheck(request: ServerRequest): Mono<ServerResponse> =
        ok().contentType(MediaType.APPLICATION_JSON)
            .body(
                request.pathVariable("id").toMono()
                    .flatMap(memberSerivce::duplicateIdCheck)
            )

}