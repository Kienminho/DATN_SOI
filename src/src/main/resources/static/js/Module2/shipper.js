const tbody = $(".tbody");

fetchData();
function fetchData() {
    fetch('/api/staff-and-shipper/get-order-by-shipper-id')
        .then(response => response.json())
        .then(data => {
            if(data.statusCode === 200) {
                displayData(data.data);
            } else {
                showToast(data.message, false);
            }
        });
}

function displayData(arr) {
    tbody.empty();
    arr.map((i, index) => {
        let status = (i.status === 1) ? "pe-none" : "";
        let object = getStatus(i.status);
        let html = `<tr>
            <td class="id d-none">${i.id}</td>
            <td>${index+1}</td>
            <<td class="name">${i.customerName ?? "_"}</td>
            <td class="phone">${i.customerPhone ?? "_"}</td>
            <td class="address">${i.customerAddress ?? "_"}</td>
            <td class="date">${convertDate(i.createdDate) ?? "_"}</td>
            <td class="status"><span class="badge ${object.color}">${object.name}</span></td>
            <td class="payment-method">${i.paymentMethod === "T" ? "Ngân hàng" : "Tiền mặt"}</td>
            <td class="total-money">${convertMoney(i.orderMoney) ?? "_"}</td>
            <td class="total-money">${convertMoney(i.collectionMoney) ?? "_"}</td>
            <td>
      <div class="dropdown">
        <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
          <i class="bx bx-dots-vertical-rounded"></i>
        </button>
        <div class="dropdown-menu">
          <a class="dropdown-item ${status}" id="delivery" onclick="DeliveryConfirm(this)" href="javascript:void(0);"><i class="bx bx-edit-alt me-1"></i>Xác nhận giao hàng</a>
          <a class="dropdown-item" href="javascript:void(0);" onclick="CancelOrder(this)"><i class='bx bxs-message-x me-1'></i>Đơn hàng từ chối</a>
        </div>
      </div>
    </td>
        </tr>`;
        tbody.append(html);
    });
}

function DeliveryConfirm(e) {
    const orderId = $(e).closest("tr").find(".id").text();
    fetch(`/api/staff-and-shipper/update-status-order`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            orderId: orderId,
            status: 1
        })
    })
        .then(response => response.json())
        .then(data => {
            if(data.statusCode === 200) {
                showToast(data.message, true);
                fetchData();
            } else {
                showToast(data.message, false);
            }
        });
}

function convertMoney(money) {
    return Number(money).toLocaleString("vi", {
        style: "currency",
        currency: "VND",
    })
}

function CancelOrder(e) {
    const orderId = $(e).closest("tr").find(".id").text();
    fetch(`/api/staff-and-shipper/update-status-order`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            orderId: orderId,
            status: 2
        })
    })
        .then(response => response.json())
        .then(data => {
            if(data.statusCode === 200) {
                showToast(data.message, true);
                fetchData();
            } else {
                showToast(data.message, false);
            }
        });

}

function convertDate(date) {
    let d = new Date(date);
    return d.toLocaleDateString("en-GB");
}

function getStatus(status) {
    switch (status) {
        case 1:
            return {name:"Đã giao hàng", color: "bg-label-success"};
        case 2:
            return {name:"Đã huỷ đơn hàng", color: "bg-label-danger"};
        default:
            return {name:"Đang giao hàng", color: "bg-label-secondary"};

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