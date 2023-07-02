package jp.developer.bbee.technicalmaster4th

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ActionMode.Callback
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import jp.developer.bbee.technicalmaster4th.databinding.ActivityMainBinding
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // ViewBinding initialize
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // file read
        val str = StringBuilder()
        try {
            openFileInput("memo.dat")
                .bufferedReader().forEachLine {
                    str.append(it)
                    str.append(System.getProperty("line.separator"))
                }
        } catch (e: Exception) {
            Toast.makeText(this, "ファイルが存在しません", Toast.LENGTH_SHORT).show()
        }

        binding.etMemo.apply {
            setText(str.toString())
            customSelectionActionModeCallback = createCallback()
        }

        // file write
        binding.btnSave.setOnClickListener {
            openFileOutput("memo.dat", MODE_PRIVATE)
                .bufferedWriter().use {
                    it.write(binding.etMemo.text.toString())
                }
        }
    }

    private fun createCallback(): Callback {
        return object: Callback {
            override fun onCreateActionMode(
                mode: android.view.ActionMode?,
                menu: Menu?
            ): Boolean {
                menu?.removeItem(android.R.id.selectAll)
                menu?.add(Menu.NONE, R.id.web_search, Menu.NONE, getString(R.string.web_search_title))
                return true
            }

            override fun onPrepareActionMode(
                mode: android.view.ActionMode?,
                menu: Menu?
            ): Boolean {
                return true
            }

            override fun onActionItemClicked(
                mode: android.view.ActionMode?,
                item: MenuItem?
            ): Boolean {
                when (item?.itemId) {
                    R.id.web_search -> {
                        val start = binding.etMemo.selectionStart
                        val end = binding.etMemo.selectionEnd
                        val query = binding.etMemo.text.toString().substring(start, end)
                        val intent = Intent(Intent.ACTION_WEB_SEARCH)
                            .putExtra(SearchManager.QUERY, query)
                        startActivity(intent)
                        return true
                    }
                    else -> return false
                }
            }

            override fun onDestroyActionMode(mode: android.view.ActionMode?) {
            }
        }
    }
}