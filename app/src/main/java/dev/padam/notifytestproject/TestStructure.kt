package dev.padam.notifytestproject

/**
 * Created by p.adam on 22/2/2018.
 */
data class TestStructure(val activityName: String,
                         val originalTitle: String)

class ChangeTitleEvent(val newTitle : String)

class RestoreTitleEvent()