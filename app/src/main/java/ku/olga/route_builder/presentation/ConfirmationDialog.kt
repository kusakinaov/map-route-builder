package ku.olga.route_builder.presentation

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.layout_confirmation_dialog.*
import ku.olga.route_builder.R

class ConfirmationDialog : DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_Dialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.layout_confirmation_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textViewMessage.text = arguments?.getString(MESSAGE) ?: ""
        buttonPositive.setOnClickListener {
            dismiss()
            setResult(Activity.RESULT_OK, Intent())
        }
        buttonCancel.setOnClickListener {
            dismiss()
            setResult(Activity.RESULT_CANCELED, Intent())
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        setResult(Activity.RESULT_CANCELED, Intent())
    }

    private fun setResult(resultCode: Int, data: Intent) {
        targetFragment?.onActivityResult(targetRequestCode, resultCode, data)
    }

    companion object {
        private val TAG = ConfirmationDialog::class.java.simpleName
        private const val MESSAGE = "message"

        fun show(target: Fragment, requestCode: Int, message: String, extras: Bundle?) {
            val fragmentManager = target.fragmentManager
            if (fragmentManager == null || fragmentManager.isStateSaved) return

            val dialog = ConfirmationDialog().apply {
                setTargetFragment(target, requestCode)
                arguments = Bundle().apply {
                    extras?.let { putAll(it) }
                    putString(MESSAGE, message)
                }
            }
            dialog.show(fragmentManager, TAG)
        }
    }
}