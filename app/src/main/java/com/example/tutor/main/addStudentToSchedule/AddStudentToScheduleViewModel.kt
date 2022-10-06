package com.example.tutor.main.addStudentToSchedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tutor.bd.entities.ScheduleEntity
import com.example.tutor.bd.entities.StudentForSchedule
import com.example.tutor.bd.entities.StudentForSpinnerModel
import com.example.tutor.repository.ScheduleRepository
import com.example.tutor.toSpinnerModel
import kotlinx.coroutines.launch

class AddStudentToScheduleViewModel(private val repository: ScheduleRepository) : ViewModel() {
    var studentID:Int? = null
    // получение из List<StudentForSchedule> List<StudentForSpinnerModel>
    fun getNewList(infoList: MutableList<StudentForSchedule>): List<StudentForSpinnerModel>{
        return infoList.map { item ->item.toSpinnerModel() }
    }
    fun searchID(list:List<StudentForSpinnerModel>):Int{
        return list.indices.find{list[it] == list.first{o ->o.id == studentID}}!!
    }

    fun insert(scheduleEntity: ScheduleEntity) = viewModelScope.launch {
        repository.insertSchedule(scheduleEntity)
    }

}
// эта конструкция необходима для того, что б проинициализировать VM в фрагменте с передачей в
// конструктор ссылки на репозиторий
class AddStudentToScheduleViewModelFactory(private val repository: ScheduleRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddStudentToScheduleViewModel::class.java)) {
            //тестовая аннотация для обнаружения ошибок. Означает, что тестовый метод не будет включен в набор тестов
            @Suppress("UNCHECKED_CAST")
            return AddStudentToScheduleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown VM")
    }
}
