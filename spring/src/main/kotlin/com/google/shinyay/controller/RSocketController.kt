package com.google.shinyay.controller

import com.google.shinyay.logger
import com.google.shinyay.model.Message
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller

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
}