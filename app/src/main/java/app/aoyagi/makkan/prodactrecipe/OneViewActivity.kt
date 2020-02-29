package app.aoyagi.makkan.prodactrecipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_oneview.*

class OneViewActivity : AppCompatActivity() {

    private val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oneview)
        supportActionBar?.hide()
        val item = RealmInfo()
        val id = intent.getStringExtra("id")
        val title = intent.getStringExtra("title")
        val means = intent.getStringExtra("means")
        val check = intent.getBooleanExtra("check", false)
        item.id = id

        toolbar3.title = title
        toolbar3.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar3.setNavigationOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
            finish()
        }
        toolbar3.inflateMenu(R.menu.toolbar_menu)
        toolbar3.setOnMenuItemClickListener {
            if (it.itemId == R.id.delete_icon) {
                delete(item.id)
                val intent = Intent(this, ListActivity::class.java)
                startActivity(intent)
                finish()
            }
            true
        }

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_icon -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
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
