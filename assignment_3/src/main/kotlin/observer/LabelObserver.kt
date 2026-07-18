package observer

import javafx.scene.control.Label


class LabelObserver<T>(val styledLabel: Label): Observer<T> {
    override fun onUpdate(value: T) {
        styledLabel.text = value.toString()
    }
}