enum class Screens(
    val route: String,
) {
    Login("login"),
    Home("home"),
    Products("products"),
    AddProduct("add_product"),
    ProductDetail("product_detail"),
    Orders("orders"),
    AddOrder("add_order"),
    OrderDetail("order_detail"),
    Cart("cart"),
    Checkout("checkout"),
    OrderConfirmation("order_confirmation"),
}