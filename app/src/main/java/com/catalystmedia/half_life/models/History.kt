package com.catalystmedia.half_life.models

class History {

    private var date: String = ""
    private var day: String = ""
    private var goalCompleted: Boolean = false
    private var mood: String = ""
    private var timeMeditated: String = ""

    constructor()
    constructor(date: String, day: String, goalCompleted: Boolean, mood: String, timeMeditated: String)
    {
        this.date = date
        this.day = day
        this.goalCompleted = goalCompleted
        this.mood = mood
        this.timeMeditated = timeMeditated

    }
    fun getDate():String
    {
        return date
    }
    fun setDate(date: String)
    {
        this.date = date
    }
    fun getDay():String
    {
    return day
    }
    fun setDay(day :String)
    {
        this.day = day
    }
    fun getGoalCompleted():Boolean
    {
        return goalCompleted
    }
    fun setGoalCompleted(goalCompleted: Boolean)
    {
        this.goalCompleted = goalCompleted
    }
    fun getMood():String
    {
        return mood
    }
    fun setMood(mood: String)
    {
        this.mood = mood
    }
    fun getTimeMeditated():String
    {
        return timeMeditated
    }
    fun setTimeMeditated(timeMeditated: String)
    {
        this.timeMeditated = timeMeditated
    }

}