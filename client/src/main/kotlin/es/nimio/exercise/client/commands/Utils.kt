package es.nimio.exercise.client.commands

import com.fasterxml.jackson.databind.ObjectMapper

infix fun Any.printWith(om: ObjectMapper) =
        println(om.writeValueAsString(this))