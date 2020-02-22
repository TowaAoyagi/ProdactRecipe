package app.aoyagi.makkan.prodactrecipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_edit_todo.*
import java.util.*

class EditTodoActivity : AppCompatActivity() {

    private val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_todo)

        supportActionBar?.hide()
        toolbar.title = "Todo"
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)


        val title = intent.getStringExtra("title")
        val means = intent.getStringExtra("means")

        titleText.setText(title)
        contentText.setText(means)

        fab1.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (titleText.text.toString() == "" && contentText.text.toString() == "") {

        } else {
            create(titleText.text.toString(), contentText.text.toString(), false)
        }
        realm.close()
    }


    //    Realmに保存する中身を指定
    fun create(title: String, means: String, check: Boolean) {
        realm.executeTransaction {
            val realmInfo = realm.createObject(RealmInfo::class.java,UUID.randomUUID().toString())
            realmInfo.title = title
            realmInfo.means = means
            realmInfo.check = check
        }
    }
}
