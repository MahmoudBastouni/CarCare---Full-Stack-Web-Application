async function handleLogin() {
    const userName = document.getElementById("username").value.trim();
    const pass = document.getElementById("password").value.trim();
    const errBox = document.getElementById("errorMsg");
    const signBtn = document.getElementById("loginBtn");

    if (!userName || !pass) {
        errBox.style.display = "block";
        errBox.textContent = "Please enter both username and password.";
        return;
    }

    signBtn.disabled = true;
    signBtn.textContent = "Signing in...";
    errBox.style.display = "none";

    const loginInfo = new URLSearchParams();
    loginInfo.append("username", userName);
    loginInfo.append("password", pass);

    try {
        const reply = await fetch("/CarCare/api/admin", {
            method: "POST",
            body: loginInfo
        });
        const result = await reply.json();

        if (result.success) {
            window.location.href = "admin.html";
        } else {
            errBox.style.display = "block";
            errBox.innerHTML = '<i class="bi bi-exclamation-circle me-2"></i>Invalid username or password.';
        }
    } catch (e) {
        errBox.style.display = "block";
        errBox.innerHTML = '<i class="bi bi-exclamation-circle me-2"></i>Could not connect to server.';
    } finally {
        signBtn.disabled = false;
        signBtn.textContent = "Sign In";
    }
}

document.addEventListener("keydown", (event) => {
    if (event.key === "Enter") handleLogin();
});