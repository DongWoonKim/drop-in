let box = 'hound_garak';
let lastCreatedAt = null; // ⭐ 마지막 가져온 createdAt
let lastId = null;        // ⭐ 마지막 가져온 id
let loading = false;
let noMorePosts = false;

$(document).ready(() => {
    h_initialize()
        .catch(console.error);

    $(document).on('click', '.toggle-comments-btn', function() {
        $(this).closest('.post-card').find('.post-comments').slideToggle(200);

        // 버튼 텍스트 토글
        if ($(this).text() === '댓글 보기') {
            $(this).text('댓글 숨기기');
        } else {
            $(this).text('댓글 보기');
        }
    });
});

async function h_initialize() {
    const homePage = $('body').data('page');
    if (homePage === 'home') {
        setupAjax();
        await handleTokenExpiration();
        await loadPosts(); // 최초 3개 로드

        $('#content').scroll(debounce(async function() {
            if (loading || noMorePosts) return;

            if ($('#content').scrollTop() + $('#content').height() >= $('#content')[0].scrollHeight - 150) {
                loading = true;
                $('#loading-spinner').show();
                await loadPosts();
                $('#loading-spinner').hide();
                loading = false;
            }
        }, 200)); // 200ms 기다렸다가 처리
    }
}

function debounce(func, delay) {
    let timeout;
    return function(...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(this, args), delay);
    };
}

async function loadPosts() {
    try {
        const size = 3;
        let url = `/communities/sns?box=1&size=${size}`;

        if (lastCreatedAt !== null && lastId !== null) {
            url += `&lastCreatedAt=${lastCreatedAt}&lastId=${lastId}`;
        }

        const response = await $.get(url);

        if (response.length === 0) {
            noMorePosts = true;
            return;
        }

        response.forEach(post => {
            const postCard = `
                <div class="post-card">
                    <div class="post-header">
                        <span class="home-wod-writer">${post.userName}</span> ·
                        <span class="home-wod-date">${post.createdAt.substring(0, 10)}</span>
                    </div>
                    <hr>
                    <div class="post-image">
                        <img src="https://daily-pr.s3.ap-northeast-2.amazonaws.com/${post.imageUrl}" alt="사진" class="home-wod-photo" />
                    </div>
                    <div class="post-content">
                        ${post.content}
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
                            <button class="add-comment-btn">등록</button>
                        </div>
                    </div>
                </div>
            `;

            $('#post-list').append(postCard);

            // ⭐ 가져온 마지막 글의 createdAt, id로 다음 요청 준비
            lastCreatedAt = post.createdAt;
            lastId = post.id;
        });
    } catch (error) {
        console.error('게시글 불러오기 실패:', error);
    }
}