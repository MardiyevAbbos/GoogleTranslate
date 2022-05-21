package com.example.googletranslate

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.googletranslate.databinding.ActivityMainBinding
import com.example.googletranslate.model.SaveTranslate
import com.example.googletranslate.ui.history.HistoryActivity
import com.example.googletranslate.ui.translate.TranslateViewModel
import com.example.googletranslate.utils.UiStateObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<TranslateViewModel>()

    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setupObservers()
        initViews()

    }

    private fun initViews() {
        allFunctionEdtText()
        editTextChanged()
        clickBackIcon()
        binding.bSaved.setOnClickListener { bSavedClick() }
        binding.ivReplace.setOnClickListener { ivReplaceIconClick() }
        binding.llNewTranslation.setOnClickListener { llNewTranslationClicked() }
        binding.ivHistory.setOnClickListener { openHistoryActivity() }
    }

    private fun openHistoryActivity() {
        val intent = Intent(this, HistoryActivity::class.java)
        launcher.launch(intent)
    }

    private val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> {
            if (it.resultCode == RESULT_OK) {
                resultStateMainPage()
                binding.bSaved.visibility = View.GONE
                binding.llNotClickState.visibility = View.GONE
                //binding.llClickState.visibility - View.VISIBLE
                val saveTranslate = it.data!!.getSerializableExtra("result") as SaveTranslate
                binding.edtEnterText.setText(saveTranslate.textFrom)
                binding.tvResultText.text = saveTranslate.textTo
            }
        }
    )

    private fun bSavedClick(){
        if (binding.edtEnterText.text.isNotEmpty()){
            val textFrom = binding.edtEnterText.text.toString()
            val textTo = binding.tvResultText.text.toString()
            if(textFrom.isNotEmpty() && textTo.isNotEmpty()){
                val saveTranslate = SaveTranslate(textFrom = textFrom, textTo = textTo)
                viewModel.insertTranslate(saveTranslate)
                Toast.makeText(this, "This Translation result saved in Local", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Wait for Result Field is fulled!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun editTextChanged(){
        binding.edtEnterText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                job?.cancel()
                job = lifecycleScope.launchWhenCreated {
                    delay(1000)
                    val text = binding.edtEnterText.text.toString()
                    if (text.isNotEmpty()){
                        viewModel.wordDetect(text)
                        //viewModel.wordTranslate(text, toL, fromL)
                    }else{
                        binding.tvResultText.text = ""
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }

    private fun setupObservers(){
        lifecycleScope.launchWhenCreated {
            viewModel.translateState.collect{
                when(it){
                    is UiStateObject.LOADING -> {}
                    is UiStateObject.SUCCESS -> {
                        it.data.data.translations.forEach { a ->
                            binding.tvResultText.text = a.translatedText
                        }
                    }
                    is UiStateObject.ERROR ->{ Log.d("@@@@@", it.message) }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.detectState.collect{
                when(it){
                    is UiStateObject.LOADING -> {}
                    is UiStateObject.SUCCESS -> {
                        it.data.data.detections.forEach { a ->
                            a.forEach { result ->
                                if(result.language == "uz" || result.isReliable || result.confidence > 0.5){
                                    viewModel.wordTranslate(binding.edtEnterText.text.toString(), "en", "uz")
                                }else{
                                    Toast.makeText(applicationContext, "You enter only O`zbek text!!!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                    is UiStateObject.ERROR ->{ Log.d("@@@@@", it.message) }
                    else -> Unit
                }
            }
        }

    }



    /*
          There are Functions for Additional animations(All icons, buttons, texts change states)
     */

    private fun noteState(){
        if (binding.bFrom.text.equals(getString(R.string.str_english))){ binding.llNoteFromL.visibility = View.VISIBLE
        }else{ binding.llNoteFromL.visibility = View.GONE }
    }

    private fun llNewTranslationClicked(){
        binding.edtEnterText.isCursorVisible = true
        binding.bSaved.visibility = View.GONE
        binding.llNewTranslation.visibility = View.GONE
        binding.llReplace.visibility = View.VISIBLE
        binding.edtEnterText.text.clear()
        binding.tvResultText.text = ""
        typeTranslationVisible()
        openKeyBoard()
    }

    private fun ivReplaceIconClick(){
        var helper = binding.bFrom.text.toString()
        binding.bFrom.text = binding.bTo.text
        binding.bTo.text = helper
        helper = binding.tvResultTypeL.text.toString()
        binding.tvResultTypeL.text = binding.tvEnterTypeL.text
        binding.tvEnterTypeL.text = helper
        noteState()
    }

    private fun clickBackIcon(){
        binding.ivBack.setOnClickListener {
            binding.llNotClickState.visibility = View.VISIBLE
            binding.llClickState.visibility = View.VISIBLE
            binding.bSaved.visibility = View.GONE
            binding.llReplace.visibility = View.VISIBLE
            binding.llNewTranslation.visibility = View.GONE
            binding.edtEnterText.text.clear()
            binding.tvResultText.text = ""
            binding.edtEnterText.isCursorVisible = false
            typeTranslationVisible()
            closeKeyBoard()
        }
    }

    private fun typeTranslationVisible(){
        binding.tvEnterTypeL.visibility = View.GONE
        binding.tvResultTypeL.visibility = View.GONE
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun allFunctionEdtText(){
        binding.edtEnterText.imeOptions = EditorInfo.IME_ACTION_NEXT;
        binding.edtEnterText.setRawInputType(InputType.TYPE_CLASS_TEXT);

        binding.edtEnterText.setOnTouchListener { _, event ->
            if (MotionEvent.ACTION_UP == event?.action){
                binding.llNotClickState.visibility = View.GONE
                binding.llClickState.visibility = View.VISIBLE
                binding.llNewTranslation.visibility = View.GONE
                binding.bSaved.visibility = View.GONE
                binding.llReplace.visibility = View.VISIBLE
                binding.edtEnterText.isCursorVisible = true
                typeTranslationVisible()
                noteState()
            }
            false
        }  // clicked editText for writing

        binding.edtEnterText.setOnEditorActionListener { v, actionId, event ->
            if (binding.edtEnterText.text.isNotEmpty()){
                resultStateMainPage()
                binding.bSaved.visibility = View.VISIBLE
            }
            false
        } // this Function when click search icon in Keyboard

    }

    private fun resultStateMainPage(){
        binding.llNewTranslation.visibility = View.VISIBLE
        binding.llReplace.visibility = View.GONE
        binding.edtEnterText.isCursorVisible = false
        binding.tvEnterTypeL.visibility = View.VISIBLE
        binding.tvResultTypeL.visibility = View.VISIBLE
        closeKeyBoard()
    }

    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun openKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInputFromInputMethod(view.windowToken, 0)
        }
    }


}