const btnRegister = $('.btn-register');
const username = $('#username');
const password = $('#password');
const email = $('#email');
const confirmPassword = $('#confirm-password');


btnRegister.on("click", () => {
    console.log(validateInfo(username.val(), password.val(), confirmPassword.val()))
    if (validateInfo(username.val(), password.val(), confirmPassword.val())) {
        const data = {
            username: username.val(),
            password: password.val(),
            email: email.val(),
        }
        fetch("/api/users/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json", // Set the content type to JSON
            },
            body: JSON.stringify(data),
        })
            .then(res => res.json())
            .then(data => {
                //thành công chuyển hướng người dùng
                if(data.statusCode === 200)
                    window.location = "/auth/login";
            })
            .catch(err => console.log(err));

    } else {
        alert("Vui lòng nhập đầy đủ thông tin!");
    }
});


function validateInfo(username,password, rePassword) {
    console.log(username,password,rePassword);
    return !(username === "" || password === "" || rePassword === "" || password !== rePassword);
}