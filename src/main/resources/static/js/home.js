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

        let clean = '';
        if (wod.program && wod.program.trim().length > 0) {
            clean = DOMPurify.sanitize(wod.program);
        } else {
            clean = '<p style="text-align:center; color:gray;">오늘의 WOD가 아직 등록되지 않았습니다.</p>';
        }
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

