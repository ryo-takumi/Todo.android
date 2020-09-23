package com.example.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.realm.Realm

import kotlinx.android.synthetic.main.activity_edit.*
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity() {

    lateinit var realm: Realm
    var strTime: String = ""
    var strTask: String = ""
    var intposition: Int = 0
    var boolMemorize: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)


        val df = SimpleDateFormat("MM/dd HH:mm")
        val date = Date()
        editTextTime.setText(df.format(date))


        val bundle = intent.extras

        val strStatus = bundle?.getString(getString(R.string.intent_key_status))
        textViewStatus.text = strStatus

        if (strStatus == getString(R.string.status_change)){
            strTime = bundle.getString(getString(R.string.intent_key_time))!!
            strTask = bundle.getString(getString(R.string.intent_key_task))!!
            boolMemorize = bundle.getBoolean(getString(R.string.intent_key_frag))

            editTextTime.setText(strTime)
            editTextTask.setText(strTask)
            checkBox.isChecked = boolMemorize

            intposition = bundle.getInt(getString(R.string.intent_key_position))
        }


        checkBox.setOnClickListener {
            boolMemorize = checkBox.isChecked

        }

        buttonRegister.setOnClickListener {

            if (strStatus == getString(R.string.status_add)) {

                addNewTask()
            } else {
                changeTask()
            }


        }

        buttonBack.setOnClickListener {

            finish()//元の画面に戻る処理
        }
    }

    override fun onResume() {
        super.onResume()
        realm = Realm.getDefaultInstance()
    }
    override fun onPause() {
        super.onPause()
        realm.close()
    }

    private fun addNewTask() {

        realm.beginTransaction()//データベースの使用開始

        val taskDB = realm.createObject(TaskDB::class.java)//データベースに尾新たにデータを追加することを宣言
        taskDB.strTime = this.editTextTime.text.toString()//strTimeに時間の文字列を追加
        taskDB.strTask = editTextTask.text.toString()//strTaskにタスク内容を文字列で追加
        realm.commitTransaction()//データベースの使用を終了する

        editTextTask.setText("")

        Toast.makeText(this@EditActivity,"登録が完了いたしました",Toast.LENGTH_SHORT).show()//トーストを出現

    }

    private fun changeTask() {
        val result = realm.where(TaskDB::class.java).findAll().sort("strTime")
        val selectDB =  result[intposition]!!
        realm.beginTransaction()
        selectDB.strTime = editTextTime.text.toString()
        selectDB.strTask = editTextTask.text.toString()
        selectDB.finishFrag = boolMemorize
        realm.commitTransaction()
        editTextTime.setText("")
        editTextTask.setText("")
        Toast.makeText(this@EditActivity,"修正が完了いたしました",Toast.LENGTH_SHORT).show()
        finish()
    }
}