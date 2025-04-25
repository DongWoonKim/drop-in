let contented = false;

$(document).ready(() => {
    i_initialize()
        .catch(console.error);
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

            getRecord();
        });
        datePicker('#individual-date-picker');
        await reqWod(getToday(), box);
    }
}

let getRecord = () => {
    let date = $('#individual-date-picker').val();
    const userId = $('#hUserId').val();

    if (!date) {
        date = getToday();
    }

    $.ajax({
        type: 'GET',
        url: '/records/me',
        data: {date, box, userId},
        dataType: 'json',
        success: (response) => {
            $('#hRecordId').val(response.id);
            if (!response.content) {
                console.log('기록이 없습니다.');
                contented = false;
                $('#record-text').val('');
                $('#record-save-btn').text('저장하기');
            } else {
                contented = true;
                $('#record-text').val(response.content);
                $('#record-save-btn').text('수정하기');
            }
        },
        error: (xhr) => {
            if (xhr.status === 401) {
            } else {
            }
        }
    });
}

let saveRecord = () => {
    if (!contented) newSave();
    else update();
}

let newSave = () => {
    const record = $('#record-text').val();
    const dateStr = $('#individual-date-picker').val();
    const userId =  $('#hUserId').val();

    let formData = {
        userId : userId,
        content: record,
        date: dateStr,
        box: box
    };

    $.ajax({
        type: 'POST',
        url: '/records', // 서버의 엔드포인트 URL
        data: JSON.stringify(formData), // 데이터를 JSON 형식으로 변환
        contentType: 'application/json; charset=utf-8', // 전송 데이터의 타입
        dataType: 'json', // 서버에서 받을 데이터의 타입
        success: (response) => {
            console.log('records :: ', response);
        },
        error: (xhr) => {
        }
    });
}

let update = () => {
    alert('개발중입니다.')
}

let datePicker = (elementId) => {
    // 1) 오늘 날짜 계산 (yyyy‑mm‑dd)
    const today = getToday();

    // 2) input 기본값으로 오늘 날짜 세팅
    $(elementId).val(today);

    // 3) 날짜 변경 시 이벤트 핸들러
    $(elementId).on('change', function() {
        $('#record-text').val('');
        const date = $(this).val();  // yyyy‑mm‑dd 형식
        getRecord();
        reqWod(date, box);
    });
}

let reqWod = (date, box) => {
    getWod(date, box).then((wod) => {
        $('#wod-title').text(wod.title);
        const clean = DOMPurify.sanitize(wod.program, {
            // 기본 설정만으로도 스크립트, 이벤트 핸들러(onclick 등) 제거
            IN_PLACE : true
        });
        $('#wod-program').html(clean);
    }).catch((xhr) => {
        if (xhr.status === 401) {
            // Refresh Token을 통해 Access Token 재발급 요청
        } else if (xhr.status === 403) {
            // window.location.href = '/access-denied';
        } else {
            // 다른 오류 처리
            console.error('요청 오류 발생:', xhr);
        }
    });
}
