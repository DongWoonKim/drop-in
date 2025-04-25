$(document).ready(() => {
    g_initialize().catch(console.error);
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
    console.log('group :: ', date);
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

            response.forEach(record => {
                const card = `
                    <div class="group-record-card">
                        <div class="group-record-user">${record.userId}</div>
                        <div class="group-record-content">${record.content}</div>
                    </div>
                `;
                container.append(card);
            });
        }
    });
}