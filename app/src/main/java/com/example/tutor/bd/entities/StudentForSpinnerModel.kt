package com.example.tutor.bd.entities

data class StudentForSpinnerModel(var id:Int, val fullName:String){
    // переопределил метод toString для отображения только fullName при обращению к List<StudentForSpinnerModel>
    override fun toString(): String {
        return fullName
    }
}