let num = 0;
const countEl = document.getElementById("count");
function updateCount(value) {
    num += value;
    if (num < 0) {
        num = 0;
        alert("Count cannot be negative!");
    }
    countEl.textContent = num;
}
function resetCount() {
    num = 0;
    countEl.textContent = num;
}