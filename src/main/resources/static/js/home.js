let box = 'hound_garak';

$(document).ready(() => {
    h_initialize()
        .catch(console.error);
});

async function h_initialize() {
    const homePage = $('body').data('page');

    if (homePage === 'home') {
        setupAjax();
        await handleTokenExpiration();
        const wod = await getWodWithRetry(getToday(), box);
        $('#home-wod-title').text(wod.title);
        const clean = DOMPurify.sanitize(wod.program, {
            // 기본 설정만으로도 스크립트, 이벤트 핸들러(onclick 등) 제거
            IN_PLACE : true
        });
        $('#home-wod-program').html(clean);

    }
}

async function getWodWithRetry(date, box) {
    try {
        return await getWod(date, box);
    } catch (xhr) {
        if (xhr.status === 401) {
            console.warn('🔁 Access Token 만료 → 재발급 시도 중...');
            await handleTokenExpiration(); // 재발급 먼저
            return await getWod(date, box); // 재요청
        } else {
            throw xhr;
        }
    }
}

