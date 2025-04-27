
$(() => {
    $('#notice-list').click((e) => {
        e.preventDefault();

        fetchNoticeList(1);
    });

    $(document).on('click', '.link-page', function(e) {
        e.preventDefault();
        if ($(this).hasClass('disabled')) return;

        const page = $(this).data('page');
        fetchNoticeList(page);
    })
});

function fetchNoticeList(page) {
    $.ajax({
        url: `/api/notices?page=${page}&size=5`,
        type: 'GET',
        success: (data) => {
            renderNoticeList(data);
        },
        error: (xhr) => {
            console.error("error: ", xhr);
        }
    });
}

function renderNoticeList(res) {
    const noticeList = res.data;
    const pagination = res.pagination;

    let noticeDom = '<table class="table">';
    noticeDom += `
        <thead>
            <tr>
                <th class="col-md-6">제목</th>
                <th class="col-md-1">작성일</th>
            </tr>
        </thead>
        <tbody>
    `;

    noticeList.forEach((notice) => {
        noticeDom += `
            <tr>
                <td class="title pt-3 pb-3"><a href="#">${notice.title}</a></td>
                <td class="pt-3 pb-3">${formatDateToString(notice.createdAt)}</td>
            </tr>
        `;
    });
    noticeDom += '</tbody></table>';

    noticeDom += `
        <div class="d-flex justify-content-center">
            <a class="p-1 link-page ${pagination.currentPage <= 1 ? 'disabled' : ''}" href="#" data-page="1"><i class="fa-solid fa-angles-left"></i></a>
            <a class="p-1 link-page ${pagination.currentPage <= 1 ? 'disabled' : ''}" href="#" data-page="${pagination.currentPage - 1}" style="margin-right: 40px;"><i class="fa-solid fa-angle-left"></i></a>
    `;

    for (let i = pagination.startPage; i <= pagination.endPage; i++) {
        if (i == pagination.currentPage) {
            noticeDom += `<span class="p-1">${i}</span>`;
        } else {
            noticeDom += `<a class="p-1 link-page" href="#" data-page=${i}>${i}</a>`
        }
    }

    noticeDom += `
            <a class="p-1 link-page ${pagination.currentPage >= pagination.totalPages ? 'disabled' : ''}" href="#" data-page="${pagination.currentPage + 1}" style="margin-left: 40px;"><i class="fa-solid fa-angle-right"></i></a>
            <a class="p-1 link-page ${pagination.currentPage >= pagination.totalPages ? 'disabled' : ''}" href="#" data-page="${pagination.totalPages}"><i class="fa-solid fa-angles-right"></i></a>
        </div>
    `;

    $('#noticeModal .modal-body').html(noticeDom);
}

function formatDateToString(string) {
    const date = new Date(string);
    const year = date.getFullYear().toString().substring(2, 4);
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}.${month}.${day}`;
}