let box = 'hound_garak';

$(document).ready(() => {
    setupAjax();
    getWod(getToday(), box).then((wod) => {
        $('#home-wod-title').text(wod.title);
        const clean = DOMPurify.sanitize(wod.program, {
            // 기본 설정만으로도 스크립트, 이벤트 핸들러(onclick 등) 제거
            IN_PLACE : true
        });
        $('#home-wod-program').html(clean);
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
});



