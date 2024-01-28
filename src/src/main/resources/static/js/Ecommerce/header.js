//get data when user login
fetch("/api/products/get-total-product-in-cart")
.then(res => res.json())
.then(data => {
    if (data.statusCode === 200)
        document.documentElement.style.setProperty('--cart-count', "'"+data.data+ "'");
})
.catch(err => {
    console.log(err);
})

function searchProduct() {
    const keywords = $("#keyword").val();
    window.location = `http://localhost:8080/searchs?keyword=${keywords}`;
}
