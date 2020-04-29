package com.google.shinyay.controller

import com.google.shinyay.logger
import com.google.shinyay.model.Message
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import java.time.Duration

@Controller
class RSocketController {
    val server = "Server"
    val response = "Response"
    val stream = "Stream"
    val channel = "Channel"

    @MessageMapping("request-response")
    fun requestResponse(request: Message): Message {
        logger.info("Received request-response request: $request")
        return Message(server, response)
    }

    @MessageMapping("fire-and-forget")
    fun findAndForget(request: Message): Unit {
        logger.info("Received fire-and-request: $request")
    }

    @MessageMapping("stream")
    fun stream(request: Message): Flux<Message> {
        logger.info("Received stream request: $request")
        return Flux.interval(Duration.ofSeconds(2))
                .map { index -> Message(server, stream, index) }
                .log()
    }

    @MessageMapping("channel")
    fun channel(config: Flux<Duration>): Flux<Message> =
            config
                    .doOnNext { config -> logger.info("Frequency config is ${config.seconds} seconds.") }
                    .switchMap { config -> Flux.interval(config)
                            .map{index -> Message(server, channel, index)}
                            .log()}
}