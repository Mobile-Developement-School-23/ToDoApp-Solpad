package com.example.todoapp.screens.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentMainBinding
import com.example.todoapp.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {


    private var _binding: FragmentSettingsBinding? =null
    private val mBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        setLigthTheme()
        setNightTheme()
        setSystemTheme()
        return mBinding.root
    }

    fun setLigthTheme(){
        mBinding.lightTheme.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
            findNavController().popBackStack()
        }
    }
    fun setNightTheme(){
        mBinding.nightTheme.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            findNavController().popBackStack()
        }
    }
    fun setSystemTheme(){
        mBinding.systemTheme.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
            findNavController().popBackStack()
        }
    }

}