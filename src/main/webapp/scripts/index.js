const API_BASE = "/CarCare/api";
console.log("index.js loaded");
async function generatePackages() {
    try {
        const res = await fetch(`${API_BASE}/services`);
        const packages = await res.json();

        const row = document.querySelector("#packages .row");

        packages.forEach((pkg, index) => {
            const features = pkg.features.split(",");

            const featuresHTML = features.map(f =>`<li><i class="bi bi-check2 me-2"></i>${f.trim()}</li>`).join("");

            const col = document.createElement("div");
            col.className = "col-md-4";
            col.innerHTML = `
                <div class="package-card h-100">
                    <div class="package-icon-box">
                        <i class="bi bi-${index === 0 ? 'bucket' : index === 1 ? 'stars' : 'shield-check'}"></i>
                    </div>
                    <p class="package-label">${pkg.label.toUpperCase()}</p>
                    <h3>${pkg.title}</h3>
                    <p class="package-text">${pkg.description}</p>
                    <div class="package-price">AED ${pkg.price}</div>
                    <p class="package-time"><i class="bi bi-clock me-2"></i>${pkg.duration}</p>
                    <ul class="list-unstyled package-list">
                        ${featuresHTML}
                    </ul>
                    <a href="booking.html" class="btn btn-primary w-100 mt-3">
                        Book ${pkg.title}
                    </a>
                </div>
            `;
            row.appendChild(col);
        });

        // dynamically updating cheapest price in hero section (took much longer than it should have)
        const cheapestPrice = Math.min(...packages.map(p => p.price));

        const priceBox = document.querySelector(".quick-box:last-child strong");
        if (priceBox) priceBox.textContent = "AED " + cheapestPrice;

        const heroNote = document.querySelector(".hero-note h6");
        if (heroNote) heroNote.textContent = "From AED " + cheapestPrice;

    } catch (err) {
        console.error("Failed to load packages:", err);
    }
}

await generatePackages();