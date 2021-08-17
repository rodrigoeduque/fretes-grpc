package br.com.rodrigoeduque

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("br.com.rodrigoeduque")
		.start()
}

