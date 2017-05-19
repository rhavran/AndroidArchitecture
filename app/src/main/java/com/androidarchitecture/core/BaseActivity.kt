package com.androidarchitecture.core

import android.arch.lifecycle.LifecycleActivity
import android.os.Handler
import android.os.Message
import android.R.id.message
import android.app.ProgressDialog
import android.view.KeyEvent.KEYCODE_BACK
import android.content.DialogInterface
import android.view.KeyEvent
import com.androidarchitecture.App
import com.androidarchitecture.di.component.AppComponent


/**
 * Created by binary on 5/18/17.
 */
abstract class BaseActivity : LifecycleActivity(), Handler.Callback, LoadingUiHandler {

    private val MSG_SHOW_LOADING_DIALOG = 0x1000      //4096 in dec
    private val MSG_UPDATE_LOADING_MESSAGE = 0x1001   //4097 in dec
    private val MSG_HIDE_LOADING_DIALOG = 0x1002      //4098 in dec

    private val mUIHandler = Handler(this@BaseActivity)
    private var mProgressDialog: ProgressDialog? = null

    //region BaseActivity
    /**
     * Returns instance of {@link AppComponent}.
     */
    fun getAppComponent() : AppComponent? = (applicationContext as App).appComponent
    //endregion

    //region Callback
    override fun handleMessage(msg: Message?): Boolean {
        var result = false
        when (msg?.what) {
            MSG_SHOW_LOADING_DIALOG -> { handleShowLoadingDialog(msg); result = true}
            MSG_UPDATE_LOADING_MESSAGE -> {handleUpdateLoadingDialog(msg); result = true}
            MSG_HIDE_LOADING_DIALOG -> {handleHideLoadingDialog(); result = true}
        }
        return result
    }
    //endregion

    //region LoadingUiHandler
    override fun showLoadingDialog(message: String) {
        val message = Message()
        message.what = MSG_SHOW_LOADING_DIALOG
        message.obj = message
        mUIHandler.sendMessage(message)
    }

    override fun updateLoadingDialog(message: String) {
        val message = Message()
        message.what = MSG_UPDATE_LOADING_MESSAGE
        message.obj = message
        mUIHandler.sendMessage(message)
    }

    override fun hideLoadingDialog() {
        val message = Message()
        message.what = MSG_HIDE_LOADING_DIALOG
        mUIHandler.sendMessage(message)
    }
    //endregion

    //region Utility API
    private fun isProgressShowing(): Boolean = null != mProgressDialog && mProgressDialog!!.isShowing

    private fun handleHideLoadingDialog() {
        if (isProgressShowing()) {
            mProgressDialog?.dismiss()
        }
    }

    private fun handleUpdateLoadingDialog(message: Message) {
        if (isProgressShowing()) {
            mProgressDialog?.setMessage(message.obj as String)
        }
    }

    private fun handleShowLoadingDialog(message: Message): Boolean {
        if (null == mProgressDialog) {
            mProgressDialog = ProgressDialog(this@BaseActivity)
        }
        mProgressDialog?.let {
            it.setMessage(message.obj as String)
            it.setCancelable(false)
            it.setOnKeyListener { dialog, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss()
                }
                false
            }
            if (!it.isShowing) {
                it.show()
            }
        }

        return true
    }
    //endregion
}