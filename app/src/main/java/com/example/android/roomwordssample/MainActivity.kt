/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.roomwordssample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val newWordActivityRequestCode = 1
    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory((application as WordsApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO -> Nenggunakan layout activity_main
        setContentView(R.layout.activity_main)

        //TODO  -> Mendefiniskan recyleview yang disertakaan adapter dan layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = WordListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        val fab = findViewById<FloatingActionButton>(R.id.fab)
        //TODO  -> ketika tombol fab diklik maka Membuat intent ke class NewWordActivity
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewWordActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }

        //TODO ->
        // Tambahkan pengamat di LiveData yang dikembalikan oleh getAlphabetizedWords.
        // Metode onChanged() diaktifkan ketika data yang diamati berubah dan aktivitasnya
        // di latar depan.
        wordViewModel.allWords.observe(owner = this) { words ->
            // TODO -> Perbarui salinan kata-kata yang di-cache di adaptor.
            words.let { adapter.submitList(it) }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)
        //TODO -> jika
        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.getStringExtra(NewWordActivity.EXTRA_REPLY)?.let { reply ->
                val word = Word(reply)
                wordViewModel.insert(word)
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
