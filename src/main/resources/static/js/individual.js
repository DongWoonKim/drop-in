let box = 'hound_garak';

$(document).ready(() => {
    setupAjax();

    userInfo().then((userInfo) => {
        $('#hUserId').val(userInfo.userId);
        $('#hUserName').val(userInfo.userName);

        getRecord();
    }).catch((error) => {
        console.error('Error while fetching user info:', error);
    });
    datePicker();
    reqWod(getToday(), box);
});

let getRecord = () => {
    const date = $('#individual-date-picker').val();
    const userId = $('#hUserId').val();

    $.ajax({
        type: 'GET',
        url: '/records',
        data: {date, box, userId},
        dataType: 'json',
        success: (response) => {
            $('#hRecordId').val(response.id);
            $('#record-text').val(response.content);
        },
        error: (xhr) => {
            if (xhr.status === 401) {
                handleTokenExpiration();
            } else {
            }
        }
    })
}

let saveRecord = () => {
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

let datePicker = () => {
    // 1) 오늘 날짜 계산 (yyyy‑mm‑dd)
    const today = getToday();

    // 2) input 기본값으로 오늘 날짜 세팅
    $('#individual-date-picker').val(today);

    // 3) 날짜 변경 시 이벤트 핸들러
    $('#individual-date-picker').on('change', function() {
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
            handleTokenExpiration();
        } else if (xhr.status === 403) {
            // window.location.href = '/access-denied';
        } else {
            // 다른 오류 처리
            console.error('요청 오류 발생:', xhr);
        }
    });
}
