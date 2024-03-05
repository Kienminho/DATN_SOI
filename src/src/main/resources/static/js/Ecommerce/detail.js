const quantity = $("#french-hens");
function addToCart() {
    const id = $(".id-product").text();
    event.preventDefault();
    fetch("/api/products/add-product-to-cart", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({productId: id, quantity: quantity.val() ?? 1})
    })
        .then(res => res.json())
        .then((res) => {
            if(res.statusCode === 400)
                flashMessage(res.message, "error");
            else
                flashMessage("Thêm vào giỏ hàng thành công", "success");
                document.documentElement.style.setProperty('--cart-count', "'"+res.data+ "'");
        })
        .catch((err) => console.log(err));
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