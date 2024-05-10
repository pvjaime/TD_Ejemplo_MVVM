package perez.jaime.tdejemplomvvm.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import perez.jaime.tdejemplomvvm.R
import perez.jaime.tdejemplomvvm.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        //Aca se configura el viewModel
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.loadUserList()

        //Se crea el adaptador con data vacia
        userAdapter = UserAdapter(emptyList()) // Inicialmente, la lista está vacía

        //Se configura el recyclerView
        recyclerView = findViewById(R.id.listUser)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = userAdapter

        //Se configura el observador que va a estar observando al sujeto "userList"
        userViewModel.userList.observe(
            this
        ) { listUsers ->
            //Recibi datos!!! actualizo
            userAdapter = UserAdapter(listUsers)
            recyclerView.adapter = userAdapter
        }

        //Otro sapo mas
        userViewModel.userList.observe(this
        ) { listUsers ->
            Toast.makeText(this, "Tengo ${listUsers.size} usuarios", Toast.LENGTH_LONG).show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}