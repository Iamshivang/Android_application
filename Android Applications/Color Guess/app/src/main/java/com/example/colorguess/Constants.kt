package com.example.colorguess

object Constants {
    const val USER_NAME: String= "user_name"
    const val SEQUENCE: String= "sequence"
    const val RESULT: String= "result"

    fun getColor(): ArrayList<colour>{
        val colorList= ArrayList<colour>()

        val violet= colour(3, "#9400D3")
        val indigo= colour(7, "#4B0082")
        val blue= colour(1, "#0000FF")
        val green= colour(5, "#00FF00")
        val yellow= colour(6, "#FFFF00")
        val orange= colour(2, "#FF7F00")
        val red= colour(4, "#FF0000")

        colorList.add(blue)
        colorList.add(orange)
        colorList.add(violet)
        colorList.add(red)
        colorList.add(green)
        colorList.add(yellow)
        colorList.add(indigo)

        return colorList

    }
}