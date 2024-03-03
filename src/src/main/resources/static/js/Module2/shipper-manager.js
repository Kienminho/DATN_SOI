const tbody = $(".tbody");
const deletedModal = $("#delete-modal");
const employeeName = $("#employee-name");
const email = $("#email");
const address = $("#address");
const phoneNumber = $("#phone-number");
let tr;
let id;

// hiển thị dữ liệu
fetchData();

function fetchData() {
    fetch("/api/staff-and-shipper/get-all-shippers")
        .then((res) => res.json())
        .then((res) => {
            console.log(res.data);
            displayData(res.data);
        })
        .catch((err) => {
            showToast("Không tìm thấy dữ liệu", false);
        });
}

function createEmployee() {
    if (
        validateForm(
            employeeName.val(),
            email.val(),
            address.val(),
            phoneNumber.val()
        )
    ) {
        //call api to create employee
        const data = {
            fullName: employeeName.val(),
            email: email.val(),
            address: address.val(),
            phoneNumber: phoneNumber.val(),
        };

        fetch("/api/staff-and-shipper/create-staff", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
        })
            .then((res) => res.json())
            .then((res) => {
                if (res.statusCode === 200) {
                    fetchData();
                    $("#create-employee-modal").modal("hide");
                    showToast("Tạo mới thành công", true);
                    $("#form-create-employee")[0].reset();
                } else {
                    $("#create-employee-modal").modal("hide");
                    showToast(res.message, false);
                }
            })
            .catch((err) => {
                showToast("Vui lòng thử lại sau ít phút", false);
                console.log(err.message);
            });
    }
}

function deletedProduct(element) {
    $(deletedModal).modal("show");
    tr = $(element).closest("tr");
    id = $(tr).find(".id").text();
}

function confirmDeleted() {
    fetch(`/api/staff-and-shipper/delete/${id}`, {
        method: "DELETE",
    })
        .then((res) => res.json())
        .then((res) => {
            if (res.statusCode === 200) {
                $(deletedModal).modal("hide");
                showToast(res.message, true);
                fetchData();
            } else {
                $(deletedModal).modal("hide");
                showToast(res.message, false);
            }
        });
}

function reactivateAccount(element) {
    tr = $(element).closest("tr");
    id = $(tr).find(".id").text();
    fetch("/api/staff-and-shipper/reactive", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ id: id }),
    })
        .then((res) => res.json())
        .then((res) => {
            if (res.statusCode === 200) {
                showToast(res.message, true);
            } else {
                showToast(res.message, false);
            }
        });
}

function displayData(arr) {
    tbody.empty();
    arr.map((i, index) => {
        let html = `<tr>
            <td class="id d-none">${i.id}</td>
            <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${
            index + 1
        }</strong></td>
            <td class="name"><strong class="cursor-pointer" onclick="getInfoDetail(this)">${i.fullName}</strong></td>
            <td class="email">${i.email}</td>
            <td class="address">${i.address}</td>
            <td class="phone">${i.phoneNumber}</td>
            <td>
                <span class="badge ${
            i.isActivated ? "bg-label-success" : "bg-label-danger"
        } me-1">
                    ${i.isActivated ? "Đã kích hoạt" : "Chưa kích hoạt"}
                </span>
            </td>
            <td>
                <span class="badge ${
            i.isDeleted ? "bg-label-danger" : "bg-label-success"
        } me-1">
                    ${i.isDeleted ? "Đã     Khoá" : "Hoạt đông"}
                </span>
            </td>
            <td>
                <div class="dropdown">
                    <button type="button" class="btn p-0 dropdown-toggle hide-arrow"
                        data-bs-toggle="dropdown">
                        <i class="bx bx-dots-vertical-rounded"></i>
                    </button>
                    <div class="dropdown-menu">
                        <a class="dropdown-item ${
            i.isActivated ? "d-none" : "d-block"
        }" href="javascript:void(0);" onclick="reactivateAccount(this)"><i
                                class="bx bx-edit-alt me-1"></i>Kích hoạt</a>
                        <a class="dropdown-item" href="javascript:void(0);" onclick="deletedProduct(this)"><i
                                class="bx bx-trash me-1" ></i> ${
            i.isDeleted ? "Mở khoá" :"Khoá"
        }</a>
                    </div>
                </div>
            </td>
        </tr>`;
        tbody.append(html);
    });
}

function getInfoDetail(e){
    const id = $(e).closest("tr").find(".id").text();
    fetch(`/api/staff-and-shipper/get-order-by-shipper-id/${id}`)
        .then(res => res.json())
        .then(res => {
            displayDetail(res.data);
            $("#detail-shipper-modal").modal("show");
        })
        .catch(error => {
            console.log(error.message);
            showToast("Đã có lỗi xảy ra!", false)
        });
}

function displayDetail(arr){
    const tbodyDetail = $(".tbody-detail");
    tbodyDetail.empty();
    arr.map((i, index) => {
        let object = getStatus(i.status);
        let html = `<tr>
            <td>${i.id}</td>
            <td>${i.customerName}</td>
            <td>${convertDate(i.createdDate)}</td>
            <td class="status"><span class="badge ${object.color}">${object.name}</span></td>
            <td>${convertMoney(i.orderMoney)}</td>
        </tr>`;
        tbodyDetail.append(html);
    });
}

function convertMoney(money) {
    return Number(money).toLocaleString("vi", {
        style: "currency",
        currency: "VND",
    })
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

function validateForm(employeeName, email, address, phoneNumber) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (
        employeeName.trim() === "" ||
        email.trim() === "" ||
        address.trim() === "" ||
        phoneNumber.trim() === ""
    ) {
        showToast("Nhập đầy đủ thông tin để tạo nhân viên mới.", false);
        return false;
    }

    if (!emailRegex.test(email)) {
        showToast("Email không hợp lệ. Vui lòng nhập đúng định dạng email.", false);
        return false;
    }

    return true;
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
