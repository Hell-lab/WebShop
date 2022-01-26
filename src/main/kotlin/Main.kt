import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.TableColumn
import javafx.scene.control.ToggleGroup
import javafx.scene.text.FontWeight
import javafx.stage.StageStyle
import tornadofx.*
import java.time.LocalDate
import java.time.LocalTime

class Main : App(Start::class)

val controller = MyController()
var event = MyEventHandler()
var sum = 0.0
var amount = 0
var textTotal = SimpleStringProperty()
val deliveryAddress = SimpleStringProperty()
val deliveryDescription = SimpleStringProperty()
private var toggleGroupDelivery = ToggleGroup()

class MyEventHandler : EventHandler<TableColumn.CellEditEvent<Product, Int>> {
	override fun handle(event: TableColumn.CellEditEvent<Product, Int>?) {
		controller.calculateTotal(
			event?.tableView?.items?.get(event?.tablePosition.row) ?: Product(
				name = "test",
				recipe = "test", weight = 1, price = 0.0F, priority = 1
			), amount
		)
		textTotal.value = "Total: $sum"
	}

}

class Start : View() {
	override val root = vbox(50) {
		primaryStage.height = 500.0
		primaryStage.width = 1155.0
		alignment = Pos.CENTER
		imageview("logo.png")
		text("Welcome to our Smart Sandwish Factory - Get sick, get one Sandwich for free!") {
			style {
				fontWeight = FontWeight.EXTRA_BOLD
				fontFamily = "Helvetica"
				fontSize = 30.px
			}
		}
		vbox(7) {
			alignment = Pos.BASELINE_CENTER
			button("New Order") {
				action {
					controller.start()
					replaceWith(CustomerView::class, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
				}
				style {
					fontWeight = FontWeight.EXTRA_BOLD
					fontFamily = "Helvetica"
					fontSize = 20.px
				}
			}
			button("Login with Customer ID") {
				action {
					replaceWith(Status::class, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
				}
				style {
					fontWeight = FontWeight.EXTRA_BOLD
					fontFamily = "Helvetica"
					fontSize = 20.px
				}
			}
		}
	}
}

class Status : View() {
	override val root = vbox {
		primaryStage.height = 500.0
		primaryStage.width = 1155.0
		vbox {
			text("Total: $sum")
			tableview (controller.getOrder())
		}
		hbox(7) {
			alignment = Pos.BASELINE_CENTER
			button("back") {
				style {
					fontWeight = FontWeight.EXTRA_BOLD
					fontFamily = "Helvetica"
					fontSize = 20.px
				}
				action {
					replaceWith(
						ProductView::class,
						ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT)
					)
				}
			}
			button("send Order") {
				style {
					fontWeight = FontWeight.EXTRA_BOLD
					fontFamily = "Helvetica"
					fontSize = 20.px
				}
				action {
					replaceWith(
						Start::class,
						ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
					)
				}
			}
		}
	}
}

class CustomerView : View() {
	var firstName = SimpleStringProperty()
	var lastName = SimpleStringProperty()
	var address = SimpleStringProperty()
	var mail = SimpleStringProperty()
	var phone = SimpleStringProperty()
	override val root = borderpane {
		primaryStage.height = 500.0
		primaryStage.width = 1155.0
		center = vbox(50) {
			form {
				fieldset("Personal Info") {
					style {
						fontWeight = FontWeight.EXTRA_BOLD
						fontFamily = "Helvetica"
						fontSize = 20.px
					}
					field("First Name") {
						style {
							fontWeight = FontWeight.BOLD
							fontFamily = "Helvetica"
							fontSize = 15.px
						}
						textfield(firstName)
					}
					field("Last Name") {
						style {
							fontWeight = FontWeight.BOLD
							fontFamily = "Helvetica"
							fontSize = 15.px
						}
						textfield(lastName)
					}
					field("Address") {
						style {
							fontWeight = FontWeight.BOLD
							fontFamily = "Helvetica"
							fontSize = 15.px
						}
						textfield(address)
					}
				}
				fieldset("Contact") {
					style {
						fontWeight = FontWeight.EXTRA_BOLD
						fontFamily = "Helvetica"
						fontSize = 20.px
					}
					field("E-Mail") {
						style {
							fontWeight = FontWeight.BOLD
							fontFamily = "Helvetica"
							fontSize = 15.px
						}
						textfield(mail)
					}
					field("Phone") {
						style {
							fontWeight = FontWeight.BOLD
							fontFamily = "Helvetica"
							fontSize = 15.px
						}
						textfield(phone)
					}
				}
			}
			hbox(7) {
				alignment = Pos.BASELINE_CENTER
				button("back") {
					style {
						fontWeight = FontWeight.EXTRA_BOLD
						fontFamily = "Helvetica"
						fontSize = 20.px
					}
					action {
						controller.stop()
						firstName.value = ""
						lastName.value = ""
						address.value = ""
						phone.value = ""
						mail.value = ""
						deliveryAddress.value = ""
						deliveryDescription.value = ""
						replaceWith(Start::class, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT))
					}
				}
				button("next") {
					style {
						fontWeight = FontWeight.EXTRA_BOLD
						fontFamily = "Helvetica"
						fontSize = 20.px
					}
					action {
						controller.addCostumerToDatabase(
							firstName.value,
							lastName.value,
							address.value,
							phone.value,
							mail.value
						)
						replaceWith(
							DeliveryView::class,
							ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
						)
					}
				}
			}

		}
	}
}

class DeliveryView : View() {
	override val root = borderpane {
		primaryStage.height = 500.0
		primaryStage.width = 1155.0
		center = vbox(200) {
			form {
				fieldset("Delivery Information") {
					style {
						fontWeight = FontWeight.EXTRA_BOLD
						fontFamily = "Helvetica"
						fontSize = 20.px
					}
					field("Delivery Address") {
						style {
							fontWeight = FontWeight.BOLD
							fontFamily = "Helvetica"
							fontSize = 15.px
						}
						textfield(deliveryAddress)
					}

					field("Delivery Type") {
						radiobutton(DeliveryType.WORMHOLE.name, toggleGroupDelivery)
						radiobutton(DeliveryType.DRONE.name, toggleGroupDelivery)
						radiobutton(DeliveryType.CAR.name, toggleGroupDelivery)
						style {
							fontWeight = FontWeight.BOLD
							fontFamily = "Helvetica"
							fontSize = 15.px
						}
					}
					field("Delivery Description") {
						style {
							fontWeight = FontWeight.BOLD
							fontFamily = "Helvetica"
							fontSize = 15.px
						}
						textfield(deliveryDescription)
					}
				}
			}
			hbox(7) {
				alignment = Pos.BASELINE_CENTER
				button("back") {
					style {
						fontWeight = FontWeight.EXTRA_BOLD
						fontFamily = "Helvetica"
						fontSize = 20.px
					}
					action {
						replaceWith(
							CustomerView::class,
							ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT)
						)
					}
				}
				button("next") {
					style {
						fontWeight = FontWeight.EXTRA_BOLD
						fontFamily = "Helvetica"
						fontSize = 20.px
					}
					action {
						controller.addDeliveryToDatabase(
							deliveryAddress.value,
							toggleGroupDelivery.selectedToggle.toString(), deliveryDescription.value
						)
						replaceWith(
							ProductView::class,
							ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
						)
					}
				}
			}
		}
	}
}

class ProductView : View() {
	private val toggleGroup = ToggleGroup()
	override val root = vbox(5) {
		primaryStage.height = 500.0
		primaryStage.width = 1155.0
		hbox(5) {
			alignment = Pos.CENTER
			tableview(controller.getProducts()) {
				isEditable = true
				readonlyColumn("Product Name", Product::name)
				readonlyColumn("Price €", Product::price)
				column("Amount", Product::amountProperty).makeEditable().setOnEditCommit { e ->
					amount = e.newValue ?: e.oldValue
					e.tableView.items[e.tablePosition.row].amountProperty().set(amount)
					event.handle(e)
					controller.updateProduct(e.tableView.items[e.tablePosition.row], amount)
				}
			}
			vbox(7) {
				text(textTotal) {
					style {
						style {
							fontFamily = "Helvetica"
							fontSize = 15.px
						}
					}
					("Total: 0")
				}
				form {
					fieldset("Payment Information") {
						style {
							fontFamily = "Helvetica"
							fontSize = 15.px
						}
						radiobutton(PaymentMethod.PAYPAL.name, toggleGroup) {
							action {
								replaceWith(
									PayPalView::class,
									ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
								)
							}
						}
						radiobutton(PaymentMethod.BANKACCOUNT.name, toggleGroup) {
							action {
								replaceWith(
									BankAccountView::class,
									ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
								)
							}
						}
						radiobutton(PaymentMethod.CREDITCARD.name, toggleGroup) {
							action {
								replaceWith(
									CreditCardView::class,
									ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
								)
							}
						}
					}
				}
			}
		}
		hbox(7) {
			alignment = Pos.BASELINE_CENTER
			button("back") {
				style {
					action {
						replaceWith(
							DeliveryView::class,
							ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT)
						)
					}
					fontWeight = FontWeight.EXTRA_BOLD
					fontFamily = "Helvetica"
					fontSize = 20.px
				}
			}
			button("next") {
				style {
					fontWeight = FontWeight.EXTRA_BOLD
					fontFamily = "Helvetica"
					fontSize = 20.px
				}
				action {
					var delivery = toggleGroupDelivery.selectedToggle.toString()
					if (delivery.subSequence(46, delivery.length - 1).equals("DRONE"))
						if (controller.isHeavy()) {
							find<MyFragment>().openModal(stageStyle = StageStyle.UTILITY)
						}
					controller.addPaymentToDatabase(toggleGroup.selectedToggle.toString())
					controller.addOrder()
					replaceWith(
						Status::class,
						ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
					)

				}
			}
		}
	}
}

class MyFragment: Fragment() {
	override val root = label("Warning: These order is too heavy for drone delivery. Please choose another delivery option!")
}

class PayPalView : View() {
	override val root = vbox {
		primaryStage.height = 500.0
		primaryStage.width = 1155.0
		form {
			fieldset("PayPal") {
				style {
					fontFamily = "Helvetica"
					fontSize = 15.px
				}
				field("Mail address") {
					textfield()
				}
				field("Password") {
					passwordfield {
						requestFocus()
					}
				}
			}
		}
		hbox(7) {
			alignment = Pos.BASELINE_CENTER
			button("back") {
				style {
					fontWeight = FontWeight.EXTRA_BOLD
					fontFamily = "Helvetica"
					fontSize = 20.px
				}
				action {
					replaceWith(
						ProductView::class,
						ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT)
					)
				}
			}
			button("next") {
				style {
					fontWeight = FontWeight.EXTRA_BOLD
					fontFamily = "Helvetica"
					fontSize = 20.px
				}
				action {
					replaceWith(
						Status::class,
						ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
					)
				}
			}
		}
	}

}

class CreditCardView : View() {
	override val root = vbox {
		primaryStage.height = 500.0
		primaryStage.width = 1155.0
		form {
			fieldset("Credit Card") {
				style {
					fontFamily = "Helvetica"
					fontSize = 15.px
				}
				field("Card Number") {
					textfield()
				}
				field("Expiration date") {
					datepicker {
						value = LocalDate.now()
					}
				}
				field("CVC") {
					textfield()
				}
			}
		}
		hbox(7) {
			alignment = Pos.BASELINE_CENTER
			button("back") {
				style {
					fontWeight = FontWeight.EXTRA_BOLD
					fontFamily = "Helvetica"
					fontSize = 20.px
				}
				action {
					replaceWith(
						ProductView::class,
						ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT)
					)
				}
			}
			button("next") {
				style {
					fontWeight = FontWeight.EXTRA_BOLD
					fontFamily = "Helvetica"
					fontSize = 20.px
				}
				action {
					replaceWith(
						Status::class,
						ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
					)
				}
			}
		}
	}

}

class BankAccountView : View() {
	override val root = vbox {
		primaryStage.height = 500.0
		primaryStage.width = 1155.0
		form {
			fieldset("Bank account") {
				style {
					fontFamily = "Helvetica"
					fontSize = 15.px
				}
				field("Account holder") {
					textfield()
				}
				field("IBAN") {
					textfield()
				}
				field("BIC") {
					textfield()
				}
			}
		}
		hbox(7) {
			alignment = Pos.BASELINE_CENTER
			button("back") {
				style {
					fontWeight = FontWeight.EXTRA_BOLD
					fontFamily = "Helvetica"
					fontSize = 20.px
				}
				action {
					replaceWith(
						ProductView::class,
						ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT)
					)
				}
			}
			button("next") {
				style {
					fontWeight = FontWeight.EXTRA_BOLD
					fontFamily = "Helvetica"
					fontSize = 20.px
				}
				action {
					replaceWith(
						Status::class,
						ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
					)
				}
			}
		}
	}

}

class MyController : Controller() {
	val products = ArrayList<Product>()
	var customer = Customer()
	var delivery= Delivery()
	var payment= Payment()
	val db = DBManager()
	var customerID = 0
	var orderID = 0

	fun start() {
		db.openConnection()
		db.initializeTables()
		db.populateProductsWithDummyData()
	}

	fun getProducts(): ObservableList<Product> {
		return FXCollections.observableList(db.getProducts())
	}

	fun stop() {
		db.closeConnection()
	}

	fun calculateTotal(product: Product, amount: Int) {
		sum += product.price * amount
	}

	fun addCostumerToDatabase(firstName: String, lastName: String, address: String, mail: String, phone: String) {
		customer.firstName = firstName
		customer.lastName = lastName
		customer.address = address
		customer.mailAddress = mail
		customer.phoneNumber = phone

		customerID = db.addCustomer(customer)
	}

	fun addPaymentToDatabase(method: String) {
		val method = method.subSequence(46, method.length - 1).toString()
		payment.paymentMethod = PaymentMethod.valueOf(method)
		db.addPayment(payment)
		db.updateOrderStatus(orderID, OrderStatus.PAID)
		db.updateOrderETA(orderID, LocalTime.now().plusMinutes(15))
	}

	fun addDeliveryToDatabase(address: String, type: String, description: String) {
		val type = type.subSequence(46, type.length - 1).toString()
		delivery.address = address
		delivery.type = DeliveryType.valueOf(type)
		delivery.description = description
		db.addDelivery(delivery)
	}

	fun addOrder() {
		orderID = db.addOrder(Order(products = products, customer = customer, delivery = delivery, payment = payment)).id
	}

	fun isHeavy(): Boolean {
		var sum = 0
		products.forEach{sum += it.amount * it.weight}
		return sum >= 500
	}

	fun updateProduct(product: Product, amount: Int) {
		product.amount = amount
		products.add(product)
	}

	fun getOrder(): ObservableList<Order> {
		return db.getOrderFromCustomer(customerID).toObservable()
	}

}


fun main(args: Array<String>) {
	launch<Main>(args)
}
