
$(document).ready(() => {
    $('#email-domain').on('change', function () {
        const selected = $(this).val();

        if (selected === 'custom') {
            $('#email-custom-domain').show();
        } else {
            $('#email-custom-domain').hide();
        }
    });
    boxes();
});


let boxes = () => {
    $.ajax({
        type: 'GET',
        url: '/boxes',
        success: (response) => {
            console.log('response :: ', response);
            response.forEach(box => {
                $('#box').append(
                    $('<option>', {
                        value: box.id,
                        text: box.boxName.replaceAll('_', ' ').toUpperCase()
                    })
                );
            });
        },
        error: (e) => {
            console.log('boxes error :: ', e);
        }
    });
}

let signUp = () => {

    const userId = $('#userId').val().trim();
    const password = $('#password').val().trim();
    const userName = $('#userName').val().trim();
    const birthDate = $('#birthDate').val().trim();
    const phone = $('#phone').val().trim();
    const emailId = $('#email-id').val().trim();
    const emailDomain = $('#email-domain').val();
    const emailCustom = $('#email-custom-domain').val().trim();
    const box = $('#box').val();

    // 1. 필수 입력 확인
    if (!userId || !password || !userName || !birthDate || !phone) {
        alert('모든 필수 항목을 입력해주세요.');
        return;
    }

    // 2. 이메일 조합
    let email = '';
    if (emailDomain === 'custom') {
        if (!emailCustom) {
            alert('이메일 도메인을 직접 입력해주세요.');
            return;
        }
        email = `${emailId}@${emailCustom}`;
    } else {
        if (!emailDomain) {
            alert('이메일 도메인을 선택해주세요.');
            return;
        }
        email = `${emailId}${emailDomain}`;
    }

    // 3. 이메일 유효성 검사
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        alert('유효한 이메일 형식이 아닙니다.');
        return;
    }

    // 4. 박스 선택 여부
    if (!box) {
        alert('Box를 선택해주세요.');
        return;
    }

    // ✅ 검증 통과 → 콘솔 또는 서버로 전송
    const formData = {
        userId,
        password,
        userName,
        birthDate,
        phone,
        email,
        box
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