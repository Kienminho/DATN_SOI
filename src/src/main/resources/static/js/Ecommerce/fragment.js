function searchProduct() {
    const keywords = $("#keyword").val();
    window.location = `http://localhost:8080/searchs?keyword=${keywords}`;
}