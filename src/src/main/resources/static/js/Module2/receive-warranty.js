const tbody = $(".tbody");

fetchData();
function fetchData() {
    fetch(`/api/guarantee/get-guarantee-by-employee`)
        .then((res) => res.json())
        .then((res) => {
            displayData(res.data);
        })
        .catch((err) => showToast(err.message, false));
}

function displayData(arr) {
    tbody.empty();
    arr.map((item,index) => {
        let html = `<tr>
            <td class="id d-none">${item.id}</td>
            <td><i class="fab fa-angular fa-lg text-danger me-3 r cursor-pointer"></i> <strong class="cursor-pointer">${index+1}</strong></td>
            <td>${item.nameProduct}</td>
            <td>${item.description}</td>
            <td>${convertDate(item.complainDate)}</td>
            <td>${convertDate(item.guaranteeDate)}</td>
            <td>
                <button class="btn btn-sm btn-success" onclick="doneWarranty(this)">Hoàn tất</button>
                <button class="btn btn-sm btn-danger" onclick="refuseWarranty(this)">Từ chối</button>
            </td>
        </tr>`;
        tbody.append(html);
    });
}

function doneWarranty(e) {
    const id = $(e).closest("tr").find(".id").text();
    const options = {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ status:3, guaranteeDate: Date.now(), guaranteeId: id }),
    };

    fetch("/api/guarantee/update-guarantee", options)
        .then((res) => res.json())
        .then((data) => {
            if (data.statusCode === 200) {
                showToast("Hoàn tất bảo hành thành công!", true);
                fetchData();
            } else {
                showToast(data.message, false);
            }
        })
        .catch((err) => showToast(err.message, false));
}

function refuseWarranty(e) {
    const id = $(e).closest("tr").find(".id").text();
    const options = {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ status:2, guaranteeDate: Date.now(), guaranteeId: id }),
    };

    fetch("/api/guarantee/update-guarantee", options)
        .then((res) => res.json())
        .then((data) => {
            if (data.statusCode === 200) {
                showToast("Đã từ chối bảo hành!", true);
                fetchData();
            } else {
                showToast(data.message, false);
            }
        })
        .catch((err) => showToast(err.message, false));
}

function convertDate(date) {
    if (!date) return "_";
    return new Date(date).toLocaleDateString("vi-VN");
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