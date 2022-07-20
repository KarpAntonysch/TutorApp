package com.example.tutor.journal.studentJournal.pager.activeStudents

import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.MenuRes
import com.example.tutor.R
import com.example.tutor.bd.entities.StudentEntity
import com.example.tutor.journal.StudentJournalViewModel
import com.example.tutor.journal.studentJournal.JourmalDialogFragment


class JournalActionModeCallback(
    private val journalViewModel: StudentJournalViewModel,
    private val studentEntity: StudentEntity,
    val am:AM
) : ActionMode.Callback {
    private var mode: ActionMode? = null
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
                am.amfun(studentEntity)
                //journalViewModel.changeStudentActive(studentEntity.id)
                mode.finish()
                true
            }
            else -> false
        }
    }

    override fun onDestroyActionMode(mode: ActionMode) {
        this.mode = null
    }

    fun startActionMode(
        view: View,
        @MenuRes menuResId: Int,
    ) {
        this.menuResId = menuResId
        view.startActionMode(this)
    }

    interface AM{
        fun amfun(studentEntity: StudentEntity)
    }
}
