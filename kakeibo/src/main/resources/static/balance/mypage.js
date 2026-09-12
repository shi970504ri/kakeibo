window.togglePassword = function() {
	const passwordInput = document.getElementById("passwordInput");
	const eyeIcon = document.getElementById("eyeIcon");
	if (!passwordInput || !eyeIcon) return;
	const openIcon = eyeIcon.dataset.openIcon;
	const closeIcon = eyeIcon.dataset.closeIcon;
	if (passwordInput.type === "password") {
		passwordInput.type = "text";
		eyeIcon.src = openIcon;
	} else {
		passwordInput.type = "password";
		eyeIcon.src = closeIcon;
	}
};