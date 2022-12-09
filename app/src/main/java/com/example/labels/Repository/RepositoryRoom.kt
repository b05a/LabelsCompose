package com.example.labels.Repository

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.labels.Clases.ColorLabel
import com.example.labels.Clases.HeightLabel
import com.example.labels.Clases.Label
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RepositoryRoom(var db: MainDb) {

    fun setLabel(label: Label){
        CoroutineScope(Dispatchers.IO).launch {
            db.getDao().insertLabel(label)
        }
    }

    fun delLabel(label: Label){
        CoroutineScope(Dispatchers.IO).launch {
            db.getDao().deleteLabel(label)
        }
    }

    fun getListLabel():SnapshotStateList<Label>{
        val list = mutableStateListOf<Label>()
        for (i in db.getDao().getLabels()){
            list.add(i)
        }
        return list
    }

    fun setHeightLabel(heightLabel: HeightLabel){
        CoroutineScope(Dispatchers.IO).launch {
            db.getDao().insertHeightLabel(heightLabel)
        }
    }

    fun delHeightLabel(heightLabel: HeightLabel){
        CoroutineScope(Dispatchers.IO).launch {
            db.getDao().deleteHeightLabel(heightLabel)
        }
    }

    fun getListHeightLabel():SnapshotStateList<HeightLabel>{
        val list = mutableStateListOf<HeightLabel>()
        for (i in db.getDao().getHeightLabel()){
            list.add(i)
        }
        return list
    }

    fun insertColorLabel(color:String){
        CoroutineScope(Dispatchers.IO).launch {
            db.getDao().changeColorLabel(ColorLabel(color = color))
        }
    }

    fun getColorLabel(): MutableState<ColorLabel> {
        var i = db.getDao().getColorLabel()
        if (i.isEmpty()){
            db.getDao().changeColorLabel(ColorLabel())
        }
        i = db.getDao().getColorLabel()
        return mutableStateOf(i[0])
    }
}