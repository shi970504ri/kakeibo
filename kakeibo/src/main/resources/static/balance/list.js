function showImage(imageSrc) {
	if (!imageSrc) return;
	var modal = document.getElementById("imageModal");
	var modalImg = document.getElementById("modalImg");
	modalImg.src = imageSrc;
	modal.style.display = "flex";
}
function closeImage() {
	var modal = document.getElementById("imageModal");
	modal.style.display = "none";
}