import tornadofx.*

class Main: App(MyView::class)



class MyView: View() {
	override val root = vbox {
		button { "Press me" }
		label { "Waiting" }
	}
}

fun main(args: Array<String>) {
	launch<Main>(args)
}
