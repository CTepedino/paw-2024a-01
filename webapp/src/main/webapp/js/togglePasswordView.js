function togglePasswordVisibility() {
    const passwordInput = document.getElementById("password");
    const toggleBtn = document.querySelector(".password-toggle-btn");
    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        toggleBtn.textContent = "visibility";
    } else {
        passwordInput.type = "password";
        toggleBtn.textContent = "visibility_off";
    }
}