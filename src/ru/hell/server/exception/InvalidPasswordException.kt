package ru.hell.server.exception

import java.lang.RuntimeException

class InvalidPasswordException (message: String) : RuntimeException(message){
}