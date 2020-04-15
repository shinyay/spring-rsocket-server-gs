package com.google.shinyay.model

import java.time.Instant

data class Message(val origin: String,
                   val interaction: String,
                   var index: Long,
                   var createdTime: Long) {
    constructor(origin: String, interaction: String): this(
            origin = origin,
            interaction = interaction,
            index = 0,
            createdTime = Instant.now().epochSecond
    )
    constructor(origin: String, interaction: String, index: Long): this(
            origin = origin,
            interaction = interaction,
            index = index,
            createdTime = Instant.now().epochSecond
    )
}