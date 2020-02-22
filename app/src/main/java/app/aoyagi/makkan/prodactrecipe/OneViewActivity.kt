package app.aoyagi.makkan.prodactrecipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.activity_oneview.*

class OneViewActivity : AppCompatActivity() {

    private val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oneview)
        supportActionBar?.hide()
        toolbar3.title = "Todo"
        val item= RealmInfo()
        val id = intent.getStringExtra("id")
        val title = intent.getStringExtra("title")
        val means = intent.getStringExtra("means")
        val check = intent.getBooleanExtra("check", false)
        item.id = id

        titleView2.text = title
        contentView2.text = means
        checkBox2.isChecked = check

        fab3.setOnClickListener {
            val intent = Intent(this, EditTodoActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("means", means)
            delete(item.id)
            startActivity(intent)
            finish()
        }


    }

    fun delete(id: String) {
        realm.executeTransaction {
            val task = realm.where(RealmInfo::class.java).equalTo("id", id).findFirst()
                ?: return@executeTransaction
            task.deleteFromRealm()
        }
    }


}
