import javafx.scene.layout.Priority
import tornadofx.getProperty
import tornadofx.property

data class Order(
	var id: Int = -1,
	val products: List<Product>,
	var status: OrderStatus = OrderStatus.CREATING,
	val customer: Customer,
	val delivery: Delivery,
	var payment: Payment
)

class Payment(
	var id: Int = -1,
	var paymentMethod: PaymentMethod = PaymentMethod.PAYPAL
)

class Delivery(
	var id: Int = -1,
	var address: String = "Test",
	var type: DeliveryType = DeliveryType.WORMHOLE,
	var description: String = "Test")

class Customer(
	var id: Int = -1,
	var firstName: String = "Test",
	var lastName: String = "Test",
	var address: String = "Test",
	var mailAddress: String = "Test",
	var phoneNumber: String = "Test"
)

enum class DeliveryType {
	WORMHOLE, DRONE, CAR
}

enum class PaymentMethod {
	PAYPAL, CREDITCARD, BANKACCOUNT
}

enum class OrderStatus {
	CREATING, PAID, WAITING, SCHEDULED, PREPARING, SHIPPING, DELIVERED, NOT_DELIVERABLE
}

data class Product(var id: Int = -1, val name: String, val weight: Int, val recipe: String, val priority: Int, val price: Float) {

	var amount by property<Int>()
	fun amountProperty() = getProperty(Product::amount)

	init {
		amount = 0
	}

	companion object {
		private val coldCheeseSandwich = Product(
			-1,
			"cold Cheese Sandwich",
			250,
			"assemble",
			1,
			2.5F
		)
		private val hotCheeseSandwich = Product(
			-1,
			"hot Cheese Sandwich",
			250,
			"assemble:bake",
			2,
			3.0F
		)
		private val hamSandwich = Product(
			-1,
			"Ham Sandwich",
			300,
			"assemble",
			1,
			3.5F
		)
		private val potatoSandwich = Product(
			-1,
			"Potato Sandwich",
			250,
			"cook:assemble",
			1,
			3.0F
		)
		private val veggieSandwich = Product(
			-1,
			"Veggie Sandwich",
			150,
			"assemble",
			1,
			4.0F
		)
		private val clubSandwich = Product(
			-1,
			"Club Sandwich",
			400,
			"assemble:bake:assemble",
			2,
			5.00F
		)


		fun getProducts(): List<Product> {
			return listOf(
				coldCheeseSandwich,
				hotCheeseSandwich,
				hamSandwich,
				potatoSandwich,
				veggieSandwich,
				clubSandwich
			)
		}
	}
}
