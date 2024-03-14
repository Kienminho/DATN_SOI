const tbody = $(".tbody");
const CartSubtotalPrice = $('.cart-subtotal-price');
const OrderTotalPrice = $('.order-total-price');
let total =0;
let orderItems = [];
let name = $('#name');
let address = $('#address');
let city = $('#city');
let email = $('#email');
let phone = $('#phone');
let selectedValue = "";
let shippingFee = 0;

// Get all radio buttons with name "paymentType"
const radioButtons = document.querySelectorAll('input[name="paymentType"]');
// Add event listener for change event
radioButtons.forEach(radioButton => {
    radioButton.addEventListener('change', function() {
        // Get the value of the selected radio button
        selectedValue = this.value;
    });
});

fetch("/api/products/cart")
    .then(res => res.json())
    .then(res => {
        displayCheckout(res.data);
        addOrderItem(res.data);
    })
    .catch(err => console.log(err))

function handleOrder() {
    if(validateForm()) {
        const infoOrder = {
            totalPrice: total,
            name: name.val(),
            address: address.val(),
            phone: phone.val(),
            email: email.val(),
            city: city.val(),
            orderItems: orderItems,
            paymentMethod: selectedValue
        }

        fetch("/api/products/checkout-order", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(infoOrder)
        })
            .then(res => res.json())
            .then(res => {
                if(res.statusCode === 200) {
                    flashMessage("Đặt hàng thành công", "success");
                    window.location.href = "/home";
                    document.documentElement.style.setProperty('--cart-count', "'0'");
                }
                else {
                    flashMessage(res.message, "error");
                }
            })
            .catch(err => console.log(err))
    }
}

function validateForm() {
    if (!name.val() || !address.val() || !city.val() || !email.val() || !phone.val()) {
        alert('Vui lòng kiểm tra lại thông tin đặt hàng.');
        return false;
    }
    return true;
}

function displayCheckout(arr) {

    arr.map(i => {
        total += i.totalPrice;
        let html = `<tr class="cart_item">
                                    <td class="product-name">
                                        ${i.product.name} <strong class="product-quantity"> x ${i.quantity}</strong>
                                    </td>
                                    <td class="product-total">
                                        <span class="amount">${i.totalPrice.toLocaleString('vi-VN') + " đ"}</span>
                                    </td>
                                </tr>`
        tbody.append(html);
    })
    displayFeeShipping(total);
    CartSubtotalPrice.text((total-35000).toLocaleString('vi-VN') + " đ");
    OrderTotalPrice.text(total.toLocaleString('vi-VN') + " đ");
}

function addOrderItem(arr) {
    arr.map(i => {
        let orderItem = {
            productId: i.product.id,
            quantity: i.quantity,
            unitPrice: i.totalPrice
        }
        orderItems.push(orderItem);
    })
}

function flashMessage(message, type) {
    Swal.fire({
        position: "center",
        icon: type,
        title: message,
        showConfirmButton: false,
        timer: 1500,
    });
}

function displayFeeShipping(totalProduct) {
    if(totalProduct >= 1000000) {
        shippingFee = 0;
        console.log(118)
        $(".free-shipping").removeClass("d-none");
        $(".shipping-fee").addClass("d-none");
    }
    else {
        shippingFee = 35000;
        total += shippingFee;
        $(".free-shipping").addClass("d-none");
        $(".shipping-fee").removeClass("d-none");
    }
}