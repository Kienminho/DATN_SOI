const wrapperProducts = $(".wrapper-product");
const select = $(".cust-select");
const sliderRange = $("#slider-range");
const productsPerPage = 12;
const urlParams = new URLSearchParams(window.location.search);
let category = urlParams.get('category') || 'Dây chuyền';
let pageIndex = urlParams.get('pageIndex') || 1
let currentData;
let sliderTimer;

document.addEventListener('DOMContentLoaded', function() {
    fetchData(category, 1);
});

//filter by price
sliderRange.on('sliderChange', function(event, values) {
    clearTimeout(sliderTimer);
    sliderTimer = setTimeout(() => {
        // Calculate the start index based on pageIndex and pageSize
        let startIndex = (pageIndex - 1) * productsPerPage;

        // Slice the currentData based on the calculated startIndex and pageSize
        let slicedData = currentData.slice(startIndex, startIndex + productsPerPage);

        // Apply the filtering based on slider values
        let filteredData = slicedData.filter(p => {
            return p.salePrice >= values[0] && p.salePrice <= values[1];
        });

        // Display products after filtering
        select.find('option:eq(0)').prop('selected', true);
        displayProducts(wrapperProducts, filteredData);
    }, 500);

});

//change select, choose options sort
select.on('change', () => {
    let i = select.prop('selectedIndex');
    let startIndex = (pageIndex - 1) * productsPerPage;
    let slicedData = currentData.slice(startIndex, startIndex + productsPerPage);
    let filteredData

    if(i === 1) {
        filteredData = slicedData.slice().sort((a, b) => a.salePrice - b.salePrice);
    }
    else if(i === 2) {
        filteredData = slicedData.slice().sort((a, b) => b.salePrice - a.salePrice);
    }
    else {
        filteredData = slicedData.sort((a, b) =>a.name.localeCompare(b.name));
    }
    displayProducts(wrapperProducts, filteredData);
});

function fetchData(category, pageIndex) {
    select.find('option:eq(0)').prop('selected', true);
    fetch(`/api/products/get-products-by-category?category=${category}&pageIndex=${pageIndex}`)
        .then(res => res.json())
        .then(data => {
            currentData = data.data;
            displayProducts(wrapperProducts, data.data);
            displayPagination(data.totalRecord, productsPerPage);
            const newUrl = `?category=${category}&pageIndex=${pageIndex}`;
            window.history.pushState({ category, pageIndex }, '', newUrl);
        })
        .catch(err => console.log(err));
}

function uncheckOtherCheckboxes(checkbox) {
    // Get all checkboxes within the same category
    let checkboxes = document.querySelectorAll('.caregory input[type="checkbox"]');
    let label = checkbox.parentElement;

    // Get the HTML content of the label
    category = label.textContent.trim();
    fetchData(category, 1);

    // Uncheck the other checkboxes
    checkboxes.forEach(function (cb) {
        // Log the HTML content to the console (you can do whatever you want with it)
        if (cb !== checkbox) {
            cb.checked = false;
        }
    });
}

function displayProducts(element,list) {
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

//hiển thị số lượng page
function displayPagination(totalProducts, productsPerPage) {
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
    fetchData(category, pageIndex);
}

function addToCart(element) {
    event.preventDefault();
    fetch("/api/products/add-product-to-cart", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({productId: element.dataset.productId, quantity: 1})
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

