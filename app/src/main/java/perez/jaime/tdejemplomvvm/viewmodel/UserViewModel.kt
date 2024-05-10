package perez.jaime.tdejemplomvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import perez.jaime.tdejemplomvvm.model.User

//Este es el view Model
class UserViewModel : ViewModel() {

    //Variable de lista de usuarios
    private val _userList = MutableLiveData<List<User>>()

    //Esta variable es la que se va a encargar de propagar el cambio a sus observadores
    val userList: LiveData<List<User>> = _userList

    //Método para cargar la lista de usuarios desde una fuente de datos (API, base de datos, etc.)
    fun loadUserList() {
        // Simulando la carga de datos desde una fuente (reemplazar con tu lógica real)
        val users = listOf(
            User(1, "Jorge Andaur", "jorge@example.com"),
            User(2, "Karen Caceres", "karen@example.com"),
            User(3, "Bob Esponja", "bob@example.com")
        )
        _userList.value = users
    }

}