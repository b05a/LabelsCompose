package com.example.labelscompose.ViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labelscompose.Clases.Label
import com.example.labelscompose.MainActivity
import com.example.labelscompose.Repository.RepositoryRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.os.Handler
import com.example.labelscompose.Cases.*
import com.example.labelscompose.Clases.ColorLabel

class MainViewModel(var displayType: DisplayType, var db: RepositoryRoom, var changeColor: ChangeColor, var handler:Handler, var toastMessage:ToastMessage, var generationId: GenerationId):ViewModel() {

    // параметры дисплея dpi x, dpi y и горизонтальный или вертикальный экран
    private lateinit var displayXY : DisplayXY
    // номер метки для редактирования в списке
    var changeListNumber = 0

    // показывается или нет окно настроек метки
    var optionView = mutableStateOf(false)
    // диалоговое окно удаления метки
    var deleteDialogAlert = mutableStateOf(false)
    // цвет меток
    var colorLabel = mutableStateOf<ColorLabel>(ColorLabel())
    // список меток
    var listLabels = mutableStateListOf<Label>()
    // создан ли listLabels
    var listLabelsCreate = mutableStateOf(false)



    // список меток относительной высоты
//    var listHeightLabel = mutableStateListOf<HeightLabel>()



    init {
        viewModelScope.launch(Dispatchers.IO) {
            // получаем цвет меток из бд
            colorLabel = db.getColorLabel()

            listLabels = db.getListLabel()

            handler.post { listLabelsCreate.value = true }

        }
    }
    // получение разрешения экрана в dpi и определение вертикальный экран или горизонтальный при переключении экрана
    fun displaySize( activity: MainActivity) {
        displayXY = displayType.getTypeDisplay(activity)


        if (!optionView.value) {
            changeListNumber = -1; return
        }
        if (changeListNumber < 0) return
        if (listLabels.isEmpty()) return

        if (!displayXY.displayVertical && listLabels[changeListNumber].verticalVal){
            // если при повороте экрана открыто окно настроек то закрываем его и сохраняем изменения
            if(optionView.value) optionView.value = false
            // если при повороте экрана открыто удаления метки то закрываем его
            if (deleteDialogAlert.value) deleteDialogAlert.value = false
        }
        if (displayXY.displayVertical && !listLabels[changeListNumber].verticalVal){
            // если при повороте экрана открыто окно настроек то закрываем его и сохраняем изменения
            if(optionView.value) optionView.value = false
            // если при повороте экрана открыто удаления метки то закрываем его
            if (deleteDialogAlert.value) deleteDialogAlert.value = false
        }

    }

    // получение разрешения экрана в dpi и определение вертикальный экран или горизонтальный (в процессе работы в активити)
    fun getDisplaySize():DisplayXY{
        return displayXY
    }
    // изменение цвета меток
    fun changeColorLabel(){
        // изменение цвета меток
        colorLabel.value = ColorLabel(color = changeColor.changeColor(colorLabel.value.color))
        // запись цвета метки в бд
        db.insertColorLabel(colorLabel.value.color)
    }
    // проверка есть ли в списке меток метки для горизонтального экрана
    fun horizontalIsEmpty():Boolean{
        if (listLabels.isEmpty()) return true
        for (i in listLabels){
            if (!i.verticalVal) return false
        }
        return true
    }
    // проверка есть ли в списке меток метки для вертикального экрана
    fun verticalIsEmpty():Boolean{
        if (listLabels.isEmpty()) return true
        for (i in listLabels){
            if (i.verticalVal) return false
        }
        return true
    }
    // добавление метки
    fun addLabel(){
        // генерируем Id
        var id = generationId.getId(listLabels, displayXY, toastMessage)
        // если превышено количество меток, завершаем функцию
        if (id < 0) return

        // добавление метки в список
        listLabels.add(Label(id,"Новая метка", 50, 50, displayXY.displayVertical))
        db.setLabel(listLabels.last())
        // показывает панель настроек
        optionView.value = true
        // добавляет номер метки в переменную определяющую какую метку редактировать
        changeListNumber = listLabels.lastIndex
    }
    // функция начала редактирования меток вертикальных
    fun editLabelVertical(label: Label):Boolean {
        // если метка горизонтальная то сообщаем что для ее изменения нужно перейти в горизонтальный режим
        if (!displayXY.displayVertical) {
            toastMessage.messageInfoVertical()
            return false
        }
        // показываем панель редактирования меток
        optionView.value = true
        // добавляет номер метки в переменную определяющую какую метку редактировать
        changeListNumber = listLabels.indexOf(label)
        println(changeListNumber)
        return true
    }
    // функция начала редактирования меток горизонтальных
    fun editLabelHorizontal(label: Label): Boolean {
        // если метка горизонтальная то сообщаем что для ее изменения нужно перейти в вертикальный режим
        if (displayXY.displayVertical) {
            toastMessage.messageInfoHorizontal()
            return false
        }
        // сохраняем в бд предыдущую метку если она открыта для редактирования
        saveChange()
        // показываем панель редактирования меток
        optionView.value = true
        // добавляет номер метки в переменную определяющую какую метку редактировать
        changeListNumber = listLabels.indexOf(label)
        return true
    }

    // функция для изменения координат меток
    fun changeLeft() {
        if (listLabels[changeListNumber].horizontal < 1/*0.1 * displayXY.x*/) return
        val e = listLabels[changeListNumber].horizontal - 1
        listLabels[changeListNumber] = listLabels[changeListNumber].copy(horizontal = e)
    }

    fun changeLeft10() {
        if (listLabels[changeListNumber].horizontal < 1/*0.1 * displayXY.x*/) return
        val e = listLabels[changeListNumber].horizontal - 10
        listLabels[changeListNumber] = listLabels[changeListNumber].copy(horizontal = e)
    }

    fun changeRight() {
        if (listLabels[changeListNumber].horizontal > 0.9 * displayXY.x) return
        val e = listLabels[changeListNumber].horizontal + 1
        listLabels[changeListNumber] = listLabels[changeListNumber].copy(horizontal = e)
    }

    fun changeRight10() {
        if (listLabels[changeListNumber].horizontal > 0.9 * displayXY.x) return
        val e = listLabels[changeListNumber].horizontal + 10
        listLabels[changeListNumber] = listLabels[changeListNumber].copy(horizontal = e)
    }

    fun changeUp() {
        if (listLabels[changeListNumber].vertical < 1/*0.1 * displayXY.y*/) return
        val e = listLabels[changeListNumber].vertical - 1
        listLabels[changeListNumber] = listLabels[changeListNumber].copy(vertical = e)
    }

    fun changeUp10() {
        if (listLabels[changeListNumber].vertical < 1/*0.1 * displayXY.y*/) return
        val e = listLabels[changeListNumber].vertical - 10
        listLabels[changeListNumber] = listLabels[changeListNumber].copy(vertical = e)
    }

    fun changeDown() {
        if (listLabels[changeListNumber].vertical > 0.9 * displayXY.y) return
        val e = listLabels[changeListNumber].vertical + 1
        listLabels[changeListNumber] = listLabels[changeListNumber].copy(vertical = e)
    }

    fun changeDown10() {
        if (listLabels[changeListNumber].vertical > 0.9 * displayXY.y) return
        val e = listLabels[changeListNumber].vertical + 10
        listLabels[changeListNumber] = listLabels[changeListNumber].copy(vertical = e)
    }

    // функция изменения названия метки
    fun changeNameLabel(name: String) {
        // если количество символов в названии метки больше 11 то не добавляем символы
        if (name.count() > 11) {
            toastMessage.messageLabelNameCount()
            return
        }
        listLabels[changeListNumber] = listLabels[changeListNumber].copy(name = name)
    }

    // сохранение метки
    fun saveChange(){
        // если если неизвестен номер метки для редактирования то завершаем функцию
        if (changeListNumber<0) return
        // сохраняем метку в бд
        db.setLabel(listLabels[changeListNumber])
    }

    // удаление метки
    fun delLabel(){
        // убираем диалоговое окно удаления метки
        deleteDialogAlert.value = !deleteDialogAlert.value;
        // убираем окно настройки меток
        optionView.value = false

        db.delLabel(listLabels[changeListNumber])

        // удаляем метку из списка меток
        listLabels.removeAt(changeListNumber)
    }

}