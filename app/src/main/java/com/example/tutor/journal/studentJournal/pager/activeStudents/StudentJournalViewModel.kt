package com.example.tutor.journal.studentJournal.pager.activeStudents

import androidx.lifecycle.*
import com.example.tutor.bd.entities.StudentEntity
import com.example.tutor.bd.entities.StudentForSchedule
import com.example.tutor.fireBase.FireBaseRepository
import com.example.tutor.repository.StudentRepository

class StudentJournalViewModel(
    private val repository: StudentRepository,
    private val fbRepository: FireBaseRepository,
) : ViewModel() {

    val allActiveStudents: LiveData<List<StudentEntity>> = repository.allActiveStudents.asLiveData()
    fun getInfo(): LiveData<MutableList<StudentForSchedule>> {
        return repository.infoForSchedule
    }

    fun changeStudentActive(studentEntity: StudentEntity) {
        repository.changeStudentActive(studentEntity.id)
        fbRepository.changeStudentActiveToFireBase(studentEntity, false)
    }

    //Получение списка всех студентов из FB. Не используется. Оставлена для примера
/*    fun getStudentsFromFB()= liveData(Dispatchers.IO){
        fbRepository.getStudents().collect{ resource ->
            emit(resource)
        }
    }*/

}

class StudentJournalViewModelFactory(
    private val repository: StudentRepository,
    private val fbRepository: FireBaseRepository,
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentJournalViewModel::class.java)) {
            //тестовая аннотация для обнаружения ошибок. Означает, что тестовый метод не будет включен в набор тестов
            @Suppress("UNCHECKED_CAST")
            return StudentJournalViewModel(repository, fbRepository) as T
        }
        throw IllegalArgumentException("Unknown VM")
    }
}
