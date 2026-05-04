const apiURL = "/CarCare/api";
const serviceNames = { 1: "Basic Wash", 2: "Premium Detail", 3: "Ceramic Coating" };

async function loadBookings() {
    const phoneNum = document.getElementById("phoneInput").value.trim();
    const errBox = document.getElementById("searchError");
    const resultsBox = document.getElementById("resultsArea");

    errBox.style.display = "none";

    if (!phoneNum) {
        errBox.textContent = "Please enter a phone number.";
        errBox.style.display = "block";
        return;
    }

    try {
        const reply = await fetch(`${apiURL}/bookings?phone=${encodeURIComponent(phoneNum)}`);
        const bookingData = await reply.json();

        resultsBox.style.display = "block";
        const resultText = document.getElementById("resultsLabel");
        const listBox = document.getElementById("bookingsList");

        if (!Array.isArray(bookingData) || bookingData.length === 0) {
            resultText.textContent = "No bookings found for this number.";
            listBox.innerHTML = `
                <div class="text-center py-4 text-muted">
                    <i class="bi bi-calendar-x" style="font-size:2rem;display:block;margin-bottom:10px"></i>
                    No bookings found. <a href="booking.html">Book a service</a> to get started.
                </div>`;
            return;
        }

        resultText.textContent = `${bookingData.length} booking(s) found`;
        listBox.innerHTML = bookingData.map(item => {
            const cancelOk = item.status === "Pending";
            return `
            <div class="booking-card status-${item.status.toLowerCase()}" id="card_${item.bookingId}">
                <div class="d-flex justify-content-between align-items-start flex-wrap gap-2">
                    <div>
                        <div class="booking-title">${serviceNames[item.serviceId] || "Service"}</div>
                        <div class="booking-meta">Booking #${item.bookingId}</div>
                    </div>
                    <div class="d-flex align-items-center gap-2">
                        <span class="status-badge badge-${item.status.toLowerCase()}">${item.status}</span>
                        ${cancelOk ? `<button class="btn-cancel" onclick="cancelBooking(${item.bookingId})">Cancel</button>` : ""}
                    </div>
                </div>
                <div class="detail-row">
                    <div class="detail-item">
                        <strong>Date</strong>${item.bookingDate}
                    </div>
                    <div class="detail-item">
                        <strong>Time</strong>${item.bookingTime}
                    </div>
                    <div class="detail-item">
                        <strong>Car Type</strong>${item.carType}
                    </div>
                    ${item.carModel ? `<div class="detail-item"><strong>Model</strong>${item.carModel}</div>` : ""}
                    <div class="detail-item">
                        <strong>Address</strong>${item.address}
                    </div>
                    ${item.notes ? `<div class="detail-item"><strong>Notes</strong>${item.notes}</div>` : ""}
                </div>
            </div>`;
        }).join("");

    } catch (e) {
        errBox.textContent = "Could not connect to server. Please try again.";
        errBox.style.display = "block";
    }
}

async function cancelBooking(id) {
    if (!confirm("Are you sure you want to cancel this booking?")) return;

    try {
        const reply = await fetch(`${apiURL}/bookings?id=${id}`, { method: "DELETE" });
        const result = await reply.json();

        if (result.success) {
            const bookingCard = document.getElementById(`card_${id}`);
            bookingCard.style.opacity = "0.5";
            bookingCard.innerHTML += `<div class="text-danger small mt-2 fw-bold"><i class="bi bi-x-circle me-1"></i>Booking cancelled.</div>`;
            setTimeout(loadBookings, 1500);
        } else {
            alert("Failed to cancel booking. Please try again.");
        }
    } catch (e) {
        alert("Server error. Please try again.");
    }
}

document.addEventListener("keydown", (e) => {
    if (e.key === "Enter") loadBookings();
});