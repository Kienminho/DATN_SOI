const tbody = $(".tbody");
const quantityInput = $(".quantity");
const totalPrice = $(".total");
const subtotal = $(".subtotal");
const totalAmount = $(".total-amount");
let shippingFee = 0;
let setQuantity;

fetch("/api/products/cart")
    .then(res => res.json())
    .then(res => {
        console.log(res.data);
        displayCart(res.data);
    })
    .catch(err => console.log(err));
function displayCart(arr) {
    tbody.empty();
    arr.map(item => {
        let html= `<tr>
                                <td class="id-product d-none">${item.product.id}</td>
                                <td class="product-thumbnail"><a href="#"><img src="${item.product.imageUrl}" alt="" /></a></td>
                                <td class="product-name"><a href="#">${item.product.name}</a></td>
                                <td class="product-price"><span class="amount">${item.product.salePrice.toLocaleString('vi-VN')} ₫</span></td>
                                <td class="product-quantity"><input type="number" oninput="changeQuantity(this)" name="quantity" class="quantity" value="${item.quantity}"/></td>
                                <td class="product-subtotal total">${item.totalPrice.toLocaleString('vi-VN')} ₫</td>
                                <td class="product-remove"><a onclick="removeProduct(this)" href="#" data-product-id="${item.product.id}"><i class="fa fa-times"></i></a></td>
                            </tr>`
        tbody.append(html);
    });
    calculateTotal();
}



//delele product in cart
function removeProduct(element) {
    console.log(30);
    event.preventDefault();
    fetch("/api/products/delete-product-in-cart", {
        method: "POST",
        headers: {
            "Content-type": "application/json",
        },
        body: JSON.stringify({id: element.dataset.productId})
    })
        .then(res => res.json())
        .then(res => {
            if (res.statusCode === 200) {
                element.closest("tr").remove();
                flashMessage("Xóa sản phẩm thành công", "success");
                document.documentElement.style.setProperty('--cart-count', "'" + res.data + "'");
                calculateTotal();
            }
        })
        .catch(err => console.log(err))
}

function changeQuantity(element) {
    clearTimeout(setQuantity);
    setQuantity = setTimeout(() => {
        let quantity = element.value;
        if(quantity < 1) {
            quantity = 1;
            element.value = quantity; // Set the input value to 1
            flashMessage("Số lượng không thể nhỏ hơn 1!", "error");
        }

        const id = $(element).closest('tr').find('.id-product').text();
        const  amount = $(element).closest('tr').find('.amount').text();
        const price = parseInt(amount.replace(/[^\d.]/g, ''));
        const totalPrice = price * quantity * 1000
        displayFeeShipping(totalPrice);
        $(element).closest('tr').find('.total').text(totalPrice.toLocaleString('vi-VN'));

        fetch("/api/products/change-quantity-in-cart", {
            method: "POST",
            headers: {
                "Content-type": "application/json"
            },
            body: JSON.stringify({idProduct: id, quantity: quantity, totalPrice: totalPrice})
        })
            .then(res => res.json())
            .then(res => {
                if(res.statusCode === 200)
                    document.documentElement.style.setProperty('--cart-count', "'" + res.data + "'");
                calculateTotal();
            })
            .catch(err => console.log(err))

    },500);
}

function calculateTotal() {
    let total = 0;
    $('.total').each(function() {
        total += parseInt($(this).text().replace(/[^\d,]/g, '').replace(/,/g, '')) ;
        console.log(total);
    });
    displayFeeShipping(total);
    let subTotal = total;
    total += shippingFee;
    subtotal.text(subTotal.toLocaleString('vi-VN')+ " đ");
    totalAmount.text(total.toLocaleString('vi-VN')+ " đ");
}

function flashMessage(message, type) {
    Swal.fire({
        position: "center",
        icon: type,
        title: message,
        showConfirmButton: false,
        timer: 1000,
    });
}

function displayFeeShipping(total) {
    if(total >= 1000000) {
        shippingFee = 0;
        console.log(118)
        $(".free-shipping").removeClass("d-none");
        $(".shipping-fee").addClass("d-none");
    }
    else {
        shippingFee = 35000;
        $(".free-shipping").addClass("d-none");
        $(".shipping-fee").removeClass("d-none");
    }
}
