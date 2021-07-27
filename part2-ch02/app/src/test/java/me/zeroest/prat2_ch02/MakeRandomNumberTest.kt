package me.zeroest.prat2_ch02

import junit.framework.TestCase

class MakeRandomNumberTest : TestCase() {

    fun testGetRandomNumber() {

        val randomNumber = MakeRandomNumber.getRandomNumber()

        println("randomNumber = ${randomNumber}")

        assertEquals(6, randomNumber.size)

    }
}