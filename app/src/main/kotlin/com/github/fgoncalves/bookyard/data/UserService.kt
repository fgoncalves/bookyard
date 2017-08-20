package com.github.fgoncalves.bookyard.data

import com.github.fgoncalves.bookyard.data.models.FirebaseDatabaseException
import com.github.fgoncalves.bookyard.data.models.User
import com.github.fgoncalves.bookyard.di.qualifiers.UsersDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.reactivex.Completable
import io.reactivex.Maybe
import javax.inject.Inject

interface UserService {
  fun get(uuid: String): Maybe<User>

  fun createOrUpdate(user: User): Completable
}

class UserServiceImpl @Inject constructor(
    @UsersDatabase val userDatabaseReference: DatabaseReference
) : UserService {
  override fun get(uuid: String): Maybe<User>
      = Maybe.create {
    if (it.isDisposed) return@create

    userDatabaseReference.child(uuid).addListenerForSingleValueEvent(object : ValueEventListener {
      override fun onCancelled(error: DatabaseError?) {
        if (error == null) {
          it.onError(RuntimeException("Couldn't get user from firebase"))
          return
        }
        it.onError(FirebaseDatabaseException(error))
      }

      override fun onDataChange(snapshot: DataSnapshot?) {
        if (snapshot == null) {
          it.onError(RuntimeException("Snapshot is null"))
          return
        }

        val user = snapshot.getValue(User::class.java)

        if (user != null) it.onSuccess(user)
        it.onComplete()
      }
    })
  }

  override fun createOrUpdate(user: User): Completable
      = Completable.fromCallable {
    val userDatabaseReference = userDatabaseReference.child(user.uuid)
    userDatabaseReference.setValue(user)
  }
}
