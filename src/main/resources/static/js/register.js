document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("form");
    
    form.addEventListener("submit", function (event) {
        event.preventDefault(); // Prevent default form submission

        const formData = new FormData(form);

        fetch("/auth/do-register", {
            method: "POST",
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            showMessage(data.message, data.type);

            if (data.status === "success") {
                form.reset(); // Reset form after successful registration
            }
        })
        .catch(error => {
            console.error("Error:", error);
            showMessage("An error occurred. Please try again.", "alert-danger");
        });
    });
});

function showMessage(content, type) {
    const messageContainer = document.getElementById("message-container");
    messageContainer.innerHTML = `<div class="alert ${type}" role="alert">${content}</div>`;

    setTimeout(() => {
        messageContainer.innerHTML = "";
    }, 5000);
}
