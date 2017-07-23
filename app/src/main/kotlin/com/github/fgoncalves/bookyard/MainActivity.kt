package com.github.fgoncalves.bookyard

import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
  val drawer: DrawerLayout by Delegates.notNull()
}
