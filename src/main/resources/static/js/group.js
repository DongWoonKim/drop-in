$(document).ready(() => {
    g_initialize()
        .catch(console.error);
});

async function g_initialize() {
    const groupPage = $('body').data('page');
    console.log('currentPage :: ', groupPage);
    if (groupPage === 'group') {
        setupAjax();
        await handleTokenExpiration();
        gDatePicker('#group-date-picker');
        await userInfo().then((userInfo) => {
            $('#hgUserId').val(userInfo.userId);
            $('#hgUserName').val(userInfo.userName);

            getRecordAll(getToday());
        }).catch((error) => {
            console.error('Error while fetching user info:', error);
        });
    }
}

let gDatePicker = (elementId) => {
    // 1) 오늘 날짜 계산 (yyyy‑mm‑dd)
    const today = getToday();

    // 2) input 기본값으로 오늘 날짜 세팅
    $(elementId).val(today);

    // 3) 날짜 변경 시 이벤트 핸들러
    $(elementId).on('change', function() {
        $('#record-text').val('');
        const date = $(this).val();  // yyyy‑mm‑dd 형식
        getRecordAll(date);
    });
}

let getRecordAll = (date) => {
    console.log('group :: ', date)

    if (!date) {
        date = getToday();
    }
    $.ajax({
        type: 'GET',
        url: '/records',
        data: {date, box},
        dataType: 'json',
        success: (response) => {
            const container = $('#group-record');
            container.empty(); // 기존 내용 비우기

            response.forEach(record => {
                const card = `
                    <div class="group-record-card">
                        <div class="group-record-user">${record.userId}</div>
                        <div class="group-record-content">${record.content}</div>
                    </div>
                `;
                container.append(card);
            });
        },
        error: (xhr) => {
        }
    });
}


