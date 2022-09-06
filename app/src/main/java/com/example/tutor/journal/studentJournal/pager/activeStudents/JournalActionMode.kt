package com.example.tutor.journal.studentJournal.pager.activeStudents

import android.util.Log
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.MenuRes
import com.example.tutor.R
import com.example.tutor.bd.entities.StudentEntity
import com.example.tutor.journal.studentJournal.pager.activeStudents.StudentJournalFragment.*


class JournalActionModeCallback(
    private val studentEntity: StudentEntity) : ActionMode.Callback {
    private var mode: ActionMode? = null
    var actionModeListener: ActionModeListener? = null // определяем переменную с типом
    // ActionModeListener как поле класса благодаря этому получаю доступ к функциям интерфейса.
    // Но интерфейс это абстракция, а нам нужна его конкретная реализация, котрая задается в функции startActionMode
    @MenuRes
    private var menuResId: Int = 0 //т.к. это многоразовый класс, то меню привязывается в фрагменте

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        this.mode = mode
        mode.menuInflater?.inflate(menuResId, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        return false
    }

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuDelete -> {
                actionModeListener?.clickToMenuDelete(studentEntity)
                true
            }
            R.id.menuEdit -> {
                actionModeListener?.clickToMenuEdit(studentEntity)
                mode.finish() //обеспечивает закрытие AM сразу после нажатия айтема меню
                true
            }
            R.id.menuWatch ->{
                actionModeListener?.clickToMenuWatch(studentEntity)
                mode.finish()
                true
            }
            else -> false
        }
    }

    override fun onDestroyActionMode(mode: ActionMode) {
        this.mode = null

    }

    fun startActionMode(// функция, которая запускает actionMode. Т.е. именно она позволяет
        // пользователю достучаться до всех его методов. Поэтому здесь происходит инициализация actionModeListener
        view: View,
        @MenuRes menuResId: Int,
        listener: ActionModeListener,// параметр, который принимает конкретную реализацию интерфейса.
        // Интерфейс реализуется в фрагменте. Путем инициализации функции clickToMenuDelete
    ) {
        this.menuResId = menuResId
        actionModeListener =
            listener // инициализируем поле класса конкретной реализацией интерфейса
        view.startActionMode(this)
    }

    // интерфейс с методом для иницилизации нажатия на кнопку меню delete : вызов  функций DialogFragment из
    //StudentJournalFragment.
    interface ActionModeListener {
        fun clickToMenuDelete(studentEntity: StudentEntity)
        fun clickToMenuEdit(studentEntity: StudentEntity)
        fun clickToMenuWatch(studentEntity: StudentEntity) {
        }
    }

    fun hideActionMode(){
        mode?.finish()
    }
}
