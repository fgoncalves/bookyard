package com.github.fgoncalves.bookyard.domain.usecases

import com.github.fgoncalves.bookyard.data.UserService
import com.github.fgoncalves.bookyard.data.models.User
import io.reactivex.Single
import javax.inject.Inject

interface GetOrCreateUserUseCase {
    /**
     * Get the user in the real time database. This user object only needs the uuid set if you're
     * sure it exists in the database. If not, all attributes will be used to store it.
     */
    fun getOrCreateUser(user: User): Single<User>
}

class GetOrCreateUserUseCaseImpl @Inject constructor(
        private val userService: UserService
) : GetOrCreateUserUseCase {
    override fun getOrCreateUser(user: User): Single<User>
            = userService.get(user.uuid)
            .switchIfEmpty(userService.createOrUpdate(user).toMaybe())
            .flatMap { return@flatMap userService.get(user.uuid) }
            .toSingle()
}
