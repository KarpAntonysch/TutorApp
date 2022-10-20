package com.example.tutor.journal.studentJournal.pager.activeStudents

import androidx.lifecycle.*
import com.example.tutor.bd.entities.StudentEntity
import com.example.tutor.bd.entities.StudentForSchedule
import com.example.tutor.fireBase.FireBaseRepository
import com.example.tutor.repository.StudentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudentJournalViewModel(private val repository: StudentRepository,
                              private val fbRepository: FireBaseRepository) : ViewModel() {
    //Создаем переменную для получения списка из локальной БД типа LD, которая инициализируется Flow из репозитория
    val allStudents: LiveData<List<StudentEntity>> = repository.allStudents.asLiveData()
    fun getInfo(): LiveData<MutableList<StudentForSchedule>>{
        return repository.infoForSchedule
    }

    fun changeStudentActive(studentID:Int) = repository.changeStudentActive(studentID)

    // функция для добавления данных в локальную БД из FB, при условии пустой лБД
       fun fillingDBWithFB(){
         CoroutineScope(Dispatchers.IO).launch {
             val fbStudentList = fbRepository.fbStudentList()
             if (allStudents.value.isNullOrEmpty() && !fbStudentList.isNullOrEmpty()){
                 repository.insertAllStudent(fbStudentList)
             }
         }
    }

    //Получение списка всех студентов из FB. Не используется. Оставлена для примера
    fun getStudentsFromFB()= liveData(Dispatchers.IO){
        fbRepository.getStudents().collect{ resource ->
            emit(resource)
        }
    }
    fun changeStudentActiveFB(studentEntity: StudentEntity){
        fbRepository.changeStudentActiveToFireBase(studentEntity,false)
    }

}
class StudentJournalViewModelFactory(private val repository: StudentRepository,
                                     private val fbRepository: FireBaseRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentJournalViewModel::class.java)) {
            //тестовая аннотация для обнаружения ошибок. Означает, что тестовый метод не будет включен в набор тестов
            @Suppress("UNCHECKED_CAST")
            return StudentJournalViewModel(repository,fbRepository) as T
        }
        throw IllegalArgumentException("Unknown VM")
    }
}
