const tbody = $(".tbody");
const btnAdd = $(".btn-add-product");
const btnExportExcel = $("#btn-export-excel");
const fileInput = $("#file-input");
const categoryName = $("#category-name");
const productImage = $("#product-image");
const description = $("#description");
const salePrice = $("#sale-price");
const quantity = $("#quantity");
const category = $("#category");
const deletedModal = $("#delete-modal");
const updateModal = $("#update-product-modal");

let tr;
let barCode;

//call api product
fetchData();
function fetchData() {
    fetch("/api/categories/get-list-category")
        .then((res) => res.json())
        .then((res) => {
            displayProduct(res.data);
        })
        .catch((error) => {
            showToast("Có lỗi xảy ra vui lòng thử lại.", false);
        });
}

//thêm sản phẩm
function addProduct() {
    if (
        validateData(
            categoryName.val(),
            description.val(),
        )
    ) {
        const form = document.getElementById("from-add-category");
        const formData = new FormData(form);
        $.ajax({
            url: "/api/categories/add-category",
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
                else {
                    $("#addProductModal").modal("hide");
                    showToast(data.message, false);
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
        const importDate =
            p.createdDate === null
                ? "-"
                : new Date(p.createdDate).toLocaleDateString("vi-VN");
        let html = `<tr>
    <td><i class="bar-code fab fa-angular fa-lg text-danger me-3"></i> <strong class= "bar-code">${
            p.id
        }</strong></td>
    <td class= "name">${p.categoryName}</td>
    <td class= "description">${p.description ?? '-'}</td>
    <td class= "total-products">${p.totalProducts}</td>
    <td class="config">${importDate}</td>
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

function displayOneProduct(p) {
    const importDate =
        p.createdDate === null
            ? "-"
            : new Date(p.createdDate).toLocaleDateString("vi-VN");
    let html = `<tr>
    <td><i class="bar-code fab fa-angular fa-lg text-danger me-3"></i> <strong class= "bar-code">${
        p.id
    }</strong></td>
    <td class= "name">${p.name}</td>
    <td class= "description">${p.description ?? '-'}</td>
    <td class= "total-products">${p.totalProducts ?? '-'}</td>
    <td class="config">${importDate}</td>
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
    categoryName,
    descriptions,
) {
    if (
        categoryName === "" ||
        descriptions === ""
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
    fetch(`/api/categories/delete-category/${barCode}`, {
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
    $("#category-name-update").val($(tr).find(".name").text());
    $("#description-update").val($(tr).find(".description").text());
    $(updateModal).modal("show");
}

function confirmUpdateProduct() {
    const form = document.getElementById("from-update-product");
    const formData = new FormData(form);

    $.ajax({
        url: "/api/categories/update-category",
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
    fetch("/api/categories/export-category-excel",{
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
            a.download = "Categories-Statistics.xlsx";
            a.click();
        })
        .catch(error => {
            showToast('Lỗi tải file, vui lòng thử lại sau.:', false);
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

//hàm lấy cắt chuỗi để lấy ram và rom
function extractNumbers(str) {
    const regex = /\b\d+\b/g;
    const matches = str.match(regex);

    return matches ? matches.map(Number) : null;
}

//convert string to number
function convertCurrencyStringToNumber(currencyString) {
    const numericString = currencyString.replace(/[^\d]/g, "");
    const result = parseInt(numericString, 10);

    return isNaN(result) ? null : result;
}
