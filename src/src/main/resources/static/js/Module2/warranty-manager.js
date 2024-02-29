const tbody = $(".tbody");
const category = $("#category");
const delivery = $("#delivery");
let idOrderCurrent = 0;

fetchOrders();
function fetchOrders() {
    fetch('/api/guarantee/get-all-guarantee')
        .then(response => response.json())
        .then(res => {
            displayOrders(res.data);
        })
        .catch(error => {
            console.log(error.message);
            showToast("Đã có lỗi xảy ra!", false)
        });
}

function getListShipper() {
    fetch('/api/staff-and-shipper/get-all-staff')
        .then(response => response.json())
        .then(res => {
            displayShipper(res.data);
        })
        .catch(error => {
            console.log(error.message);
            showToast("Đã có lỗi xảy ra!", false)
        });

}

function displayShipper(arr) {
    category.empty();
    category.append(`<option selected>Chọn</option>`);
    arr.map((c) => {
        let html = `<option value="${c.fullName}">${c.fullName}</option>`;
        category.append(html);
    });
}

function displayOrders(arr) {
    tbody.empty();
    arr.map((i, index) => {
        let disable = i.nameStaff != null ? "pe-none" : "";
        let object = getStatus(i.status);
        let date = i.guaranteeDate != null ? convertDate(i.guaranteeDate) : "_";
        let html = `<tr>
            <td class="id">${i.id}</td>
            <<td class="name-product">${i.nameProduct ?? "_"}</td>
            <td class="name-customer">${i.nameCustomer ?? "_"}</td>
            <td class="phone">${i.phone ?? "_"}</td>
            <td class="address">${i.address ?? "_"}</td>
            <td class="date">${convertDate(i.complainDate) ?? "_"}</td>
            <td class="description">${i.description ?? "_"}</td>
            <td class="status"><span class="badge ${object.color}">${object.name}</span></td>
            <td class="nameStaff">${i.nameStaff ?? "_"}</td>
            <td class="compelete-date">${date}</td>
            <td>
      <div class="dropdown">
        <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
          <i class="bx bx-dots-vertical-rounded"></i>
        </button>
        <div class="dropdown-menu">
          <a class="dropdown-item ${disable}" id="delivery" onclick="handleDelivery(this)" href="javascript:void(0);"><i class="bx bx-edit-alt me-1"></i>Bảo hành</a>
          <a class="dropdown-item ${object.status}" href="javascript:void(0);" onclick="printInvoice(this)"><i class='bx bx-receipt me-1'></i>Từ chối</a>
        </div>
      </div>
    </td>
        </tr>`;
        tbody.append(html);
    });
}

function handleDelivery(e) {
    //get id order
    idOrderCurrent = $(e).closest("tr").find(".id").text();
    getListShipper();
    $(delivery).modal("show");
}

function printInvoice(e) {
    const id = $(e).closest("tr").find(".id").text();
    const body = {
        guaranteeId: id,
        status: 2,
        guaranteeDate: Date.now()
    };

    fetch(`/api/guarantee/update-guarantee`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    })
        .then(response => response.json())
        .then(res => {
            fetchOrders();
            showToast(res.data, true);
        })
        .catch(error => {
            console.log(error.message);
            showToast("Đã có lỗi xảy ra!", false)
        });
}

function sendOrderToShipper() {
    const id = $(category).val();
    const body = {
        guaranteeId: idOrderCurrent,
        nameStaff: id,
        status:1
    };

    fetch(`/api/guarantee/update-guarantee`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    })
        .then(response => response.json())
        .then(res => {
            fetchOrders();
            showToast("Phân công bảo hành thành công!", true);
            $(delivery).modal("hide");
        })
        .catch(error => {
            console.log(error.message);
            showToast("Đã có lỗi xảy ra!", false)
        });
}

//convert date to string
function convertDate(date) {
    let d = new Date(date);
    return d.toLocaleDateString("en-GB");
}

function getStatus(status) {
    switch (status) {
        case "HANDEL":
            return {name:"Đang xử lý", color: "bg-label-warning", status: ""};
        case "CANCELED":
            return {name:"Đã hủy bỏ", color: "bg-label-danger",status: "pe-none"};
        case "DONE":
            return {name:"Đã xử lý", color: "bg-label-success",status: "pe-none"};
        default:
            return {name:"Đã đăng kí bảo hành", color: "bg-label-secondary", status: ""};
    }
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
    }, 2000);
}