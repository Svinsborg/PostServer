package ru.hell.server.exception

import java.lang.RuntimeException

class PasswordChangeException(message: String): RuntimeException(message) {
}