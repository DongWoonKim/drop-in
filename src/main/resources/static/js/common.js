let handleTokenExpiration = () => {
    return new Promise((resolve, reject) => {
        $.ajax({
            type: 'POST',
            url: '/refresh-token',
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            xhrFields: {
                withCredentials: true
            },
            success: (response) => {
                localStorage.setItem('accessToken', response.token);
                setupAjax(); // 새 토큰 적용
                resolve(); // ✅ resolve 호출
            },
            error: (error) => {
                alert('로그인이 필요합니다. 다시 로그인해주세요.');
                localStorage.removeItem('accessToken');
                window.location.href = '/members/login';
                reject(error); // ✅ reject 호출
            }
        });
    });
}

let setupAjax = () => {
    // 모든 Ajax 요청에 JWT Access Token을 포함
    $.ajaxSetup({
        beforeSend: function(xhr) {
            let token = localStorage.getItem('accessToken'); // 저장된 Access Token 가져오기
            if (token) {
                xhr.setRequestHeader('Authorization', 'Bearer ' + token); // Authorization 헤더에 Access Token 추가
            }
        }
    });
}

let logout = (message) => {
    // refresh token 제거
    $.ajax({
        type: 'POST',
        url: '/members/logout', // 서버의 엔드포인트 URL
        contentType: 'application/json; charset=utf-8', // 전송 데이터의 타입
        success: (response) => {
            if (message) {
                // 성공 시 실행될 콜백 함수
                alert(message);
            }
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

let checkToken = () => {
    let token = localStorage.getItem('accessToken');
    if (token == null || token.trim() === '') {
        return false;
    }
    return true;
}

let userInfo = () => {
    return new Promise((resolve, reject) => {
        $.ajax({
            type: 'GET',
            url: '/members/me',
            success: (response) => {
                resolve(response); // 성공 시 Promise를 해결
            },
            error: (xhr) => {
                reject(xhr); // 오류가 발생한 경우 Promise를 거부
            }
        });
    });
}

let getToday = () => {
    // 1) 오늘 날짜 계산 (yyyy‑mm‑dd)
    const now   = new Date();
    const yyyy  = now.getFullYear();
    const mm    = String(now.getMonth() + 1).padStart(2, '0');
    const dd    = String(now.getDate()).padStart(2, '0');

    return `${yyyy}-${mm}-${dd}`;
}

let getWod = (date, box) => {
    return new Promise((resolve, reject) => {
        $.ajax({
            type: 'GET',
            url: '/wods',
            data: {date, box},
            dataType: 'json',
            success: (response) => {
                resolve(response); // 성공 시 Promise를 해결
            },
            error: (xhr) => {
                reject(xhr); // 오류가 발생한 경우 Promise를 거부
            }
        })
    });

}

function detectKeyboard() {
    const bottomNav = document.querySelector('.bottom-nav');

    window.addEventListener('resize', () => {
        const windowHeight = window.innerHeight;
        const documentHeight = document.documentElement.clientHeight;

        // 키보드 올라오면 화면 높이가 줄어들어서 차이가 생김
        if (windowHeight < documentHeight * 0.8) {
            // 키보드 올라왔다고 판단
            bottomNav.style.display = 'none';
        } else {
            // 키보드 내려갔다
            bottomNav.style.display = 'flex';
        }
    });
}