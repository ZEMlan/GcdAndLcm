package ru.zemlyanaya.firstcase

import android.annotation.SuppressLint
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MainViewModel(private val iMainContract: IMainContract){
    var numbers: ArrayList<Long> = arrayListOf(2, 4)
    private var gcd: Long = 0
    private var lcm: Long = 1


    @SuppressLint("CheckResult")
    fun compute(){
        iMainContract.showComputing()
        Completable.create{
            gcd = gcd(numbers[0], numbers[1])
            lcm = lcm(numbers[0], numbers[1])
            for (i: Int in 2 until numbers.size) {
                gcd = gcd(gcd, numbers[i])
                lcm = lcm(lcm, numbers[i])
            }
            it.onComplete()
        }.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError{
                iMainContract.showError("Error while computing, please, try again")
            }
            .subscribe {
                iMainContract.showResult(listOf(gcd.toString(),  setLcd()))
            }
    }

    private fun gcd(a: Long, b: Long): Long{
        return if(b <= 0)
            a
        else
            gcd(b, a%b)
    }

    private fun lcm(a:Long, b:Long):Long = a*b/gcd(a,b)

    private fun setLcd():String = if(lcm <0) "Too big number" else lcm.toString()
}