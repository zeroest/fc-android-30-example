package me.zeroest.prat2_ch02

object MakeRandomNumber {

    fun getRandomNumber(pickNumberSet: Set<Int>): List<Int> {
        val numberList = mutableListOf<Int>()
            .apply {
                for (i in 1..45) {
                    if (pickNumberSet.contains(i)) {
                        continue
                    }
                    add(i)
                }
                this.shuffle()
            }

        val newList = pickNumberSet.toList() + numberList.subList(0, 6 - pickNumberSet.size)
        return newList.sorted()
    }

}