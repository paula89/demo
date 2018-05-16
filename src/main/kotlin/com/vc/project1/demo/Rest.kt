package com.vc.project1.demo

import io.vavr.control.Either
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class CustomerController(val repository: CustomerRepository) {
    var logger: Logger = LoggerFactory.getLogger(DemoKotlinApplication::class.java)


    @GetMapping("/")
    fun findAll() : HttpEntity<Result> {
        val result = searchAllPerson()
        return when (result) {
            is Result.Success -> ResponseEntity<Result>(result, HttpStatus.OK)
            else -> ResponseEntity<Result>(result, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    fun searchAllPerson(): Result{
        var infoPerson = ""
        return try {
            val person = repository.findAll()
            if (person.isEmpty()) {
                Result.Error("There are not persons information")
            }else {
                for (p in person){
                  infoPerson  += p.toString().trimMargin()
                }
                Result.Success(infoPerson)
            }
        }
        catch (e: Exception) {
            Result.Error(e.message ?: "Some error occurred")
        }
    }

    /* TODO("""
            What if findAll returns null respond with 200 and descriptive message like "Not Found"?
            What if findAll throws exception?
            What if findAll returns too many (Pagination)?
            """)

*/

    @GetMapping("/{lastName}")
    fun findByName(@PathVariable lastName: String) : HttpEntity<Result> {
        val result = searchPersonOrError(lastName)
        return when (result) {
            is Result.Success -> ResponseEntity<Result>(result, HttpStatus.OK)
            else -> ResponseEntity<Result>(result, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    fun searchPersonOrError(lastName: String): Result{
        return try {
             val person = repository.findByLastName(lastName)
             if (person.isEmpty()) {
                 Result.Error("The person with lastName: $lastName is not found ")
             }else {
                 Result.Success(person[0].toString())}
            }
        catch (e: Exception) {
             Result.Error(e.message ?: "Some error occurred")
        }
    }
}

/*
        TODO("""
            What if findByLastName returns null respond with 200 and descriptive message like "Not Found"?
            What if findByLastName throws exception?
            """)

*/


/*   JDBC *****************

    @Component
    class Add10toAge(val personFinderJDBC: PersonFinderJDBC) {

        fun add10ToPerson(person: Person): Result {
            if (person.age == 0) return Result.Error("Wrong age!")
            return personFinderJDBC.apply(person)
                    .map { add10OrGetError(it, person) }
                    .getOrElse { Result.Error("Not found!") }
        }

        private fun add10OrGetError(personResult: Result, person: Person): Result {
            return personResult
                    .toEither()
                    .fold({
                        it
                    }, {
                        Result.Success(person.copy(age = person.age + 10))
                    })
        }
    }

 */

/*
class Rest(val add10toAge: Add10toAge) {

    @PostMapping("/personaPost")
    fun personAgePlus10(@RequestBody person: Person): HttpEntity<Result> {
        val result = add10toAge.add10ToPerson(person)
        return when (result) {
            is Result.Success -> ResponseEntity<Result>(result, HttpStatus.OK)
            else -> ResponseEntity<Result>(result, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }


    @GetMapping("/personaGet")
    fun doGet(person: Person) = add10toAge.add10ToPerson(person)

    @RequestMapping("/")
    fun helloSpringBoot() = "Hello SpringBoot"

}

@Component
class Add10toAge(val personFinderJDBC: PersonFinderJDBC) {

    fun add10ToPerson(person: Person): Result {
        if (person.age == 0) return Result.Error("Wrong age!")
        return personFinderJDBC.apply(person)
                .map { add10OrGetError(it, person) }
                .getOrElse { Result.Error("Not found!") }
    }

    private fun add10OrGetError(personResult: Result, person: Person): Result {
        return personResult
                .toEither()
                .fold({
                    it
                }, {
                    Result.Success(person.copy(age = person.age + 10))
                })
    }
}

@Component
class PersonFinderJDBC(val jdbcTemplate: JdbcTemplate) : Function<Person, Option<Result>> {
    override fun apply(person: Person): Option<Result> {
        return try {
            val queryResult = jdbcTemplate.query("select * from person p where p.name = ? and p.lastname = ?",
                    arrayOf(person.name, person.lastname),
                    { resultSet, i ->
                        val name = resultSet.getString("name")
                        val lastname = resultSet.getString("lastname")
                        val age = resultSet.getInt("age")
                        Person(name, lastname, age)
                    })
            if (queryResult.size == 0)
                Option.none<Result>()
            else
                Option.of(Result.Success(queryResult[0]))
        } catch (e: Exception) {
            Option.of(Result.Error(e.message ?: "Some error I don't know"))
        }

    }

}*/



sealed class Result {
    abstract fun toEither(): Either<Error, Success>

    class Success(val person: String) : Result() {
        override fun toEither(): Either<Error, Success> = Either.right(this)
    }

    class Error(val errorMessage: String) : Result() {
        override fun toEither(): Either<Error, Success> = Either.left(this)
    }
}
