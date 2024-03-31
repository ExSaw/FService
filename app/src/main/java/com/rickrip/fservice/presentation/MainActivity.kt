package com.rickrip.fservice.presentation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rickrip.fservice.R
import com.rickrip.fservice.databinding.ActivityMainBinding
import com.rickrip.fservice.di.IDispatchersProvider
import com.rickrip.fservice.di.KoinQualifiers
import com.rickrip.fservice.presentation.list.FAdapter
import com.rickrip.fservice.util.FakeUploader
import com.rickrip.fservice.util.ICommunicator
import com.rickrip.fservice.util.UploadsState
import com.rickrip.fservice.util.logUnlimited
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding ?: throw IllegalArgumentException("MainActivity -> Binding cannot be null")

    private val communicator: ICommunicator by inject()
    private val dispatchers: IDispatchersProvider by inject()
    private val appScope: CoroutineScope by inject(KoinQualifiers.APP_SCOPE.qualifier)
    private val fakeUploader: FakeUploader = get()

    private val rvAdapter by lazy {
        FAdapter(
            layoutInflater = layoutInflater,
            recyclerView = binding.rvList
        ) {
            /*on item click action*/
        }
    }

    private fun subscribeForObservables() {
        lifecycleScope.launch(dispatchers.main) {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                fakeUploader.activeUploads.collectLatest {
                    handleUploads(it)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        _binding = ActivityMainBinding.bind(findViewById(R.id.main))

        subscribeForObservables()

        binding.apply {

            rvList.adapter = rvAdapter

            btnStart.setOnClickListener {
                fakeUploader.uploadNewItem(rvAdapter)
            }

            btnStop.setOnClickListener {
                fakeUploader.stopActiveUploads(rvAdapter)
            }
        }

        rvAdapter.submitList(fakeUploader.activeUploads.value.activeUploadItems)
    }

    private fun handleUploads(uploadsState: UploadsState) {
        rvAdapter.submitList(uploadsState.activeUploadItems)
        if (uploadsState.activeUploadItems.any { it.progress < 100 }) {
            startService()
        } else {
            stopService()
        }
    }

    private fun startService() {
        Intent(applicationContext, FService::class.java).also {
            it.action = FService.MyServiceAction.START.toString()
            startService(it)
        }
    }

    private fun stopService() {
        Intent(applicationContext, FService::class.java).also {
            it.action = FService.MyServiceAction.STOP.toString()
            startService(it)
        }
    }
}