const tbody = $(".tbody-guarantee");
const tbodyHistory = $(".tbody-history");
const btnUpdateInfo = $(".btn-update-profile");
const btnRegisterGuarantee = $(".btn-register-guarantee");
//fetch data
//lấy thông tin user đăng nhập
fetch("/api/users/check-login")
    .then(res => res.json())
    .then(data => {
        displayProfile(data.data);
    })
    .catch(err => console.log(err.message));

//lấy thông tin bảo hành
fetch("/api/guarantee/get-guarantee-by-user")
    .then(res => res.json())
    .then(data => {
        console.log(data);
        displayGuarantee(data.data)
    })
    .catch(err => console.log(err));

//lấy lịch sử bảo hành
getHistoryGuarantee();
function getHistoryGuarantee() {
    fetch("/api/guarantee/get-history-register-guarantee")
        .then(res => res.json())
        .then(data => {
            console.log(data);
            displayHistory(data.data)
        })
        .catch(err => console.log(err));
}
//update profile
btnUpdateInfo.on("click", function () {
    //get form data and get value radio button

    const formData = $(".form-update-profile").serializeArray();
    const dataJson = {};
    formData.forEach((item) => {
        dataJson[item.name] = item.value;
    });
    let gender = $("input[name='gender']:checked").val();
    console.log(gender)
    if(gender === undefined) return flashMessage("Vui lòng chọn giới tính", "error");
    //update profile
    fetch("/api/users/update-profile", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(dataJson),
    })
        .then((res) => res.json())
        .then((data) => {
            if (data.statusCode === 200) {
                flashMessage("Cập nhật thông tin thành công", "success");
                displayProfile(dataJson);
            } else {
                flashMessage("Cập nhật thông tin thất bại.", "error")
            }
        })
        .catch((err) => flashMessage(err.message, "error"));
});

//register guarantee
btnRegisterGuarantee.on("click", function () {
    const formData = $(".form-register-guarantee").serializeArray();
    const dataJson = {};
    formData.forEach((item) => {
        dataJson[item.name] = item.value;
    });
    console.log(dataJson);
    fetch("/api/guarantee/register-guarantee", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(dataJson),
    })
        .then((res) => res.json())
        .then((data) => {
            if (data.statusCode === 200) {
                flashMessage("Đăng ký bảo hành thành công", "success");
                getHistoryGuarantee();
            } else {
                flashMessage(data.message, "error")
            }
        })
        .catch((err) => flashMessage(err.message, "error"));
});

function displayGuarantee(arr) {
    tbody.empty();
    arr.forEach((item) => {
        let html = `<tr>
                                <td class="product-name"><a href="#">${item.codeGuarantee}</a></td>
                                <td class="product-name"><a href="#">${item.productName}</a></td>
                                <td class="product-name"><a href="#">${formatDatetime(item.purchaseDate)}</a></td>
                                <td class="product-name"><a href="#">${formatDatetime(item.expirationDate)}</a></td>
                                <td class="product-price"><span class="amount">${calculateDaysDifference(item.expirationDate)}</span></td>
                           </tr>`;
        tbody.append(html);
    });
}

function displayHistory(arr) {
    tbodyHistory.empty();
    arr.forEach((item) => {
        let formattedGuaranteeDate = item.guaranteeDate ? formatDatetime(item.guaranteeDate) : '-';
        let html = `<tr>
                                <td class="product-name"><a href="#">${item.codeGuarantee}</a></td>
                                <td class="product-name"><a href="#">${item.nameProduct}</a></td>
                                <td class="product-name"><a href="#">${formatDatetime(item.complainDate)}</a></td>
                                <td class="product-price"><span class="amount">${item.description}</span></td>
                                <td class="product-name"><a href="#">${item.status}</a></td>
                                <td class="product-name"><a href="#">${formattedGuaranteeDate}</a></td>
                           </tr>`;
        tbodyHistory.append(html);
    });
}

function displayProfile(data) {
    $("#full-name").val(data.fullName);
    $("#email").val(data.email);
    $("#phone").val(data.phone);
    $("#address-user").val(data.address);
    if (data.gender === "Nam") {
        $("input[name='gender'][value='Nam']").prop("checked", true);
    } else if (data.gender === "Nữ") {
        $("input[name='gender'][value='Nữ']").prop("checked", true);
    }
}

function formatDatetime(date) {
    let d = new Date(date);
    const options = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' };
    return d.toLocaleString('es-ES', options);
}

function calculateDaysDifference(date1) {
    let d1 = new Date(date1);
    let d2 = new Date();
    return Math.floor((d1 - d2) / (1000 * 3600 * 24));
}

function flashMessage(message, type) {
    Swal.fire({
        position: "center",
        icon: type,
        title: message,
        showConfirmButton: false,
        timer: 1500,
    });
}