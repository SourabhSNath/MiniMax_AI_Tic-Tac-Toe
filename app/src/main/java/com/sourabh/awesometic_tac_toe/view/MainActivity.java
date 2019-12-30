package com.sourabh.awesometic_tac_toe.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.sourabh.awesometic_tac_toe.R;
import com.sourabh.awesometic_tac_toe.databinding.ActivityMainBinding;
import com.sourabh.awesometic_tac_toe.viewmodel.GameViewModel;

public class MainActivity extends AppCompatActivity {

    GameViewModel gameViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameViewModel = ViewModelProviders.of(this).get(GameViewModel.class);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        activityMainBinding.setLifecycleOwner(this);
        activityMainBinding.setGameViewModel(gameViewModel);

        String[] options = getResources().getStringArray(R.array.drop_down_options);

        ArrayAdapter<String> dropDownAdapter =
                new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, options);

        AutoCompleteTextView filledExposedDropDown = findViewById(R.id.filled_exposed_dropdown);
        filledExposedDropDown.setAdapter(dropDownAdapter);
    }
}
