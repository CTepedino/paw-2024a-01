function toggleVisibility(elementId, btnClass){
    const passwordInput = document.getElementById(elementId);
    const toggleBtn = document.querySelector(btnClass);
    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        toggleBtn.textContent = "visibility";
    } else {
        passwordInput.type = "password";
        toggleBtn.textContent = "visibility_off";
    }
}


function togglePasswordVisibility() {
    toggleVisibility("password", ".password-toggle-btn");
}

function toggleRepeatPasswordVisibility(){
    toggleVisibility("repeatPassword", ".repeat-password-toggle-btn");
}

