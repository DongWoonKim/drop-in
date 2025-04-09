$(document).ready(() => {
    // 로그인 페이지 접속시 access token의 만료 여부와는 상관없이 무조건 home으로 넘긴다.
    // home에서 access token의 유효성 및 리프레쉬 토큰 발급 등을 진행한다.
    let result = checkToken();
    if (result) {
        window.location.href = '/';
    }

});

let login = () => {
    let userId    = $('#userId').val();
    let password  = $('#password').val();

    let formData = {
        userId: userId,
        password: password
    };

    $.ajax({
        type: 'POST',
        url: '/members/login', // 서버의 엔드포인트 URL
        data: JSON.stringify(formData), // 데이터를 JSON 형식으로 변환
        contentType: 'application/json; charset=utf-8', // 전송 데이터의 타입
        dataType: 'json', // 서버에서 받을 데이터의 타입
        success: (response) => {
            localStorage.setItem('accessToken', response.accessToken)
            window.location.href = "/"
        },
        error: (xhr) => {
        }
    });
};