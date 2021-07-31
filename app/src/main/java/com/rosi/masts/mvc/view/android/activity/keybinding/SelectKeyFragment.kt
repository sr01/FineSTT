package com.rosi.masts.mvc.view.android.activity.keybinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rosi.masts.R
import com.rosi.masts.databinding.FragmentSelectKeyBinding
import com.rosi.masts.utils.android.toast

class SelectKeyFragment : WizardBaseFragment() {
    private lateinit var viewBinding: FragmentSelectKeyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSelectKeyBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.buttonNext.setOnClickListener { onNextClick() }
        resetView()

        viewModel.detectedKey.observe(viewLifecycleOwner) { detectedKey ->
            onKeyDetected(detectedKey.key, detectedKey.keyDetectedCount)
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as KeyBindingActivity).setTitle(getString(R.string.key_binding_select_key_screen_title))
    }

    private fun onKeyDetected(key: String, keyDetectedCount: Int) {
        viewBinding.textKeyCount.text = keyDetectedCount.toString()
        viewBinding.textSelectedKey.text = key
        viewBinding.textSelectedKey.setTextColor(
            resources.getColor(
                R.color.select_key_text_color,
                activity?.theme
            )
        )
        viewBinding.textSelectedKey.textDirection = View.TEXT_DIRECTION_LTR
        viewBinding.buttonNext.visibility = View.VISIBLE
    }

    private fun onNextClick() {
        actor.keySelected()
    }

    private fun resetView() {
        viewBinding.textKeyCount.text = "0"
        viewBinding.textSelectedKey.textDirection = View.TEXT_DIRECTION_INHERIT
        viewBinding.textSelectedKey.text = getString(R.string.press_a_key_text)
        viewBinding.textSelectedKey.setTextColor(
            resources.getColor(
                R.color.press_a_key_text_color,
                activity?.theme
            )
        )
        viewBinding.buttonNext.visibility = View.INVISIBLE
    }
}