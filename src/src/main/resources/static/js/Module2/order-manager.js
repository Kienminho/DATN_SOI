const tbody = $(".tbody");
const category = $("#category");
const delivery = $("#delivery");
let idOrderCurrent = 0;

fetchOrders();
function fetchOrders() {
    fetch('/api/orders/get-list')
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
    fetch('/api/staff-and-shipper/get-list-shipper')
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
        let html = `<option value="${c.id}">${c.userName}</option>`;
        category.append(html);
    });
}

function displayOrders(arr) {
    tbody.empty();
    arr.map((i, index) => {
        let disable = i.issueInvoice ? "pe-none" : "";
        let status = (i.status > 1) ? "pe-none" : "";
        let object = getStatus(i.status);
        let html = `<tr>
            <td class="id">${i.id}</td>
            <<td class="name">${i.name ?? "_"}</td>
            <td class="phone">${i.phoneNumber ?? "_"}</td>
            <td class="address">${i.address ?? "_"}</td>
            <td class="date">${convertDate(i.orderDateTime) ?? "_"}</td>
            <td class="status"><span class="badge ${object.color}">${object.name}</span></td>
            <td class="payment-method">${i.paymentMethod === "T" ? "Ngân hàng" : "Tiền mặt"}</td>
            <td class="total-money">${Number(i.totalPrice).toLocaleString("vi", {
            style: "currency",
            currency: "VND",
        }) ?? "_"}</td>
            <td>
      <div class="dropdown">
        <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
          <i class="bx bx-dots-vertical-rounded"></i>
        </button>
        <div class="dropdown-menu">
          <a class="dropdown-item ${status}" id="delivery" onclick="handleDelivery(this)" href="javascript:void(0);"><i class="bx bx-edit-alt me-1"></i> Giao hàng</a>
          <a class="dropdown-item ${disable}" href="javascript:void(0);" onclick="printInvoice(this)"><i class='bx bx-receipt me-1'></i>In hoá đơn</a>
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
    fetch(`/api/orders/export-invoice-by-order/${id}`)
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
        orderId: idOrderCurrent,
        shipperId: id
    };

    fetch(`/api/staff-and-shipper/send-order-to-shipper`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    })
        .then(response => response.json())
        .then(res => {
            fetchOrders();
            showToast("Giao đến shipper thành công!", true);
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
        case 2:
            return {name:"Đang giao hàng", color: "bg-label-warning"};
        case 3:
            return {name:"Đã giao hàng", color: "bg-label-success"};
        default:
            return {name:"Chờ xác nhận", color: "bg-label-primary"};
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