package app.aoyagi.makkan.prodactrecipe

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class RealmInfo : RealmObject() {
    @PrimaryKey
    open var id: String = UUID.randomUUID().toString()

    open var check: Boolean = false
    open var title: String = ""
    open var means: String = ""
}
