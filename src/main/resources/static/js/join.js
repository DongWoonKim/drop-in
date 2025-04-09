
$(document).ready(() => {
});

let signUp = () => {

    // 각 입력 필드의 값을 가져옴
    let userId    = $('#userId').val();
    let password  = $('#password').val();
    let userName  = $('#userName').val();
    let birthDate = $('#birthDate').val();
    let phone     = $('#phone').val();
    let email     = $('#email').val();
    let box       = $('#box').val();

    // 가져온 값을 하나의 객체로 만들기도 함
    let formData = {
        userId: userId,
        password: password,
        userName: userName,
        birthDate: birthDate,
        phone: phone,
        email: email,
        box: box
    };

    $.ajax({
        type: 'POST',
        url: '/members', // 서버의 엔드포인트 URL
        data: JSON.stringify(formData), // 데이터를 JSON 형식으로 변환
        contentType: 'application/json; charset=utf-8', // 전송 데이터의 타입
        dataType: 'json', // 서버에서 받을 데이터의 타입
        success: (response) => {
            alert('회원가입 성공!')
            window.location.href = '/members/login'
        },
        error: (xhr) => {
        }
    });
}

// 비밀번호 표시/숨기기
let togglePassword = () => {
    const pwdField = document.getElementById('password');
    if (pwdField.type === 'password') {
        pwdField.type = 'text';
    } else {
        pwdField.type = 'password';
    }
}