$(document).ready(() => {
    if ( !checkToken() )
        window.location.href = '/members/login';
    setupAjax();
    bottomNav();
    m_initialize().catch(console.error)
});

async function m_initialize() {
    await userInfo().then((userInfo) => {
        userStatus(userInfo.userId)
    });
}

let userStatus = (userId) => {
    $.ajax({
        type: 'GET',
        url: '/members/'+ userId +'/status',
        success: (response) => {
            if (response && response.status === 'PENDING') {
                window.location.href = '/members/' + userId + '/pending';
            }
        },
        error: (xhr) => {
        }
    });
}

let bottomNav = () => {
    $('.bottom-nav').on('click', '.nav-item', function (e) {
        $('.bottom-nav .nav-item').removeClass('active');
        $(this).addClass('active');
    });
}