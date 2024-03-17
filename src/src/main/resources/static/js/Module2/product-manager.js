const tbody = $(".tbody");
const btnAdd = $(".btn-add-product");
const btnExportExcel = $("#btn-export-excel");
const fileInput = $("#file-input");
const productName = $("#product-name");
const productImage = $("#product-image");
const importPrice = $("#import-price");
const salePrice = $("#sale-price");
const quantity = $("#quantity");
const description = $("#description-product");
const category = $("#category");
const deletedModal = $("#delete-modal");
const updateModal = $("#update-product-modal");

let tr;
let barCode;

//call api product
fetchData();
function fetchData() {
    fetch("/api/products/get-all-products")
        .then((res) => res.json())
        .then((res) => {
            displayProduct(res.data);
        });
}

function getCategories() {
    fetch("/api/categories/get-list-category")
        .then((res) => res.json())
        .then((res) => {
            displayListCategory(res.data);
        })
        .catch((error) => {
            console.error("Error:", error);
        });
}

//thêm sản phẩm
function addProduct() {
    //validate data
    if (
        validateData(
            productName.val(),
            productImage.val(),
            importPrice.val(),
            salePrice.val(),
            quantity.val(),
            description.val(),
            category.val()
        )
    ) {
        const form = document.getElementById("from-add-product");
        const formData = new FormData(form);
        $.ajax({
            url: "/api/products/add-product",
            type: "POST",
            processData: false, // Prevent jQuery from processing the data
            contentType: false, // Prevent jQuery from setting the content type
            data: formData,
            success: function (data) {
                // Handle success
                if (data.statusCode === 200) {
                    //ẩn modal
                    $("#addProductModal").modal("hide");
                    showToast(data.message, true);
                    displayOneProduct(data.data);
                }
            },
            error: function (error) {
                // Handle error
                console.error("Error:", error);
            },
        });
    }
}

function displayProduct(arr) {
    tbody.empty();
    arr.map((p) => {
        let color = p.currentQuantity <10  ? "table-warning" : "";
        let imageUrl = p.imageUrl.startsWith('http') ? p.imageUrl : `http://localhost:8080/${p.imageUrl}`;
        const importDate =
            p.createdDate === null
                ? "-"
                : new Date(p.createdDate).toLocaleDateString("vi-VN");
        let html = `<tr class="${color}">
    <td><i class="bar-code fab fa-angular fa-lg text-danger me-3"></i> <strong class= "bar-code">${
            p.id
        }</strong></td>
    <td class= "name">${p.name}</td>
    <td>
      <ul class="list-unstyled users-list m-0 avatar-group d-flex align-items-center">
        <li data-bs-toggle="tooltip" data-popup="tooltip-custom" data-bs-placement="top"
          class="avatar avatar-xs pull-up" title="${p.name}">
          <img
            src="${imageUrl}"
            alt="Avatar" class="rounded-circle" />
        </li>
        <li data-bs-toggle="tooltip" data-popup="tooltip-custom" data-bs-placement="top"
          class="avatar avatar-xs pull-up" title="${p.name}">
          <img
            src="${imageUrl}"
            alt="Avatar" class="rounded-circle" />
        </li>
        <li data-bs-toggle="tooltip" data-popup="tooltip-custom" data-bs-placement="top"
          class="avatar avatar-xs pull-up" title="${p.name}">
          <img
            src="${imageUrl}"
            alt="Avatar" class="rounded-circle" />
        </li>
      </ul>
    </td>
    <td class="config">${importDate}</td>
    <td class="import-price">${Number(p.importPrice).toLocaleString("vi", {
            style: "currency",
            currency: "VND",
        })}</td>
    <td class="sale-price">${Number(p.salePrice).toLocaleString("vi", {
            style: "currency",
            currency: "VND",
        })}</td>
    <td>${p.category.name}</td>
    <td class="currentQuantity">${p.currentQuantity}</td>
    <td><span class="badge bg-label-primary me-1 sale-number">${
            p.saleNumber
        }</span></td>
    <td>
      <div class="dropdown">
        <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
          <i class="bx bx-dots-vertical-rounded"></i>
        </button>
        <div class="dropdown-menu">
          <a class="dropdown-item" href="javascript:void(0);" onclick="updateProduct(this)"><i class="bx bx-edit-alt me-1"></i> Sửa</a>
          <a class="dropdown-item" href="javascript:void(0);" onclick="deletedProduct(this)"><i class="bx bx-trash me-1"></i> Xoá</a>
        </div>
      </div>
    </td>
  </tr>`;
        tbody.append(html);
    });
}

function displayListCategory(arr) {
    category.empty();
    category.append(`<option selected>Chọn danh mục</option>`);
    arr.map((c) => {
        let html = `<option value="${c.categoryName}">${c.categoryName}</option>`;
        category.append(html);
    });
}

function displayOneProduct(p) {
    let imageUrl = p.imageUrl.startsWith('http') ? p.imageUrl : `http://localhost:8080/${p.imageUrl}`;
    const importDate =
        p.createdDate === null
            ? "-"
            : new Date(p.createdDate).toLocaleDateString("vi-VN");
    let html = `<tr>
    <td><i class="bar-code fab fa-angular fa-lg text-danger me-3"></i> <strong class= "bar-code">${
        p.id
    }</strong></td>
    <td class= "name">${p.name}</td>
    <td>
      <ul class="list-unstyled users-list m-0 avatar-group d-flex align-items-center">
        <li data-bs-toggle="tooltip" data-popup="tooltip-custom" data-bs-placement="top"
          class="avatar avatar-xs pull-up" title="${p.name}">
          <img
            src="${imageUrl}"
            alt="Avatar" class="rounded-circle" />
        </li>
        <li data-bs-toggle="tooltip" data-popup="tooltip-custom" data-bs-placement="top"
          class="avatar avatar-xs pull-up" title="${p.name}">
          <img
            src="${imageUrl}"
            alt="Avatar" class="rounded-circle" />
        </li>
        <li data-bs-toggle="tooltip" data-popup="tooltip-custom" data-bs-placement="top"
          class="avatar avatar-xs pull-up" title="${p.name}">
          <img
            src="${imageUrl}"
            alt="Avatar" class="rounded-circle" />
        </li>
      </ul>
    </td>
    <td class="config">${importDate}</td>
    <td class="import-price">${Number(p.importPrice).toLocaleString("vi", {
        style: "currency",
        currency: "VND",
    })}</td>
    <td class="sale-price">${Number(p.salePrice).toLocaleString("vi", {
        style: "currency",
        currency: "VND",
    })}</td>
    <td>${p.category.name}</td>
    <td class="currentQuantity">${p.currentQuantity}</td>
    <td><span class="badge bg-label-primary me-1 sale-number">${
        p.saleNumber
    }</span></td>
    <td>
      <div class="dropdown">
        <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
          <i class="bx bx-dots-vertical-rounded"></i>
        </button>
        <div class="dropdown-menu">
          <a class="dropdown-item" href="javascript:void(0);" onclick="updateProduct(this)"><i class="bx bx-edit-alt me-1"></i> Sửa</a>
          <a class="dropdown-item" href="javascript:void(0);" onclick="deletedProduct(this)"><i class="bx bx-trash me-1"></i> Xoá</a>
        </div>
      </div>
    </td>
  </tr>`;
    $(html).appendTo(tbody);
}

function showToast(message, isSuccess) {
    const toastElement = $("#liveToast");
    const toastMessageElement = $(".toast-body");

    // Set background color based on success or failure
    if (isSuccess) {
        toastElement.removeClass("bg-danger");
        toastElement.addClass("bg-success");
    } else {
        toastElement.removeClass("bg-success");
        toastElement.addClass("bg-danger");
    }

    // Set the message
    toastMessageElement.text(message);
    toastElement.show();

    setTimeout(function () {
        toastElement.hide();
    }, 1500);
}

function validateData(
    productName,
    productImage,
    importPrice,
    salePrice,
    size,
    ram,
    rom,
    descriptions,
    categorys
) {
    console.log(categorys);
    if (
        productName === "" ||
        productImage === "" ||
        importPrice === 0 ||
        salePrice === 0 ||
        size === "" ||
        ram === null ||
        rom === null ||
        descriptions === "" ||
        categorys === ""
    ) {
        showToast("Nhập đủ thông tin sản phẩm để thêm", false);
        return false;
    }
    return true;
}

function deletedProduct(element) {
    $(deletedModal).modal("show");
    tr = $(element).closest("tr");
    barCode = $(tr).find(".bar-code").text();
}

function confirmDeleted() {
    fetch(`/api/products/delete-product?id=${barCode}`, {
        method: "DELETE",
    })
        .then((res) => res.json())
        .then((res) => {
            if (res.statusCode === 200) {
                $(deletedModal).modal("hide");
                showToast(res.message, true);
                $(tr).remove();
            } else {
                $(deletedModal).modal("hide");
                showToast(res.message, false);
            }
        });
}

function updateProduct(element) {
    tr = $(element).closest("tr");
    $("#bar-code").val(parseInt($(tr).find(".bar-code").text()));
    $("#product-name-update").val($(tr).find(".name").text());
    $("#import-price-update").val(
        convertCurrencyStringToNumber($(tr).find(".import-price").text())
    );
    $("#sale-price-update").val(
        convertCurrencyStringToNumber($(tr).find(".sale-price").text())
    );
    $("#quantity-number-update").val(parseInt($(tr).find(".currentQuantity").text()));

    $(updateModal).modal("show");
}

function confirmUpdateProduct() {
    const form = document.getElementById("from-update-product");
    const formData = new FormData(form);

    $.ajax({
        url: "/api/products/update-product",
        type: "PUT",
        processData: false,
        contentType: false,
        data: formData,
        success: function (data) {
            // Handle success
            if (data.statusCode === 200) {
                //ẩn modal
                fetchData();
                $("#update-product-modal").modal("hide");
                showToast(data.message, true);
            } else {
                $("#update-product-modal").modal("hide");
                showToast(data.message, false);
            }
        },
        error: function (error) {
            // Handle error
            console.error("Error:", error);
        },
    });
}

function exportExcel() {
    fetch("/api/products/export-product-excel",{
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        },
    })
        .then((res) => res.blob())
        .then((blob) => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement("a");
            a.href = url;
            a.download = "Product-Statistics.xlsx";
            a.click();
        })
        .catch(error => {
            showToast('Error downloading Excel:', false);
        });
}

function importExcel() {
    fileInput.click();
}

fileInput.on("change", function (e) {
    const file = e.target.files[0];
    const reader = new FileReader();

    reader.onload = function (e) {
        const data = e.target.result;
        const workbook = XLSX.read(data, { type: 'binary' });
        const sheet = workbook.Sheets[workbook.SheetNames[0]];
        const jsonData = XLSX.utils.sheet_to_json(sheet);
        const arrayOfObjects = jsonData.map(row => {
            return {
                name: row['Tên sản phẩm'],
                categoryName: row['Loại sản phẩm'],
                image: row['Hình ảnh'],
                description: row['Mô tả'],
                quantity: row['Số lượng hiện có'],
                createdDate: new Date(row['Ngày nhập']),
                importPrice: row['Giá nhập'],
                priceSale: row['Giá bán'],
            }
        });
        $.ajax({
            url: "/api/products/import-product-excel",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(arrayOfObjects),
            success: function (data) {
                // Handle success
                if (data.statusCode === 200) {
                    fetchData();
                    showToast(data.message, true);
                } else {
                    showToast(data.message, false);
                }
            },
            error: function (error) {
                // Handle error
                showToast("Có lỗi xảy ra vui lòng thử lại.", false);
                console.error("Error:", error);
            },
        });
    };
    reader.readAsBinaryString(file);
});

let timer;
let keywords = $(".keywords")
function searchProduct() {
    const val = keywords.val() || "all";
    console.log(val);

    clearTimeout(timer);
    timer = setTimeout(() => {
        fetch(`/api/products/searchs?keyword=${val}&pageIndex=${1}&pageSize=${1000}`)
            .then((res) => res.json())
            .then((res) => {
                displayProduct(res.data);
            })
            .catch((err) => console.log(err));
    }, 700);
}

//convert string to number
function convertCurrencyStringToNumber(currencyString) {
    const numericString = currencyString.replace(/[^\d]/g, "");
    const result = parseInt(numericString, 10);

    return isNaN(result) ? null : result;
}
