let box = 'hound_garak';

$(document).ready(() => {
    h_initialize()
        .catch(console.error);

    for (let i = 0; i < 3; i++) {
        const postCard = `
      <div class="post-card">
        <div class="post-header">
          <span id="home-wod-writer">작성자${i+1}</span> ·
          <span id="home-wod-date">2025-04-28</span>
        </div>

        <hr>

        <div class="post-image" id="home-wod-image">
          <img src="" alt="사진" id="home-wod-photo" />
        </div>

        <div class="post-content" id="home-wod-program">
          오늘 재밌었다. (${i+1})
        </div>

        <div class="post-comment-toggle">
          <button class="toggle-comments-btn">댓글 보기</button>
        </div>

        <div class="post-comments" style="display:none;">
          <div class="comment-list">
            <p>댓글1: 예시 댓글입니다.</p>
            <p>댓글2: 또 다른 댓글입니다.</p>
          </div>
          <div class="comment-input-area">
            <textarea class="comment-input" placeholder="댓글을 입력하세요..."></textarea>
            <button id="add-comment-btn" class="add-comment-btn">등록</button>
          </div>
        </div>
      </div>
    `;

        $("#post-list").append(postCard);
    }

    // 댓글 보기 버튼 클릭 이벤트도 같이 걸어줄게
    $(document).on('click', '.toggle-comments-btn', function() {
        $(this).closest('.post-card').find('.post-comments').toggle();
    });
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

