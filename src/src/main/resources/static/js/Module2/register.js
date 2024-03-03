const username = $("#username");
const email = $("#email");
const password = $("#password");
const btnRegister = $(".btn-register");
const errorMessage = $(".message");

btnRegister.on("click", () => {

    if (validateInput(username.val(), email.val(), password.val())) {
        const data = {
            username: username.val(),
            email: email.val(),
            password: password.val(),
        };

        fetch("/api/staff-and-shipper/register-shipper", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
        })
            .then((res) => res.json())
            .then((res) => {
                console.log(res);
                if (res.statusCode === 200) {
                    window.location.href = "/dashboard/auth/login";
                } else {
                    displayErrorMessage(res.message);
                }
            })
            .catch((err) => {
                displayErrorMessage("Lỗi hệ thống vui lòng thử lại sau ít phút.");
            });
    }
    else {
        displayErrorMessage("Vui lòng không được bỏ trống tên, email hoặc mật khẩu.");
    }
});

function validateInput(username, email, password) {
    //check email
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    if (!emailRegex.test(email)) {
        return false;
    }
    //check password
    if (password.length < 6) {
        return false;
    }
    return !(username === "");
}

function displayErrorMessage(message) {
    errorMessage.text(message).fadeIn("slow");
    setTimeout(function () {
        errorMessage.fadeOut("slow");
    }, 3000);
}