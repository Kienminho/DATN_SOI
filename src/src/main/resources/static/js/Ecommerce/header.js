//get data when user login
fetch("/api/users/check-login")
    .then(res => res.json())
    .then(data => {
        if (data.statusCode === 200) {
            $(".account-Dropdown").removeClass("d-none");
            $(".logout").removeClass("d-none");
            $(".username").text(data.data.username);
            $(".create-new-account").addClass("d-none");
            $(".login").addClass("d-none");
            $(".lnr-heart").removeClass("d-none");
            $(".shopping-cart").removeClass("d-none");
        }
        else {
            $(".account-Dropdown").addClass("d-none");
            $(".logout").addClass("d-none");
            $(".create-new-account").removeClass("d-none");
            $(".login").removeClass("d-none");
        }
    })
    .catch(err => {
        console.log(err);
    })
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
