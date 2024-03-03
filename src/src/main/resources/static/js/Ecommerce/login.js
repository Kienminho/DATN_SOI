const btnLogin = $(".btn-login");
const username = $("#username");
const password = $("#password");
const messageError = $(".message-error");

$(btnLogin).click(function () {
    if (validate(username.val(), password.val())) {
        fetch("/api/users/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                username: username.val(),
                password: password.val(),
            }),
        })
            .then((res) => res.json())
            .then((res) => {
                console.log(res);
                if (res.statusCode === 200) {
                    window.location.href = "/";
                } else {
                    displayErrorMessage(res.message);
                }
            })
            .catch((err) => {
                displayErrorMessage("Lỗi hệ thống vui lòng thử lại sau ít phút.");

            })
    }
    else {
        displayErrorMessage("Vui lòng nhập đầy đủ thông tin!");
    }


});

function validate(username, password) {
    //check password have length > 6
    if (password.length < 6) {
        return false;
    }

    return !(username === "");
}

function displayErrorMessage(message) {
    messageError.text(message).fadeIn("slow");
    setTimeout(function () {
        messageError.fadeOut("slow");
    }, 3000);
}