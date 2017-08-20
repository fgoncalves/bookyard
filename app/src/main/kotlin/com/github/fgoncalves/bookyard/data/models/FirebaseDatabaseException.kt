package com.github.fgoncalves.bookyard.data.models

import com.google.firebase.database.DatabaseError

data class FirebaseDatabaseException(val error: DatabaseError) : Exception()
