package com.vc.project1.demo

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.slf4j.Logger
import org.slf4j.LoggerFactory


@SpringBootApplication
class DemoKotlinApplication{

@Bean
fun init(repository: CustomerRepository) = CommandLineRunner {
    var logger: Logger = LoggerFactory.getLogger(DemoKotlinApplication::class.java)
    repository.save(Customer("1", "Jack", "Bauer", 40))
    repository.save(Customer("2", "Chloe", "Brian", 34))
    logger.warn("Done")
}
}

fun main(args: Array<String>) {
    runApplication<DemoKotlinApplication>(*args)
}







