package com.example.mysubmission3.ui.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import com.example.mysubmission3.R
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModelProvider
import com.example.mysubmission3.ui.ViewModel.MenuViewModel
import com.example.mysubmission3.ui.ViewModel.SettingPreferences
import com.example.mysubmission3.helper.ViewModelFactory
import com.example.mysubmission3.databinding.ActivityDarkModeBinding
import com.google.android.material.switchmaterial.SwitchMaterial

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DarkModeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDarkModeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDarkModeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val switchTheme = findViewById<SwitchMaterial>(R.id.switch_theme)

        val pref = SettingPreferences.getInstance(datastore)
        val menuViewModel = ViewModelProvider(this, ViewModelFactory(pref))[MenuViewModel::class.java]

        menuViewModel.getThemeSetting().observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            menuViewModel.saveThemeSetting(isChecked)
        }
    }
}