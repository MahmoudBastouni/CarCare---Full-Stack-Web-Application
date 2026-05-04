const apiPath = "/CarCare/api";
const serviceTitleList = { 1: "Basic Wash", 2: "Premium Detail", 3: "Ceramic Coating" };

// toast
function showToast(text, okay = true) {
    const toast = document.getElementById("toastMsg");
    toast.textContent = text;
    toast.style.background = okay ? "#166534" : "#991b1b";
    toast.style.display = "block";
    setTimeout(() => toast.style.display = "none", 3000);
}

// getting bookings
async function loadBookings() {
    try {
        const answer = await fetch(`${apiPath}/admin`);
        if (answer.status === 403) {
            window.location.href = "login.html";
            return;
        }

        const bookingsArr = await answer.json();

        document.getElementById("statTotal").textContent = bookingsArr.length;
        document.getElementById("statPending").textContent = bookingsArr.filter(item => item.status === "Pending").length;
        document.getElementById("statConfirmed").textContent = bookingsArr.filter(item => item.status === "Confirmed").length;
        document.getElementById("statCompleted").textContent = bookingsArr.filter(item => item.status === "Completed").length;

        const tbody = document.getElementById("bookingsBody");
        if (bookingsArr.length === 0) {
            tbody.innerHTML = '<tr><td colspan="9" class="text-center text-muted py-4">No bookings yet.</td></tr>';
            return;
        }

        tbody.innerHTML = bookingsArr.map(row => `
            <tr>
                <td>#${row.bookingId}</td>
                <td>${row.fullName}</td>
                <td>${row.phone}</td>
                <td>${serviceTitleList[row.serviceId] || "Service #" + row.serviceId}</td>
                <td>${row.carType}${row.carModel ? " · " + row.carModel : ""}</td>
                <td>${row.bookingDate}</td>
                <td>${row.bookingTime}</td>
                <td><span class="status-badge badge-${row.status.toLowerCase()}">${row.status}</span></td>
                <td>
                    <select class="form-select form-select-sm" style="width:130px;display:inline-block"
                        onchange="updateStatus(${row.bookingId}, this.value)">
                        <option value="Pending" ${row.status === "Pending" ? "selected" : ""}>Pending</option>
                        <option value="Confirmed" ${row.status === "Confirmed" ? "selected" : ""}>Confirmed</option>
                        <option value="Completed" ${row.status === "Completed" ? "selected" : ""}>Completed</option>
                        <option value="Cancelled" ${row.status === "Cancelled" ? "selected" : ""}>Cancelled</option>
                    </select>
                </td>
            </tr>
        `).join("");
    } catch (e) {
        console.error("Error loading bookings:", e);
    }
}

// changing the status
async function updateStatus(bookingId, status) {
    const sendData = new URLSearchParams();
    sendData.append("action", "updateStatus");
    sendData.append("bookingId", bookingId);
    sendData.append("status", status);

    try {
        const answer = await fetch(`${apiPath}/admin`, { method: "POST", body: sendData });
        const result = await answer.json();
        if (result.success) {
            showToast("Status updated successfully.");
            loadBookings();
        } else {
            showToast("Failed to update status.", false);
        }
    } catch (e) {
        showToast("Server error.", false);
    }
}

// loading prices
async function loadServices() {
    try {
        const answer = await fetch(`${apiPath}/services`);
        const servicesArr = await answer.json();

        const priceBox = document.getElementById("priceList");
        priceBox.innerHTML = servicesArr.map(service => `
            <div class="price-input-row">
                <label>${service.icon} ${service.title}</label>
                <span style="color:#64748b;font-size:0.85rem">AED</span>
                <input type="number" id="price_${service.serviceId}" value="${service.price}" min="0" step="0.01">
                <button class="btn-save-price" onclick="updatePrice(${service.serviceId})">Save</button>
            </div>
        `).join("");
    } catch (e) {
        console.error("Error loading services:", e);
    }
}

// saving price
async function updatePrice(serviceId) {
    const price = document.getElementById(`price_${serviceId}`).value;
    const sendData = new URLSearchParams();

    sendData.append("action", "updatePrice");
    sendData.append("serviceId", serviceId);
    sendData.append("price", price);

    try {
        const answer = await fetch(`${apiPath}/admin`, { method: "POST", body: sendData });
        const result = await answer.json();

        if (result.success) {
            showToast("Price updated successfully.");
        } else {
            showToast("Failed to update price.", false);
        }
    } catch (e) {
        showToast("Server error.", false);
    }
}

// logout
async function logout() {
    await fetch(`${apiPath}/admin?action=logout`);
    window.location.href = "login.html";
}

// start page
loadBookings();
loadServices();