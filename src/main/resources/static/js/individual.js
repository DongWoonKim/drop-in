let contented = false;
let wodExists = false;

$(document).ready(() => {
    i_initialize().catch(console.error);
});

const checkContentAndToggleButton = () => {
    const content = $('#record-text').val().trim();
    console.log("wodExists :: ", wodExists)
    if (content.length > 0 && wodExists) {
        $('#record-save-btn').prop('disabled', false);
    } else {
        $('#record-save-btn').prop('disabled', true);
    }
};

async function i_initialize() {
    const individualPage = $('body').data('page');

    if (individualPage === 'individual') {
        setupAjax();
        await handleTokenExpiration();

        await userInfo().then((userInfo) => {
            $('#hUserId').val(userInfo.userId);
            $('#hUserName').val(userInfo.userName);
            getRecordWithRetry();
        });

        datePicker('#individual-date-picker');
        await reqWodWithRetry(getToday(), box);

        // 1. 페이지 로드될 때 처음 한번 체크
        checkContentAndToggleButton();
        // 2. 이후 input 발생할 때마다 체크
        $('#record-text').on('input', checkContentAndToggleButton);

        if (contented)
            $('#record-save-btn').text('수정하기');
    }
}

async function getRecordWithRetry() {
    try {
        await getRecord();
    } catch (xhr) {
        if (xhr.status === 401) {
            console.warn('🔁 Access Token 만료 → 재발급 후 기록 재요청');
            await handleTokenExpiration();
            await getRecord(); // 재요청
        } else {
            console.error('getRecord 실패:', xhr);
        }
    }
}

let getRecord = () => {
    let date = $('#individual-date-picker').val();
    const userId = $('#hUserId').val();

    if (!date) date = getToday();

    return $.ajax({
        type: 'GET',
        url: '/records/me',
        data: { date, box, userId },
        dataType: 'json',
        success: (response) => {
            if (!response.content) {
                contented = false;
                $('#record-text').val('');
                $('#record-save-btn').text('저장하기');
            } else {
                contented = true;
                $('#hRecordId').val(response.id);
                $('#record-text').val(response.content);
                $('#record-save-btn').text('수정하기');
            }

            checkContentAndToggleButton();
        }
    });
}

async function reqWodWithRetry(date, box) {
    try {
        await reqWod(date, box);
    } catch (xhr) {
        if (xhr.status === 401) {
            console.warn('🔁 WOD 요청 토큰 만료 → 재발급 시도');
            await handleTokenExpiration();
            await reqWod(date, box);
        } else {
            console.error('reqWod 실패:', xhr);
        }
    }
}

let reqWod = (date, box) => {
    return getWod(date, box).then((wod) => {
        $('#wod-title').text(wod.title);

        let clean = '';
        if (wod.program && wod.program.trim().length > 0) {
            wodExists = true;
            clean = DOMPurify.sanitize(wod.program);
        } else {
            wodExists = false;
            clean = '<p style="text-align:center; color:gray;">오늘의 WOD가 아직 등록되지 않았습니다.</p>';
        }

        $('#wod-program').html(clean);
    });
}

let saveRecord = () => {
    if (!contented) newSave();
    else update();
}

let newSave = () => {
    const record = $('#record-text').val();
    const dateStr = $('#individual-date-picker').val();
    const userId = $('#hUserId').val();

    // 🛡️ 입력 방어: 위험한 태그 감지
    const pattern = /<\s*(script|iframe|img|object|embed|link|style|base|form|input)[^>]*>/i;
    if (pattern.test(record)) {
        alert('스크립트나 위험한 태그는 입력할 수 없습니다.');
        return;
    }

    const formData = {
        userId: userId,
        content: record,
        date: dateStr,
        box: box
    };

    $.ajax({
        type: 'POST',
        url: '/records/me',
        data: JSON.stringify(formData),
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        success: (response) => {
            showToast('기록이 저장되었습니다.');
            if (response)
                $('#hRecordId').val(response.recordId);
        },
        error: (xhr) => {
            console.error('저장 실패:', xhr);
        }
    });
}

let update = () => {
    const record = $('#record-text').val();
    const userId = $('#hUserId').val();
    const recordId = $('#hRecordId').val();

    const formData = {
        userId: userId,
        content: record,
        recordId: recordId
    };

    $.ajax({
        type: 'PUT',
        url: '/records/me',
        data: JSON.stringify(formData),
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        success: (response) => {
            if (response && response.success) {
                // alert('기록이 수정되었습니다.')
                showToast('기록이 수정되었습니다.');
            }
        },
        error: (xhr) => {
            console.error('수정 실패:', xhr);
        }
    });
}

let datePicker = (elementId) => {
    const today = getToday();
    $(elementId).val(today);
    $(elementId).on('change', function () {
        $('#record-text').val('');
        const date = $(this).val();
        (async () => {
            await reqWodWithRetry(date, box);
            await getRecordWithRetry();
        })();
    });
}

function showToast(message) {
    const toast = $('#toast');
    toast.text(message);
    toast.fadeIn(300);
    setTimeout(() => {
        toast.fadeOut(300);
    }, 2000);
}