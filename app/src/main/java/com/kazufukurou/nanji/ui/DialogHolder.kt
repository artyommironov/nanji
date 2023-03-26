package com.kazufukurou.nanji.ui

import android.app.Dialog

class DialogHolder {
  private var currentDialog: Dialog? = null

  fun show(dialog: Dialog) {
    currentDialog?.dismiss()
    currentDialog = dialog
    currentDialog?.show()
  }

  fun dismiss() {
    currentDialog?.dismiss()
  }
}