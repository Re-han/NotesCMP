package domain

sealed class TaskAction {
    data class Add(val task: Notes) : TaskAction()
    data class Update(val task: Notes) : TaskAction()
    data class Delete(val task: Notes) : TaskAction()
    data class SetBookMark(val task: Notes, val bookMark: Boolean) : TaskAction()
}