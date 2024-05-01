function repeatPasswordCheck() {
    const password = document.getElementById("password").value;
    const repeat = document.getElementById("repeatPassword").value;

    if (password !== repeat) {
        document.getElementById("passwordErrorMessage").style.visibility= "visible";
        document.getElementById("submitBtn").disabled = true;
    } else {
        document.getElementById("passwordErrorMessage").style.visibility = "hidden";
        document.getElementById("submitBtn").disabled = false;

    }
}

