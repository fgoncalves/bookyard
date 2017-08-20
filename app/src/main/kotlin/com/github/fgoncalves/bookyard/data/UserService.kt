package com.github.fgoncalves.bookyard.data

import com.github.fgoncalves.bookyard.data.models.User
import com.github.fgoncalves.bookyard.di.qualifiers.UsersDatabase
import com.google.firebase.database.DatabaseReference
import io.reactivex.Completable
import javax.inject.Inject

interface UserService {
  fun createOrUpdate(user: User): Completable
}

class UserServiceImpl @Inject constructor(
    @UsersDatabase val userDatabaseReference: DatabaseReference
) : UserService {
  override fun createOrUpdate(user: User): Completable
      = Completable.fromCallable {
    val userDatabaseReference = userDatabaseReference.child(user.uuid)
    userDatabaseReference.setValue(user)
  }
}
