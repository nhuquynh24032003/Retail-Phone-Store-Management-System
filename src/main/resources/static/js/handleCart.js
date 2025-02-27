let updateQuantity = input => {
    let productId = $(input).data("product-id")
    let colorId = $(input).data("color-id")
    let capacityId = $(input).data("capacity-id")
    let quantity = $(input).val()
    let data = {
        productId,
        colorId,
        capacityId,
        quantity
    }
    $.ajax({
        url: "/cart/update",
        type: "POST",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(data),
        success: function(response) {
            let ul = $(input).closest("ul")
            let span = ul.find(".item-price .money")
            let priceStr = span[0].innerHTML
            let price = parseInt(priceStr.substring(0, priceStr.length - 1))
            ul.find(".full-price .money")[0].innerHTML = (price * quantity) + " VND"
            calculateTotal()
        },
        error: function(xhr, status, error) {
            console.log("Error: " + error);
        }
    });
}

let removeCartItem = a => {
    let productId = $(a).data("product-id")
    let colorId = $(a).data("color-id")
    let capacityId = $(a).data("capacity-id")
    let data = {
        productId,
        colorId,
        capacityId
    }
    $.ajax({
        url: "/cart/remove",
        type: "POST",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(data),
        success: function(response) {
            let ul = $(a).closest("ul")
            ul.remove()
            calculateTotal()
        },
        error: function(xhr, status, error) {
            console.log("Error: " + error);
        }
    });
}

function calculateTotal() {
    let cart = $(".item-wrap")
    let uls = cart.find("ul")
    let totalPrice = 0;
    uls = [...uls]
    uls.forEach(ul => {
        let span = $(ul).find(".full-price .money")
        let cartItemTotalPrice = span[0].innerHTML
        let totalPriceOfCartItem = parseInt(cartItemTotalPrice.substring(0, cartItemTotalPrice.length - 1)) // remove $
        totalPrice += totalPriceOfCartItem;
    })
    $('#total-price').html(totalPrice + " VND");
}