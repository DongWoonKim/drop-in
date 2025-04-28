$(document).ready(function() {
    const dropZone = $('#drop-zone');
    const fileInput = $('#image');
    const previewImage = $('#preview-image');
    const loading = $('#loading');

    dropZone.on('click', function(e) {
        if (e.target.id === 'drop-zone' || e.target.tagName === 'P') {
            fileInput.click();
        }
    });

    dropZone.on('dragover', function(e) {
        e.preventDefault();
        e.stopPropagation();
        dropZone.addClass('dragover');
    });

    dropZone.on('dragleave', function(e) {
        e.preventDefault();
        e.stopPropagation();
        dropZone.removeClass('dragover');
    });

    dropZone.on('drop', function(e) {
        e.preventDefault();
        e.stopPropagation();
        dropZone.removeClass('dragover');

        const files = e.originalEvent.dataTransfer.files;
        if (files.length > 0) {
            validateAndPreview(files[0]);
            fileInput[0].files = files;
        }
    });

    fileInput.on('change', function(e) {
        if (this.files && this.files[0]) {
            validateAndPreview(this.files[0]);
        }
    });

    function validateAndPreview(file) {
        const validExtensions = ['jpg', 'jpeg', 'png', 'gif'];
        const maxSize = 5 * 1024 * 1024; // 5MB

        const fileName = file.name;
        const fileExtension = fileName.split('.').pop().toLowerCase();

        if (!validExtensions.includes(fileExtension)) {
            alert('jpg, jpeg, png, gif 파일만 업로드할 수 있습니다.');
            fileInput.val(''); // 선택 해제
            previewImage.hide();
            return;
        }

        if (file.size > maxSize) {
            alert('파일 크기는 5MB를 초과할 수 없습니다.');
            fileInput.val('');
            previewImage.hide();
            return;
        }

        const reader = new FileReader();
        reader.onload = function(e) {
            previewImage.attr('src', e.target.result);
            previewImage.show();
        }
        reader.readAsDataURL(file);
    }

    // 폼 제출할 때 로딩 표시
    $('#upload-form').on('submit', function() {
        loading.show();
    });
});