function validateLoginForm() {
    let email = document.getElementById("email").value.trim();
    let password = document.getElementById("password").value.trim();

    let errorBox = document.getElementById("loginError");

    errorBox.style.display = "none";

    // Email format (must be like name@gmail.com)
    let emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    // Password format (example: Amo@10)
    let passwordPattern = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@$!%*?&]).{5,}$/;

    if (!emailPattern.test(email) || !passwordPattern.test(password)) {
        errorBox.style.display = "block";
        return false;
    }

    return true;
}