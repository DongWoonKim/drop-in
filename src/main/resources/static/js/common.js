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