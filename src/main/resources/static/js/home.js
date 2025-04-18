$(document).ready(() => {
    setupAjax();

    console.log('access :: ', localStorage.getItem('accessToken'))
    datePicker();

    let box = 'hound_garak';
    getWod(getToday(), box);
});

let getToday = () => {
    // 1) 오늘 날짜 계산 (yyyy‑mm‑dd)
    const now   = new Date();
    const yyyy  = now.getFullYear();
    const mm    = String(now.getMonth() + 1).padStart(2, '0');
    const dd    = String(now.getDate()).padStart(2, '0');

    return `${yyyy}-${mm}-${dd}`;

}

let datePicker = () => {
    // 1) 오늘 날짜 계산 (yyyy‑mm‑dd)
    const today = getToday();

    // 2) input 기본값으로 오늘 날짜 세팅
    $('#date-picker').val(today);

    // 3) 날짜 변경 시 이벤트 핸들러
    $('#date-picker').on('change', function() {
        const date = $(this).val();  // yyyy‑mm‑dd 형식
        let box = 'hound_garak';
        console.log('날짜가 변경되었습니다:', date);
        getWod(date, box);
    });
}

let getWod = (date, box) => {
    $.ajax({
        type: 'GET',
        url: '/wods',
        data: { date, box },
        dataType: 'json',
        success: (response) => {
            console.log('res :: ', response)
            // set the title as plain text
            $('#wod-title').text(response.title);
            const clean = DOMPurify.sanitize(response.program, {
                // 기본 설정만으로도 스크립트, 이벤트 핸들러(onclick 등) 제거
                IN_PLACE : true
            });
            $('#wod-program').html(clean);
        },
        error: (xhr) => {
            if (xhr.status === 401) {
                // Refresh Token을 통해 Access Token 재발급 요청
                handleTokenExpiration();
            } else if (xhr.status === 403) {
                // window.location.href = '/access-denied';
            } else {
                // 다른 오류 처리
                console.error('요청 오류 발생:', xhr);
            }
        }
    });
}