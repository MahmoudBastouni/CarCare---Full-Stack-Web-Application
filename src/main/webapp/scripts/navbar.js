function setActiveLink(allLinks, activeLink)
{
    allLinks.forEach((link) => {
        const isActive = link === activeLink;
        link.classList.toggle("is-active", isActive);

        if (isActive) {
            const currentType = link.classList.contains("btn-book") ? "page" : "location";
            link.setAttribute("aria-current", currentType);
        }
        else {
            link.removeAttribute("aria-current");
        }
    });
}

function syncScrollState(navbar)
{
    navbar.classList.toggle("nav-scrolled", window.scrollY > 12);
}

export function initNavbar(currentPage)
{
    const navbar = document.querySelector(".custom-nav");
    if (!navbar) {
        return;
    }

    const allLinks = Array.from(navbar.querySelectorAll(".nav-link"));
    const bookingLink = navbar.querySelector(".btn-book");

    syncScrollState(navbar);
    window.addEventListener("scroll", () => syncScrollState(navbar), { passive: true });

    if (currentPage === "booking") {
        setActiveLink(allLinks, bookingLink);
        return;
    }

    const sectionLinks = allLinks.filter((link) => {
        const href = link.getAttribute("href") || "";
        return href.startsWith("#");
    });

    const sections = sectionLinks
        .map((link) => {
            const target = document.querySelector(link.getAttribute("href"));
            if (!target) {
                return null;
            }

            return { link, target };
        })
        .filter(Boolean);

    if (sections.length === 0) {
        return;
    }

    function updateActiveSection()
    {
        const pageBottom = window.scrollY + window.innerHeight;
        const documentBottom = document.documentElement.scrollHeight;
        if (documentBottom - pageBottom <= 8) {
            setActiveLink(allLinks, sections[sections.length - 1].link);
            return;
        }

        const activationPoint = window.scrollY + navbar.offsetHeight + 96;
        let activeLink = null;

        sections.forEach(({ link, target }) => {
            if (activationPoint >= target.offsetTop) {
                activeLink = link;
            }
        });

        setActiveLink(allLinks, activeLink);
    }

    sectionLinks.forEach((link) => {
        link.addEventListener("click", () => setActiveLink(allLinks, link));
    });

    let isTicking = false;
    function handleScroll()
    {
        if (isTicking) {
            return;
        }

        isTicking = true;
        window.requestAnimationFrame(() => {
            updateActiveSection();
            isTicking = false;
        });
    }

    window.addEventListener("scroll", handleScroll, { passive: true });
    window.addEventListener("resize", handleScroll);
    window.addEventListener("hashchange", updateActiveSection);

    updateActiveSection();
}
