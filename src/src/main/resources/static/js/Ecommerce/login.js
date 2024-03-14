const btnLogin = $(".btn-login");
const username = $("#username");
const password = $("#password");
const messageError = $(".message-error");

$(btnLogin).click(function () {
    let isValidate = validate(username.val(), password.val());
    if (isValidate.code === true) {
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
        displayErrorMessage(isValidate.message);
    }
});

function validate(username, password) {
    //check password have length > 6
    if (password.length < 6) {
        return { code: false, message: "Mật khẩu không thể ít hơn 6 ký tự!" };
    }
    //check if username is empty
    if (username === "") {
        return { code: false, message: "Tên người dùng không được để trống!" };
    }

    //return null if validation passes
    return { code: true, message: "Done" };
}

function displayErrorMessage(message) {
    messageError.text(message).fadeIn("slow");
    setTimeout(function () {
        messageError.fadeOut("slow");
    }, 3000);
}