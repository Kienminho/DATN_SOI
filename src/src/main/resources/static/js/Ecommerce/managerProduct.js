$("#uploadForm").on("click","textarea", function(){
    $("#alert").fadeOut("fast");
})

$("#uploadForm").on("click","input", function(){
    $("#alert").fadeOut("fast");
})

$("#uploadForm").on("click","select", function(){
    $("#alert").fadeOut("fast");
})

$("#editModal").on("click","textarea", function(){
    $('#editModal').find("#alert").fadeOut("fast");
})

$("#editModal").on("click","input", function(){
    $('#editModal').find("#alert").fadeOut("fast");
})

$("#editModal").on("click","select", function(){
    $('#editModal').find("#alert").fadeOut("fast");
})

$("#imageModal").on("click","#fileImage", function(){
    $('#imageModal').find("#alert").fadeOut("fast");
})

const tbody = $("#filesTable");
console.log(tbody);

fetch("/api/products/get-all-products")
    .then(res =>res.json())
    .then(res => {
        displayProducts(tbody,res.data);
    })
    .catch(err => {
        console.log(err);
    })

function displayProducts(element, arr) {
    element.empty();
    arr.map(item => {
        let hotProduct = item.currentQuantity < 10 ? "[Best sale]" : ""
        // Determine the lock icon class based on the isDelete status
        const lockIconClass = item._deleted ? 'fa-lock' : 'fa-unlock';

        let html = `<tr>
                    <td>
                        <a href="#">${item.id}</a>
                    </td>
                    <td data-bs-toggle="tooltip" data-bs-placement="bottom" title="${item.description}">
                        ${hotProduct+" "+item.name}
                    </td>
                    <td>
                        <img loading="lazy" src="${item.imageUrl}"
                             class="img-fluid float-start rounded mx-auto d-block" alt="..." width="75" height="auto">
                    </td>
                    <td>
                        ${item.category.name}
                    </td>
                    <td>
                        ${item.description}
                    </td>
                    <td>
                        ${item.salePrice}
                    </td>
                    <td>
                        <div class="dropdown">
                            <button aria-expanded="false" data-bs-toggle="dropdown" id="edit" type="button" class="btn "><i
                                    class="fas fa-edit"></i></button>
                            <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
                                <li><a id="editDetail" class="dropdown-item">Edit details</a></li>
                               
                            </ul>
                        </div>

                        <button id="block" type="button" class="btn">
                            <i class='fa-sharp fa-solid ${lockIconClass} fa-lg'></i>
                        </button>
                    </td>
                </tr>`;

        // Append or do something with the generated HTML
        element.append(html);
    });

}

$("#from-add").submit(function (e) {
    e.preventDefault();

    var imageExtension = ["jpeg", "jpg", "png"];

    let image = $("#product-image").val();
    let category = $("#category").val();
    let description = $("#description").val();

    let imageType = image.substr(image.lastIndexOf(".") + 1, image.length);

    if (!$("#product-name").val() ||$(".product-price").val() === 0 || !image || category === "none" || !description) {
        setAlert("Please fill all the field to make a request!");

    } else if (!imageExtension.includes(imageType)) {
        setAlert("Invalid image format!");
    } else {
        $("#btn-add-product").addClass("disabled");
        var formData = new FormData(this);
        $.ajax({
            type: "POST",
            url: "/api/products/add-product",
            data: formData,
            processData: false,
            contentType: false,
            xhr: function () {
                var xhr = new window.XMLHttpRequest();
                // Upload progress
                xhr.upload.addEventListener("progress", function (e) {
                    if (e.lengthComputable) {
                        var percentComplete = (e.loaded / e.total) * 100;
                        $(".container-progress").show();
                        $(".progress-bar").width(percentComplete + "%");
                        if (percentComplete === 100) {
                            setTimeout(function () {
                                $(".container-progress").hide();
                                $(".progress-bar").width(0);
                            }, 1500);
                        }
                    }
                });
                return xhr;
            },
            success: function (r) {
                console.log(r);
                $("#successAlert").text("Your request has been sent");
                $("#successAlert").show();
                setTimeout(function () {
                    $("#product-price").val('');
                    $("#product-image").val('');
                    $("#description").val('');
                    $("#product-name").val('');
                    $("#category").val('none').change();

                    $("#successAlert").hide();
                    $("#uploadModal .btn-close").click();
                    $("#btn-add-product").removeClass("disabled");
                    location.reload();
                }, 3000);
            },
            error: function (e) {
                $("#btn-add-product").removeClass("disabled");
                console.log("Error: ", e);
            },
        });
    }
});



$("table").on("click", "#block",function(){
    let id = $(this).closest('tr').find('td:eq(0)').text().trim();
    fetch(`/api/products/delete-product?id=${id}`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        }
    })
        .then(res => res.json())
        .then(res => {
            if(res.statusCode === 200) {
                $("#liveToast").addClass("bg-success text-white");
                $("#liveToast").find(".toast-body").text("Success");
                $("#liveToast").toast("show");
                setTimeout(() => {
                    location.reload();
                }, 1000);

            }
            else {
                $("#liveToast").addClass("bg-danger text-white");
                $("#liveToast").find(".toast-body").text("Edit failed");
                $("#liveToast").toast("show");
            }
        })
})


$("table").on("click", '#editDetail',function(e){
    let id = $(this).closest('tr').find('td:eq(0)').text().trim();
    let name = $(this).closest('tr').find('td:eq(1)').text().trim();
    let price = $(this).closest('tr').find('td:eq(5)').text().trim();
    let description = $(this).closest('tr').find('td:eq(4)').text().trim();

    $('#editModal').find("#edit-category").val(category).change();
    $('#editModal').find("h4").html('Update product &nbsp;<strong class="id d-none">' + id + '</strong>')
    $('#editModal').find("#edit-name").val(name);
    $('#editModal').find("#edit-price").val(price);
    $('#editModal').find("#edit-description").val(description);
    $('#editModal').modal('show');
})

function saveChanges(){
    let id =  $('#editModal').find(".modal-title").find("strong").text()
    let name = $('#editModal').find("#edit-name").val();
    let price = $('#editModal').find("#edit-price").val();
    let description =$('#editModal').find("#edit-description").val();
    if (!name || price === 0 || !description) {
        $('#editModal').find("#alert").text('Please fill all the fields!')
        $('#editModal').find("#alert").fadeIn("fast");
    }else{
        $.ajax({
            url : "api/products/update-product",
            type: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            data: JSON.stringify({
                id : id,
                name: name,
                price: price,
                description : description,
            }),
            dataType: "json",
            success: function (r) {
                if(r.statusCode === 200){
                    $('#editModal').modal('hide');
                    $("#liveToast").addClass("bg-success text-white")
                    $("#liveToast").find(".toast-body").text("Success")
                    $("#liveToast").toast("show")

                    setTimeout(() => {
                        location.reload();
                    }, 1000);
                }else{
                    $("#alert").text(r.message)
                    $("#alert").fadeIn("fast");
                }
            },
            error: function(e){
                console.log(e)
            }
        })
    }
}


function setAlert(text){
    $("#alert").text(text)
    $("#alert").fadeIn("fast");
}
