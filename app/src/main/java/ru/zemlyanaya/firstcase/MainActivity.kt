package ru.zemlyanaya.firstcase

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.absoluteValue


class MainActivity : AppCompatActivity(), IMainContract {

    private lateinit var alertDialog: AlertDialog
    var mainViewModel = MainViewModel(this)

    private val convert: (String) -> Long = { s: String -> s.toLong().absoluteValue}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        butCompute.setOnClickListener{
            hideKeyboard()
            try{
                val numbers = numbersText.text!!.split(" ")
                    .map(convert)
                    .toCollection(ArrayList())
                if(numbers.size < 2)
                    throw java.lang.Exception()
                mainViewModel.numbers = numbers
                mainViewModel.compute()
            }
            catch (e: Exception){
                showError("Invalid input!")
            }
        }
    }

    override fun showComputing() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(this.layoutInflater.inflate(R.layout.computing_dialog, null))
        alertDialog = builder.create()
        alertDialog.show()
    }

    override fun showResult(res: List<String>) {
        alertDialog.dismiss()
        gcdText.text = res[0]
        lcmText.text = res[1]
    }

    override fun showError(message: String) {
        Snackbar.make(root_layout, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun hideKeyboard() {
        val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = this.currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}

interface IMainContract{
    fun showComputing()

    fun showResult(res: List<String>)

    fun showError(message: String)
}
