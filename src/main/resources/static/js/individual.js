let contented = false;

$(document).ready(() => {
    i_initialize().catch(console.error);
});

async function i_initialize() {
    const individualPage = $('body').data('page');
    console.log('currentPage :: ', individualPage);

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
            console.log('indi :: ', response);
            $('#hRecordId').val(response.id);
            if (!response.content) {
                contented = false;
                $('#record-text').val('');
                $('#record-save-btn').text('저장하기');
            } else {
                contented = true;
                $('#record-text').val(response.content);
                $('#record-save-btn').text('수정하기');
            }
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
        const clean = DOMPurify.sanitize(wod.program, { IN_PLACE: true });
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

    const formData = {
        userId: userId,
        content: record,
        date: dateStr,
        box: box
    };

    console.log('formData :: ', formData);

    $.ajax({
        type: 'POST',
        url: '/records',
        data: JSON.stringify(formData),
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        success: (response) => {
            console.log('records 저장 완료:', response);
        },
        error: (xhr) => {
            console.error('저장 실패:', xhr);
        }
    });
}

let update = () => {
    alert('개발중입니다.');
}

let datePicker = (elementId) => {
    const today = getToday();
    $(elementId).val(today);
    $(elementId).on('change', function () {
        $('#record-text').val('');
        const date = $(this).val();
        getRecordWithRetry();
        reqWodWithRetry(date, box);
    });
}