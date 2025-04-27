$(document).ready(() => {
    g_initialize().catch(console.error);
});

async function g_initialize() {
    const groupPage = $('body').data('page');

    if (groupPage === 'group') {
        setupAjax();
        await handleTokenExpiration();
        gDatePicker('#group-date-picker');

        await userInfo().then((userInfo) => {
            $('#hgUserId').val(userInfo.userId);
            $('#hgUserName').val(userInfo.userName);
            getRecordAllWithRetry(getToday());
        }).catch((error) => {
            console.error('Error while fetching user info:', error);
        });
    }
}

function gDatePicker(elementId) {
    const today = getToday();
    $(elementId).val(today);

    $(elementId).on('change', function () {
        $('#record-text').val('');
        const date = $(this).val();
        getRecordAllWithRetry(date);
    });
}

async function getRecordAllWithRetry(date) {
    try {
        await getRecordAll(date);
    } catch (xhr) {
        if (xhr.status === 401) {
            console.warn('🔁 Access Token 만료 → 재발급 후 그룹 기록 재요청');
            await handleTokenExpiration();
            await getRecordAll(date);
        } else {
            console.error('getRecordAll 실패:', xhr);
        }
    }
}

function getRecordAll(date) {
    if (!date) {
        date = getToday();
    }

    return $.ajax({
        type: 'GET',
        url: '/records',
        data: { date, box },
        dataType: 'json',
        success: (response) => {
            const container = $('#group-record');
            container.empty();

            if (response && response.length > 0) {
                response.forEach(record => {
                    const card = `
                        <div class="group-record-card">
                            <div class="group-record-user">${record.userId}</div>
                            <div class="group-record-content">${record.content}</div>
                        </div>
                    `;
                    container.append(card);
                });
            } else {
                // 🛡️ 기록이 없을 때 기본 문구 표시
                container.append('<p style="text-align:center; color:gray;">아직 등록된 기록이 없습니다.</p>');
            }
        }
    });
}