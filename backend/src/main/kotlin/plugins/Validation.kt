package eu.karcags.mythscape.plugins

import eu.karcags.mythscape.dtos.campaigns.CampaignEditDTO
import eu.karcags.mythscape.dtos.auth.LoginDTO
import eu.karcags.mythscape.dtos.auth.RegisterDTO
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureValidation() {
    install(RequestValidation) {
        validate<LoginDTO> {
            Validation.start(it)
                .notEmpty("username") { username }
                .notEmpty("password") { password }
                .validate()
        }

        validate<RegisterDTO> {
            Validation.start(it)
                .notEmpty("username") { username }
                .minLength("username", 6) { username }
                .maxLength("username", 24) { username }
                .notEmpty("email") { email }
                .maxLength("username", 120) { email }
                .notEmpty("password") { password }
                .notEmpty("passwordConfirm") { passwordConfirm }
                .minLength("password", 8) { password }
                .maxLength("password", 32) { password }
                .maxLength("fullname", 80) { fullname }
                .rule("Password and confirmation must be the same") {
                    password == passwordConfirm
                }
                .rule("Password must contains capital letters") {
                    password.contains(Regex("[A-Z]"))
                }
                .rule("Password must contains lower letters") {
                    password.contains(Regex("[a-z]"))
                }
                .rule("Password must contains numeric letters") {
                    password.contains(Regex("[0-9]"))
                }
                .rule("Password must contains special characters") {
                    password.contains(Regex("[!@#$%^&*()+=_?]"))
                }
                .validate()
        }

        validate<CampaignEditDTO> {
            Validation.start(it)
                .notEmpty("name") { name }
                .maxLength("name", 40) { name }
                .validate()
        }
    }
}

class Validation<T>(private val entity: T) {
    private val rules: MutableList<Pair<String, T.() -> Boolean>> = mutableListOf()

    companion object {
        fun <T> start(entity: T): Validation<T> {
            return Validation(entity)
        }
    }

    fun rule(message: String, rule: T.() -> Boolean): Validation<T> {
        rules.add(Pair(message, rule))

        return this
    }

    fun notEmpty(name: String, getter: T.() -> String): Validation<T> {
        return rule("Field $name is empty") {
            getter().isNotEmpty() && getter().isNotBlank()
        }
    }

    fun maxLength(name: String, length: Int, getter: T.() -> String): Validation<T> {
        return rule("Field $name maximum length is $length") {
            getter().length <= length
        }
    }

    fun minLength(name: String, length: Int, getter: T.() -> String): Validation<T> {
        return rule("Field $name minimum length is $length") {
            getter().length >= length
        }
    }

    fun <U : Comparable<U>> max(name: String, max: U, getter: T.() -> U): Validation<T> {
        return rule("Field $name maximum value is $max") {
            getter() <= max
        }
    }

    fun <U : Comparable<U>> min(name: String, min: U, getter: T.() -> U): Validation<T> {
        return rule("Field $name minimum value is $min") {
            getter() >= min
        }
    }

    fun <U> required(name: String, getter: T.() -> U?): Validation<T> {
        return rule("Field $name is required") {
            getter() != null
        }
    }

    fun validate(): ValidationResult {
        val errors = rules
            .filter { (_, rule) ->
                !rule(entity)
            }.map { (message, _) ->
                message
            }

        return if (errors.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(errors)
    }
}