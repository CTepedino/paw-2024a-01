function repeatPasswordCheck() {
    const password = document.getElementById("password").value;
    const repeat = document.getElementById("repeatPassword").value;

    if (password !== repeat) {
        document.getElementById("passwordErrorMessage").style.visibility= "visible";
    } else {
        document.getElementById("passwordErrorMessage").style.visibility = "hidden";
    }
}