$(document).ready(function() {
    u_initialize()
        .catch(console.error);
});

async function u_initialize() {
    const uploadPage = $('body').data('page');
    if (uploadPage === 'upload') {
        setupAjax();
        await handleTokenExpiration();
        await userInfo().then((userInfo) => {
            $('#u_hUserId').val(userInfo.userId);
            $('#u_hUserName').val(userInfo.userName);

        });

        await userInfoByUserId($('#u_hUserId').val()).then((userInfo) => {
            $('#u_hBoxId').val(userInfo.boxId);
        });

        initDropZone();
        initFileInput();
        initFormSubmit();
    }
}

// -------------------- DropZone 초기화 --------------------
function initDropZone() {
    const dropZone = $('#drop-zone');
    dropZone.on('click', handleDropZoneClick);
    dropZone.on('dragover', handleDragOver);
    dropZone.on('dragleave', handleDragLeave);
    dropZone.on('drop', handleDrop);
}

function handleDropZoneClick(e) {
    if (e.target.id === 'drop-zone' || e.target.tagName === 'P') {
        $('#image').click();
    }
}

function handleDragOver(e) {
    e.preventDefault();
    e.stopPropagation();
    $('#drop-zone').addClass('dragover');
}

function handleDragLeave(e) {
    e.preventDefault();
    e.stopPropagation();
    $('#drop-zone').removeClass('dragover');
}

function handleDrop(e) {
    e.preventDefault();
    e.stopPropagation();
    $('#drop-zone').removeClass('dragover');

    const files = e.originalEvent.dataTransfer.files;
    if (files.length > 0) {
        validateAndPreview(files[0]);
        $('#image')[0].files = files;
    }
}

// -------------------- FileInput 초기화 --------------------
function initFileInput() {
    $('#image').on('change', handleFileInputChange);
}

function handleFileInputChange(e) {
    if (this.files && this.files[0]) {
        validateAndPreview(this.files[0]);
    }
}

// -------------------- Form 초기화 --------------------
function initFormSubmit() {
    $('#upload-form').on('submit', handleFormSubmit);
}

function handleFormSubmit(e) {
    e.preventDefault();
    $('#loading').show();

    const formData = new FormData();
    const imageFile = $('#image')[0].files[0];
    const content   = $('#upload-content').val();
    const userId    = $('#u_hUserId').val();
    const userName  = $('#u_hUserName').val();
    const boxId     = $('#u_hBoxId').val()

    if (!imageFile) {
        alert('이미지를 선택해주세요.');
        $('#loading').hide();
        return;
    }

    formData.append('image', imageFile);
    formData.append('content', content);
    formData.append('userId', userId);
    formData.append('userName', userName);
    formData.append('boxId', boxId);

    $.ajax({
        url: '/communities/posts',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function(response) {
            $('#loading').hide();
            showToast('내용이 게시되었습니다.');
            window.location.href = '/'
        },
        error: function(xhr, status, error) {
            console.error(error);
            $('#loading').hide();
            showToast('게시하는중 문제가 발생하였습니다.');
        }
    });
}

// -------------------- 파일 유효성 검사 + 미리보기 --------------------
function validateAndPreview(file) {
    const validExtensions = ['jpg', 'jpeg', 'png', 'gif'];
    const maxSize = 20 * 1024 * 1024; // 5MB

    const fileName = file.name;
    const fileExtension = fileName.split('.').pop().toLowerCase();

    if (!validExtensions.includes(fileExtension)) {
        alert('jpg, jpeg, png, gif 파일만 업로드할 수 있습니다.');
        resetFileInput();
        return;
    }

    if (file.size > maxSize) {
        alert('파일 크기는 5MB를 초과할 수 없습니다.');
        resetFileInput();
        return;
    }

    const reader = new FileReader();
    reader.onload = function(e) {
        $('#preview-image').attr('src', e.target.result).show();
    }
    reader.readAsDataURL(file);
}

function resetFileInput() {
    $('#image').val('');
    $('#preview-image').hide();
}