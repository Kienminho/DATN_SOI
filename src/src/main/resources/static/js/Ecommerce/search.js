const wrapperProducts = $(".wrapper-product");
const selects = $(".cust-select");
const productsPerPage = 12;
// path của đường dẫn hiện tại
let path = window.location.pathname
// lấy url hiện tại
let currentURL = new URL(window.location.href);
//lấy giá trị keyword
const keyword = currentURL.searchParams.get("keyword");
const pageIndex = currentURL.searchParams.get("pageIndex") || 1;
let listProducts;

document.addEventListener('DOMContentLoaded', function() {
    fetchDateGetSearch(keyword, 1);
});

function fetchDateGetSearch(key, pageIndex) {
    selects.find('option:eq(0)').prop('selected', true);
    fetch(`/api/products/searchs?keyword=${key}&pageIndex=${pageIndex}`)
        .then(res => res.json())
        .then(data => {
            listProducts = data.data;
            displaySearchProducts(wrapperProducts, data.data);
            displayPage(data.totalRecord, productsPerPage);
            const newUrl = `?keyword=${key}&pageIndex=${pageIndex}`;
            window.history.pushState({ key, pageIndex }, '', newUrl);
        })
        .catch(err => console.log(err));
}

function displaySearchProducts(element,list) {
    element.empty();
    list.map(product => {
        let html = `<div class="col-lg-3 col-md-4 col-sm-6 col-xs-12">
                                    <div class="single-new-product mt-40 category-new-product">
                                        <div class="product-img">
                                            <a href="/product/${product.id}">
                                                <img src="${product.imageUrl}" class="first_img" alt="" />

                                            </a>
                                            <div class="new-product-action">
                                                <!-- <a href="#"><span class="lnr lnr-sync"></span></a> -->
                                                <a href="#" data-product-id="${product.id}" onclick="addToCart(this)"><span class="lnr lnr-cart cart_pad"></span>Add to Cart</a>
                                                <a href="#"><span class="lnr lnr-heart"></span></a>
                                            </div>
                                        </div>
                                        <div class="product-content text-center">
                                            <a href="/product/${product.id}"><h3>${product.name}</h3></a>
                                            <div class="product-price-star">
                                                <i class="fa fa-star"></i>
                                                <i class="fa fa-star"></i>
                                                <i class="fa fa-star"></i>
                                                <i class="fa fa-star-o"></i>
                                                <i class="fa fa-star-o"></i>
                                            </div>
                                            <div class="price">
                                                <h4>${product.salePrice.toLocaleString('vi-VN')} ₫</h4>
                                            </div>
                                        </div>
                                    </div>
                                </div>`
        element.append(html);
    });
}

function displayPage(totalProducts, productsPerPage) {
    // Calculate the total number of pages
    const totalPages = Math.ceil(totalProducts / productsPerPage);

    // Get the pagination ul element
    const paginationElement = $(".pagination");
    paginationElement.empty();

    // Add "Page" label
    const pageLabel = `<li><a href="">Page</a></li>`;
    paginationElement.append(pageLabel);

    // Add page numbers
    for (let i = 1; i <= totalPages; i++) {
        let pageItem = `<li><a href="#" onclick="goToPage(this)">${i}</a></li>`;
        paginationElement.append(pageItem);
    }
}

function goToPage(element) {
    let pageIndex = element.textContent;
    fetchDateGetSearch(keyword, pageIndex);
}

selects.on('change', () => {
    let i = selects.prop('selectedIndex');
    let startIndex = (pageIndex - 1) * productsPerPage;
    let slicedData = listProducts.slice(startIndex, startIndex + productsPerPage);
    let filterData;

    if(i === 1) {
        filterData = slicedData.slice().sort((a, b) => a.salePrice - b.salePrice);
    }
    else if(i === 2) {
        filterData = slicedData.slice().sort((a, b) => b.salePrice - a.salePrice);
    }
    else {
        filterData = slicedData.sort((a, b) =>a.name.localeCompare(b.name));
    }
    displaySearchProducts(wrapperProducts, filterData);
});