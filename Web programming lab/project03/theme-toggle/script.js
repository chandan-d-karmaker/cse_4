const btn = document.getElementById("toggleBtn");
btn.onclick = function () {
    document.body.classList.toggle("dark-mode");
    btn.innerHTML = document.body.classList.contains("dark-mode")
        ? '<i class="fas fa-sun"></i> Switch to Light Mode'
        : '<i class="fas fa-moon"></i> Switch to Dark Mode';
};