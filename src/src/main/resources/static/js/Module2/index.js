let info = sessionStorage.getItem('userInfo');
//parse json
info = JSON.parse(info);
if(info.roleName === "ROLE_SHIPPER") {
    $(".date-input").addClass("d-none");
    $(".tr").addClass("d-none");
}
let start = new Date();
start.setHours(0, 0, 0, 0); // Set hours, minutes, seconds, and milliseconds to 0

// Adjust for timezone offset
start.setMinutes(start.getMinutes() - start.getTimezoneOffset());

let end = new Date();
end.setHours(23, 59, 59, 999); // Set hours, minutes, seconds, and milliseconds to the end of the day

// Adjust for timezone offset
end.setMinutes(end.getMinutes() - end.getTimezoneOffset());

const tbody = $(".tbody");
const tbodyDetails = $(".tbody-detail");
const employee = $(".employee");
const invoice = $(".invoice");
const user = $(".user");
const money = $(".total-money");
const quantity = $(".quantity");
const fromDateDefault = $(".from-date").val(start.toISOString().slice(0, 16));
const toDateDefault = $(".to-date").val(end.toISOString().slice(0, 16));

getData(fromDateDefault, toDateDefault);

fetch("/api/staff-and-shipper/statistic")
    .then((res) => res.json())
    .then((res) => {
        $(employee).text(res.data.totalStaff);
        $(invoice).text(res.data.totalOrder);
        $(money).text(res.data.totalMoney.toLocaleString("vi-VN"));
        $(quantity).text(res.data.totalShipper);
        $(user).text(res.data.totalUser);
    })
    .catch((err) => console.log(err));

function search() {
    const fromDate = $(".from-date").val();
    const toDate = $(".to-date").val();
    getData(fromDate, toDate);
}

function getData(fromDate, toDate) {
    fetch("/api/orders/get-list-filter", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ fromDate: fromDate, toDate: toDate }),
    })
        .then((res) => res.json())
        .then((res) => {
            displayData(res.data);
        })
        .catch((err) => console.log(err));
}

function displayData(arr) {
    tbody.empty();
    arr.map((item) => {
        let html = `<tr>
    <td><i class="fab fa-angular fa-lg text-danger me-3 r cursor-pointer"></i> <strong onclick="deatailInvoice(this)" class="cursor-pointer id">${
            item.invoiceCode
        }</strong></td>
    <td>${item.salePeople}</td>
    <td>${item.customerName}</td>
    <td>${item.quantity}</td>
    <td>${item.totalMoney.toLocaleString("vi-VN")}</td>
    <td>${new Date(item.createdDate).toLocaleDateString("vi-VN")}</td>`;
        tbody.append(html);
    });
}

function deatailInvoice(element) {
    $("#detail-invoice-modal").modal("show");
    const id = $(element).text();
    fetch(`/api/orders/get-list-invoice/${id}`)
        .then((res) => res.json())
        .then((res) => {
            displayDetails(res.data);
        })
        .catch((err) => console.log(err));
}

function displayDetails(arr) {
    tbodyDetails.empty();
    arr.map((item, index) => {
        let html = `<tr>
    <td><i class="fab fa-angular fa-lg text-danger me-3 r"></i> <strong onclick="deatailInvoice(this)" class="cursor-pointe id">${
            index + 1
        }</strong></td>
    <td>${item.invoiceCode}</td>
    <td>${item.nameProduct}</td>
    <td>${item.quantity}</td>
    <td>${item.unitPrice.toLocaleString("vi-VN")}</td>
    <td>${new Date(item.createdDate).toLocaleDateString("vi-VN")}</td>`;
        tbodyDetails.append(html);
    });
}
