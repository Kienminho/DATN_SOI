function addToCart() {
    const id = $(".id-product").text();
    event.preventDefault();
    fetch("/api/products/add-product-to-cart", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({productId: id})
    })
        .then(res => res.json())
        .then((res) => {
            if(res.statusCode === 400)
                alert(res.message);
            else
                document.documentElement.style.setProperty('--cart-count', "'"+res.data+ "'");
        })
        .catch((err) => console.log(err));
}