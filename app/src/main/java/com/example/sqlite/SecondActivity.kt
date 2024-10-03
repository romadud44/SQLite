package com.example.sqlite

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sqlite.databinding.ActivityMainBinding
import com.example.sqlite.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding
    private val db = DBHelper(this, null)
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySecondBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.toolbarSecond.title = "База данных"
        binding.toolbarSecond.subtitle = "версия 1.0"
        setSupportActionBar(binding.toolbarSecond)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, Person.positionsArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.enterPositionSP.adapter = adapter

        binding.saveBTN.setOnClickListener{
            val name = binding.enterNameET.text.toString()
            val age = binding.enterAgeET.text.toString()
            val position = binding.enterPositionSP.selectedItem.toString()
            db.addName(name, age, position)
            Toast.makeText(this, "$name Добавлен в базу данных", Toast.LENGTH_LONG).show()
            clearFields()
        }
        binding.loadBTN.setOnClickListener{
            val cursor = db.getInfo()
            if (cursor != null && cursor.moveToFirst()) {
                cursor.moveToFirst()
                binding.nameTV.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME)) + "\n")
                binding.ageTV.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_AGE)) + "\n")
                binding.positionTV.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_POSITION)) + "\n")
            }
            while (cursor?.moveToNext() == true) {
                binding.nameTV.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME)) + "\n")
                binding.ageTV.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_AGE)) + "\n")
                binding.positionTV.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_POSITION)) + "\n")
            }
            cursor?.close()
        }
        binding.removeBTN.setOnClickListener{
            db.removeAll()
            binding.nameTV.text =""
            binding.ageTV.text =""
            binding.positionTV.text =""
        }

    }

    private fun clearFields() {
        binding.enterNameET.text.clear()
        binding.enterAgeET.text.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_second, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exitMenuSecond -> {
                finishAndRemoveTask()
                finishAffinity()
                finish()
                Toast.makeText(this, "Программа завершена", Toast.LENGTH_LONG).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}