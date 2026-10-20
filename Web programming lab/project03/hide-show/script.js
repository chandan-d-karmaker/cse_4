const passwordInput = document.getElementById("password");
const btn = document.getElementById("togglePass");
btn.onclick = function () {
    const isSecret = passwordInput.type === "password";
    passwordInput.type = isSecret ? "text" : "password";
    btn.innerHTML = isSecret ? '<i class="fas fa-eye"></i>' : '<i class="fas fa-eye-slash"></i>';
};
