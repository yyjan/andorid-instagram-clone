package com.example.yun.yunstagram.views

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.yun.yunstagram.R
import com.example.yun.yunstagram.utilities.Constants.REQUEST_CODE_FOR_PERMISSIONS_EXTERNAL_STORAGE
import dagger.android.support.DaggerAppCompatActivity

class ProfileEditActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ProfileEditFragment.newInstance())
                .commitNow()
        }

        checkPermissions(REQUEST_CODE_FOR_PERMISSIONS_EXTERNAL_STORAGE)
    }

    private fun checkPermissions(requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), requestCode)
            return false
        }
        return true
    }

}
