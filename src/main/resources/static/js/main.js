
$(document).ready(() => {
    if ( !checkToken() )
        window.location.href = '/members/login';
    // access token 유효성, status 상태 체크 로직 추가 필요
    setupAjax();
    bottomNav();
});

let bottomNav = () => {
    $('.bottom-nav').on('click', '.nav-item', function (e) {
        $('.bottom-nav .nav-item').removeClass('active');
        $(this).addClass('active');
    });
}

let logout = () => {
    // refresh token 제거
    $.ajax({
        type: 'POST',
        url: '/members/logout', // 서버의 엔드포인트 URL
        contentType: 'application/json; charset=utf-8', // 전송 데이터의 타입
        success: (response) => {
            // 성공 시 실행될 콜백 함수
            alert('로그아웃 되었습니다.')
            // access token 제거
            localStorage.removeItem('accessToken');
            // 성공 후 로그인 페이지로 이동.
            window.location.href = '/members/login';
        },
        error: (error) => {
            // 실패 시 실행될 콜백 함수
            console.error('오류 발생:', error);
            alert('로그아웃 중 오류가 발생했습니다.');
        }
    });
}