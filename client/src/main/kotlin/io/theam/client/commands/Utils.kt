package io.theam.client.commands

import com.fasterxml.jackson.databind.ObjectMapper

inline infix fun Any.printWith(om: ObjectMapper) =
        println(om.writeValueAsString(this))