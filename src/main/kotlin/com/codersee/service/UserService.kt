package com.codersee.service

import com.codersee.model.User
import com.codersee.repository.UserRepository

class UserService(
    private val userRepository: UserRepository
) {

    fun findAll(): List<User> =
        userRepository.findAll()

    fun findById(id: Int): User? =
        userRepository.findById(id = id)

    fun findByUsername(username: String): User? =
        userRepository.findByUsername(username)

    fun save(user: User): User? {
//        val foundUser = userRepository.findByUsername(user.username)
//        return if (foundUser == null) {
        userRepository.save(user)
        return user
//        } else null
    }
}
