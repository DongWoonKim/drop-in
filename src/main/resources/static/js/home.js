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


        $('#toggle-comments-btn').click(function() {
            $('#home-wod-comments').slideToggle(200);

            // 버튼 텍스트 토글
            if ($(this).text() === '댓글 보기') {
                $(this).text('댓글 숨기기');
            } else {
                $(this).text('댓글 보기');
            }
        });

        // 예시로 사진 넣는 코드 (Thymeleaf에서 직접 채워도 됨)
        $('#home-wod-photo').attr('src', '/images/sample-photo.jpg');

        // 예시 작성자/날짜 데이터 (Thymeleaf에서 직접 채워도 됨)
        $('#home-wod-writer').text('홍길동');
        $('#home-wod-date').text('2025-04-28');
    }
}

